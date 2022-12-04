package com.example.apiibge;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SecondActivity extends AsyncTask<String, Void, String> {

    MainActivity mainActivity = new MainActivity();
//    ProgressBar carregando = mainActivity.carregando;
    private Context spContext;

    public SecondActivity(Context context){
        spContext = context;
    }
    @Override
    protected void onPreExecute(){
        mainActivity.exibirProgress(true);

    }

    @Override
    protected String doInBackground(String... params) {


        if(params[0].equals("estado")){

            StringBuilder respostaEstados = new StringBuilder();

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
                    respostaEstados.append(scanner.next());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



            return respostaEstados.toString();

        }else  if(params[0].equals("municipio")){

            StringBuilder respostaMunicipios = new StringBuilder();

            try {
                URL urlMunicipios = new URL("https://servicodados.ibge.gov.br/api/v1/localidades/estados/"+params[1] +"/municipios");

                HttpURLConnection conexao = (HttpURLConnection) urlMunicipios.openConnection();
                conexao.setRequestMethod("GET");
                conexao.setRequestProperty("Content-type","application/json");
                conexao.setDoOutput(true);
                conexao.setConnectTimeout(3000);
                conexao.connect();

                Scanner scanner = new Scanner(urlMunicipios.openStream());
                while(scanner.hasNext()){
                    respostaMunicipios.append(scanner.next());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



            return respostaMunicipios.toString();
        }
            return null;
    }

    @Override
    protected void onPostExecute(String dados){
        mainActivity.exibirProgress(false);

    }


}
