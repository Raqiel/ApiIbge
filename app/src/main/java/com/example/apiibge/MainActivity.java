package com.example.apiibge;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.apiibge.objetos.Estado;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public ProgressDialog carregando;
    Spinner spinnerEstados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerEstados = findViewById(R.id.spinner_estados);

        String respostaIBGE = null;
        SecondActivity secondActivity = new SecondActivity(this);

        try {
            respostaIBGE = secondActivity.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Gson gsonEstados = new GsonBuilder().setPrettyPrinting().create();
        Estado[] estados = gsonEstados.fromJson(respostaIBGE, Estado[].class);

        ArrayList<String> estadosParaSpinner = new ArrayList<>();

        for(Estado estado: estados){
           estadosParaSpinner.add(estado.getNome());
        }

        //formata o arraylist de arraylist para uma forma que seja compativel com o spinner
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                estadosParaSpinner);

        spinnerEstados.setAdapter(adapterEstados);


    }
}