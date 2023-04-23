package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Perfil extends AppCompatActivity {

    TextView correo, nombre, apellido, contrasenia;
    Button btnEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        correo = findViewById(R.id.textView18);
        nombre = findViewById(R.id.textView549);
        apellido = findViewById(R.id.textView449);
        contrasenia = findViewById(R.id.textView49);

        btnEditar = findViewById(R.id.button554);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}