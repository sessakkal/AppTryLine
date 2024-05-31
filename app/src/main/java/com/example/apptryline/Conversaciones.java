package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
    private TextView noDataMessage;  // Añadir esta línea

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversaciones);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Remove default title to use custom title
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Ensure the toolbar title color is white
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setTextColor(getResources().getColor(android.R.color.white));

        ListView listView = findViewById(R.id.listViewUsuarios);
        noDataMessage = findViewById(R.id.no_data_message);  // Añadir esta línea

        usuarios = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.lista_conversaciones, R.id.text1, usuarios);
        listView.setAdapter(adapter);

        obtenerEquipoIdUsuarioActual();

        // Agregar un listener al ListView para manejar la selección de usuarios
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String usuarioSeleccionado = usuarios.get(position);
                // Obtener el ID del usuario seleccionado y abrir la actividad de conversación individual
                abrirConversacionIndividual(usuarioSeleccionado);
            }
        });
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

                if (usuarios.isEmpty()) {
                    noDataMessage.setVisibility(View.VISIBLE);
                } else {
                    noDataMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Conversaciones.this, "Error al cargar usuarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void abrirConversacionIndividual(String nombreUsuario) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uid = snapshot.getKey();
                    String nombreUsuarioFirebase = snapshot.child("nombreUsuario").getValue(String.class);
                    if (nombreUsuarioFirebase != null && nombreUsuarioFirebase.equals(nombreUsuario)) {
                        // Obtener el ID del usuario seleccionado y abrir la actividad de conversación individual
                        String idUsuarioSeleccionado = uid;
                        Intent intent = new Intent(Conversaciones.this, Chat.class);
                        intent.putExtra("otroUsuarioId", idUsuarioSeleccionado); // Aquí se pasa el ID del usuario seleccionado
                        startActivity(intent);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Conversaciones.this, "Error al abrir la conversación individual", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void irPartidos(View view) {
        Intent intent = new Intent(this, MainPartidos.class);
        startActivity(intent);
    }

    public void irEntrenos(View view) {
        Intent intent = new Intent(this, MainEntrenos.class);
        startActivity(intent);
    }

    public void irPerfil(View view) {
        Intent intent = new Intent(this, EditarPerfil.class);
        startActivity(intent);
    }

    public void irConversaciones(View view) {
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

    public void irGlobal(View view) {
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

    public void goBack(View view) {
        onBackPressed();
    }
}
