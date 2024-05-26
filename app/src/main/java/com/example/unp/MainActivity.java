package com.example.unp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.unp.service.ApiService;
import com.example.unp.service.HttpServices;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    private HttpServices httpServices;
    private TableLayout tableLayout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appbar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        tableLayout = findViewById(R.id.score_table);


        ExecutorService executorService = Executors.newSingleThreadExecutor();


        executorService.execute(() -> {
            ApiService api = new ApiService();
            try {
                String response = api.request("GET","/", null);

                runOnUiThread(() -> {
                    Log.i("Entrou","Entrou");
                    //tableLayout = findViewById(R.id.score_table);

                    // Suponha que você tenha o JSON como uma string
                    String jsonData = response;
                    try {
                        JSONArray jsonArray = new JSONArray(jsonData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String id = jsonObject.getString("id");
                            String nome = jsonObject.getString("name");
                            String email = jsonObject.getString("email");


                            TableRow row = addTable(id,nome,email);
                            // Adicione a linha à tabela
                            tableLayout.addView(row);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

    }

    public TableRow addTable(String id, String nome, String email){

        // Adicione uma linha à tabela com os dados
        TableRow row = new TableRow(MainActivity.this);

        TextView textViewId = new TextView(MainActivity.this);
        textViewId.setText(id);
        row.addView(textViewId);

        TextView textViewNome = new TextView(MainActivity.this);
        textViewNome.setText(nome);
        row.addView(textViewNome);

        TextView textViewEmail = new TextView(MainActivity.this);
        textViewEmail.setText(email);

        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableRowParams.setMargins(30, 10, 30, 10); // Margens da linha
        row.setLayoutParams(tableRowParams);
        row.addView(textViewEmail);

        // Adicione um evento de clique ao valor de "id" na tabela
        textViewId.setOnClickListener(v -> {
            // Obtenha o id clicado
            String idClicado = ((TextView) v).getText().toString();

            // Inicie uma nova Activity para exibir os detalhes com base no id clicado
            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
            intent.putExtra("id", idClicado);Livro - O Médico E O Monstro & Outras Histórias - Capa Dura
            startActivity(intent);
        });

        return row;

    }

    public void refreshTable(View view){
        recreate();
    }

    public void alterarText(View view) {

        EditText name = (EditText)findViewById(R.id.inputName);
        EditText email = (EditText)findViewById(R.id.inputEmail);

        String jsonInputString = "{\"name\": \""+name.getText()+"\", \"email\": \""+email.getText()+"\"}";


        ExecutorService executorService = Executors.newSingleThreadExecutor();


        executorService.execute(() -> {
            ApiService api = new ApiService();
            try {
                String response = api.request("POST","/", jsonInputString);

                runOnUiThread(() -> {
                    TextView txt = (TextView) findViewById(R.id.msgTxt);
                    String mensagem = null;
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        mensagem = jsonObject.getString("msg");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    txt.setText(mensagem);

                });
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

    }


}