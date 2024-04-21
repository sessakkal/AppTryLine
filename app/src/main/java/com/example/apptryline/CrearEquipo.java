package com.example.apptryline;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrearEquipo extends AppCompatActivity {

    private EditText nombreEquipoEditText;
    private EditText editTextCorreo;
    private EditText editTextContraseña;
    private EditText editTextRepetirContraseña;
    private Button buttonRegistrar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_admin);

        mAuth = FirebaseAuth.getInstance();

        nombreEquipoEditText = findViewById(R.id.nombre_equipo_edittext);
        editTextCorreo = findViewById(R.id.email_edittext);
        editTextContraseña = findViewById(R.id.password_edittext);
        editTextRepetirContraseña = findViewById(R.id.repetir_contra);
        buttonRegistrar = findViewById(R.id.registro_button);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String nombreEquipo = nombreEquipoEditText.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String contraseña = editTextContraseña.getText().toString().trim();
        String repetirContraseña = editTextRepetirContraseña.getText().toString().trim();

        if (TextUtils.isEmpty(nombreEquipo) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(contraseña) || TextUtils.isEmpty(repetirContraseña)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contraseña.equals(repetirContraseña)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(CrearEquipo.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(CrearEquipo.this, "Error al registrar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
