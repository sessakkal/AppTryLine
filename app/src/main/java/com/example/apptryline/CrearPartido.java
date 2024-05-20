package com.example.apptryline;

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
        String fecha = fechaEditText.getText().toString().trim();
        String horaInicio = horaInicioEditText.getText().toString().trim();
        String coordenadas = coordenadasEditText.getText().toString().trim();
        String ubicacionTexto = ubicacionTextoEditText.getText().toString().trim();
        String equipoLocal = equipoLocalEditText.getText().toString().trim();
        String equipoVisitante = equipoVisitanteEditText.getText().toString().trim();

        if (fecha.isEmpty() || horaInicio.isEmpty() || coordenadas.isEmpty() || ubicacionTexto.isEmpty() || equipoLocal.isEmpty() || equipoVisitante.isEmpty()) {
            Toast.makeText(CrearPartido.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference partidoRef = equiposRef.child(equipoId).child("Partidos").push();
        String partidoId = partidoRef.getKey();

        PartidoDatos partido = new PartidoDatos(partidoId, fecha, horaInicio, coordenadas, ubicacionTexto, equipoLocal, equipoVisitante);

        partidoRef.setValue(partido).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CrearPartido.this, "Partido creado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CrearPartido.this, "Error al crear el partido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
