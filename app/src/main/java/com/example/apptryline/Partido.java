package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Partido extends AppCompatActivity {

    private TextView textViewFecha, textViewHoraInicio, textViewUbicacionTexto, textViewEquipoLocal, textViewEquipoVisitante, resultadoVisitanteTextView, resultadoLocalTextView;
    private ImageView editarIcono, eliminarIcono;
    private FirebaseAuth mAuth;
    private String equipoId;
    private RecyclerView recyclerViewEstadisticas;
    private EstadisticasAdapter estadisticasAdapter;
    private List<Estadistica> estadisticasList;

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
        textViewFecha = findViewById(R.id.fecha_partido_cabecera);
        textViewHoraInicio = findViewById(R.id.hora_inicio_partido);
        textViewUbicacionTexto = findViewById(R.id.ubicacion_texto_partido);
        textViewEquipoLocal = findViewById(R.id.equipo_local_partido);
        textViewEquipoVisitante = findViewById(R.id.equipo_visitante_partido);
        resultadoLocalTextView = findViewById(R.id.resultado_local);
        resultadoVisitanteTextView = findViewById(R.id.resultado_visitante);
        editarIcono = findViewById(R.id.editar);
        eliminarIcono = findViewById(R.id.eliminar);

        recyclerViewEstadisticas = findViewById(R.id.recycler_view_estadisticas);
        recyclerViewEstadisticas.setLayoutManager(new LinearLayoutManager(this));
        estadisticasList = new ArrayList<>();
        estadisticasAdapter = new EstadisticasAdapter(this, estadisticasList);
        recyclerViewEstadisticas.setAdapter(estadisticasAdapter);
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
                                resultadoVisitanteTextView.setText(partido.getResultadovisitante());
                                resultadoLocalTextView.setText(partido.getResultadolocal());

                                // Cargar estadísticas
                                DatabaseReference estadisticasRef = equipoSnapshot.child("Partidos").child(partidoId).child("Estadisticas").getRef();
                                cargarEstadisticas(estadisticasRef);
                            }

                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Partido.this, "Error al cargar los detalles del partido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarEstadisticas(DatabaseReference estadisticasRef) {
        estadisticasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                estadisticasList.clear();
                for (DataSnapshot statSnapshot : dataSnapshot.getChildren()) {
                    Estadistica estadistica = statSnapshot.getValue(Estadistica.class);
                    if (estadistica != null) {
                        int totalDato = estadistica.getDatoFavor() + estadistica.getDatoContra();
                        if (totalDato > 0) {
                            int porcentajeDatoAFavor = (estadistica.getDatoFavor() * 100) / totalDato;
                            estadistica.setProgreso(porcentajeDatoAFavor);
                        } else {
                            estadistica.setProgreso(0);
                        }
                        estadisticasList.add(estadistica);
                    }
                }
                estadisticasAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Partido.this, "Error al cargar las estadísticas", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Partido.this, "Error al eliminar el partido", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
