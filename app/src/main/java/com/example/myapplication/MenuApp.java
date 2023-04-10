package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MenuApp extends AppCompatActivity {

    ImageButton btnEncuestar, btnUsuario, btnInformacion, btnCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_app);

        btnEncuestar = findViewById(R.id.btnEncuestar);
        btnUsuario = findViewById(R.id.btnUsuario);
        btnInformacion = findViewById(R.id.btnInformacion);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnEncuestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuApp.this, Encuesta.class);
                startActivity(intent);
            }
        });

        btnUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnInformacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferencias = getSharedPreferences("PrefLogin", Context.MODE_PRIVATE);
                preferencias.edit().clear().commit();
                Intent intent = new Intent(MenuApp.this, Inicio.class);
                startActivity(intent);
                finish();
            }
        });
    }
}