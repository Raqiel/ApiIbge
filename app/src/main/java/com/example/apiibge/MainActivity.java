package com.example.apiibge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.apiibge.objetos.Estado;
import com.example.apiibge.objetos.Municipio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static ProgressBar carregando;
    Spinner spinnerEstados;
    Spinner spinnerMunicipios;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerEstados = findViewById(R.id.spinner_estados);
        spinnerMunicipios = findViewById(R.id.spinner_municipios);
        carregando = findViewById(R.id.progressBar);

        String respostaEstados = getRespostaIBGE("estado");


        Gson gsonEstados = new GsonBuilder().setPrettyPrinting().create();
        Estado[] estados = gsonEstados.fromJson(respostaEstados, Estado[].class);

        ArrayList<String> estadosParaSpinner = new ArrayList<>();
        ArrayList<String> siglasEstados = new ArrayList<>();

        for(Estado estado: estados){
           estadosParaSpinner.add(estado.getNome());
           siglasEstados.add(estado.getSigla());
        }

        Collections.sort(estadosParaSpinner);

        //formata o arraylist de arraylist para uma forma que seja compativel com o spinner
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                estadosParaSpinner);

        spinnerEstados.setAdapter(adapterEstados);

        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                solicitaMunicipios(siglasEstados.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMunicipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void solicitaMunicipios(String siglasEstado) {

        String respostaMunicipios = getRespostaIBGE("municipio", siglasEstado);

        Gson gsonMunicipios = new GsonBuilder().setPrettyPrinting().create();
        Municipio[] municipios = gsonMunicipios.fromJson(String.valueOf(respostaMunicipios), Municipio[].class);

        final ArrayList<String> municipiosParaSpinner = new ArrayList<>();
        ArrayList<String> idMunicipios = new ArrayList<>();

        for(Municipio municipio: municipios){
            municipiosParaSpinner.add(municipio.getNome());
            idMunicipios.add(String.valueOf(municipio.getId()));
        }

        Collections.sort(municipiosParaSpinner);

        //formata o arraylist de arraylist para uma forma que seja compativel com o spinner
        ArrayAdapter<String> adapterMunicipios = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                municipiosParaSpinner);

        spinnerMunicipios.setAdapter(adapterMunicipios);
    }

    private String getRespostaIBGE(String... params) {

        String respostaIBGE = null;

        SecondActivity secondActivity = new SecondActivity(this);

        try {
            respostaIBGE = secondActivity.execute(params).get();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return respostaIBGE;
    }

    public static void exibirProgress(boolean exibir){
        carregando.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }
}