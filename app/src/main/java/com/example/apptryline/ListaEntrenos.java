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
    private String equipoId = "equipoId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_entrenos);

        recyclerViewEntrenos = findViewById(R.id.recyclerViewEntrenos);
        recyclerViewEntrenos.setLayoutManager(new LinearLayoutManager(this));

        entrenoAdapter = new EntrenoAdapter(new ArrayList<>(), equipoId, this);
        recyclerViewEntrenos.setAdapter(entrenoAdapter);

        DatabaseReference entrenosRef = FirebaseDatabase.getInstance().getReference("Equipos").child(equipoId).child("Entrenos");
        entrenosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> entrenos = new ArrayList<>();
                for (DataSnapshot entrenoSnapshot : dataSnapshot.getChildren()) {
                    String entrenoId = entrenoSnapshot.getKey();
                    entrenos.add(entrenoId);
                }
                entrenoAdapter.updateEntrenos(entrenos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
