//Author: Vishnu V
//Project Name: Containment Zone Alerting Application
//MD5 hash for project authentic verification : 763f8185a7ba4ebea28cd69d233fb6c1 (Cannot be decrypted except by the author)

package com.Zalertcorp.zalert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;




public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "Already Logged in";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        checkUser();

        Button signin = findViewById(R.id.signin_main);
        Button signup = findViewById(R.id.signup_main);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent moveToSignIn = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(moveToSignIn);
            }
        });
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent moveToSignUp = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(moveToSignUp);
            }
        });
    }
    private void checkUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            Log.d(TAG, "CheckUser: Already Logged In");
            startActivity(new Intent(this, homepage.class));
            finish();
        }
    }
}








