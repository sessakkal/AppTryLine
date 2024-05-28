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
    private String equipoId = "equipoId"; // Supongamos que el equipoId es conocido por ahora

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partidos);

        recyclerViewPartidos = findViewById(R.id.recyclerViewPartidos);
        recyclerViewPartidos.setLayoutManager(new LinearLayoutManager(this));


        partidoAdapter = new PartidoAdapter(new ArrayList<>(), equipoId, this);
        recyclerViewPartidos.setAdapter(partidoAdapter);


        DatabaseReference partidosRef = FirebaseDatabase.getInstance().getReference("Equipos").child(equipoId).child("Partidos");
        partidosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> partidos = new ArrayList<>();
                for (DataSnapshot partidoSnapshot : dataSnapshot.getChildren()) {
                    String nombrePartido = partidoSnapshot.child("nombre").getValue(String.class);
                    partidos.add(nombrePartido);
                }
                partidoAdapter.updatePartidos(partidos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
