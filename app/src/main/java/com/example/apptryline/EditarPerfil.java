package com.example.apptryline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;

public class EditarPerfil extends AppCompatActivity {

    private static final String TAG = "EditarPerfil";

    ImageView photoImageView;
    TextView displayNameTextView, emailTextView;
    EditText nameEditText;
    Button saveButton, changePasswordButton;
    ImageButton changePhotoButton;
    Uri photoUri;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_perfil);

        initWidgets();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            displayNameTextView.setText(user.getDisplayName());
            emailTextView.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);

            changePhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showImagePickerDialog();
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveChanges(user);
                }
            });

            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showChangePasswordDialog(user);
                }
            });
        }
    }

    private void initWidgets() {
        photoImageView = findViewById(R.id.photoImageView);
        displayNameTextView = findViewById(R.id.displayNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        nameEditText = findViewById(R.id.nameEditText);
        saveButton = findViewById(R.id.saveButton);
        changePhotoButton = findViewById(R.id.changePhotoButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
    }

    private void showImagePickerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar foto de perfil");
        builder.setItems(new String[]{"Elegir de galería", "Tomar foto"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    openGallery();
                } else if (which == 1) {
                    openCamera();
                }
            }
        });
        builder.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE && data != null) {
                photoUri = data.getData();
                photoImageView.setImageURI(photoUri);
                Log.d(TAG, "Photo selected from gallery: " + photoUri);
            } else if (requestCode == CAMERA_REQUEST_CODE && data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                if (imageBitmap != null) {
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "Title", null);
                    photoUri = Uri.parse(path);
                    photoImageView.setImageURI(photoUri);
                    Log.d(TAG, "Photo taken with camera: " + photoUri);
                } else {
                    Toast.makeText(this, "Error al obtener la imagen de la cámara", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error al obtener la imagen de la cámara");
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "El permiso de la cámara ha sido denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveChanges(FirebaseUser user) {
        String newName = nameEditText.getText().toString();

        boolean isNameChanged = !newName.isEmpty() && !newName.equals(user.getDisplayName());
        boolean isPhotoChanged = photoUri != null;

        Log.d(TAG, "Saving changes - Name changed: " + isNameChanged + ", Photo changed: " + isPhotoChanged);

        if (isNameChanged || isPhotoChanged) {
            UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();

            if (isNameChanged) {
                profileUpdatesBuilder.setDisplayName(newName);
            }

            if (isPhotoChanged) {
                StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("profile_photos/" + user.getUid());
                photoRef.putFile(photoUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                profileUpdatesBuilder.setPhotoUri(uri);
                                updateProfile(user, profileUpdatesBuilder.build(), isNameChanged, newName, uri.toString());
                            }).addOnFailureListener(e -> Log.e(TAG, "Failed to get download URL: ", e));
                        }).addOnFailureListener(e -> Log.e(TAG, "Failed to upload photo: ", e));
            } else {
                updateProfile(user, profileUpdatesBuilder.build(), isNameChanged, newName, null);
            }
        } else {
            Toast.makeText(EditarPerfil.this, "No se realizaron cambios", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile(FirebaseUser user, UserProfileChangeRequest profileUpdates, boolean isNameChanged, String newName, String photoUrl) {
        user.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Profile updated successfully.");
                        if (isNameChanged) {
                            displayNameTextView.setText(newName);
                            updatePosts(user.getUid(), newName, photoUrl);
                        } else if (photoUrl != null) {
                            updatePosts(user.getUid(), null, photoUrl);
                        }
                        Intent intent = new Intent(EditarPerfil.this, Calendario.class);
                        startActivity(intent);
                        Toast.makeText(EditarPerfil.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e(TAG, "Error updating profile: ", task.getException());
                        Toast.makeText(EditarPerfil.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePosts(String userId, String newName, String newPhotoUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query postsQuery = db.collection("posts").whereEqualTo("uid", userId);

        postsQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WriteBatch batch = db.batch();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    if (newName != null) {
                        batch.update(document.getReference(), "author", newName);
                    }
                    if (newPhotoUrl != null) {
                        batch.update(document.getReference(), "authorPhotoUrl", newPhotoUrl);
                    }
                }
                batch.commit().addOnCompleteListener(batchTask -> {
                    if (batchTask.isSuccessful()) {
                        Toast.makeText(EditarPerfil.this, "Posts actualizados correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditarPerfil.this, "Error al actualizar los posts", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e(TAG, "Error getting posts: ", task.getException());
                Toast.makeText(EditarPerfil.this, "Error al actualizar los posts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showChangePasswordDialog(FirebaseUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cambiar Contraseña");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.cambio_contra, null, false);
        final EditText currentPassword = viewInflated.findViewById(R.id.currentPassword);
        final EditText newPassword = viewInflated.findViewById(R.id.newPassword);

        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            String currentPass = currentPassword.getText().toString();
            String newPass = newPassword.getText().toString();

            if (currentPass.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(EditarPerfil.this, "Por favor, completa ambos campos", Toast.LENGTH_SHORT).show();
            } else {
                changePassword(user, currentPass, newPass);
            }
        });

        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void changePassword(FirebaseUser user, String currentPassword, String newPassword) {
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                user.updatePassword(newPassword).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(EditarPerfil.this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditarPerfil.this, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al actualizar la contraseña: ", task1.getException());
                    }
                });
            } else {
                Toast.makeText(EditarPerfil.this, "La autenticación falló. Verifica tu contraseña actual", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "La autenticación falló: ", task.getException());
            }
        });
    }

    public void onOption1Click(View view) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usuarioActualRef = usuariosRef.child(userId);

        usuarioActualRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("equipoId")) {
                    String equipoId = dataSnapshot.child("equipoId").getValue(String.class);
                    if (equipoId != null && !equipoId.isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), General.class);
                        intent.putExtra("equipoId", equipoId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error al obtener el ID del equipo: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onOption2Click(View view) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usuarioActualRef = usuariosRef.child(userId);

        usuarioActualRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("equipoId")) {
                    String equipoId = dataSnapshot.child("equipoId").getValue(String.class);
                    if (equipoId != null && !equipoId.isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), Conversaciones.class);
                        intent.putExtra("equipoId", equipoId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error al obtener el ID del equipo: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onOption3Click(View view) {
        Intent intent = new Intent(this, Calendario.class);
        startActivity(intent);
    }

    public void onOption4Click(View view) {
        Intent intent = new Intent(this, EditarPerfil.class);
        startActivity(intent);
    }

    public void salir(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, InicioRegistro.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    public void goBack(View view) {
        onBackPressed();
    }
}
