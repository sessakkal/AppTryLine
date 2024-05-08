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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CrearEquipo extends AppCompatActivity {

    private EditText nombreEquipoEditText;
    private EditText editTextCorreo;
    private EditText editTextPassword;
    private EditText editTextRepetirPassword;
    private Button buttonRegistrar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_admin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Equipos");

        nombreEquipoEditText = findViewById(R.id.nombre_equipo_edittext);
        editTextCorreo = findViewById(R.id.email_edittext);
        editTextPassword = findViewById(R.id.password_edittext);
        editTextRepetirPassword = findViewById(R.id.repetir_contra);
        buttonRegistrar = findViewById(R.id.registro_button);

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        final String nombreEquipo = nombreEquipoEditText.getText().toString().trim();
        String correo = editTextCorreo.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String repetirpassword = editTextRepetirPassword.getText().toString().trim();

        if (TextUtils.isEmpty(nombreEquipo) || TextUtils.isEmpty(correo) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(repetirpassword)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(repetirpassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(CrearEquipo.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();

                            // Obtenemos una referencia a la base de datos
                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                            // Creamos un nuevo nodo "usuarios" y obtenemos su referencia
                            DatabaseReference usuariosRef = databaseRef.child("usuarios");

                            // Creamos un nuevo nodo con el ID del usuario y establecemos los valores
                            String userID = user.getUid();
                            DatabaseReference currentUserRef = usuariosRef.child(userID);
                            currentUserRef.child("nombre").setValue(nombreEquipo);
                            currentUserRef.child("correo").setValue(correo);
                            // Añade más campos si es necesario

                            // Intent para iniciar la actividad de la pantalla de calendario
                            Intent intent = new Intent(CrearEquipo.this, Calendario.class);
                            startActivity(intent);
                            finish(); // Esto cierra la actividad actual para que el usuario no pueda volver atrás con el botón de retroceso
                        } else {
                            Toast.makeText(CrearEquipo.this, "Error al registrar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    private void guardarEquipoEnFirebase(String userId, String nombreEquipo) {
        DatabaseReference equiposRef = mDatabase.child("equipos"); // Referencia a la colección de equipos
        DatabaseReference equipoRef = equiposRef.push(); // Genera un nuevo ID único para el equipo
        String equipoId = equipoRef.getKey(); // Obtiene el ID del equipo generado

        // Construye el objeto del equipo
        HashMap<String, Object> equipoMap = new HashMap<>();
        equipoMap.put("admin_id", userId); // ID del administrador
        equipoMap.put("nombre", nombreEquipo); // Nombre del equipo

        // Guarda el equipo en la base de datos
        equipoRef.setValue(equipoMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CrearEquipo.this, "Equipo creado exitosamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CrearEquipo.this, "Error al crear el equipo: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void goBack(View view) {
        onBackPressed();
    }
}
