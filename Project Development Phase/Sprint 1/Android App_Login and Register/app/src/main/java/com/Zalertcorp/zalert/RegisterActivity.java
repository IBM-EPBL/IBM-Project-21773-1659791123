//Author: Vishnu V
//Project Name: Containment Zone Alerting Application
//MD5 hash for project authentic verification : 763f8185a7ba4ebea28cd69d233fb6c1 (Cannot be decrypted except by the author)

package com.Zalertcorp.zalert;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {


    private EditText emailTextView, passwordTextView;
    private ImageButton Btn;
    private ProgressBar progressbar;
    private FirebaseAuth mAuth;
    private Button log;
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        Btn = findViewById(R.id.btn_register);
        emailTextView = findViewById(R.id.regemail);
        passwordTextView = findViewById(R.id.regpassword);
        progressbar = findViewById(R.id.progressbar);
        log = findViewById(R.id.btn_go_to_login);
        //SignInButton signInButton = findViewById(R.id.sign_in_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        Toast.makeText(RegisterActivity.this,"Disable Battery Optimizations to run the app in Background ",Toast.LENGTH_LONG).show();
        ignoreBatteryOptimization();

        //Go_to_Login_Page
        log.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                Intent launchActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(launchActivity);

            }
        });

        //Register with Email and password
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });


/*        //Google Sign-in button process start
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "OnClick: begin Google SignIn");
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        // Google sign in button process ends
*/

    }

    //Battery optimisation
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void ignoreBatteryOptimization() {
        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
            intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);
        }
    }




    //Register user with custom mail and password
    private void registerNewUser() {

        progressbar.setVisibility(View.VISIBLE);
        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        // Validations for input email and password
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

        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    FirebaseUser fuser = mAuth.getCurrentUser();

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Registration Success
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {

                            // Registration failed
                            Toast.makeText(getApplicationContext(), "Registration failed!!" + " Please try again later", Toast.LENGTH_LONG).show();

                            // hide the progress bar
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
    }
    //Register process ends


/*    //Google Sign in process
    @Override
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
                            Toast.makeText(RegisterActivity.this, "Account created !\n" + email, Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "OnSuccess: Existing User...\n" + email);
                            Toast.makeText(RegisterActivity.this, "Logging in as\n" + email, Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(RegisterActivity.this, homepage.class));
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
*/




}