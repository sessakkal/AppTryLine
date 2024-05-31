package com.example.apptryline;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CrearPartido extends AppCompatActivity {

    private EditText fechaEditText, horaInicioEditText, ubicacionTextoEditText, equipoLocalEditText, equipoVisitanteEditText, resultadoLocalEditText, resultadoVisitanteEditText;
    private Button anadirPartidoButton, addStatButton;
    private LinearLayout statisticsContainer;
    private FirebaseAuth mAuth;
    private DatabaseReference equiposRef;
    private int statCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crear_partido);

        mAuth = FirebaseAuth.getInstance();
        equiposRef = FirebaseDatabase.getInstance().getReference().child("Equipos");

        fechaEditText = findViewById(R.id.fecha);
        horaInicioEditText = findViewById(R.id.hora_inicio);
        ubicacionTextoEditText = findViewById(R.id.ubi_texto);
        equipoLocalEditText = findViewById(R.id.equipolocal);
        equipoVisitanteEditText = findViewById(R.id.equipovisitante);
        resultadoLocalEditText = findViewById(R.id.resultado_local);
        resultadoVisitanteEditText = findViewById(R.id.resultado_visitante);
        anadirPartidoButton = findViewById(R.id.anadirpartido);
        addStatButton = findViewById(R.id.add_stat_button);
        statisticsContainer = findViewById(R.id.statistics_container);

        anadirPartidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerEquipoId();
            }
        });

        addStatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStatField();
            }
        });
    }

    private void addStatField() {
        statCount++;

        LinearLayout statLayout = new LinearLayout(this);
        statLayout.setOrientation(LinearLayout.HORIZONTAL);
        statLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        EditText statTitle = new EditText(this);
        statTitle.setHint("Dato " + statCount);
        statTitle.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        statTitle.setTextColor(getResources().getColor(R.color.white));
        statTitle.setHintTextColor(getResources().getColor(R.color.gris_flojo));
        statTitle.setBackgroundTintList(getResources().getColorStateList(R.color.green));

        EditText statLocal = new EditText(this);
        statLocal.setHint("Local");
        statLocal.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        statLocal.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        statLocal.setTextColor(getResources().getColor(R.color.white));
        statLocal.setHintTextColor(getResources().getColor(R.color.gris_flojo));
        statLocal.setBackgroundTintList(getResources().getColorStateList(R.color.green));

        EditText statVisit = new EditText(this);
        statVisit.setHint("Visitante");
        statVisit.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        statVisit.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        statVisit.setTextColor(getResources().getColor(R.color.white));
        statVisit.setHintTextColor(getResources().getColor(R.color.gris_flojo));
        statVisit.setBackgroundTintList(getResources().getColorStateList(R.color.green));

        statLayout.addView(statTitle);
        statLayout.addView(statLocal);
        statLayout.addView(statVisit);

        statisticsContainer.addView(statLayout);
    }

    private void obtenerEquipoId() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String equipoId = dataSnapshot.child("equipoId").getValue(String.class);
                        if (equipoId != null && !equipoId.isEmpty()) {
                            crearPartido(equipoId);
                        } else {
                            Toast.makeText(CrearPartido.this, "El usuario no tiene un equipo asignado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CrearPartido.this, "No se encontraron datos para el usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(CrearPartido.this, "Error al obtener el ID del equipo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void crearPartido(String equipoId) {
        String fecha = fechaEditText.getText().toString().trim();
        String horaInicio = horaInicioEditText.getText().toString().trim();
        String ubicacionTexto = ubicacionTextoEditText.getText().toString().trim();
        String equipoLocal = equipoLocalEditText.getText().toString().trim();
        String equipoVisitante = equipoVisitanteEditText.getText().toString().trim();
        String resultadoLocal = resultadoLocalEditText.getText().toString().trim();
        String resultadoVisitante = resultadoVisitanteEditText.getText().toString().trim();

        if (TextUtils.isEmpty(fecha) || TextUtils.isEmpty(horaInicio) || TextUtils.isEmpty(ubicacionTexto) || TextUtils.isEmpty(equipoLocal) || TextUtils.isEmpty(equipoVisitante)) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference partidosRef = equiposRef.child(equipoId).child("Partidos");
        String partidoId = partidosRef.push().getKey();

        PartidoDatos partido = new PartidoDatos(partidoId, parseDate(fecha), horaInicio, ubicacionTexto, equipoLocal, equipoVisitante, resultadoLocal, resultadoVisitante);

        partidosRef.child(partidoId).setValue(partido)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            guardarEstadisticas(equipoId, partidoId);
                        } else {
                            Toast.makeText(CrearPartido.this, "Error al crear el partido: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void guardarEstadisticas(String equipoId, String partidoId) {
        DatabaseReference estadisticasRef = equiposRef.child(equipoId).child("Partidos").child(partidoId).child("Estadisticas");

        for (int i = 0; i < statisticsContainer.getChildCount(); i++) {
            View statLayout = statisticsContainer.getChildAt(i);

            if (statLayout instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) statLayout;
                EditText statTitle = (EditText) linearLayout.getChildAt(0);
                EditText statLocal = (EditText) linearLayout.getChildAt(1);
                EditText statVisit = (EditText) linearLayout.getChildAt(2);

                String titulo = statTitle.getText().toString().trim();
                int local = Integer.parseInt(statLocal.getText().toString().trim());
                int visitante = Integer.parseInt(statVisit.getText().toString().trim());

                Estadistica estadistica = new Estadistica(titulo, local, visitante, 100, 0); // El valor de max y progreso puede ser ajustado según la necesidad
                estadisticasRef.push().setValue(estadistica);
            }
        }

        Toast.makeText(CrearPartido.this, "Partido y estadísticas creados exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }

    private Date parseDate(String fecha) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return dateFormat.parse(fecha);
        } catch (ParseException e) {
            return null;
        }
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
