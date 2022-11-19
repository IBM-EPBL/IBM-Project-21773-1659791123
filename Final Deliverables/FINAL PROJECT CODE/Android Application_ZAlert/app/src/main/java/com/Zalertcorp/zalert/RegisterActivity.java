//Author: Vishnu V & Team
//Project Name: Containment Zone Alerting Application
//MD5 hash : 763f8185a7ba4ebea28cd69d233fb6c1 ( Cannot be decrypted except by the author -- Hash was done to verify Author Authenticity of the project )


package com.Zalertcorp.zalert;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    private EditText rname;
    private EditText remail;
    private EditText rpassword;
    private ImageButton registerbtn;
    private String url = "http://192.168.78.146:5000/android_sign_up";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //assigning
        rname = findViewById(R.id.reg_username);
        remail = findViewById(R.id.reg_email);
        rpassword = findViewById(R.id.password);
        registerbtn = findViewById(R.id.btn_register);

        //Using SharedPreferences to remember User
        sharedpreferences = getApplicationContext().getSharedPreferences("user_data", 0);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        if (sharedpreferences.getAll().size() >= 3) {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RegisterActivity.this, "Loggin in....", Toast.LENGTH_SHORT).show();
                signup();
            }
        });

    }

    // Sign up function
    private void signup() {
        final String name = this.rname.getText().toString().trim();
        final String email = this.remail.getText().toString().trim();
        final String password = this.rpassword.getText().toString().trim();

        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.103:5000/android_sign_up";

        //Creating JSON Object
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("name", name);
            postparams.put("email", email);
            postparams.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Sending JSON Object Request
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, url, postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", response.toString());
                        try {
                            int userId = response.getInt("id");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putInt("id", 1);
                            editor.commit();
                            Intent intent = new Intent(RegisterActivity.this, HomePage.class);
                            startActivity(intent);

                        }
                        // Exception is caught here
                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
                    }
                });

        queue.add(jsonObjReq);
    }
}