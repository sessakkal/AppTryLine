package com.example.apptryline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

public class ResultadoPartido extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultado_partido);

        // Configurar el ViewPager y el TabLayout
        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        // Crear el adaptador para ViewPager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Agregar los fragmentos a ViewPager
        adapter.addActivity(EstadisticasPartido.class, "Estadisticas");
        adapter.addActivity(Partido.class, "Partido");



        // Establecer el adaptador en ViewPager
        viewPager.setAdapter(adapter);

        // Vincular el ViewPager con el TabLayout
        tabLayout.setupWithViewPager(viewPager);
    }
}

