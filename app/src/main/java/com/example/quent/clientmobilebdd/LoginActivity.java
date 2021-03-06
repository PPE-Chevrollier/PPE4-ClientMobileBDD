package com.example.quent.clientmobilebdd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final Button bLogin = (Button) findViewById(R.id.bSignIn);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();

                // Response received from the server
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            AlertDialog.Builder builder;
                            JSONObject jsonResponse = new JSONObject(response);
                            int result = jsonResponse.getInt("etat");

                            switch (result){
                                case 1:
                                    String name = jsonResponse.getString("prenom");
                                    Intent intent = new Intent(LoginActivity.this, UserAreaActivity.class);
                                    intent.putExtra("name", name);
                                    LoginActivity.this.startActivity(intent);
                                    break;
                                case 0:
                                    builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Utilisateur introuvable ou mot de passe incorrect")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                    break;
                                case 2:
                                    builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("Vous devez disposez des droits d'administrateur")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                    break;
                            }

                            etPassword.setText("");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                BddRequest loginRequest = new BddRequest(responseListener, "connexion/"+username+"/"+password);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}
