package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class AdminHomePage extends AppCompatActivity {

    Button add_club,add_internship,logout;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        mauth=FirebaseAuth.getInstance();
logout= findViewById(R.id.logout);
        add_club = findViewById(R.id.add_club);
         add_internship = findViewById(R.id.add_internship);
         add_club.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i =new Intent(AdminHomePage.this,AddClubForm.class);
                 startActivity(i);
             }
         });

        add_internship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(AdminHomePage.this,AddInternshipForm.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mauth.signOut();
                Intent i =new Intent(getApplicationContext(),SignUpLogin.class);
                startActivity(i);
                finish();
            }
        });
    }


}