package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CrearEntreno extends AppCompatActivity {

    private EditText nombreEditText, fechaEditText, horaInicioEditText, horaFinEditText, lugarEditText;
    private Button anadirEntrenoButton;
    private FirebaseAuth mAuth;
    private DatabaseReference equiposRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_entreno);

        mAuth = FirebaseAuth.getInstance();
        equiposRef = FirebaseDatabase.getInstance().getReference().child("Equipos");

        nombreEditText = findViewById(R.id.nombre);
        fechaEditText = findViewById(R.id.fecha);
        horaInicioEditText = findViewById(R.id.hora_inicio);
        horaFinEditText = findViewById(R.id.hora_fin);
        lugarEditText = findViewById(R.id.lugar);
        anadirEntrenoButton = findViewById(R.id.anadirpartido);

        anadirEntrenoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerEquipoIdYCrearEntreno();
            }
        });
    }

    private void obtenerEquipoIdYCrearEntreno() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String equipoId = dataSnapshot.child("equipoId").getValue(String.class);
                        if (equipoId != null && !equipoId.isEmpty()) {
                            crearEntreno(equipoId);
                        } else {
                            Toast.makeText(CrearEntreno.this, "El usuario no tiene un equipo asignado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CrearEntreno.this, "No se encontraron datos para el usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(CrearEntreno.this, "Error al obtener el ID del equipo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void crearEntreno(String equipoId) {
        String nombre = nombreEditText.getText().toString().trim();
        String fechaString = fechaEditText.getText().toString().trim();
        String horaInicio = horaInicioEditText.getText().toString().trim();
        String horaFin = horaFinEditText.getText().toString().trim();
        String lugar = lugarEditText.getText().toString().trim();

        // Validar que se ingresen todos los campos
        if (nombre.isEmpty() || fechaString.isEmpty() || horaInicio.isEmpty() || horaFin.isEmpty() || lugar.isEmpty()) {
            Toast.makeText(CrearEntreno.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir la cadena de fecha a formato Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fecha = null;
        try {
            fecha = dateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(CrearEntreno.this, "Error al convertir la fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto EntrenoDatos con los datos proporcionados
        EntrenoDatos entreno = new EntrenoDatos(null, nombre, fecha, horaInicio, horaFin, lugar);

        // Guardar el entreno en la base de datos dentro del equipo
        DatabaseReference entrenoRef = equiposRef.child(equipoId).child("Entrenos").push();
        String entrenoId = entrenoRef.getKey();
        entreno.setId(entrenoId); // Asignar el ID generado al objeto EntrenoDatos

        entrenoRef.setValue(entreno).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CrearEntreno.this, "Entreno creado exitosamente", Toast.LENGTH_SHORT).show();
                    // Redirigir al usuario a la pantalla de calendario después de crear el entreno
                    Intent intent = new Intent(CrearEntreno.this, Calendario.class);
                    startActivity(intent);
                    finish(); // Esto evita que el usuario pueda volver atrás con el botón de retroceso y volver a la pantalla de creación de entreno
                } else {
                    Toast.makeText(CrearEntreno.this, "Error al crear el entreno", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
