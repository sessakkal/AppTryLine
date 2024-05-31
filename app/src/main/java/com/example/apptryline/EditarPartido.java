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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditarPartido extends AppCompatActivity {

    private EditText fechaEditText, horaInicioEditText, ubicacionTextoEditText, equipoLocalEditText, equipoVisitanteEditText, resultadoLocalEditText, resultadoVisitanteEditText;
    private Button guardarCambiosButton, addStatButton;
    private LinearLayout statisticsContainer;
    private FirebaseAuth mAuth;
    private DatabaseReference equiposRef;
    private String partidoId;
    private String equipoId;
    private int statCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_partido);

        mAuth = FirebaseAuth.getInstance();
        equiposRef = FirebaseDatabase.getInstance().getReference().child("Equipos");

        fechaEditText = findViewById(R.id.fecha);
        horaInicioEditText = findViewById(R.id.hora_inicio);
        ubicacionTextoEditText = findViewById(R.id.ubi_texto);
        equipoLocalEditText = findViewById(R.id.equipolocal);
        equipoVisitanteEditText = findViewById(R.id.equipovisitante);
        resultadoLocalEditText = findViewById(R.id.resultado_local);
        resultadoVisitanteEditText = findViewById(R.id.resultado_visitante);
        guardarCambiosButton = findViewById(R.id.guardar_cambios);
        addStatButton = findViewById(R.id.add_stat_button);
        statisticsContainer = findViewById(R.id.statistics_container);

        partidoId = getIntent().getStringExtra("partidoId");
        equipoId = getIntent().getStringExtra("equipoId");

        if (partidoId != null && equipoId != null) {
            cargarDatosPartido(equipoId, partidoId);
        }

        guardarCambiosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPartido(equipoId, partidoId);
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

    private void cargarDatosPartido(String equipoId, String partidoId) {
        DatabaseReference partidoRef = equiposRef.child(equipoId).child("Partidos").child(partidoId);
        partidoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    PartidoDatos partido = dataSnapshot.getValue(PartidoDatos.class);
                    if (partido != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        fechaEditText.setText(dateFormat.format(partido.getFecha()));
                        horaInicioEditText.setText(partido.getHoraInicio());
                        ubicacionTextoEditText.setText(partido.getUbicacionTexto());
                        equipoLocalEditText.setText(partido.getEquipoLocal());
                        equipoVisitanteEditText.setText(partido.getEquipoVisitante());
                        resultadoLocalEditText.setText(partido.getResultadolocal());
                        resultadoVisitanteEditText.setText(partido.getResultadovisitante());
                        cargarEstadisticas(partidoId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditarPartido.this, "Error al cargar los datos del partido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarEstadisticas(String partidoId) {
        DatabaseReference estadisticasRef = equiposRef.child(equipoId).child("Partidos").child(partidoId).child("Estadisticas");
        estadisticasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                statisticsContainer.removeAllViews();
                for (DataSnapshot statSnapshot : dataSnapshot.getChildren()) {
                    Estadistica estadistica = statSnapshot.getValue(Estadistica.class);
                    if (estadistica != null) {
                        addStatFieldWithData(estadistica);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditarPartido.this, "Error al cargar las estadísticas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addStatFieldWithData(Estadistica estadistica) {
        statCount++;

        LinearLayout statLayout = new LinearLayout(this);
        statLayout.setOrientation(LinearLayout.HORIZONTAL);
        statLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        EditText statTitle = new EditText(this);
        statTitle.setHint("Dato " + statCount);
        statTitle.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        statTitle.setText(estadistica.getTitulo());
        statTitle.setTextColor(getResources().getColor(R.color.white));
        statTitle.setHintTextColor(getResources().getColor(R.color.gris_flojo));
        statTitle.setBackgroundTintList(getResources().getColorStateList(R.color.green));
        statTitle.setId(View.generateViewId());

        EditText statLocal = new EditText(this);
        statLocal.setHint("Local");
        statLocal.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        statLocal.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        statLocal.setText(String.valueOf(estadistica.getDatoFavor()));
        statLocal.setTextColor(getResources().getColor(R.color.white));
        statLocal.setHintTextColor(getResources().getColor(R.color.gris_flojo));
        statLocal.setBackgroundTintList(getResources().getColorStateList(R.color.green));
        statLocal.setId(View.generateViewId());

        EditText statVisit = new EditText(this);
        statVisit.setHint("Visitante");
        statVisit.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        statVisit.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        statVisit.setText(String.valueOf(estadistica.getDatoContra()));
        statVisit.setTextColor(getResources().getColor(R.color.white));
        statVisit.setHintTextColor(getResources().getColor(R.color.gris_flojo));
        statVisit.setBackgroundTintList(getResources().getColorStateList(R.color.green));
        statVisit.setId(View.generateViewId());

        statLayout.addView(statTitle);
        statLayout.addView(statLocal);
        statLayout.addView(statVisit);

        statisticsContainer.addView(statLayout);
    }

    private void actualizarPartido(String equipoId, String partidoId) {
        String fechaString = fechaEditText.getText().toString().trim();
        String horaInicio = horaInicioEditText.getText().toString().trim();
        String ubicacionTexto = ubicacionTextoEditText.getText().toString().trim();
        String equipoLocal = equipoLocalEditText.getText().toString().trim();
        String equipoVisitante = equipoVisitanteEditText.getText().toString().trim();
        String resultadoLocal = resultadoLocalEditText.getText().toString().trim();
        String resultadoVisitante = resultadoVisitanteEditText.getText().toString().trim();

        // Validar que se ingresen todos los campos
        if (fechaString.isEmpty() || horaInicio.isEmpty() || ubicacionTexto.isEmpty() || equipoLocal.isEmpty() || equipoVisitante.isEmpty()) {
            Toast.makeText(EditarPartido.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir la cadena de fecha a formato Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fecha = null;
        try {
            fecha = dateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(EditarPartido.this, "Error al convertir la fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto PartidoDatos con los datos proporcionados
        PartidoDatos partido = new PartidoDatos(partidoId, fecha, horaInicio, ubicacionTexto, equipoLocal, equipoVisitante, resultadoLocal, resultadoVisitante);

        // Guardar los cambios del partido en la base de datos
        DatabaseReference partidoRef = equiposRef.child(equipoId).child("Partidos").child(partidoId);
        partidoRef.setValue(partido).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    guardarEstadisticas(equipoId, partidoId);
                } else {
                    Toast.makeText(EditarPartido.this, "Error al actualizar el partido", Toast.LENGTH_SHORT).show();
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

        Toast.makeText(EditarPartido.this, "Partido y estadísticas creados exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void goBack(View view) {
        onBackPressed();
    }
}

