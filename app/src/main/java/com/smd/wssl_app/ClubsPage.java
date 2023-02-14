
package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClubsPage extends AppCompatActivity {
ImageButton back;
    RecyclerView rv;
    EditText search_bar;
    Button search;

    List<ClubModel> ls;
    ClubsAdapter adapter;
    FirebaseFirestore db;
    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clubs_page);
        Navbarfunctions();
        db = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();


        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rv=findViewById(R.id.rv);
        ls=new ArrayList<>();


        adapter=new ClubsAdapter(ls,ClubsPage.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(ClubsPage.this);
        rv.setLayoutManager(lm);
//        ls.add(mod);
        adapter.notifyDataSetChanged();



        db.collection("clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                ls.add(new ClubModel(document.getString("club_name"),document.getString("amount_of_users")+" Members",document.getString("interest"),document.getString("dp")));
                                adapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d("fire", "Error getting documents: ", task.getException());
                        }
                    }
                });




        search_bar = findViewById(R.id.search_bar);
        search = findViewById(R.id.search);

        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                filter(search_bar.getText().toString());

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

    private void filter(String text){
        ArrayList<ClubModel> filteredstalls = new ArrayList<>();
        for(ClubModel item: ls){
            if(item.getClub_name().toLowerCase().contains(text.toLowerCase())){
                filteredstalls.add(item);
            }
        }
        adapter.filterlist(filteredstalls);
    }
}