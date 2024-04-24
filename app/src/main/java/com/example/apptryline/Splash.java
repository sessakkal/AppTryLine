package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    ImageView appLogoImage;
    TextView textViewTryLine;
    //LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer tiempo = new Timer();
        tiempo.schedule(tarea, 2000);

        //animaciones

        appLogoImage = findViewById(R.id.imageViewsplash);
        textViewTryLine = findViewById(R.id.textViewTryLine);
        //lottie = findViewById(R.id.lottie);


        Animation animationUp = AnimationUtils.loadAnimation(this, R.anim.anim_up);
        Animation animationDown = AnimationUtils.loadAnimation(this, R.anim.anim_down);

        appLogoImage.setAnimation(animationUp);
        textViewTryLine.setAnimation(animationDown);



    }

}



