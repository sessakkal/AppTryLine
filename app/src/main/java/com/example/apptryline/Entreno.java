package com.example.apptryline;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class Entreno extends AppCompatActivity {

    private TextView textViewNombre, textViewFecha, textViewHoraInicio, textViewHoraFin, textViewLugar;
    private CheckBox checkBoxConfirmar;
    private Button botonGuardar;
    private FirebaseAuth mAuth;
    private DatabaseReference entrenosRef;
    private String entrenoId;
    private String equipoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entreno);

        mAuth = FirebaseAuth.getInstance();
        entrenosRef = FirebaseDatabase.getInstance().getReference().child("Equipos");

        textViewNombre = findViewById(R.id.nombre_entreno);
        textViewFecha = findViewById(R.id.fecha_entreno);
        textViewHoraInicio = findViewById(R.id.hora_inicio_entreno);
        textViewHoraFin = findViewById(R.id.hora_fin_entreno);
        textViewLugar = findViewById(R.id.lugar_entreno);
        checkBoxConfirmar = findViewById(R.id.checkbox_confirmar);
        botonGuardar = findViewById(R.id.boton_guardar);

        entrenoId = getIntent().getStringExtra("entrenoId");
        equipoId = getIntent().getStringExtra("equipoId");

        if (entrenoId != null && equipoId != null) {
            cargarDatosEntreno(equipoId, entrenoId);
        }

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmarEntreno(equipoId, entrenoId);
            }
        });
    }

    private void cargarDatosEntreno(String equipoId, String entrenoId) {
        DatabaseReference entrenoRef = entrenosRef.child(equipoId).child("Entrenos").child(entrenoId);
        entrenoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    String fecha = dataSnapshot.child("fecha").getValue(String.class);
                    String horaInicio = dataSnapshot.child("horaInicio").getValue(String.class);
                    String horaFin = dataSnapshot.child("horaFin").getValue(String.class);
                    String lugar = dataSnapshot.child("lugar").getValue(String.class);
                    Boolean confirmado = dataSnapshot.child("confirmado").getValue(Boolean.class);

                    textViewNombre.setText(nombre);
                    textViewFecha.setText(fecha);
                    textViewHoraInicio.setText(horaInicio);
                    textViewHoraFin.setText(horaFin);
                    textViewLugar.setText(lugar);

                    checkBoxConfirmar.setChecked(Boolean.TRUE.equals(confirmado));
                } else {
                    Toast.makeText(Entreno.this, "No se encontraron datos para el entreno", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Entreno.this, "Error al cargar los datos del entreno", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmarEntreno(String equipoId, String entrenoId) {
        DatabaseReference entrenoRef = entrenosRef.child(equipoId).child("Entrenos").child(entrenoId);
        boolean isChecked = checkBoxConfirmar.isChecked();
        entrenoRef.child("confirmado").setValue(isChecked).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Entreno.this, "Entrenamiento " + (isChecked ? "confirmado" : "no confirmado"), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Entreno.this, "Error al actualizar la confirmación del entreno", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
