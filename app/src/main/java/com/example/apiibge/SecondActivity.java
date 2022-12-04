package com.example.apiibge;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.PipedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SecondActivity extends AsyncTask<Void, Void, String> {

    MainActivity mainActivity = new MainActivity();
    ProgressDialog carregando = mainActivity.carregando;

    private Context spContext;
    public SecondActivity(Context context){
        spContext = context;
    }
    @Override
    protected void onPreExecute(){
        carregando = ProgressDialog.show(spContext, "Carregando Estados", "Por favor Aguarde...");
    }

    @Override
    protected String doInBackground(Void... voids) {

        Log.d("Local", "segundo plano");

        StringBuilder respostaIBGE = new StringBuilder();

        try {
            URL urlEstados = new URL("https://servicodados.ibge.gov.br/api/v1/localidades/estados/");

            HttpURLConnection conexao = (HttpURLConnection) urlEstados.openConnection();
            conexao.setRequestMethod("GET");
            conexao.setRequestProperty("Content-type","application/json");
            conexao.setDoOutput(true);
            conexao.setConnectTimeout(3000);
            conexao.connect();

            Scanner scanner = new Scanner(urlEstados.openStream());
            while(scanner.hasNext()){
                respostaIBGE.append(scanner.next());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



        return respostaIBGE.toString();
    }

    @Override
    protected void onPostExecute(String dados){
        if (dados != null) {
            Log.d("onPost", dados);
        }else{
            Log.d("onPost", "null");
        }

    }


}
