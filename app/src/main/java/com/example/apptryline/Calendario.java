package com.example.apptryline;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.Calendar;

public class Calendario extends Fragment {

    private CalendarView calendarView;
    NavController navController;
    ImageView imageArrowLeft;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendario, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        imageArrowLeft = view.findViewById(R.id.imageArrowleft);

        // Busca el CalendarView en el layout
        calendarView = view.findViewById(R.id.calendarView);

        // Configura un listener para detectar cambios en la fecha seleccionada
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Aquí puedes manejar la fecha seleccionada, por ejemplo, mostrarla en un Toast
                Toast.makeText(getActivity(), "Fecha seleccionada: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
            }
        });
        imageArrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigateUp(); // Regresar al fragmento anterior
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Aquí puedes manejar la fecha seleccionada, por ejemplo, mostrarla en un Toast
                        Toast.makeText(getActivity(), "Fecha seleccionada: " + dayOfMonth + "/" + (month + 1) + "/" + year, Toast.LENGTH_SHORT).show();
                    }
                }, year, month, day);

                // Muestra el DatePickerDialog
                datePickerDialog.show();
            }
        });

        return view;
    }
}
