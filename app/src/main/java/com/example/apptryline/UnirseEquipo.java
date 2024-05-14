package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UnirseEquipo extends AppCompatActivity {

    private EditText emailAdminEditText, emailEditText, nombreUsuarioEditText, passwordEditText, repetirContraEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_user);

        emailAdminEditText = findViewById(R.id.email_admin_edittext);
        emailEditText = findViewById(R.id.email_edittext);
        nombreUsuarioEditText = findViewById(R.id.nombre_usuario);
        passwordEditText = findViewById(R.id.password_edittext);
        repetirContraEditText = findViewById(R.id.repetir_contra);
        Button registroButton = findViewById(R.id.registro_button);

        registroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        final String emailAdmin = emailAdminEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String nombreUsuario = nombreUsuarioEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        String repetirContra = repetirContraEditText.getText().toString().trim();

        if (TextUtils.isEmpty(emailAdmin) || TextUtils.isEmpty(email) || TextUtils.isEmpty(nombreUsuario) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repetirContra)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repetirContra)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios");
        ref.orderByChild("correoElectronico").equalTo(emailAdmin)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final String adminUid = snapshot.getKey();
                                firebaseAuth.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(UnirseEquipo.this, task -> {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getUid());
                                                userRef.child("nombreUsuario").setValue(nombreUsuario);
                                                userRef.child("equipoId").setValue(adminUid);

                                                // Agregar al usuario como miembro del equipo
                                                DatabaseReference equipoRef = FirebaseDatabase.getInstance().getReference("Equipos").child(adminUid).child("miembros");
                                                equipoRef.child(user.getUid()).setValue(true);

                                                Toast.makeText(UnirseEquipo.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();

                                                // Acceder a la actividad Calendario
                                                startActivity(new Intent(UnirseEquipo.this, Calendario.class));
                                                finish();
                                            } else {
                                                Toast.makeText(UnirseEquipo.this, "Error al registrar usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                return;
                            }
                        } else {
                            Toast.makeText(UnirseEquipo.this, "No se encontró un administrador con ese correo electrónico", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(UnirseEquipo.this, "Error al buscar administrador: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
