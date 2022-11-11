//Author: Vishnu V
//Project Name: Containment Zone Alerting Application
//MD5 hash for project authentic verification : 763f8185a7ba4ebea28cd69d233fb6c1 (Cannot be decrypted except by the author)

package com.Zalertcorp.zalert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homepage extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   setContentView(R.layout.activity_homepage);

        email = findViewById(R.id.useremail);
        Button logoutt = findViewById(R.id.logoutbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        //Toast.makeText(getApplicationContext(),"Hello World", Toast.LENGTH_SHORT).show();
        logoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                checkUser();
            }
        });
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            String fireemail = firebaseUser.getEmail();
            email.setText(fireemail);

        }*/
    }
}














