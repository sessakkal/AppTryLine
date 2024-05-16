package com.example.apptryline;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaPartidos extends AppCompatActivity {

    private RecyclerView recyclerViewPartidos;
    private PartidoAdapter partidoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partidos);

        recyclerViewPartidos = findViewById(R.id.recyclerViewPartidos);
        recyclerViewPartidos.setLayoutManager(new LinearLayoutManager(this));

        // Configurar el adaptador con una lista vacía por ahora
        partidoAdapter = new PartidoAdapter(new ArrayList<>());
        recyclerViewPartidos.setAdapter(partidoAdapter);

        // Obtener la referencia a la base de datos de Firebase donde se almacenan los partidos
        // Obtener la referencia a la base de datos de Firebase donde se almacenan los partidos
        DatabaseReference partidosRef = FirebaseDatabase.getInstance().getReference("Equipos").child("TuEquipoId").child("Partidos");
        partidosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> partidos = new ArrayList<>(); // Lista para almacenar los nombres de los partidos
                for (DataSnapshot partidoSnapshot : dataSnapshot.getChildren()) {
                    String nombrePartido = partidoSnapshot.child("nombre").getValue(String.class);
                    partidos.add(nombrePartido);
                }
                // Actualizar el adaptador con los datos obtenidos de Firebase
                partidoAdapter.updatePartidos(partidos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error si la lectura de datos es cancelada
            }
        });

    }
}
