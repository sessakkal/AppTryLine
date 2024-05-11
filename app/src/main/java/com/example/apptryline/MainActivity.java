package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Iniciar la actividad InicioRegistro
        Intent intent = new Intent(this, InicioRegistro.class);
        startActivity(intent);
    }
}
