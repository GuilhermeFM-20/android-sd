package com.example.unp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.unp.service.ApiService;
import com.example.unp.service.HttpServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UpdateActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.appbar), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();

        String idClicado = intent.getStringExtra("id");

        // Verifique se há dados extras passados com o Intent
        if (intent != null) {
            // Recupere o valor do "id" passado como um extra


            // Faça o que quiser com o id recebido, por exemplo, exiba-o em um TextView
//            EditText nome = (EditText) findViewById(R.id.inputName);
//            EditText email = (EditText) findViewById(R.id.inputEmail);

            HttpServices httpServices = null;

            //httpServices = new HttpServices();



            //textView.setText("ID: " + idClicado);

            assert idClicado != null;
            Log.i("IdAccount",idClicado);
//            httpServices.sendGetRequest("/"+idClicado, new HttpServices.ResponseCallback() {
//                @Override
//                public void onSuccess(String response) {
//                    runOnUiThread(() -> {
//                        TextView txt = (TextView) findViewById(R.id.msgTxt);
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = new JSONObject(response);
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//
//                        // Obtém o valor da chave "msg"
//
//                        String name;
//                        String email;
//                        try {
//                            name = jsonObject.getString("name");
//                            email = jsonObject.getString("email");
//                        } catch (JSONException e) {
//                            throw new RuntimeException(e);
//                        }
//                        EditText inputName = (EditText) findViewById(R.id.inputName);
//                        EditText inputEmail = (EditText) findViewById(R.id.inputEmail);
//
//                        inputName.setText(name);
//                        inputEmail.setText(email);
//
//
//                    });
//                }
//
//                @Override
//                public void onFailure(Exception exception) {
//                    // Handle error
//                    runOnUiThread(() -> {
//                        // Por exemplo, exiba uma mensagem de erro
//                        Toast.makeText(UpdateActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                    });
//                }
//            });



            ExecutorService executorService = Executors.newSingleThreadExecutor();


            executorService.execute(() -> {
                ApiService api = new ApiService();

                try {
                    String response = api.request("GET","/"+idClicado, null);

                    runOnUiThread(() -> {
                        TextView txt = (TextView) findViewById(R.id.msgTxt);
                        JSONObject jsonObject = null;

                        String name2 = null;
                        String email2 = null;
                        try {
                            jsonObject = new JSONObject(response);
                            name2 = jsonObject.getString("name");
                            email2 = jsonObject.getString("email");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        EditText inputName = (EditText) findViewById(R.id.inputName);
                        EditText inputEmail = (EditText) findViewById(R.id.inputEmail);

                        inputName.setText(name2);
                        inputEmail.setText(email2);

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(UpdateActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });


        }
    }


    public void updateUser(View view){

        Intent intent = getIntent();

        String idClicado = intent.getStringExtra("id");

        EditText name = (EditText)findViewById(R.id.inputName);
        EditText email = (EditText)findViewById(R.id.inputEmail);

        String jsonInputString = "{\"name\": \""+name.getText()+"\", \"email\": \""+email.getText()+"\"}";


        HttpServices httpServices = new HttpServices();
        httpServices.sendPutRequest("/"+idClicado,jsonInputString,new HttpServices.ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                // Atualize a UI com a resposta
                runOnUiThread(() -> {
                    TextView txt = (TextView) findViewById(R.id.msgTxt);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    // Obtém o valor da chave "msg"
                    String mensagem;
                    try {
                        mensagem = jsonObject.getString("msg");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    // Por exemplo, atualize um TextView com a resposta
                    txt.setText(mensagem);
                    recreate();

                });
            }

            @Override
            public void onFailure(Exception exception) {
                // Handle error
                runOnUiThread(() -> {
                    // Por exemplo, exiba uma mensagem de erro
                    Toast.makeText(UpdateActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    public void deleteUser(View view){
        Intent intent = getIntent();

        String idClicado = intent.getStringExtra("id");

        HttpServices httpServices = new HttpServices();
        httpServices.sendDeleteRequest("/"+idClicado,new HttpServices.ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                // Atualize a UI com a resposta
                runOnUiThread(() -> {
                    TextView txt = (TextView) findViewById(R.id.msgTxt);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    // Obtém o valor da chave "msg"
                    String mensagem;
                    try {
                        mensagem = jsonObject.getString("msg");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    // Por exemplo, atualize um TextView com a resposta
                    txt.setText(mensagem);
                    recreate();

                });
            }

            @Override
            public void onFailure(Exception exception) {
                // Handle error
                runOnUiThread(() -> {
                    // Por exemplo, exiba uma mensagem de erro
                    Toast.makeText(UpdateActivity.this, "Error: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

}