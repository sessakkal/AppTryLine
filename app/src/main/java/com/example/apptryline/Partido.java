package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private ImageView editarIcono, eliminarIcono;
    private FirebaseAuth mAuth;
    private String equipoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partido);

        mAuth = FirebaseAuth.getInstance();
        initViews();

        String partidoId = getIntent().getStringExtra("partidoId");
        if (partidoId != null) {
            loadPartidoDetails(partidoId);
            checkIfAdmin();
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
        editarIcono = findViewById(R.id.editar);
        eliminarIcono = findViewById(R.id.eliminar);

        for (int i = 0; i < playerTextViews.length; i++) {
            int resId = getResources().getIdentifier("player" + (i + 1), "id", getPackageName());
            playerTextViews[i] = findViewById(resId);
        }
    }

    private void checkIfAdmin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Boolean isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);
                        if (Boolean.TRUE.equals(isAdmin)) {
                            editarIcono.setVisibility(View.VISIBLE);
                            eliminarIcono.setVisibility(View.VISIBLE);
                        } else {
                            editarIcono.setVisibility(View.GONE);
                            eliminarIcono.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Partido.this, "Error al verificar administrador", Toast.LENGTH_SHORT).show();
                }
            });
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
                            equipoId = equipoSnapshot.getKey(); // Obtener el equipoId

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

                                int totalMeles = partido.getMelesAFavor() + partido.getMelesEnContra();
                                if (totalMeles > 0) {
                                    int porcentajeMelesAFavor = (partido.getMelesAFavor() * 100) / totalMeles;
                                    progressBarMeles.setProgress(porcentajeMelesAFavor);
                                } else {
                                    progressBarMeles.setProgress(0);
                                }

                                int totalTries = partido.getTriesAFavor() + partido.getTriesEnContra();
                                if (totalTries > 0) {
                                    int porcentajeTriesAFavor = (partido.getTriesAFavor() * 100) / totalTries;
                                    progressBarTries.setProgress(porcentajeTriesAFavor);
                                } else {
                                    progressBarTries.setProgress(0);
                                }

                                loadPlayerNames(equipoId, partidoId);
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

    private void loadPlayerNames(String equipoId, String partidoId) {
        DatabaseReference alineacionRef = FirebaseDatabase.getInstance().getReference().child("Equipos").child(equipoId).child("Partidos").child(partidoId).child("Alineacion");
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

    public void editarPartido(View view) {
        String partidoId = getIntent().getStringExtra("partidoId");
        Intent intent = new Intent(this, EditarPartido.class);
        intent.putExtra("partidoId", partidoId);
        intent.putExtra("equipoId", equipoId); // Pasar el equipoId
        startActivity(intent);
    }

    public void eliminarPartido(View view) {
        String partidoId = getIntent().getStringExtra("partidoId");
        DatabaseReference partidoRef = FirebaseDatabase.getInstance().getReference().child("Equipos");
        partidoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot equipoSnapshot : dataSnapshot.getChildren()) {
                        if (equipoSnapshot.child("Partidos").hasChild(partidoId)) {
                            equipoSnapshot.child("Partidos").child(partidoId).getRef().removeValue()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Partido.this, "Partido eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Partido.this, "Error al eliminar el partido", Toast.LENGTH_SHORT).show();
                                        }
                                    });
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
}
