package com.example.apptryline;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConsultarAlineacion extends AppCompatActivity {

    private TextView[] playerTextViews = new TextView[30];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alineacion);

        // Inicializar las referencias a los TextViews de los jugadores
        initPlayerTextViews();

        // Obtener el equipoId y partidoId de los extras del Intent
        String equipoId = getIntent().getStringExtra("equipoId");
        String partidoId = getIntent().getStringExtra("partidoId");

        if (equipoId != null && partidoId != null) {
            loadPlayerNames(equipoId, partidoId);
        }
    }

    private void initPlayerTextViews() {
        for (int i = 0; i < playerTextViews.length; i++) {
            String playerId = "player" + (i + 1);
            int resId = getResources().getIdentifier(playerId, "id", getPackageName());
            playerTextViews[i] = findViewById(resId);
        }
    }

    private void loadPlayerNames(String equipoId, String partidoId) {
        DatabaseReference alineacionRef = FirebaseDatabase.getInstance().getReference()
                .child("Equipos").child(equipoId).child("Partidos").child(partidoId).child("Alineacion");

        alineacionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (int i = 0; i < playerTextViews.length; i++) {
                        String playerKey = "Jugador" + (i + 1);
                        if (dataSnapshot.hasChild(playerKey)) {
                            String playerName = dataSnapshot.child(playerKey).child("nombre").getValue(String.class);
                            playerTextViews[i].setText(playerName);
                        } else {
                            playerTextViews[i].setText("");
                        }
                    }
                } else {
                    for (TextView playerTextView : playerTextViews) {
                        playerTextView.setText("");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar error de la base de datos
            }
        });
    }
}
