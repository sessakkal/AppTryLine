package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InicioRegistro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_registro);

        Button botonCrearEquipo = findViewById(R.id.boton_crear_equipo);
        Button botonUnirseEquipo = findViewById(R.id.boton_unirse_equipo);
        Button botonIniciarSesion = findViewById(R.id.boton_iniciar_sesion);

        botonCrearEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadCrearEquipo();
            }
        });

        botonUnirseEquipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadUnirseEquipo();
            }
        });

        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadIniciarSesion();
            }
        });
    }

    // Métodos para abrir las actividades correspondientes
    private void abrirActividadCrearEquipo() {
        Intent intent = new Intent(this, CrearEquipo.class);
        startActivity(intent);
    }

    private void abrirActividadUnirseEquipo() {
        Intent intent = new Intent(this, UnirseEquipo.class);
        startActivity(intent);
    }

    private void abrirActividadIniciarSesion() {
        Intent intent = new Intent(this, IniciarSesion.class);
        startActivity(intent);
    }
}
