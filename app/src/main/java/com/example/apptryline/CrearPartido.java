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

public class CrearPartido extends AppCompatActivity {

    private EditText fechaEditText, horaInicioEditText, ubicacionTextoEditText, equipoLocalEditText, equipoVisitanteEditText, resultEditText, melesAFavorEditText, melesEnContraEditText, triesAFavorEditText, triesEnContraEditText;
    private Button anadirPartidoButton;
    private FirebaseAuth mAuth;
    private DatabaseReference equiposRef;

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
        anadirPartidoButton = findViewById(R.id.anadirpartido);
        resultEditText = findViewById(R.id.result);
        melesAFavorEditText = findViewById(R.id.meles_a_favor);
        melesEnContraEditText = findViewById(R.id.meles_en_contra);
        triesAFavorEditText = findViewById(R.id.tries_a_favor);
        triesEnContraEditText = findViewById(R.id.tries_en_contra);

        anadirPartidoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerEquipoId();
            }
        });
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
        if (fechaString.isEmpty() || horaInicio.isEmpty() || ubicacionTexto.isEmpty() || equipoLocal.isEmpty() || equipoVisitante.isEmpty() || result.isEmpty() || melesAFavor.isEmpty() || melesEnContra.isEmpty() || triesAFavor.isEmpty() || triesEnContra.isEmpty()) {
            Toast.makeText(CrearPartido.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir la cadena de fecha a formato Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fecha = null;
        try {
            fecha = dateFormat.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(CrearPartido.this, "Error al convertir la fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto PartidoDatos con los datos proporcionados
        PartidoDatos partido = new PartidoDatos(null, fecha, horaInicio, ubicacionTexto, equipoLocal, equipoVisitante, result, Integer.parseInt(melesAFavor), Integer.parseInt(melesEnContra), Integer.parseInt(triesEnContra), Integer.parseInt(triesAFavor));

        // Guardar el partido en la base de datos
        DatabaseReference partidoRef = equiposRef.child(equipoId).child("Partidos").push();
        String partidoId = partidoRef.getKey();
        partido.setId(partidoId); // Asignar el ID generado al objeto PartidoDatos

        partidoRef.setValue(partido).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CrearPartido.this, "Partido creado exitosamente", Toast.LENGTH_SHORT).show();
                    guardarAlineacion(equipoId, partidoId); // Guardar alineación después de crear el partido
                    // Redirigir al usuario a la pantalla de calendario después de crear el partido
                    Intent intent = new Intent(CrearPartido.this, Calendario.class);
                    startActivity(intent);
                    finish(); // Esto evita que el usuario pueda volver atrás con el botón de retroceso y volver a la pantalla de creación de partido
                } else {
                    Toast.makeText(CrearPartido.this, "Error al crear el partido", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void guardarAlineacion(String equipoId, String partidoId) {
        DatabaseReference alineacionRef = equiposRef.child(equipoId).child("Partidos").child(partidoId).child("Alineacion");
        guardarJugadorCoordenadas(alineacionRef, "Jugador1", R.id.player1);
        guardarJugadorCoordenadas(alineacionRef, "Jugador2", R.id.player2);
        guardarJugadorCoordenadas(alineacionRef, "Jugador3", R.id.player3);
        guardarJugadorCoordenadas(alineacionRef, "Jugador4", R.id.player4);
        guardarJugadorCoordenadas(alineacionRef, "Jugador5", R.id.player5);
        guardarJugadorCoordenadas(alineacionRef, "Jugador6", R.id.player6);
        guardarJugadorCoordenadas(alineacionRef, "Jugador7", R.id.player7);
        guardarJugadorCoordenadas(alineacionRef, "Jugador8", R.id.player8);
        guardarJugadorCoordenadas(alineacionRef, "Jugador9", R.id.player9);
        guardarJugadorCoordenadas(alineacionRef, "Jugador10", R.id.player10);
        guardarJugadorCoordenadas(alineacionRef, "Jugador11", R.id.player11);
        guardarJugadorCoordenadas(alineacionRef, "Jugador12", R.id.player12);
        guardarJugadorCoordenadas(alineacionRef, "Jugador13", R.id.player13);
        guardarJugadorCoordenadas(alineacionRef, "Jugador14", R.id.player14);
        guardarJugadorCoordenadas(alineacionRef, "Jugador15", R.id.player15);
        guardarJugadorCoordenadas(alineacionRef, "Jugador16", R.id.player16);
        guardarJugadorCoordenadas(alineacionRef, "Jugador17", R.id.player17);
        guardarJugadorCoordenadas(alineacionRef, "Jugador18", R.id.player18);
        guardarJugadorCoordenadas(alineacionRef, "Jugador19", R.id.player19);
        guardarJugadorCoordenadas(alineacionRef, "Jugador20", R.id.player20);
        guardarJugadorCoordenadas(alineacionRef, "Jugador21", R.id.player21);
        guardarJugadorCoordenadas(alineacionRef, "Jugador22", R.id.player22);
        guardarJugadorCoordenadas(alineacionRef, "Jugador23", R.id.player23);
        guardarJugadorCoordenadas(alineacionRef, "Jugador24", R.id.player24);
        guardarJugadorCoordenadas(alineacionRef, "Jugador25", R.id.player25);
        guardarJugadorCoordenadas(alineacionRef, "Jugador26", R.id.player26);
        guardarJugadorCoordenadas(alineacionRef, "Jugador27", R.id.player27);
        guardarJugadorCoordenadas(alineacionRef, "Jugador28", R.id.player28);
        guardarJugadorCoordenadas(alineacionRef, "Jugador29", R.id.player29);
        guardarJugadorCoordenadas(alineacionRef, "Jugador30", R.id.player30);
    }

    private void guardarJugadorCoordenadas(DatabaseReference alineacionRef, String nombreJugador, int editTextId) {
        EditText jugadorEditText = findViewById(editTextId);
        String nombre = jugadorEditText.getText().toString();

        // Guardar en Firebase bajo el ID del partido
        alineacionRef.child(nombreJugador).child("nombre").setValue(nombre);

    }

    public void goBack(View view) {
        onBackPressed();
    }
}
