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
import java.util.Map;

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
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
        final String correo = editTextCorreo.getText().toString().trim(); // Agregar correo
        final String password = editTextPassword.getText().toString().trim(); // Agregar password
        String repetirpassword = editTextRepetirPassword.getText().toString().trim();
        final String nombreUsuario = nombreEquipo; // Usamos el nombre del equipo como nombre de usuario para el admin

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

                            // Crear equipo y guardar en Firebase
                            guardarUsuarioYEquipo(user.getUid(), nombreEquipo, nombreUsuario, correo, password); // Pasar correo y password

                            // Iniciar sesión automáticamente después de crear el usuario
                            mAuth.signInWithEmailAndPassword(correo, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Inicio de sesión exitoso, redirigir al usuario a la actividad Calendario
                                                startActivity(new Intent(CrearEquipo.this, Calendario.class));
                                                finish();
                                            } else {
                                                // Error al iniciar sesión automáticamente
                                                Toast.makeText(CrearEquipo.this, "Error al iniciar sesión automáticamente: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(CrearEquipo.this, "Error al registrar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void guardarUsuarioYEquipo(final String userId, final String nombreEquipo, final String nombreUsuario, final String correo, final String password) {
        // Obtenemos una referencia a la ubicación de los usuarios en la base de datos
        DatabaseReference usuariosRef = mDatabase.child("Usuarios");

        // Creamos un mapa para almacenar los datos del usuario
        Map<String, Object> userData = new HashMap<>();
        userData.put("correoElectronico", mAuth.getCurrentUser().getEmail());
        userData.put("nombreUsuario", nombreUsuario);
        userData.put("nombre", nombreEquipo);
        userData.put("admin", true); // El primer usuario siempre será el administrador
        userData.put("equipoId", userId); // El ID del equipo será el mismo que el ID del usuario

        // Guardamos los datos del usuario en la base de datos
        usuariosRef.child(userId).setValue(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Obtenemos una referencia a la ubicación de los equipos en la base de datos
                            DatabaseReference equiposRef = mDatabase.child("Equipos").child(userId);

                            // Creamos un mapa para almacenar los datos del equipo
                            Map<String, Object> equipoData = new HashMap<>();
                            equipoData.put("admin_id", userId); // ID del administrador
                            equipoData.put("nombre", nombreEquipo); // Nombre del equipo

                            // Creamos un mapa para almacenar los miembros del equipo
                            Map<String, Object> miembros = new HashMap<>();
                            miembros.put(userId, true); // Añadimos al administrador como miembro
                            equipoData.put("miembros", miembros); // Agregamos los miembros al mapa de datos del equipo

                            // Guardamos los datos del equipo en la base de datos
                            equiposRef.setValue(equipoData)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Ambas operaciones de guardado completadas con éxito
                                                Toast.makeText(CrearEquipo.this, "Usuario y equipo registrados exitosamente", Toast.LENGTH_SHORT).show();

                                                // Iniciar sesión automáticamente después de crear el usuario
                                                mAuth.signInWithEmailAndPassword(correo, password)
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    // Inicio de sesión exitoso, redirigir al usuario a la actividad Calendario
                                                                    startActivity(new Intent(CrearEquipo.this, Calendario.class));
                                                                    finish();
                                                                } else {
                                                                    // Error al iniciar sesión automáticamente
                                                                    Toast.makeText(CrearEquipo.this, "Error al iniciar sesión automáticamente: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            } else {
                                                // Error al guardar datos del equipo
                                                Toast.makeText(CrearEquipo.this, "Error al registrar el equipo: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Error al guardar datos del usuario
                            Toast.makeText(CrearEquipo.this, "Error al registrar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    public void goBack(View view) {
        onBackPressed();
    }
}
