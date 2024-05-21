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

public class CrearPartido extends AppCompatActivity {

    private EditText fechaEditText, horaInicioEditText, coordenadasEditText, ubicacionTextoEditText, equipoLocalEditText, equipoVisitanteEditText;
    private Button anadirPartidoButton;
    private FirebaseAuth mAuth;
    private DatabaseReference equiposRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_partido);

        mAuth = FirebaseAuth.getInstance();
        equiposRef = FirebaseDatabase.getInstance().getReference().child("Equipos");

        fechaEditText = findViewById(R.id.fecha);
        horaInicioEditText = findViewById(R.id.hora_inicio);
        coordenadasEditText = findViewById(R.id.coordenadas);
        ubicacionTextoEditText = findViewById(R.id.ubi_texto);
        equipoLocalEditText = findViewById(R.id.equipolocal);
        equipoVisitanteEditText = findViewById(R.id.equipovisitante);
        anadirPartidoButton = findViewById(R.id.anadirpartido);

        anadirPartidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerEquipoId();
            }
        });
    }

    private void obtenerEquipoId() {
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
                            crearPartido(equipoId);
                        } else {
                            Toast.makeText(CrearPartido.this, "El usuario no tiene un equipo asignado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CrearPartido.this, "No se encontraron datos para el usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(CrearPartido.this, "Error al obtener el ID del equipo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void crearPartido(String equipoId) {
        String fechaString = fechaEditText.getText().toString().trim();
        String horaInicio = horaInicioEditText.getText().toString().trim();
        String coordenadas = coordenadasEditText.getText().toString().trim();
        String ubicacionTexto = ubicacionTextoEditText.getText().toString().trim();
        String equipoLocal = equipoLocalEditText.getText().toString().trim();
        String equipoVisitante = equipoVisitanteEditText.getText().toString().trim();

        // Validar que se ingresen todos los campos
        if (fechaString.isEmpty() || horaInicio.isEmpty() || coordenadas.isEmpty() || ubicacionTexto.isEmpty() || equipoLocal.isEmpty() || equipoVisitante.isEmpty()) {
            Toast.makeText(CrearPartido.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir la cadena de fecha a formato Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fecha = null;
        try {
            fecha = dateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(CrearPartido.this, "Error al convertir la fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto PartidoDatos con los datos proporcionados
        PartidoDatos partido = new PartidoDatos(null, fecha, horaInicio, coordenadas, ubicacionTexto, equipoLocal, equipoVisitante);

        // Guardar el partido en la base de datos
        DatabaseReference partidoRef = equiposRef.child(equipoId).child("Partidos").push();
        String partidoId = partidoRef.getKey();
        partido.setId(partidoId); // Asignar el ID generado al objeto PartidoDatos

        partidoRef.setValue(partido).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CrearPartido.this, "Partido creado exitosamente", Toast.LENGTH_SHORT).show();
                    // Redirigir al usuario a la pantalla de calendario después de crear el partido
                    Intent intent = new Intent(CrearPartido.this, Calendario.class);
                    startActivity(intent);
                    finish(); // Esto evita que el usuario pueda volver atrás con el botón de retroceso y volver a la pantalla de creación de partido
                } else {
                    Toast.makeText(CrearPartido.this, "Error al crear el partido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
