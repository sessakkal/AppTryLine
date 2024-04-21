package com.example.apptryline;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Navegar al fragmento MenuIniciarSesionRegistrar
        navController.navigate(R.id.InicioRegistro);
    }




}
