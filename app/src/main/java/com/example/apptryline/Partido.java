package com.example.apptryline;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Partido extends AppCompatActivity {

    private TextView textViewFecha, textViewHoraInicio, textViewUbicacionTexto, textViewEquipoLocal, textViewEquipoVisitante, textViewResultado, textViewMelesAFavor, textViewMelesEnContra, textViewTriesAFavor, textViewTriesEnContra;
    private ProgressBar progressBarMeles, progressBarTries;
    private TextView[] playerTextViews = new TextView[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partido);

        initViews();

        String partidoId = getIntent().getStringExtra("partidoId");
        if (partidoId != null) {
            loadPartidoDetails(partidoId);
        }
    }

    private void initViews() {
        textViewFecha = findViewById(R.id.fecha_partido);
        textViewHoraInicio = findViewById(R.id.hora_inicio_partido);
        textViewUbicacionTexto = findViewById(R.id.ubicacion_texto_partido);
        textViewEquipoLocal = findViewById(R.id.equipo_local_partido);
        textViewEquipoVisitante = findViewById(R.id.equipo_visitante_partido);
        textViewResultado = findViewById(R.id.resultado_partido);
        textViewMelesAFavor = findViewById(R.id.meles_a_favor_partido);
        textViewMelesEnContra = findViewById(R.id.meles_en_contra_partido);
        textViewTriesAFavor = findViewById(R.id.tries_a_favor_partido);
        textViewTriesEnContra = findViewById(R.id.tries_en_contra_partido);
        progressBarMeles = findViewById(R.id.progress_meles);
        progressBarTries = findViewById(R.id.tries_meles);

        for (int i = 0; i < playerTextViews.length; i++) {
            int resId = getResources().getIdentifier("player" + (i + 1), "id", getPackageName());
            playerTextViews[i] = findViewById(resId);
        }
    }

    private void loadPartidoDetails(String partidoId) {
        DatabaseReference partidoRef = FirebaseDatabase.getInstance().getReference().child("Equipos");

        partidoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot equipoSnapshot : dataSnapshot.getChildren()) {
                        if (equipoSnapshot.child("Partidos").hasChild(partidoId)) {
                            PartidoDatos partido = equipoSnapshot.child("Partidos").child(partidoId).getValue(PartidoDatos.class);

                            if (partido != null) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String fechaString = dateFormat.format(partido.getFecha());
                                textViewFecha.setText(fechaString);
                                textViewHoraInicio.setText(partido.getHoraInicio());
                                textViewUbicacionTexto.setText(partido.getUbicacionTexto());
                                textViewEquipoLocal.setText(partido.getEquipoLocal());
                                textViewEquipoVisitante.setText(partido.getEquipoVisitante());
                                textViewResultado.setText(partido.getResultado());
                                textViewMelesAFavor.setText(String.valueOf(partido.getMelesAFavor()));
                                textViewMelesEnContra.setText(String.valueOf(partido.getMelesEnContra()));
                                textViewTriesAFavor.setText(String.valueOf(partido.getTriesAFavor()));
                                textViewTriesEnContra.setText(String.valueOf(partido.getTriesEnContra()));

                                // Calcula el porcentaje para la progress bar de Meles
                                int totalMeles = partido.getMelesAFavor() + partido.getMelesEnContra();
                                if (totalMeles > 0) {
                                    int porcentajeMelesAFavor = (partido.getMelesAFavor() * 100) / totalMeles;
                                    progressBarMeles.setProgress(porcentajeMelesAFavor);
                                } else {
                                    progressBarMeles.setProgress(0);
                                }

                                // Calcula el porcentaje para la progress bar de Tries
                                int totalTries = partido.getTriesAFavor() + partido.getTriesEnContra();
                                if (totalTries > 0) {
                                    int porcentajeTriesAFavor = (partido.getTriesAFavor() * 100) / totalTries;
                                    progressBarTries.setProgress(porcentajeTriesAFavor);
                                } else {
                                    progressBarTries.setProgress(0);
                                }

                                // Cargar y mostrar los nombres de los jugadores
                                loadPlayerNames(partidoId);
                            }

                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void loadPlayerNames(String partidoId) {
        DatabaseReference alineacionRef = FirebaseDatabase.getInstance().getReference().child("Alineacion").child(partidoId);
        alineacionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (int i = 0; i < playerTextViews.length; i++) {
                        String playerKey = "Jugador" + (i + 1);
                        if (dataSnapshot.hasChild(playerKey)) {
                            String playerName = dataSnapshot.child(playerKey).child("nombre").getValue(String.class);
                            playerTextViews[i].setText(playerName);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
