package com.projects.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.projects.storefinder.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ygor on 11/02/2016.
 */

public class FormPedidos extends Activity{

    private AlertDialog alerta;
    private EditText edtNome, edtEmail, edtTel, edtEnd, edtCep, edtHora, edtTroco;
    private List<String> pagamento = new ArrayList<String>();
    private Spinner spn1;
    private String nomePagamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.form_pedidos);

        //Adicionando Nomes no ArrayList
        pagamento.add("Formas de Pagamento");
        pagamento.add("Dinheiro");

        //Identifica o Spinner no layout
        spn1 = (Spinner) findViewById(R.id.Pagamento);
        //Cria um ArrayAdapter usando um padrão de layout da classe R do android, passando o ArrayList nomes
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pagamento);
        ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spn1.setAdapter(spinnerArrayAdapter);

        //Método do Spinner para capturar o item selecionado
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                nomePagamento = parent.getItemAtPosition(posicao).toString();
                //imprime um Toast na tela com o nome que foi selecionado
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnFim = (Button) findViewById(R.id.btnEnd);
        edtNome = (EditText)findViewById(R.id.nome);
        edtEmail = (EditText)findViewById(R.id.email);
        edtTel = (EditText)findViewById(R.id.telefone);
        edtEnd = (EditText)findViewById(R.id.endereco);
        edtCep = (EditText)findViewById(R.id.cep);
        edtHora = (EditText)findViewById(R.id.hora);
        edtTroco = (EditText)findViewById(R.id.troco);

        edtTroco.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtTroco.addTextChangedListener(new TextWatcher() {
            private boolean isUpdating = false;
            // Pega a formatacao do sistema, se for brasil R$ se EUA US$
            private NumberFormat nf = NumberFormat.getCurrencyInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int after) {
                // Evita que o método seja executado varias vezes.
                // Se tirar ele entre em loop
                if (isUpdating) {
                    isUpdating = false;
                    return;
                }

                isUpdating = true;
                String str = s.toString();
                // Verifica se já existe a máscara no texto.
                boolean hasMask = ((str.indexOf("R$") > -1 || str.indexOf("$") > -1) &&
                        (str.indexOf(".") > -1 || str.indexOf(",") > -1));
                // Verificamos se existe máscara
                if (hasMask) {
                    // Retiramos a máscara.
                    str = str.replaceAll("[R$]", "").replaceAll("[,]", "")
                            .replaceAll("[.]", "");
                }

                try {
                    // Transformamos o número que está escrito no EditText em
                    // monetário.
                    str = nf.format(Double.parseDouble(str) / 100);
                    edtTroco.setText(str);
                    edtTroco.setSelection(edtTroco.getText().length());
                } catch (NumberFormatException e) {
                    s = "";
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não utilizamos
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FormPedidos.this);//Cria o gerador do AlertDialog
                builder.setTitle("Finalizando Pedido");//define o titulo
                //define um botão como positivo
                builder.setPositiveButton("Enviar Pedido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        if (edtNome.getText().toString().length() > 0 && edtEnd.getText().toString().length() != 0) {
                            Toast.makeText(FormPedidos.this, "Pedido enviado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (edtEnd.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtEnd.setError("Campo vazio");
                            }

                            if (edtNome.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtNome.setError("Campo vazio");
                            }

                            if (edtEmail.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtEmail.setError("Campo vazio");
                            }

                            if (edtCep.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtCep.setError("Campo vazio");
                            }

                            if (edtTel.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtTel.setError("Campo vazio");
                            }

                            if (edtHora.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtHora.setError("Campo vazio");
                            }

                            if (edtTroco.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtTroco.setError("Campo vazio");
                            }

                        }

                    }
                });
                //define um botão como negativo.
                builder.setNegativeButton("Salvar Pedido", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        if (edtNome.getText().toString().length() > 0 && edtEnd.getText().toString().length() != 0) {
                            Toast.makeText(FormPedidos.this, "Pedido salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {

                            if (edtEnd.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtEnd.setError("Campo vazio");
                            }

                            if (edtNome.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtNome.setError("Campo vazio");
                            }

                            if (edtEmail.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtEmail.setError("Campo vazio");
                            }

                            if (edtCep.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtCep.setError("Campo vazio");
                            }

                            if (edtTel.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtTel.setError("Campo vazio");
                            }

                            if (edtHora.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtHora.setError("Campo vazio");
                            }

                            if (edtTroco.getText().length() == 0) {//como o tamanho é zero é nulla aresposta
                                edtTroco.setError("Campo vazio");
                            }
                        }

                    }
                });
                alerta = builder.create();//cria o AlertDialog
                alerta.show();//Exibe
            }
        });

    }

}
