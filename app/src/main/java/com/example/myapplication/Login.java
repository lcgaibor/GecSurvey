package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText txtUsuario, txtContrasenia;
    Button btnIngresar;

    private String IP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //validarSesion();

        txtUsuario = findViewById(R.id.usuario);
        txtContrasenia = findViewById(R.id.contrasenia);

        btnIngresar = findViewById(R.id.btnIngresar);
        //btnRegistrar = findViewById(R.id.registro);

        IP = getString(R.string.ip_serv);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usuario = txtUsuario.getText().toString();
                String contrasenia = txtContrasenia.getText().toString();
                Toast.makeText(getApplicationContext(),usuario, Toast.LENGTH_SHORT);
                validarUsuario(usuario, contrasenia);
            }
        });

    }
/*
    private void validarSesion(){
        SharedPreferences preferencias = getSharedPreferences("PrefLogin", Context.MODE_PRIVATE);
        boolean sesion = preferencias.getBoolean("sesion",false);
        if(sesion){
            Intent intent = new Intent(Login.this, MenuApp.class);
            startActivity(intent);
            finish();
        }
    }
*/
    private void validarUsuario(String usuario, String contrasenia){

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

        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP+"/loginGec/sign",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Logueo correcto", Toast.LENGTH_SHORT).show();
                            guardarPreferencias(usuario, contrasenia);
                            Intent intent = new Intent(Login.this, MenuApp.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"Logueo incorrecto", Toast.LENGTH_SHORT).show();
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
                params.put("contrasenia", contrasenia);

                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void guardarPreferencias(String usuario, String contrasenia){
        SharedPreferences preferencias = getSharedPreferences("PrefLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("usuario", usuario);
        editor.putString("contrasenia", contrasenia);
        editor.putBoolean("sesion",true);
        editor.commit();
    }

    private void recuperarPreferencias(){
        SharedPreferences preferencias = getSharedPreferences("PrefLogin", Context.MODE_PRIVATE);
        txtUsuario.setText(preferencias.getString("usuario", ""));
        txtContrasenia.setText(preferencias.getString("contrasenia",""));
    }
}