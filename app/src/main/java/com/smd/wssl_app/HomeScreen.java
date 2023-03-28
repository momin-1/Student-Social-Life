package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class HomeScreen extends AppCompatActivity {
    ImageButton clubs,internships,your_profile,subject_groups;
FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Navbarfunctions();
        db = FirebaseFirestore.getInstance();
        clubs = findViewById(R.id.clubs);
        internships = findViewById(R.id.internships);
        subject_groups = findViewById(R.id.subject_groups);
        your_profile = findViewById(R.id.your_profile);

        clubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("poos","clubs1");

                try {
//                    FileInputStream file = new FileInputStream(new File("F:/clubs.xlsx"));
                    File file = new File(Environment.getExternalStorageDirectory(), "F:/clubs.xlsx");
                    FileInputStream inputStream = new FileInputStream(file);

                    Log.d("poos","clubs2");

                } catch (FileNotFoundException e) {
                    Log.d("poos",e.toString());
                    e.printStackTrace();
                }

                Intent i = new Intent(HomeScreen.this,ClubsPage.class);
                startActivity(i);
            }
        });

        internships.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this,InternshipsPage.class);
                startActivity(i);
            }
        });
        subject_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this,SubjectGroups.class);
                startActivity(i);
            }
        });
        your_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeScreen.this,YourProfilePage.class);
                startActivity(i);
            }
        });






    }


    public void Navbarfunctions(){
        ImageButton home,football,groups,notifications,profile;

        home = findViewById(R.id.home);
        football = findViewById(R.id.football);
        groups = findViewById(R.id.groups);
        notifications = findViewById(R.id.notifcations);
        profile = findViewById(R.id.profile);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!this.getClass().getName().contains("HomeScreen")){
                Intent i = new Intent(getApplicationContext(),HomeScreen.class);
                startActivity(i);}
            }
        });

        football.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!this.getClass().getName().contains("ClubsPage")){

                Intent i = new Intent(getApplicationContext(),ClubsPage.class);
                startActivity(i);
            }}
        });

        groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!this.getClass().getName().contains("SubjectGroups")){

                    Intent i = new Intent(getApplicationContext(),SubjectGroups.class);
                startActivity(i);}
            }
        });

        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!this.getClass().getName().contains("Internships")){

                    Intent i = new Intent(getApplicationContext(),InternshipsPage.class);
                    startActivity(i);}
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!this.getClass().getName().contains("YourProfilePage")){

                    Intent i = new Intent(getApplicationContext(),YourProfilePage.class);
                startActivity(i);}
            }
        });


    }








}