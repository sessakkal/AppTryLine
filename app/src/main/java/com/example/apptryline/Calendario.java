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

        Button boton1 = findViewById(R.id.boton3);
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendario.this, Partido.class);
                startActivity(intent);
            }
        });

        Button boton4 = findViewById(R.id.boton4);
        boton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calendario.this, CrearPartido.class);
                startActivity(intent);
            }
        });

        partidoAdapter = new PartidoAdapter(partidosIds);
        partidosRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        partidosRecyclerView.setAdapter(partidoAdapter);

        cargarPartidosDesdeFirebase();
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
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usuarioActualRef = usuariosRef.child(userId);

        usuarioActualRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("equipoId")) {
                    String equipoId = dataSnapshot.child("equipoId").getValue(String.class);
                    if (equipoId != null && !equipoId.isEmpty()) {
                        Intent intent = new Intent(getApplicationContext(), General.class);
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

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    private void cargarPartidosDesdeFirebase() {
        DatabaseReference partidosRef = FirebaseDatabase.getInstance().getReference("Equipos").child("TuEquipoId").child("Partidos");
        partidosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot partidoSnapshot : dataSnapshot.getChildren()) {
                    String partidoId = partidoSnapshot.getKey();
                    partidosIds.add(partidoId);
                    String partido = partidoSnapshot.child("coordenadas").getValue(String.class); // Puedes obtener otros datos del partido si lo necesitas
                    partidosIds.add(partido);
                }
                partidoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error al cargar partidos desde Firebase: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
