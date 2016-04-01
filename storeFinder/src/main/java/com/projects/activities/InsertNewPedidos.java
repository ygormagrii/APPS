package com.projects.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.db.DbHelper;
import com.db.Queries;
import com.libraries.usersession.UserAccessSession;
import com.libraries.usersession.UserSession;
import com.projects.storefinder.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InsertNewPedidos extends Activity {

    String qtd;
    String descricao;
    InputStream is=null;
    String result=null;
    String line=null;
    int code;
    private Spinner spn1;
    private Spinner spn2;
    private List<String> categorias = new ArrayList<String>();
    private String nome;
    private String nomeUnidade;
    private static SQLiteDatabase db;
    private static DbHelper dbHelper;
    private List<String> edtUnidade = new ArrayList<String>();
    private static Queries q;
    public Queries getQueries() {
        return q;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_pedidostwo);
        dbHelper = new DbHelper(this);
        q = new Queries(db, dbHelper);
        final Queries q = getQueries();

        //Identifica o Spinner no layout
        ArrayList<String> categories = q.getCategoryNames();
        String allCategories = "Todas categorias";
        categories.add(0, allCategories);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                InsertNewPedidos.this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spn1 = (Spinner) findViewById(R.id.spinnerCategories);
        spn1.setAdapter(dataAdapter);

        //Método do Spinner para capturar o item selecionado
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                nome = parent.getItemAtPosition(posicao).toString();
                //imprime um Toast na tela com o nome que foi selecionado
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Adicionando Nomes no ArrayList
        edtUnidade.add("Quilograma - kg");
        edtUnidade.add("Litro - l");
        edtUnidade.add("Unidade - un");
        edtUnidade.add("Caixa - cx");
        edtUnidade.add("Metro - m");

        //Identifica o Spinner no layout
        spn2 = (Spinner) findViewById(R.id.Unidade);
        //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, edtUnidade);
        ArrayAdapter<String> spinnerArrayAdapter2 = arrayAdapter2;
        spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spn2.setAdapter(spinnerArrayAdapter2);

        //Método do Spinner para capturar o item selecionado
        spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                nomeUnidade = parent.getItemAtPosition(posicao).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final EditText e_qtd=(EditText) findViewById(R.id.descricao);
        final EditText e_descricao=(EditText) findViewById(R.id.quantidade);
        Button btnBack = (Button) findViewById(R.id.buttonCancelar);
        Button insert=(Button) findViewById(R.id.button1);


        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(e_qtd.getText().toString().length()>0 && e_descricao.getText().toString().length() != 0) {

                    // TODO Auto-generated method stub
                    qtd = e_qtd.getText().toString();
                    descricao = e_descricao.getText().toString();
                    new loadData().execute();

                }else{
                    if(e_descricao.getText().length() == 0){//como o tamanho é zero é nulla aresposta
                        e_descricao.setError("Campo vazio");
                    }

                    if(e_qtd.getText().length() == 0){//como o tamanho é zero é nulla aresposta
                        e_qtd.setError("Campo vazio");
                    }
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InsertNewPedidos.this, PreHome.class);
                startActivity(intent);
                finish();
            }
        });
}


    class loadData extends AsyncTask<String, Integer, String> {
        private StringBuilder sb;
        private ProgressDialog pr;
        private HttpResponse req;
        private InputStream is;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Salvando ...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... arg0) {

            UserAccessSession userAccess = UserAccessSession.getInstance(InsertNewPedidos.this);
            UserSession userSession = userAccess.getUserSession();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();


            nameValuePairs.add(new BasicNameValuePair("nome", nome));
            nameValuePairs.add(new BasicNameValuePair("descricao", descricao));
            nameValuePairs.add(new BasicNameValuePair("qtd",qtd));
            nameValuePairs.add(new BasicNameValuePair("nomeUnidade", nomeUnidade));
            nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(userSession.getUser_id())));

            try
            {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://marketingdigitalabc.com.br/buysell/pedidos_insert.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                InputStreamReader ireader = new InputStreamReader(is);
                BufferedReader bf = new BufferedReader(ireader);
                sb = new StringBuilder();
                String line = null;
                while ((line = bf.readLine()) != null) {
                    sb.append(line);
                }
                Log.e("pass 1", "connection success ");
            }
            catch(Exception e)
            {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }
            return qtd;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), "Pedido adicionado com sucesso", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(InsertNewPedidos.this, FinalizarPedido.class);
            startActivity(intent);
            finish();
        }

    }

}