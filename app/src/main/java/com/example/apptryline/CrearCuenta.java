package com.example.apptryline;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CrearCuenta extends Activity {

    private EditText editTextCorreo;
    private EditText editTextContraseña;
    private EditText editTextRepetirContraseña;
    private Button buttonRegistrar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_user1);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Referenciar los elementos de la interfaz de usuario
        editTextCorreo = findViewById(R.id.correo_electronico);
        editTextContraseña = findViewById(R.id.contraseña);
        editTextRepetirContraseña = findViewById(R.id.repetir_contraseña);
        buttonRegistrar = findViewById(R.id.registerButton);

        // Configurar el evento de clic del botón de registro
        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    // Método para registrar un nuevo usuario
    private void registrarUsuario() {
        String correo = editTextCorreo.getText().toString().trim();
        String contraseña = editTextContraseña.getText().toString().trim();
        String repetirContraseña = editTextRepetirContraseña.getText().toString().trim();

        // Validar que los campos no estén vacíos
        if (correo.isEmpty() || contraseña.isEmpty() || repetirContraseña.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que las contraseñas coincidan
        if (!contraseña.equals(repetirContraseña)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // El usuario se ha registrado correctamente
                        FirebaseUser user = mAuth.getCurrentUser();
                        // Aquí puedes realizar acciones adicionales, como enviar un correo de verificación
                        Toast.makeText(CrearCuenta.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                        // Cerrar la actividad de creación de cuenta
                        finish();
                    } else {
                        // Si hubo un error al registrar el usuario, mostrar un mensaje de error
                        Toast.makeText(CrearCuenta.this, "Error al registrar el usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
