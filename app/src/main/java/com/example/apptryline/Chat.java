package com.example.apptryline;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
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

public class Chat extends AppCompatActivity {

    private GeneralBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ModelChat> chatArrayList;
    private AdapterChat adapterChat;
    private FirebaseUser firebaseUser;
    private String otroUsuarioId;
    private String equipoId;
    private boolean isGroupChat;
    private ImageView fotoPerfil; // Añadir esta línea
    private TextView nombreTextView; // Cambiar el nombre de esta línea

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = GeneralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar la barra de herramientas
        Toolbar toolbar = findViewById(R.id.toolbarChat);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Inicializar la foto de perfil y nombre del equipo
        fotoPerfil = findViewById(R.id.fotoPerfil); // Añadir esta línea
        nombreTextView = findViewById(R.id.nombreEquipo); // Cambiar el nombre de esta línea

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setStackFromEnd(true);
        binding.recyclerChat.setHasFixedSize(true);
        binding.recyclerChat.setLayoutManager(linearLayout);

        // Obtener datos del Intent
        otroUsuarioId = getIntent().getStringExtra("otroUsuarioId");
        equipoId = getIntent().getStringExtra("equipoId");
        isGroupChat = getIntent().getBooleanExtra("isGroupChat", false);

        // Verificar si los datos son nulos
        if (otroUsuarioId == null && equipoId == null) {
            Toast.makeText(this, "Datos no proporcionados", Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad si los datos son nulos
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

        if (isGroupChat) {
            obtenerNombreEquipo();
            leerMensajesGrupo();
        } else {
            cargarFotoPerfil();
            obtenerNombreUsuario(); // Añadir esta línea
            leerMensajesIndividuales();
        }
    }

    private void leerMensajesIndividuales() {
        DatabaseReference mensajesRef = FirebaseDatabase.getInstance().getReference("MensajesIndividuales")
                .child(firebaseUser.getUid()).child(otroUsuarioId);
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
                Toast.makeText(Chat.this, "Error al cargar mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void leerMensajesGrupo() {
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
                Toast.makeText(Chat.this, "Error al cargar mensajes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enviarMensaje(String mensaje) {
        DatabaseReference mensajesRef;
        if (isGroupChat) {
            mensajesRef = FirebaseDatabase.getInstance().getReference("MensajesEquipos").child(equipoId);
        } else {
            mensajesRef = FirebaseDatabase.getInstance().getReference("MensajesIndividuales")
                    .child(firebaseUser.getUid()).child(otroUsuarioId);
        }

        String messageId = mensajesRef.push().getKey();
        ModelChat modelChat = new ModelChat(mensaje, firebaseUser.getUid(), isGroupChat ? equipoId : otroUsuarioId, String.valueOf(System.currentTimeMillis()));

        if (messageId != null) {
            mensajesRef.child(messageId).setValue(modelChat)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(Chat.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                            binding.mensajeAEnviar.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Chat.this, "Error al enviar el mensaje: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void cargarFotoPerfil() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(otroUsuarioId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fotoPerfilUrl = snapshot.child("fotoPerfil").getValue(String.class);
                if (fotoPerfilUrl != null && !fotoPerfilUrl.isEmpty()) {
                    Glide.with(Chat.this).load(fotoPerfilUrl).into(fotoPerfil); // Modificar esta línea
                } else {
                    fotoPerfil.setImageResource(R.drawable.perfil_predeterminado); // Modificar esta línea
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                fotoPerfil.setImageResource(R.drawable.perfil_predeterminado); // Modificar esta línea
            }
        });
    }

    private void obtenerNombreEquipo() {
        DatabaseReference equipoRef = FirebaseDatabase.getInstance().getReference("Equipos").child(equipoId);
        equipoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nombreEquipoString = snapshot.child("nombre").getValue(String.class); // Cambiar el nombre de esta línea
                if (nombreEquipoString != null) {
                    nombreTextView.setText(nombreEquipoString); // Cambiar el nombre de esta línea
                } else {
                    nombreTextView.setText("Nombre no disponible"); // Cambiar el nombre de esta línea
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this, "Error al obtener el nombre del equipo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerNombreUsuario() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Usuarios").child(otroUsuarioId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nombreUsuario = snapshot.child("nombreUsuario").getValue(String.class);
                if (nombreUsuario != null) {
                    nombreTextView.setText(nombreUsuario); // Cambiar el nombre de esta línea
                } else {
                    nombreTextView.setText("Nombre no disponible"); // Cambiar el nombre de esta línea
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Chat.this, "Error al obtener el nombre del usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
