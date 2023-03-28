package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpLogin extends AppCompatActivity {

    Button su,si;
    Fragment fsi,fsu;
FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_login);

mauth = FirebaseAuth.getInstance();



        fsi = new SignInFragment();
        fsu = new SignUpFragment();

        su = findViewById(R.id.signup);
        si = findViewById(R.id.signin);

        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
        ft3.add(R.id.fragment_container, fsu);
        ft3.commit();


        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!fsi.isAdded() && fsu.isAdded()) {
                    si.setText(R.string.underlined_signin);
                    su.setText("Sign up");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(fsu);
                    ft.commit();

                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.add(R.id.fragment_container, fsi);
                ft2.commit();
                }
            }
        });

        su.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!fsu.isAdded() && fsi.isAdded()) {
                    su.setText(R.string.underlined_signup);
                    si.setText("Sign in");


                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.remove(fsi);
                    ft.commit();

                    FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                    ft2.add(R.id.fragment_container, fsu);
                    ft2.commit();
                }
            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mauth.getCurrentUser();
        if(user!=null && user.isEmailVerified()){
            if(user.getUid().contains("LFBmnV7bLVPEeJ5W0BKdLReyeew2")){
//            Toast.makeText(getApplicationContext(), user.getUid()+"already", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(SignUpLogin.this,AdminHomePage.class);
            startActivity(i);
            finish();
            }
            else{
//                Toast.makeText(getApplicationContext(), user.getUid()+"already", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignUpLogin.this,HomeScreen.class);
                startActivity(i);
                finish();
            }
        }
    }
}