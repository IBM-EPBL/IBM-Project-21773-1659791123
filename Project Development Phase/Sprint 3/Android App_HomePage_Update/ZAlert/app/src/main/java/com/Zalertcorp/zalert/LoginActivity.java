package com.Zalertcorp.zalert;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText    email, password;
    private Button     btn_login;
    private Button     gotoreg;
    private ProgressBar     loading;
    private static String   URL_REGIST = "http://192.168.0.103/zalert/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loading     = findViewById(R.id.progressbar);
        email        = findViewById(R.id.log_email);
        password    = findViewById(R.id.log_password);
        btn_login  = findViewById(R.id.btn_login);
        gotoreg = findViewById(R.id.btn_go_to_register);

        gotoreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoregister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(gotoregister);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login(){
        loading.setVisibility(View.VISIBLE);
        final String email      = this.email.getText().toString().trim();
        final String password   = this.password.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(LoginActivity.this, "Logged In !!!", Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);
                            Intent gotolog = new Intent(LoginActivity.this,HomePage.class);
                            startActivity(gotolog);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Login Error! " + e.toString(), Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.GONE);


                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Login Response Error!" + error.toString(), Toast.LENGTH_SHORT).show();
                        loading.setVisibility(View.GONE);


                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}