package com.example.apptryline;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Partido extends AppCompatActivity {

    private TextView textViewFecha, textViewHoraInicio, textViewCoordenadas, textViewUbicacionTexto, textViewEquipoLocal, textViewEquipoVisitante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partido);

        initViews();

        String partidoId = getIntent().getStringExtra("partidoId");
        if (partidoId != null) {
            // Load partido details using the partidoId
            loadPartidoDetails(partidoId);
        }
    }

    private void initViews() {
        textViewFecha = findViewById(R.id.fecha_partido);
        textViewHoraInicio = findViewById(R.id.hora_inicio_partido);
        textViewCoordenadas = findViewById(R.id.coordenadas_partido);
        textViewUbicacionTexto = findViewById(R.id.ubicacion_texto_partido);
        textViewEquipoLocal = findViewById(R.id.equipo_local_partido);
        textViewEquipoVisitante = findViewById(R.id.equipo_visitante_partido);
    }

    private void loadPartidoDetails(String partidoId) {
        DatabaseReference partidoRef = FirebaseDatabase.getInstance().getReference()
                .child("Equipos");

        partidoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot equipoSnapshot : dataSnapshot.getChildren()) {
                        if (equipoSnapshot.child("Partidos").hasChild(partidoId)) {
                            // Se encontró el equipo que contiene el partido
                            PartidoDatos partido = equipoSnapshot.child("Partidos").child(partidoId).getValue(PartidoDatos.class);

                            // Actualizar las vistas con los datos del partido
                            textViewFecha.setText(partido.getFecha());
                            textViewHoraInicio.setText(partido.getHoraInicio());
                            textViewCoordenadas.setText(partido.getCoordenadas());
                            textViewUbicacionTexto.setText(partido.getUbicacionTexto());
                            textViewEquipoLocal.setText(partido.getEquipoLocal());
                            textViewEquipoVisitante.setText(partido.getEquipoVisitante());

                            // Salir del bucle una vez que se haya encontrado y mostrado el partido
                            break;
                        }
                    }
                } else {
                    // Manejar el caso en que no se encuentren datos para el partido
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de lectura de datos
            }
        });
    }

    public void goBack(View view) {
        onBackPressed();
    }
}

