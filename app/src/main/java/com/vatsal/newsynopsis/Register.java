package com.vatsal.newsynopsis;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vatsal.newsynopsis.utils.Utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private RequestQueue queue;
    private final String TAG = Register.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnRegister = findViewById(R.id.registration_page_button);
        final EditText etUsername = findViewById(R.id.reg_username);
        final EditText etPass = findViewById(R.id.reg_password);
        final EditText etCpass = findViewById(R.id.reg_cpassword);



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsername.getText().toString();
                String password = etPass.getText().toString();
                String cpass = etCpass.getText().toString();
                if (username.isEmpty()) {
                    Utils.makeToast(getApplicationContext(), "Username must not be empty");
                } else if (password.isEmpty() || cpass.isEmpty()) {
                    Utils.makeToast(getApplicationContext(), "Both passwords must not be empty");
                } else if (!cpass.equals(password)) {
                    Utils.makeToast(getApplicationContext(), "Confirm Password does not match Password text.");
                } else if (password.length() < 8) {
                    Utils.makeToast(getApplicationContext(), "Password must be greater than 8 characters.");
                } else {
                    Log.d("debug", "in else");
                    // send post request here to node js server.
                     sendRequest(username, password);
                }
            }
        });

    }

    public void sendRequest(String username, String password){
        queue = Volley.newRequestQueue(this);
        String url = "https://newsynopsis-api.herokuapp.com/v1/user/register";
        HashMap<String,String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,response.toString());
                try {
                    JSONObject resp = new JSONObject(response.toString());
                    if(resp.getBoolean("success")){
                        Intent i = new Intent(Register.this, MainActivity.class);
                        i.putExtra("msg",resp.getString("msg"));
                        startActivity(i);
                    }else{
                        Utils.makeToast(getApplicationContext(),"Username already exist.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}
