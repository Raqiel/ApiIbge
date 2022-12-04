package com.example.apiibge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.apiibge.objetos.Estado;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String respostaIBGE = null;
        SecondActivity secondActivity = new SecondActivity();

        try {
            respostaIBGE = secondActivity.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Gson gsonEstados = new GsonBuilder().setPrettyPrinting().create();
        Estado[] estados = gsonEstados.fromJson(respostaIBGE, Estado[].class);

        for(Estado estado: estados){
            Log.d("ID", String.valueOf(estado.getId()));
            Log.d("Sigla", estado.getSigla());
            Log.d("Nome", estado.getNome());
        }


    }
}