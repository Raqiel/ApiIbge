package com.example.API_IBGE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.API_IBGE.objetos.Estado;
import com.example.API_IBGE.objetos.Municipio;
import com.example.API_IBGE.objetos.Subdistrito;
import com.example.API_IBGE.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static ProgressBar carregando;
    Spinner spinnerEstados;
    Spinner spinnerMunicipios;
    Spinner spinnerSubdistritos;
    Municipio[] municipios = null;
    final ArrayList<String> subdistritosParaSpinner = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerEstados = findViewById(R.id.spinner_estados);
        spinnerMunicipios = findViewById(R.id.spinner_municipios);
        spinnerMunicipios.setEnabled(false);
        spinnerSubdistritos = findViewById(R.id.spinner_subdistritos);
        spinnerSubdistritos.setEnabled(false);

        carregando = findViewById(R.id.progressBar);

        String respostaEstados = getRespostaIBGE("estado");


        Gson gsonEstados = new GsonBuilder().setPrettyPrinting().create();
        final Estado[] estados = gsonEstados.fromJson(respostaEstados, Estado[].class);


        ArrayList<String> estadosParaSpinner = new ArrayList<>();

        for(Estado estado: estados){
           estadosParaSpinner.add(estado.getNome());
        }

        Collections.sort(estadosParaSpinner);

        estadosParaSpinner.add(0, "Selecione o estado");

        //formata o arraylist de arraylist para uma forma que seja compativel com o spinner
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                estadosParaSpinner);

        spinnerEstados.setAdapter(adapterEstados);

        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                for (Estado estado:estados){
                    if (estado.getNome().equals(spinnerEstados.getSelectedItem().toString())){
                        solicitaMunicipios(estado.getSigla());
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerMunicipios.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                for (Municipio municipio:municipios){
                    if (municipio.getNome().equals(spinnerMunicipios.getSelectedItem().toString())){
                        solicitaSubdistritos(String.valueOf(municipio.getId()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void solicitaMunicipios(String siglasEstado) {

        String respostaMunicipios = getRespostaIBGE("municipio", siglasEstado);

        Gson gsonMunicipios = new GsonBuilder().setPrettyPrinting().create();
        municipios = gsonMunicipios.fromJson(String.valueOf(respostaMunicipios), Municipio[].class);

        final ArrayList<String> municipiosParaSpinner = new ArrayList<>();
        ArrayList<String> idMunicipios = new ArrayList<>();

        for(Municipio municipio: municipios){
            municipiosParaSpinner.add(municipio.getNome());
            idMunicipios.add(String.valueOf(municipio.getId()));
        }

        Collections.sort(municipiosParaSpinner);

        municipiosParaSpinner.add(0,"Selecione o municipio");
        spinnerMunicipios.setEnabled(true);
        spinnerSubdistritos.setEnabled(false);
        subdistritosParaSpinner.add(0, "selecione o subdistrito");
        spinnerSubdistritos.setSelection(0);

        ArrayAdapter<String> adapterMunicipios = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                municipiosParaSpinner);

        spinnerMunicipios.setAdapter(adapterMunicipios);
    }

    private void solicitaSubdistritos(String idMunicipio) {

        String respostaSubdistritos = getRespostaIBGE("subdistrito", idMunicipio);

        Gson gsonSubdistritos = new GsonBuilder().setPrettyPrinting().create();
        Subdistrito[] subdistritos = gsonSubdistritos.fromJson(String.valueOf(respostaSubdistritos), Subdistrito[].class);



        if(subdistritos.length > 0){

            for(Subdistrito subdistrito: subdistritos){
                subdistritosParaSpinner.add(subdistrito.getNome());
            }

            Collections.sort(subdistritosParaSpinner);

            subdistritosParaSpinner.add(0, "selecione o subdistrito");
            spinnerSubdistritos.setEnabled(true);
        }else{
            subdistritosParaSpinner.add(0, "Não existe subdistrito para esse municipio");
            spinnerSubdistritos.setEnabled(false);
        }





        //formata o arraylist de arraylist para uma forma que seja compativel com o spinner
        ArrayAdapter<String> adapterSubdistritos = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                subdistritosParaSpinner);

        spinnerSubdistritos.setAdapter(adapterSubdistritos);
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