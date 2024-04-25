package com.example.apptryline;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Map;

public class Partido extends AppCompatActivity {

    private TextView nombreTextView;
    private TextView fechaTextView;
    private TextView horaInicioTextView;
    private TextView coordenadasTextView;
    private TextView ubicacionTextoTextView;
    private TextView equipoLocalTextView;
    private TextView equipoVisitanteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.partido);

        // Inicializar vistas
        nombreTextView = findViewById(R.id.nombre_partido);
        fechaTextView = findViewById(R.id.fecha_partido);
        horaInicioTextView = findViewById(R.id.hora_inicio_partido);
        coordenadasTextView = findViewById(R.id.coordenadas_partido);
        ubicacionTextoTextView = findViewById(R.id.ubicacion_texto_partido);
        equipoLocalTextView = findViewById(R.id.equipo_local_partido);
        equipoVisitanteTextView = findViewById(R.id.equipo_visitante_partido);

        // Obtener referencia a la base de datos Firebase
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        // Obtener los datos del nodo "partido_ejemplo"
        dbRef.child("partido_ejemplo").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                if (dataSnapshot.exists()) {
                    // Obtener los valores de los nodos
                    String nombre = dataSnapshot.child("nombre").getValue(String.class);
                    String fecha = dataSnapshot.child("fecha").getValue(String.class);
                    String horaInicio = dataSnapshot.child("horaInicio").getValue(String.class);
                    Map<String, Double> coordenadasMap = dataSnapshot.child("coordenadas").getValue(Map.class);
                    String ubicacionTexto = dataSnapshot.child("ubicacionTexto").getValue(String.class);
                    String equipoLocal = dataSnapshot.child("equipoLocal").getValue(String.class);
                    String equipoVisitante = dataSnapshot.child("equipoVisitante").getValue(String.class);

                    // Convertir las coordenadas de Map a una cadena legible
                    String coordenadas = coordenadasMap.get("latitud") + ", " + coordenadasMap.get("longitud");

                    // Mostrar los valores en las TextViews
                    nombreTextView.setText(nombre);
                    fechaTextView.setText(fecha);
                    horaInicioTextView.setText(horaInicio);
                    coordenadasTextView.setText(coordenadas);
                    ubicacionTextoTextView.setText(ubicacionTexto);
                    equipoLocalTextView.setText(equipoLocal);
                    equipoVisitanteTextView.setText(equipoVisitante);
                } else {
                    Log.d("Partido", "No existe el nodo 'partido_ejemplo'");
                }
            } else {
                Log.d("Partido", "Error al obtener datos: ", task.getException());
            }
        });
    }
}
