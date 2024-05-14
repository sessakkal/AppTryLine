package com.example.apptryline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Calendario extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    private LocalDate todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);
        initWidgets();

        // Obtener la fecha de hoy
        todayDate = LocalDate.now();

        selectedDate = LocalDate.now();
        setMonthView();
        Button boton1 = findViewById(R.id.boton3);

        // Establecer un listener de clic para el botón
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad Partido cuando se hace clic en el botón
                Intent intent = new Intent(Calendario.this, Partido.class);
                startActivity(intent);
            }
        });
        Button boton4 = findViewById(R.id.boton4);

        // Establecer un listener de clic para el botón
        boton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad Partido cuando se hace clic en el botón
                Intent intent = new Intent(Calendario.this, CrearPartido.class);
                startActivity(intent);
            }
        });
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, selectedDate, todayDate); // Pasa todayDate al adaptador
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }


    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        // Obtener el primer día de la semana (puede variar según la localización)
        DayOfWeek firstDayOfWeek = DayOfWeek.MONDAY; // Por ejemplo, comienza el domingo
        int dayOfWeekValue = firstDayOfWeek.getValue();

        // Obtener el primer día del mes y el día de la semana
        LocalDate firstOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);
        int firstDayOfMonthValue = firstOfMonth.getDayOfWeek().getValue();

        // Agregar los espacios en blanco hasta llegar al primer día de la semana
        for (int i = 1; i < firstDayOfMonthValue; i++) {
            daysInMonthArray.add("");
        }

        // Agregar los días del mes
        for (int i = 1; i <= daysInMonth; i++) {
            daysInMonthArray.add(String.valueOf(i));
        }

        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }
    public void onOption4Click(View view) {
        Intent intent = new Intent(this, EditarPerfil.class);
        startActivity(intent);
    }
    public void onOption2Click(View view) {
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usuarioActualRef = usuariosRef.child(userId);

        usuarioActualRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("equipoId")) {
                    String equipoId = dataSnapshot.child("equipoId").getValue(String.class);
                    if (equipoId != null && !equipoId.isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), Conversaciones.class);
                        intent.putExtra("equipoId", equipoId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error al obtener el ID del equipo: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onOption3Click(View view) {
        Intent intent = new Intent(this, Calendario.class);
        startActivity(intent);
    }
    public void onOption1Click(View view) {
        // Obtener una referencia a la base de datos de Firebase
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");

        // Obtener el ID del usuario actual (puedes cambiar esto dependiendo de cómo estés autenticando al usuario)
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Obtener la referencia del usuario actual
        DatabaseReference usuarioActualRef = usuariosRef.child(userId);

        // Agregar un listener para obtener los datos del usuario actual
        usuarioActualRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Verificar si existe el campo "equipoId" en los datos del usuario
                if (dataSnapshot.hasChild("equipoId")) {
                    // Obtener el ID del equipo del usuario
                    String equipoId = dataSnapshot.child("equipoId").getValue(String.class);

                    // Verificar si se obtuvo un ID de equipo válido
                    if (equipoId != null && !equipoId.isEmpty()) {
                        // Crear un intent para iniciar la actividad General y pasar el ID del equipo como extra
                        Intent intent = new Intent(getApplicationContext(), General.class);
                        intent.putExtra("equipoId", equipoId);
                        startActivity(intent);
                    } else {
                        // Manejar el caso cuando el ID del equipo es nulo o vacío
                        Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Manejar el caso cuando el campo "equipoId" no está presente en los datos del usuario
                    Toast.makeText(getApplicationContext(), "ID de equipo no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar errores de base de datos si es necesario
                Toast.makeText(getApplicationContext(), "Error al obtener el ID del equipo: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }
}
