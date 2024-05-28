package com.example.apptryline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditarPerfil extends AppCompatActivity {

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    ImageView photoImageView;
    TextView emailTextView;
    EditText nameEditText, currentPasswordEditText, newPasswordEditText;
    ImageButton changePhotoButton;
    Button saveButton, changePasswordButton;
    Uri photoUri;
    DatabaseReference userRef;
    FirebaseUser user;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_perfil);

        photoImageView = findViewById(R.id.photoImageView);
        emailTextView = findViewById(R.id.emailTextView);
        changePhotoButton = findViewById(R.id.changePhotoButton);
        nameEditText = findViewById(R.id.nameEditText);
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        saveButton = findViewById(R.id.saveButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            emailTextView.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(photoImageView);

            // Cargar el nombre de usuario actual desde Firebase Database
            userRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nombreUsuario = snapshot.child("nombreUsuario").getValue(String.class);
                        nameEditText.setText(nombreUsuario);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditarPerfil.this, "Error al cargar el nombre de usuario", Toast.LENGTH_SHORT).show();
                }
            });

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String photoUrl = snapshot.child("photoUrl").getValue(String.class);
                        if (photoUrl != null) {
                            Glide.with(EditarPerfil.this).load(photoUrl).into(photoImageView);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EditarPerfil.this, "Error al cargar la foto de perfil", Toast.LENGTH_SHORT).show();
                }
            });

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
                    changePassword(user);
                }
            });
        }
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    photoUri = data.getData();
                    photoImageView.setImageURI(photoUri);
                }
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                if (data != null && data.getExtras() != null) {
                    photoUri = (Uri) data.getExtras().get("data");
                    photoImageView.setImageURI(photoUri);
                }
            }
        }
    }


    private void saveChanges(FirebaseUser user) {
        String newName = String.valueOf(nameEditText.getText());

        // Verifica si el nombre ha cambiado
        if (!newName.equals(user.getDisplayName())) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(newName)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("EditarPerfil", "User display name updated.");
                                Toast.makeText(EditarPerfil.this, "Nombre actualizado", Toast.LENGTH_SHORT).show();

                                // Actualizar el nombre en Firebase Database
                                userRef.child("nombreUsuario").setValue(newName);
                            } else {
                                Toast.makeText(EditarPerfil.this, "Error al actualizar nombre", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        // Verifica si la foto ha cambiado
        if (photoUri != null) {
            // Sube la nueva foto a Firebase Storage
            StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("profile_photos/" + user.getUid() + ".jpg");
            photoRef.putFile(photoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Obtén la URL de descarga de la foto subida
                            photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Actualiza la URL de la foto en Firebase Database
                                    userRef.child("photoUrl").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("EditarPerfil", "User photo URL updated.");
                                                Toast.makeText(EditarPerfil.this, "Foto de perfil actualizada", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(EditarPerfil.this, "Error al actualizar foto de perfil", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
        }
    }

    private void changePassword(FirebaseUser user) {
        String currentPassword = currentPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();

        if (!currentPassword.isEmpty() && !newPassword.isEmpty()) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("EditarPerfil", "User password updated.");
                                                    Toast.makeText(EditarPerfil.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(EditarPerfil.this, "Error al actualizar contraseña", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(EditarPerfil.this, "Contraseña actual incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Por favor, introduce la contraseña actual y la nueva", Toast.LENGTH_SHORT).show();
        }
    }
}