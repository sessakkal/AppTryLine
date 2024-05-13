// UnirseEquipo.java
package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UnirseEquipo extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText, nombreUsuarioEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_user);

        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        confirmPasswordEditText = findViewById(R.id.repetir_contra);
        nombreUsuarioEditText = findViewById(R.id.nombre_usuario);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.registro_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        final String nombreUsuario = nombreUsuarioEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(nombreUsuario)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Cambiar este valor según corresponda
                            boolean admin = false; // O true si es un administrador
                            String equipoId = ""; // Aquí debes asignar el ID del equipo al que se une el usuario
                            guardarInformacionUsuario(user.getUid(), email, nombreUsuario, admin, equipoId);
                        } else {
                            Toast.makeText(UnirseEquipo.this, "Error al registrar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void guardarInformacionUsuario(String userId, String email, String nombreUsuario, boolean admin, String equipoId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios");
        Usuario usuario = new Usuario(email, nombreUsuario, "", admin, equipoId);
        ref.child(userId).setValue(usuario)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UnirseEquipo.this, "Usuario registrado y datos guardados exitosamente", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UnirseEquipo.this, Calendario.class));
                            finish();
                        } else {
                            Toast.makeText(UnirseEquipo.this, "Error al guardar la información del usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
