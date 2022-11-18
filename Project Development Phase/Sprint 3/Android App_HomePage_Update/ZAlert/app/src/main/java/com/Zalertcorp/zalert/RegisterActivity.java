package com.Zalertcorp.zalert;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText rname;
    private EditText remail;
    private EditText rpassword;
    private ImageButton registerbtn;
    private String url = "http://192.168.78.146:5000/android_sign_up";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rname = findViewById(R.id.reg_username);
        remail = findViewById(R.id.reg_email);
        rpassword = findViewById(R.id.password);
        registerbtn = findViewById(R.id.btn_register);

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

    private void signup() {
        final String name = this.rname.getText().toString().trim();
        final String email = this.remail.getText().toString().trim();
        final String password = this.rpassword.getText().toString().trim();

        final RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.0.103:5000/android_sign_up";

        JSONObject postparams = new JSONObject();
        try {
            postparams.put("name", name);
            postparams.put("email", email);
            postparams.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

                        } catch (JSONException e) {
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