package com.example.apptryline;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

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
    }
    public void irAEstadisticas(View view) {
        Intent intent = new Intent(this, EstadisticasPartido.class);
        startActivity(intent);
    }
}


