package com.example.apptryline;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;

public class Partido extends AppCompatActivity {

    private TextView nombreTextView;
    private TextView fechaTextView;
    private TextView horaInicioTextView;
    private TextView coordenadasTextView;
    private TextView ubicacionTextoTextView;
    private TextView equipoLocalTextView;
    private TextView equipoVisitanteTextView;
    private HorizontalBarChart horizontalBarChart;

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
        horizontalBarChart = findViewById(R.id.horizontalBarChart);

        // Datos de ejemplo para el partido (goles, tiros a puerta, tarjetas, etc.)
        ArrayList<BarEntry> localData = new ArrayList<>();
        ArrayList<BarEntry> visitorData = new ArrayList<>();

        // Agregar datos de ejemplo para el equipo local y el equipo visitante
        localData.add(new BarEntry(1, -7));  // Tres goles para el equipo local
        localData.add(new BarEntry(2, -5));  // Cinco tiros a puerta para el equipo local

        visitorData.add(new BarEntry(1, 4));  // Dos goles para el equipo visitante
        visitorData.add(new BarEntry(2, 9));  // Tres tiros a puerta para el equipo visitante

        // Crear conjuntos de datos para el equipo local y el equipo visitante
        BarDataSet localDataSet = new BarDataSet(localData, "Equipo Local");
        localDataSet.setColor(Color.GREEN);

        BarDataSet visitorDataSet = new BarDataSet(visitorData, "Equipo Visitante");
        visitorDataSet.setColor(Color.GREEN);

        // Crear BarData con los conjuntos de datos
        BarData barData = new BarData(localDataSet, visitorDataSet);

        // Establecer los datos en el gráfico
        horizontalBarChart.setData(barData);
        horizontalBarChart.getLegend().setEnabled(false);
        horizontalBarChart.setDrawBorders(false);

        // Personalizar el aspecto del gráfico según tus preferencias
        horizontalBarChart.setDrawValueAboveBar(false); // Mostrar los valores encima de las barras
        horizontalBarChart.getAxisLeft().setAxisMinimum(-10); // Establecer el mínimo del eje izquierdo
        horizontalBarChart.getAxisRight().setAxisMinimum(-10); // Establecer el mínimo del eje derecho

        // Ocultar la cuadrícula y los números en el eje X
        XAxis xAxis = horizontalBarChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);

        // Ocultar la cuadrícula y los números en el eje Y izquierdo
        YAxis leftAxis = horizontalBarChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);

        // Ocultar la cuadrícula y los números en el eje Y derecho
        YAxis rightAxis = horizontalBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        // Invalidar el gráfico para que se actualice
        horizontalBarChart.invalidate();
    }
}


