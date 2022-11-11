//Author: Vishnu V
//Project Name: Containment Zone Alerting Application
//MD5 hash : 763f8185a7ba4ebea28cd69d233fb6c1 (Cannot be decrypted except by the author)

package com.Zalertcorp.zalert;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    private EditText emailTextView, passwordTextView;
    private Button Btn;
    private ProgressBar progressbar;
    private Button regis;
    private Button google;
    private ImageButton backtohome;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    private static final int RC_SIGN_IN = 100;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // CZAP FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        regis = findViewById(R.id.btn_go_to_register);
        checkUser();
        // initialising all views and buttons for CZAP app through id defined above
        emailTextView = findViewById(R.id.logemail);
        passwordTextView = findViewById(R.id.et_password);
        Btn = findViewById(R.id.btn_login);
        progressbar = findViewById(R.id.progressbar);

        backtohome=findViewById(R.id.btn_back);


        //Go to homepage
        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backtohom = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(backtohom);
            }
        });

        //Sign In with Google
        google = findViewById(R.id.btn_google);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "OnClick: begin Google SignIn");
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


        //Go to register page
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regact = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regact);
            }
        });





        // Set on Click Listener on Sign-in button
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
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


    //Google Sign in process
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "OnActivityResult: Google signin intent result");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogleAccount(account);

            } catch (ApiException e) {
                Log.d(TAG, "onActivityResult: " + e.getMessage());
            }
        }
    }

    private void firebaseAuthWithGoogleAccount(GoogleSignInAccount account) {
        Log.e(TAG, "firebaseAuthWithGoogleAccount:begin firebase");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(TAG, "OnSuccess: Logged In");
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();
                        Log.d(TAG, "OnSuccess: Email" + email);
                        Log.d(TAG, "OnSuccess: UID" + uid);

                        if (authResult.getAdditionalUserInfo().isNewUser()) {
                            Log.d(TAG, "OnSuccess: Account Created...\n" + email);
                            Toast.makeText(LoginActivity.this, "Account created !\n" + email, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "OnSuccess: Existing User...\n" + email);
                            Toast.makeText(LoginActivity.this, "Logged in as\n" + email, Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(LoginActivity.this, homepage.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "OnFailure: Logged Failed" + e.getMessage());
                    }
                });
    }
    //Google sign in process ends




    //Login process starts
    private void loginUserAccount() {
        progressbar.setVisibility(View.VISIBLE);
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
            progressbar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
            progressbar.setVisibility(View.GONE);
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);

                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent = new Intent(LoginActivity.this, homepage.class);
                                    startActivity(intent);
                                } else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        });
    }
    //Login process ends
}

