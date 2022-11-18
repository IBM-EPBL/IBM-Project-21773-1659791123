package com.Zalertcorp.zalert;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import kotlin.jvm.internal.Intrinsics;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Splash Screen
        ImageView logo = (ImageView)this.findViewById(R.id.splashScreenLogo);
        Intrinsics.checkNotNullExpressionValue(logo, "logo");
        logo.setAlpha(0.0F);
        logo.animate().setDuration(1000L).alpha(1.0F).withEndAction((Runnable)(new Runnable() {
            public final void run() {
                Intent x = new Intent((Context)SplashScreen.this, RegisterActivity.class);
                SplashScreen.this.startActivity(x);
                SplashScreen.this.overridePendingTransition(17432576, 17432577);
                SplashScreen.this.finish();
            }
        }));

        //Top Cirlce
        ImageView topcircle = (ImageView)this.findViewById(R.id.top_circle);
        Intrinsics.checkNotNullExpressionValue(topcircle, "logo");
        topcircle.setAlpha(0.0F);
        topcircle.animate().setDuration(1000L).alpha(1.0F).withEndAction((Runnable)(new Runnable() {
            public final void run() {
                SplashScreen.this.overridePendingTransition(17432576, 17432577);
            }
        }));

        //Bottom Circle
        ImageView bottomcircle = (ImageView)this.findViewById(R.id.bottom_circle);
        Intrinsics.checkNotNullExpressionValue(bottomcircle, "logo");
        bottomcircle.setAlpha(0.0F);
        bottomcircle.animate().setDuration(1000L).alpha(1.0F).withEndAction((Runnable)(new Runnable() {
            public final void run() {
                SplashScreen.this.overridePendingTransition(17432576, 17432577);
            }
        }));

    }
}









