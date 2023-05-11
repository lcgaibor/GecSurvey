package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.regex.Pattern;

public class Perfil extends AppCompatActivity {

    TextView correo, nombre, apellido, contrasenia, encuestasRealizadas;
    Button btnEditar;

    String Scorreo, Snombre, SApellido, SContrasenia, SencuestasRealizadas;

    private static final String PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.,;])(?=\\S+$).{8,15}$";

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);

    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        correo = findViewById(R.id.textView18);
        nombre = findViewById(R.id.textView549);
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[\\p{L}a-zA-Z ]+")) {
                    nombre.setError("Solo se permiten caracteres del alfabeto");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        apellido = findViewById(R.id.textView449);
        apellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[\\p{L}a-zA-Z ]+")) {
                    apellido.setError("Solo se permiten caracteres del alfabeto");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contrasenia = findViewById(R.id.textView49);

        contrasenia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!validarContrasena(s.toString())){
                    contrasenia.setError("La contraseña debe contener mínimo 8 caracteres y máximo 15, " +
                            "además esta contraseña debe contener letras mayúsculas, minúsculas, numeros y simbolos");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


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

    public boolean validarContrasena(String contrasena) {
        return PASSWORD_PATTERN.matcher(contrasena).matches();
    }
    private void cargarPerfil() {

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(ConnectivityManager.class);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
            final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            String ssid = null;
            if (connectionInfo != null) {
                ssid = connectionInfo.getSSID();
            }
            if(ssid.contains("ESPE") || ssid.contains("INVITADOS") || ssid.contains("eduroam")  ){
                IP = "http://10.3.0.251:3000";
            }
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP+"/usuarioGec",
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

        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(ConnectivityManager.class);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){
            final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            String ssid = null;
            if (connectionInfo != null) {
                ssid = connectionInfo.getSSID();
            }
            if(ssid.contains("ESPE") || ssid.contains("INVITADOS") || ssid.contains("eduroam")  ){
                IP = "http://10.3.0.251:3000";
            }
        }

        Snombre = nombre.getText().toString();
        SApellido = apellido.getText().toString();
        SContrasenia = contrasenia.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, IP+"/usuarioGec/"+Scorreo,
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