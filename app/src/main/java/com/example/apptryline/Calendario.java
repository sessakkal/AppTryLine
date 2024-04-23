package com.example.apptryline;

import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Calendario extends AppCompatActivity {

    private ImageView imageArrowLeft;
    private CalendarView calendarView;
    private List<Long> selectedDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);

        imageArrowLeft = findViewById(R.id.imageArrowLeft);
        calendarView = findViewById(R.id.calendarView);

        selectedDates = new ArrayList<>();

        imageArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Regresar a la actividad anterior
            }
        });

        // Cambiar los nombres de los días de la semana
        String[] daysOfWeek = { "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb","Dom"};
        calendarView.setFirstDayOfWeek(Calendar.MONDAY); // Establecer el primer día de la semana
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // Puedes hacer algo aquí si lo necesitas
            }
        });


        // Cambiar los nombres de los meses
        String[] months = {"Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Sep", "Oct", "Nov", "Dic"};
        calendarView.setDateTextAppearance(R.style.CustomCalendarDateText);

        // Configurar un listener para detectar cuando el usuario selecciona una fecha en el CalendarView
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                long selectedMillis = selectedDate.getTimeInMillis();

                // Verificar si la fecha ya está seleccionada o no
                if (selectedDates.contains(selectedMillis)) {
                    selectedDates.remove(selectedMillis);
                    Toast.makeText(Calendario.this, "Día deseleccionado: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                } else {
                    selectedDates.add(selectedMillis);
                    Toast.makeText(Calendario.this, "Día seleccionado: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
