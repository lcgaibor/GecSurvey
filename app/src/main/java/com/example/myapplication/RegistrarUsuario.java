package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegistrarUsuario extends AppCompatActivity {
    EditText usuarioR, nombreR, apellidoR, contraseniaR;
    Button btnRegistrar;
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrar_usuario);

        usuarioR = findViewById(R.id.usuarioR);

        String usuario = usuarioR.getText().toString();

        usuarioR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no se requiere implementación
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    if (s.toString().endsWith("@espe.edu.ec")) {
                        // el correo electrónico pertenece al dominio example.com
                    } else {
                        usuarioR.setError("El correo electrónico no pertenece al dominio espe.edu.ec");
                    }
                } else {
                    // el correo electrónico no tiene un formato válido
                    usuarioR.setError("El correo electrónico no tiene un formato válido");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no se requiere implementación
            }
        });

        nombreR = findViewById(R.id.nombreR);
        nombreR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[a-zA-Z ]+")) {
                    nombreR.setError("Solo se permiten caracteres del alfabeto");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        apellidoR = findViewById(R.id.apellidoR);
        apellidoR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[a-zA-Z ]+")) {
                    apellidoR.setError("Solo se permiten caracteres del alfabeto");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contraseniaR = findViewById(R.id.contraseniaR);

        btnRegistrar = findViewById(R.id.btnRegistrar);

        IP = getString(R.string.ip_serv);

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = usuarioR.getText().toString();
                String nombre = nombreR.getText().toString();
                String apellido = apellidoR.getText().toString();
                String contrasenia = contraseniaR.getText().toString();
                Toast.makeText(getApplicationContext(),usuario, Toast.LENGTH_SHORT);
                registrarUsuario(usuario, nombre, apellido, contrasenia);
            }
        });
    }

    private void registrarUsuario(String usuario, String nombre, String apellido, String contrasenia){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP+"/login/signup",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Registro correcto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrarUsuario.this, Inicio.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),"Registro incorrecto", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usuario", usuario);
                params.put("nombre",nombre);
                params.put("apellido", apellido);
                params.put("contrasenia", contrasenia);

                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}