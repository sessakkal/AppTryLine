package com.example.apptryline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Calendario extends AppCompatActivity {

    private RecyclerView partidosRecyclerView;
    private RecyclerView entrenosRecyclerView;
    private PartidoAdapter partidoAdapter;
    private EntrenoAdapter entrenoAdapter;
    private List<String> partidosIds = new ArrayList<>();
    private List<String> entrenosIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);

        initWidgets();

        checkAdminStatus();
        cargarPartidosYEntrenosDesdeFirebase();
    }

    private void checkAdminStatus() {
        Button boton4 = findViewById(R.id.boton4);
        Button boton1 = findViewById(R.id.boton1);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Boolean isAdmin = snapshot.child("admin").getValue(Boolean.class);
                        if (isAdmin != null && isAdmin) {
                            boton4.setVisibility(View.VISIBLE);
                            boton1.setVisibility(View.VISIBLE);
                        } else {
                            boton4.setVisibility(View.GONE);
                            boton1.setVisibility(View.GONE);
                        }
                    } else {
                        boton4.setVisibility(View.GONE);
                        boton1.setVisibility(View.GONE);
                    }
                    setUpButtons();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Calendario.this, "Error al verificar el estado de administrador", Toast.LENGTH_SHORT).show();
                    boton4.setVisibility(View.GONE);
                    boton1.setVisibility(View.GONE);
                    setUpButtons();
                }
            });
        } else {
            boton4.setVisibility(View.GONE);
            boton1.setVisibility(View.GONE);
            setUpButtons();
        }
    }

    private void setUpButtons() {
        Button boton4 = findViewById(R.id.boton4);
        boton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calendario.this, CrearPartido.class));
            }
        });

        Button boton1 = findViewById(R.id.boton1);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calendario.this, CrearEntreno.class));
            }
        });
    }

    private void initWidgets() {
        partidosRecyclerView = findViewById(R.id.partidosRecyclerView);
        entrenosRecyclerView = findViewById(R.id.entrenosRecyclerView);

        partidosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        partidoAdapter = new PartidoAdapter(partidosIds, "", this); // Inicializar el adapter aquí
        partidosRecyclerView.setAdapter(partidoAdapter);

        entrenosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        entrenoAdapter = new EntrenoAdapter(entrenosIds, "", this); // Inicializar el adapter aquí
        entrenosRecyclerView.setAdapter(entrenoAdapter);
    }

    public void onOption4Click(View view) {
        Intent intent = new Intent(this, EditarPerfil.class);
        startActivity(intent);
    }

    public void onOption2Click(View view) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usuarioActualRef = usuariosRef.child(userId);

        usuarioActualRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("equipoId")) {
                    String equipoId = dataSnapshot.child("equipoId").getValue(String.class);
                    if (equipoId != null && !equipoId.isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), Conversaciones.class);
                        intent.putExtra("equipoId", equipoId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error al obtener el ID del equipo: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onOption3Click(View view) {
        Intent intent = new Intent(this, Calendario.class);
        startActivity(intent);
    }

    public void onOption1Click(View view) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usuarioActualRef = usuariosRef.child(userId);

        usuarioActualRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("equipoId")) {
                    String equipoId = dataSnapshot.child("equipoId").getValue(String.class);
                    if (equipoId != null && !equipoId.isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), General.class);
                        intent.putExtra("equipoId", equipoId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error al obtener el ID del equipo: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerViews(String equipoId) {
        partidoAdapter = new PartidoAdapter(partidosIds, equipoId, this); // Asegúrate de inicializar el adapter con el equipoId correcto
        partidosRecyclerView.setAdapter(partidoAdapter);

        entrenoAdapter = new EntrenoAdapter(entrenosIds, equipoId, this); // Asegúrate de inicializar el adapter con el equipoId correcto
        entrenosRecyclerView.setAdapter(entrenoAdapter);
    }

    private void cargarPartidosYEntrenosDesdeFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String equipoId = snapshot.child("equipoId").getValue(String.class);
                        if (equipoId != null && !equipoId.isEmpty()) {
                            cargarPartidosEquipo(equipoId);
                            cargarEntrenosEquipo(equipoId);
                            setUpRecyclerViews(equipoId); // Asegúrate de llamar esto aquí
                        } else {
                            Toast.makeText(Calendario.this, "El usuario no tiene un equipo asignado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Calendario.this, "No se encontraron datos para el usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Calendario.this, "Error al obtener el ID del equipo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void cargarPartidosEquipo(String equipoId) {
        DatabaseReference partidosRef = FirebaseDatabase.getInstance().getReference().child("Equipos").child(equipoId).child("Partidos");
        partidosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    partidosIds.clear();
                    for (DataSnapshot partidoSnapshot : snapshot.getChildren()) {
                        String partidoId = partidoSnapshot.getKey();
                        partidosIds.add(partidoId);
                    }
                    partidoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Calendario.this, "No hay partidos programados para el equipo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Calendario.this, "Error al cargar los partidos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarEntrenosEquipo(String equipoId) {
        DatabaseReference entrenosRef = FirebaseDatabase.getInstance().getReference().child("Equipos").child(equipoId).child("Entrenos");
        entrenosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    entrenosIds.clear();
                    for (DataSnapshot entrenoSnapshot : snapshot.getChildren()) {
                        String entrenoId = entrenoSnapshot.getKey();
                        entrenosIds.add(entrenoId);
                    }
                    entrenoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Calendario.this, "No hay entrenos programados para el equipo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Calendario.this, "Error al cargar los entrenos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
