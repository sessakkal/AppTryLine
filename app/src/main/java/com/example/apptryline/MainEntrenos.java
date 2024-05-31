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

public class MainEntrenos extends AppCompatActivity {

    private RecyclerView entrenosRecyclerView;
    private EntrenoAdapter entrenoAdapter;
    private List<String> entrenosIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_entrenos);

        initWidgets();
        checkAdminStatus();
        cargarEntrenosDesdeFirebase();
    }

    private void checkAdminStatus() {
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
                            boton1.setVisibility(View.VISIBLE);
                        } else {
                            boton1.setVisibility(View.GONE);
                        }
                    } else {
                        boton1.setVisibility(View.GONE);
                    }
                    setUpButtons();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainEntrenos.this, "Error al verificar el estado de administrador", Toast.LENGTH_SHORT).show();
                    boton1.setVisibility(View.GONE);
                    setUpButtons();
                }
            });
        } else {
            boton1.setVisibility(View.GONE);
            setUpButtons();
        }
    }

    private void setUpButtons() {
        Button boton1 = findViewById(R.id.boton1);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainEntrenos.this, CrearEntreno.class));
            }
        });
    }

    private void initWidgets() {
        entrenosRecyclerView = findViewById(R.id.entrenosRecyclerView);
        entrenosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        entrenoAdapter = new EntrenoAdapter(entrenosIds, "", this);
        entrenosRecyclerView.setAdapter(entrenoAdapter);
    }

    private void cargarEntrenosDesdeFirebase() {
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
                            cargarEntrenosEquipo(equipoId);
                        } else {
                            Toast.makeText(MainEntrenos.this, "El usuario no tiene un equipo asignado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainEntrenos.this, "No se encontraron datos para el usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainEntrenos.this, "Error al obtener el ID del equipo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void cargarEntrenosEquipo(String equipoId) {
        DatabaseReference entrenosRef = FirebaseDatabase.getInstance().getReference().child("Equipos").child(equipoId).child("Entrenos");
        entrenosRef.addValueEventListener(new ValueEventListener() {
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
                    Toast.makeText(MainEntrenos.this, "No hay entrenos programados para el equipo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainEntrenos.this, "Error al cargar los entrenos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
