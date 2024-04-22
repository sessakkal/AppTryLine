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

public class UnirseEquipo extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_user);

        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        confirmPasswordEditText = findViewById(R.id.repetir_contra);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.registro_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
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
                            // Aquí se inicia la actividad MenuPrincipal después de un registro exitoso
                            startActivity(new Intent(UnirseEquipo.this, Calendario.class));
                            // Se finaliza esta actividad para evitar volver a ella con el botón "Atrás"
                            finish();
                        } else {
                            Toast.makeText(UnirseEquipo.this, "Error al registrar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void goBack(View view) {
        onBackPressed();
    }

}
