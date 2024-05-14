package com.example.apptryline;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Conversaciones extends AppCompatActivity {

    private ArrayList<String> usuarios;
    private ArrayAdapter<String> adapter;
    private String equipoIdActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversaciones);

        ListView listView = findViewById(R.id.listViewUsuarios);
        usuarios = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, usuarios);
        listView.setAdapter(adapter);

        obtenerEquipoIdUsuarioActual();
    }

    private void obtenerEquipoIdUsuarioActual() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios").child(user.getUid()).child("equipoId");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                equipoIdActual = dataSnapshot.getValue(String.class);
                if (equipoIdActual != null) {
                    cargarUsuarios();
                } else {
                    Toast.makeText(Conversaciones.this, "El usuario no tiene equipo asignado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Conversaciones.this, "Error al obtener el equipo del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarUsuarios() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usuarios.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    String nombreUsuario = snapshot.child("nombreUsuario").getValue(String.class);
                    String equipoId = snapshot.child("equipoId").getValue(String.class);
                    if (nombreUsuario != null && equipoId != null && !uid.equals(FirebaseAuth.getInstance().getCurrentUser().getUid()) && equipoId.equals(equipoIdActual)) {
                        usuarios.add(nombreUsuario);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Conversaciones.this, "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
