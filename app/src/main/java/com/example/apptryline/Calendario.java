package com.example.apptryline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.List;

public class Calendario extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView partidosRecyclerView;
    private LocalDate selectedDate;
    private LocalDate todayDate;
    private PartidoAdapter partidoAdapter;
    private List<String> partidosIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);

        initWidgets();
        todayDate = LocalDate.now();
        selectedDate = LocalDate.now();
        setMonthView();

        setUpButtons();
        cargarPartidosDesdeFirebase(); // Se llama después de setUpButtons
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        partidosRecyclerView = findViewById(R.id.partidosRecyclerView);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, selectedDate, todayDate);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();
        DayOfWeek firstDayOfWeek = DayOfWeek.MONDAY;
        int firstDayOfWeekValue = firstDayOfWeek.getValue();

        LocalDate firstOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);
        int firstDayOfMonthValue = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 1; i < firstDayOfMonthValue; i++) {
            daysInMonthArray.add("");
        }

        for (int i = 1; i <= daysInMonth; i++) {
            daysInMonthArray.add(String.valueOf(i));
        }

        return daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return date.format(formatter);
    }

    public void previousMonthAction(View view) {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view) {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    private void setUpButtons() {

        Button boton4 = findViewById(R.id.boton4);
        boton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Calendario.this, CrearPartido.class));
            }
        });
    }

    private void setUpRecyclerView(String equipoId) {
        partidosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        partidoAdapter = new PartidoAdapter(partidosIds, equipoId, this);
        partidosRecyclerView.setAdapter(partidoAdapter);
    }

    private void cargarPartidosDesdeFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String equipoId = snapshot.child("equipoId").getValue(String.class);
                        if (equipoId != null && !equipoId.isEmpty()) {
                            cargarPartidosEquipo(equipoId);
                        } else {
                            Toast.makeText(Calendario.this, "El usuario no tiene un equipo asignado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Calendario.this, "No se encontraron datos para el usuario", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Calendario.this, "Error al obtener el ID del equipo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void cargarPartidosEquipo(String equipoId) {
        DatabaseReference partidosRef = FirebaseDatabase.getInstance().getReference().child("Equipos").child(equipoId).child("Partidos");
        partidosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    partidosIds.clear();
                    for (DataSnapshot partidoSnapshot : snapshot.getChildren()) {
                        String partidoId = partidoSnapshot.getKey();
                        partidosIds.add(partidoId);
                    }
                    setUpRecyclerView(equipoId);
                    partidoAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Calendario.this, "No hay partidos programados para el equipo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Calendario.this, "Error al cargar los partidos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.isEmpty()) {
            String message = "Selected Date: " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
