package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Encuesta extends AppCompatActivity {

    RadioGroup grupoS1Preg1, grupoS2Preg1, grupoS3Preg1, grupoS6Preg1,
            grupoS1Preg2, grupoS2Preg2, grupoS2Preg3, grupoS3Preg2, grupoS3Preg3, grupoS3Preg4, grupoS3Preg5,
            grupoS4Preg1, grupoS4Preg2, grupoS4Preg3, grupoS4Preg4, grupoS4Preg5,grupoS4Preg6,
            grupoS5Preg1,grupoS5Preg2,grupoS5Preg3,grupoS5Preg4,grupoS5Preg5;
    CardView pSPregunta1, pNPregunta1, siPregunta1S2, pSPregunta1S3, pSPregunta1S6;

    Map<String, String> params;

    private String IP;

    SharedPreferences preferencias;

    boolean bandera;

    EditText editTextTextPersonName, editTextDate, editTextTextEmailAddress, editTextNumber;

    // Guardar el último año, mes y día del mes
    private int ultimoAnio, ultimoMes, ultimoDiaDelMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encuesta);

        grupoS1Preg1 = findViewById(R.id.grupoS1Preg1);
        pSPregunta1 = findViewById(R.id.pSPregunta1);
        pNPregunta1 = findViewById(R.id.pNPregunta1);

        grupoS1Preg2 = findViewById(R.id.grupoS1Preg2);

        grupoS2Preg1 = findViewById(R.id.grupoS2Preg1);
        siPregunta1S2 = findViewById(R.id.pSPregunta1S2);

        grupoS2Preg2 = findViewById(R.id.grupoS2Preg2);
        grupoS2Preg3 = findViewById(R.id.grupoS2Preg3);

        grupoS3Preg1 = findViewById(R.id.grupoS3Preg1);
        pSPregunta1S3 = findViewById(R.id.pSPregunta1S3);

        grupoS3Preg2 = findViewById(R.id.grupoS3Preg2);
        grupoS3Preg3 = findViewById(R.id.grupoS3Preg3);
        grupoS3Preg4 = findViewById(R.id.grupoS3Preg4);
        grupoS3Preg5 = findViewById(R.id.grupoS3Preg5);

        grupoS4Preg1 = findViewById(R.id.grupoS4Preg1);
        grupoS4Preg2 = findViewById(R.id.grupoS4Preg2);
        grupoS4Preg3 = findViewById(R.id.grupoS4Preg3);
        grupoS4Preg4 = findViewById(R.id.grupoS4Preg4);
        grupoS4Preg5 = findViewById(R.id.grupoS4Preg5);
        grupoS4Preg6 = findViewById(R.id.grupoS4Preg6);

        grupoS5Preg1 = findViewById(R.id.grupoS5Preg1);
        grupoS5Preg2 = findViewById(R.id.grupoS5Preg2);
        grupoS5Preg3 = findViewById(R.id.grupoS5Preg3);
        grupoS5Preg4 = findViewById(R.id.grupoS5Preg4);
        grupoS5Preg5 = findViewById(R.id.grupoS5Preg5);

        grupoS6Preg1 = findViewById(R.id.grupoS6Preg1);
        pSPregunta1S6 = findViewById(R.id.pSPregunta1S6);

        eventosRadio();

        params = new HashMap<>();

        IP = getString(R.string.ip_serv);

        preferencias = getSharedPreferences("PrefLogin", Context.MODE_PRIVATE);

        bandera = false;

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);

        editTextTextPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().matches("[\\p{L}a-zA-Z ]+")) {
                    editTextTextPersonName.setError("Solo se permiten caracteres del alfabeto");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        editTextDate = findViewById(R.id.editTextDate);

        // Poner último año, mes y día a la fecha de hoy
        final Calendar calendario = Calendar.getInstance();
        ultimoAnio = calendario.get(Calendar.YEAR);
        ultimoMes = calendario.get(Calendar.MONTH);
        ultimoDiaDelMes = calendario.get(Calendar.DAY_OF_MONTH);

        // Refrescar la fecha en el EditText
        //refrescarFechaEnEditText();

        // Hacer que el datepicker se muestre cuando toquen el EditText; recuerda
        // que se podría invocar en el click de cualquier otro botón, o en cualquier
        // otro evento

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí es cuando dan click así que mostramos el DatePicker

                // Le pasamos lo que haya en las globales
                DatePickerDialog dialogoFecha = new DatePickerDialog(Encuesta.this, listenerDeDatePicker, ultimoAnio, ultimoMes, ultimoDiaDelMes);
                //Mostrar
                dialogoFecha.show();
            }
        });


        editTextDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no se requiere implementación

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validarEdad(s.toString())) {
                } else {
                    // el correo electrónico no tiene un formato válido
                    editTextDate.setError("La edad no es válida");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no se requiere implementación
            }
        });



        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no se requiere implementación
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {

                } else {
                    // el correo electrónico no tiene un formato válido
                    editTextTextEmailAddress.setError("El correo electrónico no tiene un formato válido");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no se requiere implementación
            }
        });

        editTextNumber = findViewById(R.id.editTextNumber);

        editTextNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // no se requiere implementación

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validarCedula(s.toString())) {
                } else {
                    // el correo electrónico no tiene un formato válido
                    editTextNumber.setError("El numero de cedula no válido");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // no se requiere implementación
            }
        });
    }

    // Crear un listener del datepicker;
    private DatePickerDialog.OnDateSetListener listenerDeDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int anio, int mes, int diaDelMes) {
            ultimoAnio = anio;
            ultimoMes = mes;
            ultimoDiaDelMes = diaDelMes;

            refrescarFechaEnEditText();

        }
    };

    public void refrescarFechaEnEditText() {
        // Formateamos la fecha pero podríamos hacer cualquier otra cosa ;)
        String fecha = String.format(Locale.getDefault(), "%02d-%02d-%02d", ultimoAnio, ultimoMes+1, ultimoDiaDelMes);

        // La ponemos en el editText
        editTextDate.setText(fecha);
    }


    private void eventosRadio (){
        grupoS1Preg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                // Comprueba qué botón de radio está seleccionado
                if (checkedId == R.id.radioButton) {
                    // Muestra el componente si el primer botón de radio está seleccionado
                    pSPregunta1.setVisibility(View.VISIBLE);
                    pNPregunta1.setVisibility(View.GONE);
                } else if (checkedId == R.id.radioButton2) {
                    // Oculta el componente si el segundo botón de radio está seleccionado
                    pSPregunta1.setVisibility(View.GONE);
                    pNPregunta1.setVisibility(View.VISIBLE);
                }
            }
        });

        grupoS2Preg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radioButton19) {
                    siPregunta1S2.setVisibility(View.GONE);
                } else  {
                    siPregunta1S2.setVisibility(View.VISIBLE);
                }
            }
        });

        grupoS3Preg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radioButton34) {
                    pSPregunta1S3.setVisibility(View.VISIBLE);
                } else  {
                    pSPregunta1S3.setVisibility(View.GONE);
                }
            }
        });

        grupoS6Preg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radioButton83) {
                    pSPregunta1S6.setVisibility(View.VISIBLE);
                } else  {
                    pSPregunta1S6.setVisibility(View.GONE);
                }
            }
        });

    }

    public void enviarEncuesta (View view){

        params.put("usuario", preferencias.getString("usuario", ""));

        datosPersonales();

        if(!bandera){seccion1();};

        if(!bandera){seccion2();};

        if(!bandera){seccion3();};

        if(!bandera){seccion4();};

        if(!bandera){seccion5();};

        if(!bandera){seccion6();};

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

        if(!bandera) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, IP + "/encuestaGec",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (!response.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "Encuesta enviada", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Encuesta.this, MenuApp.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Encuesta no enviada", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return params;
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }

        bandera = false;

    }



    public void validar(){
        Toast.makeText(getApplicationContext(),"Faltan campos por llenar", Toast.LENGTH_SHORT).show();
        params.clear();
        bandera = true;
    }

    private void datosPersonales() {

        EditText editText = findViewById(R.id.editTextTextPersonName);
        String campo = editText.getText().toString();
        if (TextUtils.isEmpty(campo)) {
            validar(); 
            return;
        }
        params.put("nombre", campo);

        campo = editTextNumber.getText().toString();
        if (TextUtils.isEmpty(campo)) {
            validar(); 
            return;
        }
        params.put("cedula", campo);

        editText = findViewById(R.id.editTextPhone);
        campo = editText.getText().toString();
        if (TextUtils.isEmpty(campo)) {
            validar(); 
            return;
        }
        params.put("telefono", campo);

        editText = findViewById(R.id.editTextTextPersonName2);
        campo = editText.getText().toString();
        if (TextUtils.isEmpty(campo)) {
            validar(); 
            return;
        }
        params.put("direccion", campo);

        editText = findViewById(R.id.editTextDate);
        campo = editText.getText().toString();
        if (TextUtils.isEmpty(campo)) {
            validar(); 
            return;
        }
        params.put("fNac", campo);

        //editText = findViewById(R.id.editTextTextEmailAddress);
        campo = editTextTextEmailAddress.getText().toString();
        if (TextUtils.isEmpty(campo)) {
            validar(); 
            return;
        }
        params.put("correo", campo);
    }

    public boolean validarCedula(String cedula) {

        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int total = 0;
        int tamanoLongitudCedula = 10;
        int residuo = 0;
        int digitoVerificador = 0;

        if (cedula == null || cedula.length() != tamanoLongitudCedula) {
            return false;
        }

        String digitoString = cedula.substring(9,10);
        digitoVerificador = Integer.parseInt(digitoString);

        for (int i = 0; i < (tamanoLongitudCedula - 1); i++) {
            int valor = Integer.parseInt(cedula.substring(i, i + 1));
            int tmp = valor * coeficientes[i];
            if (tmp > 9) {
                tmp -= 9;
            }
            total += tmp;
        }

        residuo = total % 10;

        if (residuo == 0) {
            return digitoVerificador == 0;
        } else {
            return digitoVerificador == (10 - residuo);
        }

    }

    public boolean validarEdad(String edad){
        int edadN;
        if(edad.length() < 4){

        } else {
            try {
                edadN = Integer.parseInt(edad.substring(0,4));
            } catch (Exception e){
                return false;
            }
            if((ultimoAnio - edadN) <= 130){
                return true;
            }
        }
        return false;
    }

    private void seccion6() {
        RadioButton auxRadBut;

        int checkedRadioButtonId = grupoS6Preg1.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S6", auxRadBut.getText().toString());

            if( checkedRadioButtonId == R.id.radioButton83){
                cargarpSPregunta1S6();
            }
        }
    }

    private void cargarpSPregunta1S6() {
        RadioGroup rgP = findViewById(R.id.rgP1S6);
        RadioButton auxRadBut;

        int checkedRadioButtonId = rgP.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S6_P1",auxRadBut.getText().toString());
        }


        rgP = findViewById(R.id.rgP2S6);
        checkedRadioButtonId = rgP.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S6_P2",auxRadBut.getText().toString());
        }

        rgP = findViewById(R.id.rgP3S6);
        checkedRadioButtonId = rgP.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S6_P3",auxRadBut.getText().toString());
        }

    }

    private void seccion5() {
        RadioButton auxRadBut;

        int checkedRadioButtonId = grupoS5Preg1.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S5", auxRadBut.getText().toString());
        }

        checkedRadioButtonId = grupoS5Preg2.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P2_S5", auxRadBut.getText().toString());
        }

        checkedRadioButtonId = grupoS5Preg3.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P3_S5", auxRadBut.getText().toString());
        }

        checkedRadioButtonId = grupoS5Preg4.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P4_S5", auxRadBut.getText().toString());

        }
        checkedRadioButtonId = grupoS5Preg5.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar();
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P5_S5", auxRadBut.getText().toString());

        }

    }

    private void seccion4() {
        RadioButton auxRadBut;

        int checkedRadioButtonId = grupoS4Preg1.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S4", auxRadBut.getText().toString());
        }

        checkedRadioButtonId = grupoS4Preg2.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P2_S4", auxRadBut.getText().toString());
        }

        checkedRadioButtonId = grupoS4Preg3.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P3_S4", auxRadBut.getText().toString());
        }

        checkedRadioButtonId = grupoS4Preg4.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P4_S4", auxRadBut.getText().toString());
        }

        checkedRadioButtonId = grupoS4Preg5.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P5_S4", auxRadBut.getText().toString());
        }

        checkedRadioButtonId = grupoS4Preg6.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P6_S4", auxRadBut.getText().toString());
        }
    }

    private void seccion3() {
        RadioButton auxRadBut;

        int checkedRadioButtonId = grupoS3Preg1.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S3", auxRadBut.getText().toString());

            if( checkedRadioButtonId == R.id.radioButton34){
                cargarpSPregunta1S3();
            }
        }
        

        checkedRadioButtonId = grupoS3Preg2.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P2_S3", auxRadBut.getText().toString());
        }

        
        checkedRadioButtonId = grupoS3Preg3.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P3_S3", auxRadBut.getText().toString());
        }
        
        checkedRadioButtonId = grupoS3Preg4.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P4_S3", auxRadBut.getText().toString());
        }
        
        checkedRadioButtonId = grupoS3Preg5.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P5_S3", auxRadBut.getText().toString());
        }
    }

    private void cargarpSPregunta1S3() {
        RadioGroup rgP = findViewById(R.id.rgP1S3);
        RadioButton auxRadBut;
        int checkedRadioButtonId = rgP.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S3_P1",auxRadBut.getText().toString());
        }
        EditText editText = findViewById(R.id.editTextTextPersonName8);
        params.put("P1_S3_P1otro",editText.getText().toString());

        CheckBox checkBox, checkBox2, checkBox3, checkBox4;
        checkBox = findViewById(R.id.checkBox8);
        checkBox2 = findViewById(R.id.checkBox9);
        checkBox3 = findViewById(R.id.checkBox10);
        checkBox4 = findViewById(R.id.checkBox11);

        editText = findViewById(R.id.editTextTextPersonName9);

        String val1 = (checkBox.isChecked()) ? checkBox.getText().toString()+", " : "";
        String val2 = (checkBox2.isChecked()) ? checkBox2.getText().toString()+", " : "";
        String val3 = (checkBox3.isChecked()) ? checkBox3.getText().toString()+", " : "";
        String val4 = (checkBox4.isChecked()) ? checkBox4.getText().toString()+", " : "";
        String val5 = editText.getText().toString()+", ";


        String respuesta = (val1+val2+val3+val4+val5).replaceFirst("..$","");

        if (TextUtils.isEmpty(respuesta)) {
            validar(); 
            return;
        }

        params.put("P1_S3_P2",respuesta);
    }

    private void seccion2() {
        RadioButton auxRadBut;

        int checkedRadioButtonId = grupoS2Preg1.getCheckedRadioButtonId();
        
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S2", auxRadBut.getText().toString());

            if( checkedRadioButtonId != R.id.radioButton19){
                cargarpSPregunta1S2();
            }
        }
        
        checkedRadioButtonId = grupoS2Preg2.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P2_S2", auxRadBut.getText().toString());
        }
        
        checkedRadioButtonId = grupoS2Preg3.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            switch (checkedRadioButtonId) {
                case R.id.radioButton29:
                    auxRadBut = findViewById(R.id.radioButton29);
                    params.put("P3_S2", auxRadBut.getText().toString());
                    break;
                case R.id.radioButton30:
                    auxRadBut = findViewById(R.id.radioButton30);
                    params.put("P3_S2", auxRadBut.getText().toString());
                    break;
                case R.id.radioButton31:
                    auxRadBut = findViewById(R.id.radioButton31);
                    params.put("P3_S2", auxRadBut.getText().toString());
                    break;
                case R.id.radioButton32:
                    auxRadBut = findViewById(R.id.radioButton32);
                    params.put("P3_S2", auxRadBut.getText().toString());
                    break;
                case R.id.radioButton33:
                    auxRadBut = findViewById(R.id.radioButton33);
                    params.put("P3_S2", auxRadBut.getText().toString());
                    break;
            }
            //params.put("P3_S2", auxRadBut.getText().toString());
        }

    }

    private void cargarpSPregunta1S2() {
        RadioGroup rgP = findViewById(R.id.rgP1S2);
        RadioButton auxRadBut;
        int checkedRadioButtonId = rgP.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S2_P1",auxRadBut.getText().toString());
        }
        
        

        CheckBox checkBox, checkBox2, checkBox3;
        checkBox = findViewById(R.id.checkBox5);
        checkBox2 = findViewById(R.id.checkBox6);
        checkBox3 = findViewById(R.id.checkBox7);

        EditText editText = findViewById(R.id.editTextTextPersonName6);

        String val1 = (checkBox.isChecked()) ? checkBox.getText().toString()+", " : "";
        String val2 = (checkBox2.isChecked()) ? checkBox2.getText().toString()+", " : "";
        String val3 = (checkBox3.isChecked()) ? checkBox3.getText().toString()+", " : "";
        String val4 = editText.getText().toString()+", ";


        String respuesta = (val1+val2+val3+val4).replaceFirst("..$","");

        if (TextUtils.isEmpty(respuesta)) {
            validar(); 
            return;
        }
        params.put("P1_S2_P2",respuesta);

        editText = findViewById(R.id.editTextTextPersonName7);
        respuesta = editText.getText().toString();

        if (TextUtils.isEmpty(respuesta)) {
            validar(); 
            return;
        }
        params.put("P1_S2_P3",respuesta);

    }

    public void seccion1 () {

        RadioButton auxRadBut;

        int checkedRadioButtonId = grupoS1Preg1.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            switch (checkedRadioButtonId) {
                case R.id.radioButton:
                    auxRadBut = findViewById(R.id.radioButton);
                    params.put("P1_S1", auxRadBut.getText().toString());
                    cargarpSPregunta1();
                    break;
                case R.id.radioButton2:
                    auxRadBut = findViewById(R.id.radioButton2);
                    params.put("P1_S1", auxRadBut.getText().toString());
                    cargarpNPregunta1();
                    break;
                default:
                    Toast.makeText(this, "No ha seleccionado en la 1", Toast.LENGTH_SHORT).show();
                    break;
            }
        }


        checkedRadioButtonId = grupoS1Preg2.getCheckedRadioButtonId();

        if (checkedRadioButtonId == -1) {
            int lastChildPos=grupoS1Preg2.getChildCount()-1;
            ((RadioButton)grupoS1Preg2.getChildAt(lastChildPos)).setError("Debes completar esta pregunta");
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P2_S1", auxRadBut.getText().toString());
        }
    }

    private void cargarpNPregunta1() {
        EditText editText = findViewById(R.id.editTextTextPersonName5);

        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError("Debes completar esta pregunta");
            validar(); 
            return;
        }
        params.put("P1_S1_P1_N",editText.getText().toString());
    }

    private void cargarpSPregunta1() {
        RadioGroup rgP = findViewById(R.id.rgP2);
        RadioButton auxRadBut;
        int checkedRadioButtonId = rgP.getCheckedRadioButtonId();

        EditText editText = findViewById(R.id.editTextTextPersonName3);
        String valor = editText.getText().toString();

        if((checkedRadioButtonId == -1) && valor.isEmpty()){
            editText.setError("Debes completar esta pregunta");
            validar();
            return;
        }

        //solo si esta seleccionado se envia el campo
        if(!(checkedRadioButtonId == -1)){
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S1_P1",auxRadBut.getText().toString());
        }

        if (!valor.isEmpty()){
            params.put("P1_S1_P1otro",valor);
            return;
        }

        rgP = findViewById(R.id.rgP3);
        checkedRadioButtonId = rgP.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S1_P2",auxRadBut.getText().toString());
        }

        rgP = findViewById(R.id.rgP4);
        checkedRadioButtonId = rgP.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S1_P3",auxRadBut.getText().toString());
        }

        rgP = findViewById(R.id.rgP5);
        checkedRadioButtonId = rgP.getCheckedRadioButtonId();
        if (checkedRadioButtonId == -1) {
            validar(); 
            return;
        } else {
            auxRadBut = findViewById(checkedRadioButtonId);
            params.put("P1_S1_P4",auxRadBut.getText().toString());
        }

        CheckBox checkBox, checkBox2, checkBox3, checkBox4;
        checkBox = findViewById(R.id.checkBox);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);

        editText = findViewById(R.id.editTextTextPersonName4);

        String val1 = (checkBox.isChecked()) ? checkBox.getText().toString()+", " : "";
        String val2 = (checkBox2.isChecked()) ? checkBox2.getText().toString()+", " : "";
        String val3 = (checkBox3.isChecked()) ? checkBox3.getText().toString()+", " : "";
        String val4 = (checkBox4.isChecked()) ? checkBox4.getText().toString()+", " : "";
        String val5 = editText.getText().toString()+", ";

        String respuesta = (val1+val2+val3+val4+val5).replaceFirst("..$","");

        if (TextUtils.isEmpty(respuesta)) {
            editText.setError("Debes completar esta pregunta");
            validar(); 
            return;
        }

        params.put("P1_S1_P5",respuesta);

    }
}