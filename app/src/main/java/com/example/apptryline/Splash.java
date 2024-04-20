package com.example.apptryline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    ImageView logoSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        TimerTask task =new TimerTask() {
            @Override
            public void run() {
                Intent intent= new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer tiempo= new Timer();
        tiempo.schedule(task,2000);

        //animacion
        logoSplash = findViewById(R.id.logosplash);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
        logoSplash.setAnimation(animation);
    }
}