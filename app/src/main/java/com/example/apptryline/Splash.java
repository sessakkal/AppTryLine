package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    ImageView logoSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Obtener la referencia de la imagen de la pelota
        logoSplash = findViewById(R.id.logosplash);

        // Cargar la animación desde el archivo XML
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animacion_pelota);

        // Aplicar la animación a la imagen de la pelota
        logoSplash.startAnimation(animation);

        // Configurar el temporizador para pasar a la actividad principal después de cierto tiempo
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer tiempo = new Timer();
        tiempo.schedule(task, 1200);
    }
}




