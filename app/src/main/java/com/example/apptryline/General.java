package com.example.apptryline;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.apptryline.databinding.GeneralBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class General extends AppCompatActivity {

    private GeneralBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelChat> chatArrayList;
    private AdapterChat adapterChat;
    private FirebaseUser firebaseUser;
    private String equipoId;
    private String nombreEquipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GeneralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setStackFromEnd(true);
        binding.recyclerChat.setHasFixedSize(true);
        binding.recyclerChat.setLayoutManager(linearLayout);

        // Obtener el ID del equipo del Intent
        equipoId = getIntent().getStringExtra("equipoId");

        // Verificar si equipoId es nulo
        if (equipoId == null) {
            Toast.makeText(this, "ID de equipo no proporcionado", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si equipoId es nulo
            return;
        }

        // Configurar el botón de volver
        binding.botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Configurar el botón de enviar mensaje
        binding.botonEnviarMensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mensaje = binding.mensajeAEnviar.getText().toString().trim();
                if (!TextUtils.isEmpty(mensaje)) {
                    enviarMensaje(mensaje);
                }
            }
        });

        chatArrayList = new ArrayList<>();
        adapterChat = new AdapterChat(this, chatArrayList);
        binding.recyclerChat.setAdapter(adapterChat);

        obtenerNombreEquipo();
        leerMensajes();
    }

    private void obtenerNombreEquipo() {
        DatabaseReference equipoRef = FirebaseDatabase.getInstance().getReference("Equipos").child(equipoId);
        equipoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nombreEquipo = snapshot.child("nombre").getValue(String.class);
                if (nombreEquipo != null) {
                    binding.nombreEquipo.setText(nombreEquipo);
                } else {
                    binding.nombreEquipo.setText("Nombre no disponible");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(General.this, "Error al obtener el nombre del equipo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void leerMensajes() {
        DatabaseReference mensajesRef = FirebaseDatabase.getInstance().getReference("MensajesEquipos").child(equipoId);
        mensajesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelChat modelChat = dataSnapshot.getValue(ModelChat.class);
                    if (modelChat != null) {
                        chatArrayList.add(modelChat);
                    }
                }
                adapterChat.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(General.this, "Error al cargar mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarMensaje(String mensaje) {
        DatabaseReference mensajesRef = FirebaseDatabase.getInstance().getReference("MensajesEquipos").child(equipoId);
        String messageId = mensajesRef.push().getKey();
        String idMyUser = firebaseUser.getUid();
        ModelChat modelChat = new ModelChat(mensaje, idMyUser, equipoId, String.valueOf(System.currentTimeMillis()));

        if (messageId != null) {
            mensajesRef.child(messageId).setValue(modelChat)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(General.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                            binding.mensajeAEnviar.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(General.this, "Error al enviar el mensaje: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
