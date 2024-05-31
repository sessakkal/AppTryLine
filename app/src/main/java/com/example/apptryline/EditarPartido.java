package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Date;
import java.util.Locale;

public class EditarPartido extends AppCompatActivity {

    private EditText fechaEditText, horaInicioEditText, ubicacionTextoEditText, equipoLocalEditText, equipoVisitanteEditText, resultEditText, melesAFavorEditText, melesEnContraEditText, triesAFavorEditText, triesEnContraEditText;
    private EditText[] playerEditTexts = new EditText[30];
    private Button guardarCambiosButton;
    private FirebaseAuth mAuth;
    private DatabaseReference equiposRef;
    private String partidoId;
    private String equipoId;

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
        resultEditText = findViewById(R.id.result);
        melesAFavorEditText = findViewById(R.id.meles_a_favor);
        melesEnContraEditText = findViewById(R.id.meles_en_contra);
        triesAFavorEditText = findViewById(R.id.tries_a_favor);
        triesEnContraEditText = findViewById(R.id.tries_en_contra);
        guardarCambiosButton = findViewById(R.id.guardar_cambios);

        for (int i = 0; i < playerEditTexts.length; i++) {
            int resId = getResources().getIdentifier("player" + (i + 1), "id", getPackageName());
            playerEditTexts[i] = findViewById(resId);
        }

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
                        resultEditText.setText(partido.getResultado());
                        melesAFavorEditText.setText(String.valueOf(partido.getMelesAFavor()));
                        melesEnContraEditText.setText(String.valueOf(partido.getMelesEnContra()));
                        triesAFavorEditText.setText(String.valueOf(partido.getTriesAFavor()));
                        triesEnContraEditText.setText(String.valueOf(partido.getTriesEnContra()));
                        cargarNombresJugadores(equipoId, partidoId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditarPartido.this, "Error al cargar los datos del partido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarNombresJugadores(String equipoId, String partidoId) {
        DatabaseReference alineacionRef = equiposRef.child(equipoId).child("Partidos").child(partidoId).child("Alineacion");
        alineacionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (int i = 0; i < playerEditTexts.length; i++) {
                        String playerKey = "Jugador" + (i + 1);
                        if (dataSnapshot.hasChild(playerKey)) {
                            String playerName = dataSnapshot.child(playerKey).child("nombre").getValue(String.class);
                            playerEditTexts[i].setText(playerName);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditarPartido.this, "Error al cargar los nombres de los jugadores", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarPartido(String equipoId, String partidoId) {
        String fechaString = fechaEditText.getText().toString().trim();
        String horaInicio = horaInicioEditText.getText().toString().trim();
        String ubicacionTexto = ubicacionTextoEditText.getText().toString().trim();
        String equipoLocal = equipoLocalEditText.getText().toString().trim();
        String equipoVisitante = equipoVisitanteEditText.getText().toString().trim();
        String result = resultEditText.getText().toString().trim();
        String melesAFavor = melesAFavorEditText.getText().toString().trim();
        String melesEnContra = melesEnContraEditText.getText().toString().trim();
        String triesAFavor = triesAFavorEditText.getText().toString().trim();
        String triesEnContra = triesEnContraEditText.getText().toString().trim();

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
        PartidoDatos partido = new PartidoDatos(partidoId, fecha, horaInicio, ubicacionTexto, equipoLocal, equipoVisitante, result, Integer.parseInt(melesAFavor), Integer.parseInt(melesEnContra), Integer.parseInt(triesAFavor), Integer.parseInt(triesEnContra));

        // Guardar los cambios del partido en la base de datos
        DatabaseReference partidoRef = equiposRef.child(equipoId).child("Partidos").child(partidoId);

        partidoRef.setValue(partido).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    guardarAlineacion(equipoId, partidoId);
                } else {
                    Toast.makeText(EditarPartido.this, "Error al actualizar el partido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void guardarAlineacion(String equipoId, String partidoId) {
        DatabaseReference alineacionRef = equiposRef.child(equipoId).child("Partidos").child(partidoId).child("Alineacion");
        for (int i = 0; i < playerEditTexts.length; i++) {
            String playerKey = "Jugador" + (i + 1);
            String nombre = playerEditTexts[i].getText().toString();
            alineacionRef.child(playerKey).child("nombre").setValue(nombre);
        }
        Toast.makeText(EditarPartido.this, "Partido y alineación actualizados exitosamente", Toast.LENGTH_SHORT).show();
        // Redirigir al usuario a la pantalla de calendario después de actualizar el partido
        Intent intent = new Intent(EditarPartido.this, MainPartidos.class);
        startActivity(intent);
        finish(); // Esto evita que el usuario pueda volver atrás con el botón de retroceso y volver a la pantalla de edición de partido
    }

    public void goBack(View view) {
        onBackPressed();
    }
}
