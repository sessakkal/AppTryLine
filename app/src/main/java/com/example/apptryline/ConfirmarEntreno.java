package com.example.apptryline;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ConfirmarEntreno extends AppCompatActivity {

    private ListView listViewConfirmaciones;
    private ConfirmacionAdapter confirmacionAdapter;
    private List<Confirmacion> confirmacionList;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmar_entreno);

        // Configuración del Toolbar
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        listViewConfirmaciones = findViewById(R.id.lista_confirmaciones);
        confirmacionList = new ArrayList<>();
        confirmacionAdapter = new ConfirmacionAdapter(this, confirmacionList);
        listViewConfirmaciones.setAdapter(confirmacionAdapter);

        String entrenoId = getIntent().getStringExtra("entrenoId");
        String equipoId = getIntent().getStringExtra("equipoId");

        if (entrenoId != null && equipoId != null) {
            loadConfirmaciones(equipoId, entrenoId);
        }
    }

    private void loadConfirmaciones(String equipoId, String entrenoId) {
        DatabaseReference confirmacionesRef = FirebaseDatabase.getInstance().getReference()
                .child("Equipos").child(equipoId).child("Entrenos").child(entrenoId).child("Confirmaciones");

        confirmacionesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                confirmacionList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Confirmacion confirmacion = snapshot.getValue(Confirmacion.class);
                    confirmacionList.add(confirmacion);
                }
                confirmacionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ConfirmarEntreno.this, "Error al cargar confirmaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
