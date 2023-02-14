package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                Intent intent=new Intent(SplashScreen.this,Welcome.class);
                startActivity(intent);
                finish();



            }
        },4000);

    }
}