package com.vatsal.newsynopsis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vatsal.newsynopsis.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        if (i.getStringExtra("msg") != null) {
            Utils.makeToast(getApplicationContext(), "Successfully Registered.");
        }

        final Button btnRegister = findViewById(R.id.register);
        final Button btnSignIn = findViewById(R.id.signin_custom);
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        findViewById(R.id.sign_in_google).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Utils.makeToast(getApplicationContext(), "SignIn button clicked");
//            }
//        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, Register.class);
                startActivity(registerIntent);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eusername = findViewById(R.id.username);
                String username = eusername.getText().toString();

                EditText epassword = findViewById(R.id.password);
                String password = epassword.getText().toString();

                verifyUser(username, password);
//                Utils.makeToast(getApplicationContext(), "Your username or Password is not correct.");
            }
        });

    }

    public void verifyUser(String username, String password) {
        Log.d("Main",password);
        queue = Volley.newRequestQueue(this);
        String url = "https://newsynopsis-api.herokuapp.com/v1/user/login/";
        HashMap<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Resp",response.toString());
                    if (response.getBoolean("result")) {
                        Intent i = new Intent(MainActivity.this, News.class);
                        startActivity(i);
                    } else {
                        Utils.makeToast(getApplicationContext(), "Username or password is incorrect. ");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}
