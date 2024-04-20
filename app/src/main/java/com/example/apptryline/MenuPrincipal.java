package com.example.apptryline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

public class MenuPrincipal extends Fragment {

    private Button buttonCrearEntrenamiento;
    private Button buttonCrearPartido;
    private Button buttonEditarPartido;
    private Button buttonAnadirUsuario;
    private Button buttonConversacionGlobal;
    private NavController navController;

    public MenuPrincipal() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.menu_principal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inicializar NavController
        navController = Navigation.findNavController(view);

        // Obtener referencias a los botones
        buttonCrearEntrenamiento = view.findViewById(R.id.crearentrenamiento);
        buttonCrearPartido = view.findViewById(R.id.crearpartido);
        buttonEditarPartido = view.findViewById(R.id.editarpartido);
        buttonAnadirUsuario = view.findViewById(R.id.anadirusuario);
        buttonConversacionGlobal = view.findViewById(R.id.global);

        // Configurar clics de los botones
        buttonCrearEntrenamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el clic del botón "Crear entrenamiento"
                // Aquí debes escribir el código que se ejecutará cuando se haga clic en el botón "Crear entrenamiento"
            }
        });

        buttonCrearPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el clic del botón "Crear partido"
                // Aquí debes escribir el código que se ejecutará cuando se haga clic en el botón "Crear partido"
            }
        });

        buttonEditarPartido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el clic del botón "Editar partido"
                // Aquí debes escribir el código que se ejecutará cuando se haga clic en el botón "Editar partido"
            }
        });

        buttonAnadirUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el clic del botón "Añadir usuario"
                // Aquí debes escribir el código que se ejecutará cuando se haga clic en el botón "Añadir usuario"
            }
        });

        buttonConversacionGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para el clic del botón "Conversación global"
                // Navegar a la pantalla de Chat cuando se hace clic en el botón "Conversación global"
                navController.navigate(R.id.Chat);
            }
        });
    }


    // Métodos para manejar clics en los elementos de la barra de navegación inferior
    public void onOption1Click(View view) {
        // Lógica para la opción 1 (menú)
    }

    public void onOption2Click(View view) {
        // Lógica para la opción 2 (conversaciones)
        // Navegar a la pantalla de Chat cuando se hace clic en el botón "Conversación global"
        navController.navigate(R.id.Chat);
    }

    public void onOption3Click(View view) {
        // Lógica para la opción 3 (calendario)
    }

    public void onOption4Click(View view) {
        // Lógica para la opción 4 (perfil)
    }
}
