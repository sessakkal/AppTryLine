package com.example.apptryline;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InicioRegistro extends Activity {

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

    // Método para abrir la actividad de creación de equipo
    private void abrirActividadCrearEquipo() {
        Intent intent = new Intent(InicioRegistro.this, CrearCuenta.class);
        startActivity(intent);
    }

    // Método para abrir la actividad de unirse a equipo
    private void abrirActividadUnirseEquipo() {
        Intent intent = new Intent(InicioRegistro.this, UnirseEquipo.class);
        startActivity(intent);
    }

    // Método para abrir la actividad de iniciar sesión
    private void abrirActividadIniciarSesion() {
        Intent intent = new Intent(InicioRegistro.this, IniciarSesion.class);
        startActivity(intent);
    }
}

