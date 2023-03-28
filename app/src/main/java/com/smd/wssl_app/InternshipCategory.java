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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class InternshipCategory extends AppCompatActivity {
    ImageButton back;
    RecyclerView rv;
    EditText search_bar;
    Button search;
    List<InternshipModel> ls;
    FirebaseFirestore db;

    InternshipsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internship_category);
        Bundle extras = getIntent().getExtras();
        Navbarfunctions();

        String name = extras.getString("name");
        int image = extras.getInt("img");

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView img = findViewById(R.id.img);
        TextView nametv = findViewById(R.id.name);
        nametv.setText(name);
        img.setImageResource(image);
        rv=findViewById(R.id.rv);
        ls=new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        db.collection("internships")
                .whereEqualTo("job-family",name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("fire", document.getId() + " => " + document.getData());
                                Log.d("fire", document.getId() + " aaaaaaaaaaaaa" + document.getData());
                                String type = document.getString("type");
                                Log.d("fire",  "22222"+document.getString("type")+"2222");

                                ls.add(new InternshipModel(document.getString("title"),type+": "+document.getString("salary")+"/month",document.getString("location"),document.getString("interested")+" interested",document.getString("link")));
                                adapter.notifyDataSetChanged();

                            }
                        } else {
                            Log.d("fire", "Error getting documents: ", task.getException());
                        }
                    }
                });

        adapter=new InternshipsAdapter(ls,InternshipCategory.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(InternshipCategory.this);
        rv.setLayoutManager(lm);
//        ls.add(mod);
        adapter.notifyDataSetChanged();

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

    private void filter(String text){
        ArrayList<InternshipModel> filteredstalls = new ArrayList<>();
        for(InternshipModel item: ls){
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredstalls.add(item);
            }
        }
        adapter.filterlist(filteredstalls);
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