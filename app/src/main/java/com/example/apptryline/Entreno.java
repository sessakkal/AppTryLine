package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Entreno extends AppCompatActivity {

    private TextView textViewFecha, textViewHoraInicio, textViewHoraFin, textViewLugar;
    private CheckBox checkBoxConfirmar;
    private EditText editTextComentario;
    private ImageView editarIcono, eliminarIcono, listaIcono;
    private FirebaseAuth mAuth;
    private String equipoId;
    private String entrenoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entreno);

        mAuth = FirebaseAuth.getInstance();
        initViews();

        entrenoId = getIntent().getStringExtra("entrenoId");
        if (entrenoId != null) {
            loadEntrenoDetails(entrenoId);
            checkIfAdmin();
        }
    }

    private void initViews() {
        textViewFecha = findViewById(R.id.fecha_entreno);
        textViewHoraInicio = findViewById(R.id.hora_inicio_entreno);
        textViewHoraFin = findViewById(R.id.hora_fin_entreno);
        textViewLugar = findViewById(R.id.lugar_entreno);
        checkBoxConfirmar = findViewById(R.id.checkbox_confirmar);
        editTextComentario = findViewById(R.id.comentario_entreno);
        editarIcono = findViewById(R.id.editar);
        eliminarIcono = findViewById(R.id.eliminar);
        listaIcono = findViewById(R.id.lista);
    }

    private void checkIfAdmin() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Boolean isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);
                        if (Boolean.TRUE.equals(isAdmin)) {
                            editarIcono.setVisibility(View.VISIBLE);
                            eliminarIcono.setVisibility(View.VISIBLE);
                            listaIcono.setVisibility(View.VISIBLE);
                        } else {
                            editarIcono.setVisibility(View.GONE);
                            eliminarIcono.setVisibility(View.GONE);
                            listaIcono.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(Entreno.this, "Error al verificar administrador", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadEntrenoDetails(String entrenoId) {
        DatabaseReference entrenoRef = FirebaseDatabase.getInstance().getReference().child("Equipos");

        entrenoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot equipoSnapshot : dataSnapshot.getChildren()) {
                        if (equipoSnapshot.child("Entrenos").hasChild(entrenoId)) {
                            EntrenoDatos entreno = equipoSnapshot.child("Entrenos").child(entrenoId).getValue(EntrenoDatos.class);
                            equipoId = equipoSnapshot.getKey(); // Obtener el equipoId

                            if (entreno != null) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                String fechaString = dateFormat.format(entreno.getFecha());
                                textViewFecha.setText(fechaString);
                                textViewHoraInicio.setText(entreno.getHoraInicio());
                                textViewHoraFin.setText(entreno.getHoraFin());
                                textViewLugar.setText(entreno.getLugar());
                                checkBoxConfirmar.setChecked(Boolean.TRUE.equals(entreno.getConfirmado()));
                                editTextComentario.setText(entreno.getComentario());
                            }

                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    public void goBack(View view) {
        onBackPressed();
    }

    public void editarEntreno(View view) {
        Intent intent = new Intent(this, EditarEntreno.class);
        intent.putExtra("entrenoId", entrenoId);
        intent.putExtra("equipoId", equipoId); // Pasar el equipoId
        startActivity(intent);
    }

    public void eliminarEntreno(View view) {
        DatabaseReference entrenoRef = FirebaseDatabase.getInstance().getReference().child("Equipos");
        entrenoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot equipoSnapshot : dataSnapshot.getChildren()) {
                        if (equipoSnapshot.child("Entrenos").hasChild(entrenoId)) {
                            equipoSnapshot.child("Entrenos").child(entrenoId).getRef().removeValue()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Entreno.this, "Entreno eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Entreno.this, "Error al eliminar el entreno", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    public void mostrarLista(View view) {
        Intent intent = new Intent(this, ConfirmarEntreno.class);
        intent.putExtra("entrenoId", entrenoId);
        intent.putExtra("equipoId", equipoId);
        startActivity(intent);
    }
}
