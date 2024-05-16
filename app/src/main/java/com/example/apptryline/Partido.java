package com.example.apptryline;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Partido extends AppCompatActivity {

    private TextView fechaTextView;
    private TextView horaInicioTextView;
    private TextView coordenadasTextView;
    private TextView ubicacionTextoTextView;
    private TextView equipoLocalTextView;
    private TextView equipoVisitanteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partido); // Reemplaza "tu_layout" con el nombre de tu archivo XML

        // Inicializar vistas
        fechaTextView = findViewById(R.id.fecha_partido);
        horaInicioTextView = findViewById(R.id.hora_inicio_partido);
        coordenadasTextView = findViewById(R.id.coordenadas_partido);
        ubicacionTextoTextView = findViewById(R.id.ubicacion_texto_partido);
        equipoLocalTextView = findViewById(R.id.equipo_local_partido);
        equipoVisitanteTextView = findViewById(R.id.equipo_visitante_partido);

        // Obtener el ID del partido enviado desde la actividad anterior
        String partidoId = getIntent().getStringExtra("partidoId");

        // Obtener una referencia a la base de datos de Firebase
        DatabaseReference partidoRef = FirebaseDatabase.getInstance().getReference().child("Equipos").child("TuEquipoId").child("Partidos").child(partidoId);

        // Leer los datos del partido desde Firebase
        partidoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Obtener los datos del partido
                    String fecha = dataSnapshot.child("fecha").getValue(String.class);
                    String horaInicio = dataSnapshot.child("horaInicio").getValue(String.class);
                    String coordenadas = dataSnapshot.child("coordenadas").getValue(String.class);
                    String ubicacionTexto = dataSnapshot.child("ubicacionTexto").getValue(String.class);
                    String equipoLocal = dataSnapshot.child("equipoLocal").getValue(String.class);
                    String equipoVisitante = dataSnapshot.child("equipoVisitante").getValue(String.class);

                    // Establecer los datos en las vistas
                    fechaTextView.setText(fecha);
                    horaInicioTextView.setText(horaInicio);
                    coordenadasTextView.setText(coordenadas);
                    ubicacionTextoTextView.setText(ubicacionTexto);
                    equipoLocalTextView.setText(equipoLocal);
                    equipoVisitanteTextView.setText(equipoVisitante);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error si la lectura de datos es cancelada
            }
        });
    }
}



