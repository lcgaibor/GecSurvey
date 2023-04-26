package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Encuesta extends AppCompatActivity {

    RadioGroup grupoS1Preg1, grupoS2Preg1, grupoS3Preg1, grupoS6Preg1,
            grupoS1Preg2, grupoS2Preg2, grupoS2Preg3, grupoS3Preg2, grupoS3Preg3, grupoS3Preg4, grupoS3Preg5,
            grupoS4Preg1, grupoS4Preg2, grupoS4Preg3, grupoS4Preg4, grupoS4Preg5,grupoS4Preg6,
            grupoS5Preg1,grupoS5Preg2,grupoS5Preg3,grupoS5Preg4,grupoS5Preg5;
    CardView pSPregunta1, pNPregunta1, siPregunta1S2, pSPregunta1S3, pSPregunta1S6;

    Map<String, String> params;

    private String IP;

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

        grupoS6Preg1 = findViewById(R.id.grupoS6Preg1);
        pSPregunta1S6 = findViewById(R.id.pSPregunta1S6);

        eventosRadio();

        params = new HashMap<>();

        IP = getString(R.string.ip_serv);

        SharedPreferences preferencias = getSharedPreferences("PrefLogin", Context.MODE_PRIVATE);

        params.put("usuario", preferencias.getString("usuario", ""));
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

        //obtengo el texto
        //String text = auxRadBut.getText().toString();

        datosPersonales();

        seccion1 ();

        seccion2();

        seccion3();

        seccion4();

        seccion5();

        seccion6();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, IP+"/encuesta",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.isEmpty()){
                            Toast.makeText(getApplicationContext(),"Encuesta enviada", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Encuesta.this, MenuApp.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(),"Encuesta no enviada", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void datosPersonales() {

        EditText editText = findViewById(R.id.editTextTextPersonName);
        params.put("nombre", editText.getText().toString());

        editText = findViewById(R.id.editTextNumber);
        params.put("cedula", editText.getText().toString());

        editText = findViewById(R.id.editTextPhone);
        params.put("telefono", editText.getText().toString());

        editText = findViewById(R.id.editTextTextPersonName2);
        params.put("direccion", editText.getText().toString());

        editText = findViewById(R.id.editTextDate);
        params.put("fNac", editText.getText().toString());

        editText = findViewById(R.id.editTextTextEmailAddress);
        params.put("correo", editText.getText().toString());

    }

    private void seccion6() {
        RadioButton auxRadBut;

        int auxR = grupoS6Preg1.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P1_S6", auxRadBut.getText().toString());

        if( auxR == R.id.radioButton83){
            cargarpSPregunta1S6();
        }
    }

    private void cargarpSPregunta1S6() {
        RadioGroup rgP = findViewById(R.id.rgP1S6);
        int auxRadButId = rgP.getCheckedRadioButtonId();
        RadioButton auxRadBut = findViewById(auxRadButId);
        params.put("P1_S6_P1",auxRadBut.getText().toString());

        rgP = findViewById(R.id.rgP2S6);
        auxRadButId = rgP.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxRadButId);
        params.put("P1_S6_P2",auxRadBut.getText().toString());

        rgP = findViewById(R.id.rgP3S6);
        auxRadButId = rgP.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxRadButId);
        params.put("P1_S6_P3",auxRadBut.getText().toString());


    }

    private void seccion5() {
        RadioButton auxRadBut;

        int auxR = grupoS5Preg1.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P1_S5", auxRadBut.getText().toString());

        auxR = grupoS5Preg2.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P2_S5", auxRadBut.getText().toString());

        auxR = grupoS5Preg3.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P3_S5", auxRadBut.getText().toString());

        auxR = grupoS5Preg4.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P4_S5", auxRadBut.getText().toString());

    }

    private void seccion4() {
        RadioButton auxRadBut;

        int auxR = grupoS4Preg1.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P1_S4", auxRadBut.getText().toString());

        auxR = grupoS4Preg2.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P2_S4", auxRadBut.getText().toString());

        auxR = grupoS4Preg3.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P3_S4", auxRadBut.getText().toString());

        auxR = grupoS4Preg4.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P4_S4", auxRadBut.getText().toString());

        auxR = grupoS4Preg5.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P5_S4", auxRadBut.getText().toString());

        auxR = grupoS4Preg6.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P6_S4", auxRadBut.getText().toString());
    }

    private void seccion3() {
        RadioButton auxRadBut;

        int auxR = grupoS3Preg1.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P1_S3", auxRadBut.getText().toString());

        if( auxR == R.id.radioButton34){
            cargarpSPregunta1S3();
        }

        auxR = grupoS3Preg2.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P2_S3", auxRadBut.getText().toString());

        auxR = grupoS3Preg3.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P3_S3", auxRadBut.getText().toString());

        auxR = grupoS3Preg4.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P4_S3", auxRadBut.getText().toString());

        auxR = grupoS3Preg5.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxR);
        params.put("P5_S3", auxRadBut.getText().toString());

    }

    private void cargarpSPregunta1S3() {
        RadioGroup rgP = findViewById(R.id.rgP1S3);
        int auxRadButId = rgP.getCheckedRadioButtonId();
        RadioButton auxRadBut = findViewById(auxRadButId);
        params.put("P1_S3_P1",auxRadBut.getText().toString());

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

        params.put("P1_S3_P2",respuesta);
    }

    private void seccion2() {
        RadioButton auxRadBut;

        int auxR = grupoS2Preg1.getCheckedRadioButtonId();

        auxRadBut = findViewById(auxR);

        params.put("P1_S2", auxRadBut.getText().toString());

        if( auxR != R.id.radioButton19){
            cargarpSPregunta1S2();
        }

        auxR = grupoS2Preg2.getCheckedRadioButtonId();

        auxRadBut = findViewById(auxR);

        params.put("P2_S2", auxRadBut.getText().toString());


        switch (grupoS2Preg3.getCheckedRadioButtonId()){
            case R.id.radioButton29:
                auxRadBut = findViewById(R.id.radioButton29);
                break;
            case R.id.radioButton30:
                auxRadBut = findViewById(R.id.radioButton30);
                break;
            case R.id.radioButton31:
                auxRadBut = findViewById(R.id.radioButton31);
                break;
            case R.id.radioButton32:
                auxRadBut = findViewById(R.id.radioButton32);
                break;
            case R.id.radioButton33:
                auxRadBut = findViewById(R.id.radioButton33);
                break;

        }

        params.put("P3_S2", auxRadBut.getText().toString());

    }

    private void cargarpSPregunta1S2() {
        RadioGroup rgP = findViewById(R.id.rgP1S2);
        int auxRadButId = rgP.getCheckedRadioButtonId();
        RadioButton auxRadBut = findViewById(auxRadButId);

        params.put("P1_S2_P1",auxRadBut.getText().toString());

        CheckBox checkBox, checkBox2, checkBox3, checkBox4;
        checkBox = findViewById(R.id.checkBox5);
        checkBox2 = findViewById(R.id.checkBox6);
        checkBox3 = findViewById(R.id.checkBox7);

        EditText editText = findViewById(R.id.editTextTextPersonName6);

        String val1 = (checkBox.isChecked()) ? checkBox.getText().toString()+", " : "";
        String val2 = (checkBox2.isChecked()) ? checkBox2.getText().toString()+", " : "";
        String val3 = (checkBox3.isChecked()) ? checkBox3.getText().toString()+", " : "";
        String val4 = editText.getText().toString()+", ";


        String respuesta = (val1+val2+val3+val4).replaceFirst("..$","");

        params.put("P1_S2_P2",respuesta);

        editText = findViewById(R.id.editTextTextPersonName7);

        respuesta = editText.getText().toString();

        params.put("P1_S2_P3",respuesta);

    }

    public void seccion1 () {

        RadioButton auxRadBut;

        int checkedRadioButtonId = grupoS1Preg1.getCheckedRadioButtonId();
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

        switch (grupoS1Preg2.getCheckedRadioButtonId()){
            case R.id.radioButton17:
                auxRadBut = findViewById(R.id.radioButton17);
                params.put("P1_S1", auxRadBut.getText().toString());
                break;
            case R.id.radioButton18:
                auxRadBut = findViewById(R.id.radioButton18);
                params.put("P1_S1", auxRadBut.getText().toString());
                break;
            default:
                Toast.makeText(this, "No ha seleccionado en la 2", Toast.LENGTH_SHORT ).show();
        }
    }

    private void cargarpNPregunta1() {
        EditText editText = findViewById(R.id.editTextTextPersonName5);
        params.put("P1_S1_P1_N",editText.getText().toString());
    }

    private void cargarpSPregunta1() {
        RadioGroup rgP = findViewById(R.id.rgP2);
        int auxRadButId = rgP.getCheckedRadioButtonId();
        RadioButton auxRadBut = findViewById(auxRadButId);
        params.put("P1_S1_P1",auxRadBut.getText().toString());

        EditText editText = findViewById(R.id.editTextTextPersonName3);
        params.put("P1_S1_P1otro",editText.getText().toString());

        rgP = findViewById(R.id.rgP3);
        auxRadButId = rgP.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxRadButId);
        params.put("P1_S1_P2",auxRadBut.getText().toString());

        rgP = findViewById(R.id.rgP4);
        auxRadButId = rgP.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxRadButId);
        params.put("P1_S1_P3",auxRadBut.getText().toString());

        rgP = findViewById(R.id.rgP5);
        auxRadButId = rgP.getCheckedRadioButtonId();
        auxRadBut = findViewById(auxRadButId);
        params.put("P1_S1_P4",auxRadBut.getText().toString());

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

        params.put("P1_S1_P5",respuesta);

    }
}