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
    private List<PartidoDatos> partidos;
    private String equipoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_partidos);

        recyclerViewPartidos = findViewById(R.id.recyclerViewPartidos);
        recyclerViewPartidos.setLayoutManager(new LinearLayoutManager(this));

        partidos = new ArrayList<>();
        partidoAdapter = new PartidoAdapter(partidos, this);
        recyclerViewPartidos.setAdapter(partidoAdapter);

        cargarPartidosDesdeFirebase();
    }

    private void cargarPartidosDesdeFirebase() {
        DatabaseReference partidosRef = FirebaseDatabase.getInstance().getReference("Equipos").child(equipoId).child("Partidos");
        partidosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                partidos.clear();
                for (DataSnapshot partidoSnapshot : dataSnapshot.getChildren()) {
                    PartidoDatos partido = partidoSnapshot.getValue(PartidoDatos.class);
                    partidos.add(partido);
                }
                partidoAdapter.updatePartidos(partidos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error de la base de datos
            }
        });
    }
}
