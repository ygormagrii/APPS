package com.projects.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Ygor on 23/02/2016.
 */
public class FinalizarPedido extends Activity {

    private TextView tvDescricao;
    private TextView tvQuantidade;
    private String Descricao;
    private String Unidade;
    private String Categoria;
    private String Qtd;
    private AlertDialog alerta;
    String user_id;
    InputStream is=null;
    String result=null;
    String line=null;
    ArrayList<String> pedidos_id = new ArrayList<String>();
    ArrayList<String> pedidos_categoria = new ArrayList<String>();
    ArrayList<String> pedidos_descricao = new ArrayList<String>();
    ArrayList<String> pedidos_qtd = new ArrayList<String>();
    ArrayList<String> pedidos_unidade = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finaliza_pedido);

        Button novoPedido = (Button) findViewById(R.id.novo_pedido);
        Button btnEnd = (Button) findViewById(R.id.buttonFim);

        new select().execute();

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalizarPedido.this, FormPedidos.class);
                startActivity(intent);
            }
        });


        novoPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FinalizarPedido.this, InsertNewPedidos.class);
                startActivity(intent);
            }
        });

    }

    class select extends AsyncTask<String, Integer, String> {

        private StringBuilder sb;
        private ProgressDialog pr;
        private HttpResponse req;
        private InputStream is;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Captando ...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... arg0) {

            UserAccessSession userAccess = UserAccessSession.getInstance(FinalizarPedido.this);
            UserSession userSession = userAccess.getUserSession();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("user_id", String.valueOf(userSession.getUser_id())));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://marketingdigitalabc.com.br/buysell/pedidos_show.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            try {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.e("pass 2", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }

            try {

                JSONObject json_data = new JSONObject(result);
                JSONArray arr =  json_data.getJSONArray("message");
                for (int i=0; i < arr.length(); i++) {
                    JSONObject  json_dat = arr.getJSONObject(i);

                    pedidos_id.add((json_dat.getString("pedidos_id")));
                    pedidos_categoria.add((json_dat.getString("pedidos_categoria")));
                    pedidos_descricao.add((json_dat.getString("pedidos_descricao")));
                    pedidos_qtd.add((json_dat.getString("pedidos_qtd")));
                    pedidos_unidade.add((json_dat.getString("pedidos_unidade")));

                }


            } catch (Exception e) {
                Log.e("Fail 3", e.toString());
            }
            return user_id;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            for (int i=0; i < pedidos_id.size(); i++) {

                final ListView listaPedido = (ListView) findViewById(R.id.lista);

                TextView pedidoId = (TextView) findViewById(R.id.textView7);
                pedidoId.setText("Resumo do Pedido #"+pedidos_id.get(i));

                if(i == 1){
                    String[] lista = {pedidos_id.get(0)};
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(FinalizarPedido.this, android.R.layout.simple_list_item_1, lista);
                    listaPedido.setAdapter(adapter1);
                }else if(i == 2){
                    String[] lista = {pedidos_id.get(0)+"  "+pedidos_qtd.get(0),pedidos_descricao.get(1)+"  "+pedidos_qtd.get(1)};
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(FinalizarPedido.this, android.R.layout.simple_list_item_1, lista);
                    listaPedido.setAdapter(adapter1);
                }else if(i == 3){
                    String[] lista = {pedidos_id.get(0)+"  "+pedidos_qtd.get(0),pedidos_descricao.get(1)+"  "+pedidos_qtd.get(1),pedidos_descricao.get(2)+"  "+pedidos_qtd.get(2)};
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(FinalizarPedido.this, android.R.layout.simple_list_item_1, lista);
                    listaPedido.setAdapter(adapter1);
                }else if(i == 4){
                    String[] lista = {pedidos_id.get(0)+"  "+pedidos_qtd.get(0),pedidos_descricao.get(1)+"  "+pedidos_qtd.get(1),pedidos_descricao.get(2)+"  "+pedidos_qtd.get(2),pedidos_descricao.get(3)+"  "+pedidos_qtd.get(3)};
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(FinalizarPedido.this, android.R.layout.simple_list_item_1, lista);
                    listaPedido.setAdapter(adapter1);
                }else if(i == 5){
                    String[] lista = {pedidos_id.get(0)+"  "+pedidos_qtd.get(0),pedidos_descricao.get(1)+"  "+pedidos_qtd.get(1),pedidos_descricao.get(2)+"  "+pedidos_qtd.get(2),pedidos_descricao.get(3)+"  "+pedidos_qtd.get(3),pedidos_descricao.get(4)+"  "+pedidos_qtd.get(4)};
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(FinalizarPedido.this, android.R.layout.simple_list_item_1, lista);
                    listaPedido.setAdapter(adapter1);
                }

                listaPedido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> lista, View item, int posicao, long id) {
                        listaPedido.getItemAtPosition(posicao);

                        if (posicao == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FinalizarPedido.this);//Cria o gerador do AlertDialog
                            builder.setTitle("Pedido ID: " + pedidos_id.get(0));
                            builder.setMessage("Categoria: " + pedidos_categoria.get(0) + "\nDescrição: " + pedidos_descricao.get(0) + "\nQtd: " + pedidos_qtd.get(0) + "\nUnidade: " + pedidos_unidade.get(0));
                            builder.setNegativeButton("Fechar", null);
                            alerta = builder.create();
                            alerta.show();
                        } else if (posicao == 1) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FinalizarPedido.this);//Cria o gerador do AlertDialog
                            builder.setTitle("Pedido ID: " + pedidos_id.get(1));
                            builder.setMessage("Categoria: " + pedidos_categoria.get(1) + "\nDescrição: " + pedidos_descricao.get(1) + "\nQtd: " + pedidos_qtd.get(1) + "\nUnidade: " + pedidos_unidade.get(1));
                            builder.setNegativeButton("Fechar", null);
                            alerta = builder.create();
                            alerta.show();
                        }  else if (posicao == 2) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FinalizarPedido.this);//Cria o gerador do AlertDialog
                            builder.setTitle("Pedido ID: " + pedidos_id.get(2));
                            builder.setMessage("Categoria: " + pedidos_categoria.get(2) + "\nDescrição: " + pedidos_descricao.get(2) + "\nQtd: " + pedidos_qtd.get(2) + "\nUnidade: " + pedidos_unidade.get(2));
                            builder.setNegativeButton("Fechar", null);
                            alerta = builder.create();
                            alerta.show();
                        }   else if (posicao == 3) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FinalizarPedido.this);//Cria o gerador do AlertDialog
                            builder.setTitle("Pedido ID: " + pedidos_id.get(2));
                            builder.setMessage("Categoria: " + pedidos_categoria.get(2) + "\nDescrição: " + pedidos_descricao.get(2) + "\nQtd: " + pedidos_qtd.get(2) + "\nUnidade: " + pedidos_unidade.get(2));
                            builder.setNegativeButton("Fechar", null);
                            alerta = builder.create();
                            alerta.show();
                        }   else if (posicao == 4) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FinalizarPedido.this);//Cria o gerador do AlertDialog
                            builder.setTitle("Pedido ID: " + pedidos_id.get(3));
                            builder.setMessage("Categoria: " + pedidos_categoria.get(3) + "\nDescrição: " + pedidos_descricao.get(3) + "\nQtd: " + pedidos_qtd.get(3) + "\nUnidade: " + pedidos_unidade.get(3));
                            builder.setNegativeButton("Fechar", null);
                            alerta = builder.create();
                            alerta.show();
                        }   else if (posicao == 5) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FinalizarPedido.this);//Cria o gerador do AlertDialog
                            builder.setTitle("Pedido ID: " + pedidos_id.get(4));
                            builder.setMessage("Categoria: " + pedidos_categoria.get(4) + "\nDescrição: " + pedidos_descricao.get(4) + "\nQtd: " + pedidos_qtd.get(4) + "\nUnidade: " + pedidos_unidade.get(4));
                            builder.setNegativeButton("Fechar", null);
                            alerta = builder.create();
                            alerta.show();
                        }
                    }
                });
            }

        }

    }
}
