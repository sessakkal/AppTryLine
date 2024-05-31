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

public class ListaEntrenos extends AppCompatActivity {

    private RecyclerView recyclerViewEntrenos;
    private EntrenoAdapter entrenoAdapter;
    private List<EntrenoDatos> entrenos;
    private String equipoId = "equipoId"; // Supongamos que el equipoId es conocido por ahora

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_entrenos);

        recyclerViewEntrenos = findViewById(R.id.recyclerViewEntrenos);
        recyclerViewEntrenos.setLayoutManager(new LinearLayoutManager(this));

        entrenos = new ArrayList<>();
        entrenoAdapter = new EntrenoAdapter(entrenos, this);
        recyclerViewEntrenos.setAdapter(entrenoAdapter);

        cargarEntrenosDesdeFirebase();
    }

    private void cargarEntrenosDesdeFirebase() {
        DatabaseReference entrenosRef = FirebaseDatabase.getInstance().getReference("Equipos").child(equipoId).child("Entrenos");
        entrenosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                entrenos.clear();
                for (DataSnapshot entrenoSnapshot : dataSnapshot.getChildren()) {
                    EntrenoDatos entreno = entrenoSnapshot.getValue(EntrenoDatos.class);
                    entrenos.add(entreno);
                }
                entrenoAdapter.updateEntrenos(entrenos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error de la base de datos
            }
        });
    }
}
