package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Perfil extends AppCompatActivity {

    TextView correo, nombre, apellido, contrasenia, encuestasRealizadas;
    Button btnEditar;

    String Scorreo, Snombre, SApellido, SContrasenia, SencuestasRealizadas;

    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        correo = findViewById(R.id.textView18);
        nombre = findViewById(R.id.textView549);
        apellido = findViewById(R.id.textView449);
        contrasenia = findViewById(R.id.textView49);
        encuestasRealizadas = findViewById(R.id.textView19);

        btnEditar = findViewById(R.id.button554);

        IP = getString(R.string.ip_serv);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarPerfil();
            }
        });

        SharedPreferences preferencias = getSharedPreferences("PrefLogin", Context.MODE_PRIVATE);
        Scorreo = preferencias.getString("usuario","n/a");

        cargarPerfil();

    }

    private void cargarPerfil() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP+"/usuario",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (!response.isEmpty()){
                                JSONObject respuesta = new JSONObject(response);

                                Snombre = respuesta.getString("nombre");
                                SApellido = respuesta.getString("apellido");
                                SencuestasRealizadas = respuesta.getString("encuestasRealizadas");
                                SContrasenia = respuesta.getString("contrasenia");

                                correo.setText(Scorreo);
                                nombre.setText(Snombre);
                                apellido.setText(SApellido);
                                contrasenia.setText(SContrasenia);
                                encuestasRealizadas.setText(SencuestasRealizadas);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                params.put("usuario", Scorreo);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void actualizarPerfil() {

        Snombre = nombre.getText().toString();
        SApellido = apellido.getText().toString();
        SContrasenia = contrasenia.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, IP+"/usuario/"+Scorreo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Actualizacion de datos correcta", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Perfil.this, MenuApp.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"Error al actualizar datos", Toast.LENGTH_SHORT).show();
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
                params.put("apellido", SApellido);
                params.put("nombre", Snombre);
                params.put("contrasenia", SContrasenia);
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}