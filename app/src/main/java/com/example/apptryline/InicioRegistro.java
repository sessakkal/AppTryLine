package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class InicioRegistro extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View rootView = inflater.inflate(R.layout.inicio_registro, container, false);

        Button botonCrearEquipo = rootView.findViewById(R.id.boton_crear_equipo);
        Button botonUnirseEquipo = rootView.findViewById(R.id.boton_unirse_equipo);
        Button botonIniciarSesion = rootView.findViewById(R.id.boton_iniciar_sesion);

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

        return rootView;
    }

    // Métodos para abrir las actividades correspondientes
    private void abrirActividadCrearEquipo() {
        Intent intent = new Intent(getActivity(), CrearEquipo.class);
        startActivity(intent);
    }

    private void abrirActividadUnirseEquipo() {
        Intent intent = new Intent(getActivity(), UnirseEquipo.class);
        startActivity(intent);
    }

    private void abrirActividadIniciarSesion() {
        Intent intent = new Intent(getActivity(), IniciarSesion.class);
        startActivity(intent);
    }
}


