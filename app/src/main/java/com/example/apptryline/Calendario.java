package com.example.apptryline;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Calendario extends AppCompatActivity {

    private CalendarView calendarView;
    private ImageView imageArrowLeft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);

        // Busca el CalendarView en el layout
        calendarView = findViewById(R.id.calendarView);
        imageArrowLeft = findViewById(R.id.imageArrowleft);

        imageArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Regresar a la actividad anterior
            }
        });

        // Configura un listener para detectar cambios en la fecha seleccionada
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Aquí puedes manejar la fecha seleccionada, por ejemplo, mostrarla en un Toast
                Toast.makeText(Calendario.this, "Fecha seleccionada: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });

        // Configura un listener para abrir un DatePickerDialog al hacer clic en el CalendarView
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén la fecha actual para establecerla como fecha inicial en el DatePickerDialog
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);

                // Crea un DatePickerDialog con la fecha actual
                DatePickerDialog datePickerDialog = new DatePickerDialog(Calendario.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Aquí puedes manejar la fecha seleccionada, por ejemplo, mostrarla en un Toast
                        Toast.makeText(Calendario.this, "Fecha seleccionada: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);

                // Muestra el DatePickerDialog
                datePickerDialog.show();
            }
        });

        // Aplica el estilo personalizado para el texto de los días del calendario
        calendarView.setDateTextAppearance(R.style.CustomCalendarDateText);
    }
}
