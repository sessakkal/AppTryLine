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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Calendario extends AppCompatActivity implements CalendarAdapter.OnItemListener {

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private RecyclerView partidosRecyclerView;
    private LocalDate selectedDate;
    private LocalDate todayDate;
    private PartidoAdapter partidoAdapter;
    private List<String> partidosIds = new ArrayList<>();
    private List<String> diasConPartidos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);

        initWidgets();
        todayDate = LocalDate.now();
        selectedDate = LocalDate.now();
        setMonthView();

        checkAdminStatus();
        cargarPartidosDesdeFirebase();
    }
    private void checkAdminStatus() {
        Button boton4 = findViewById(R.id.boton4);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference().child("Usuarios").child(userId);
            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Boolean isAdmin = snapshot.child("admin").getValue(Boolean.class);
                        if (isAdmin != null && isAdmin) {
                            boton4.setVisibility(View.VISIBLE);
                        } else {
                            boton4.setVisibility(View.GONE);
                        }
                    } else {
                        boton4.setVisibility(View.GONE);
                    }
                    setUpButtons();  // Llamar a setUpButtons() aquí después de verificar el estado del usuario
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Calendario.this, "Error al verificar el estado de administrador", Toast.LENGTH_SHORT).show();
                    boton4.setVisibility(View.GONE);
                    setUpButtons();  // Asegurarse de que setUpButtons() se llame incluso si hay un error
                }
            });
        } else {
            boton4.setVisibility(View.GONE);
            setUpButtons();  // Asegurarse de que setUpButtons() se llame si el usuario no está autenticado
        }
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

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        partidosRecyclerView = findViewById(R.id.partidosRecyclerView);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, selectedDate, todayDate, diasConPartidos); // Pasar diasConPartidos al constructor
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

                        // Obtener los datos del partido
                        String fechaString;
                        Object fechaObject = partidoSnapshot.child("fecha").getValue();
                        if (fechaObject instanceof String) {
                            // Si el valor es una cadena, asignarlo a fechaString
                            fechaString = (String) fechaObject;
                        } else if (fechaObject instanceof HashMap) {
                            // Si el valor es un HashMap, acceder al campo "date" u otro campo que contenga la fecha
                            // Por ejemplo, si la fecha se almacena bajo el campo "date"
                            fechaString = ((HashMap<String, Object>) fechaObject).get("date").toString();
                        } else {
                            // Si el valor no es ni una cadena ni un HashMap, manejar el caso según sea necesario
                            fechaString = ""; // O asignar otro valor predeterminado
                        }

                        // Convertir la cadena de fecha a un objeto Date
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        Date fecha;
                        try {
                            fecha = dateFormat.parse(fechaString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            fecha = null;
                        }

                        // Ahora puedes usar "fecha" como objeto Date para lo que necesites
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
            if (diasConPartidos.contains(dayText)) {
                Intent intent = new Intent(this, Partido.class);
                intent.putExtra("selectedDay", dayText);
                intent.putExtra("selectedMonth", monthYearFromDate(selectedDate));
                startActivity(intent);
            } else {
                String message = "Selected Date: " + dayText + " " + monthYearFromDate(selectedDate);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

