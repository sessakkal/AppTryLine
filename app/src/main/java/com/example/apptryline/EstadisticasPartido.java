package com.example.apptryline;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;

public class EstadisticasPartido extends AppCompatActivity {

    private HorizontalBarChart horizontalBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estadisticas_partido);

        horizontalBarChart = findViewById(R.id.tries);

        // Datos de ejemplo para el partido (goles, tiros a puerta, tarjetas, etc.)
        ArrayList<BarEntry> localData = new ArrayList<>();
        ArrayList<BarEntry> visitorData = new ArrayList<>();

        // Agregar datos de ejemplo para el equipo local y el equipo visitante
        localData.add(new BarEntry(1, -7));  // Tres goles para el equipo local


        visitorData.add(new BarEntry(1, 4));  // Dos goles para el equipo visitante


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
        horizontalBarChart.setDrawValueAboveBar(true); // Mostrar los valores encima de las barras
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


        // Desactivar interacción con el gráfic
        horizontalBarChart.setTouchEnabled(false);
        horizontalBarChart.setDragEnabled(false);
        horizontalBarChart.setScaleEnabled(false);



        // Invalidar el gráfico para que se actualice
        horizontalBarChart.invalidate();
    }
}


