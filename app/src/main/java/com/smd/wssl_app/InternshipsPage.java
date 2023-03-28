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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class InternshipsPage extends AppCompatActivity {
    ImageButton back;
    RecyclerView rv;
    EditText search_bar;
    Button search;
    List<InternshipModel> ls;
    FirebaseFirestore db;
    InternshipsAdapter adapter;
    ImageButton[] b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internships_page);
        Navbarfunctions();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        db = FirebaseFirestore.getInstance();

        rv=findViewById(R.id.rv);
        ls=new ArrayList<>();






        adapter=new InternshipsAdapter(ls,InternshipsPage.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(InternshipsPage.this);
        rv.setLayoutManager(lm);
//        ls.add(mod);

        db.collection("internships")
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

                                ls.add(new InternshipModel(document.getString("title"),type+": "+document.getString("salary")+"/month",document.getString("location"),document.getString("interested")+" interested", document.getString("link")));
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



        b = new ImageButton[]{(ImageButton)findViewById(R.id.cat_img_1),
                (ImageButton)findViewById(R.id.cat_img_2),
                (ImageButton)findViewById(R.id.cat_img_3),
                (ImageButton)findViewById(R.id.cat_img_4),
                (ImageButton)findViewById(R.id.cat_img_5),
                (ImageButton)findViewById(R.id.cat_img_6)};





        for(int i=0; i < b.length;i++) {

        b[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(b[0].isPressed()){
                    Intent i = new Intent(InternshipsPage.this,InternshipCategory.class);
                    i.putExtra("name", "CS");
                    i.putExtra("img", R.drawable.inter1);

                    startActivity(i);

                }
                if(b[1].isPressed()){
                    Intent i = new Intent(InternshipsPage.this,InternshipCategory.class);
                    i.putExtra("name", "Business");
                    i.putExtra("img", R.drawable.inter2);

                    startActivity(i);
                }
                if(b[2].isPressed()){
                    Intent i = new Intent(InternshipsPage.this,InternshipCategory.class);
                    i.putExtra("name", "Law");
                    i.putExtra("img", R.drawable.inter3);

                    startActivity(i);                }
                if(b[3].isPressed()){
                    Intent i = new Intent(InternshipsPage.this,InternshipCategory.class);
                    i.putExtra("name", "Finance");
                    i.putExtra("img", R.drawable.inter4);

                    startActivity(i);                }
                if(b[4].isPressed()){
                    Intent i = new Intent(InternshipsPage.this,InternshipCategory.class);
                    i.putExtra("name", "Engineering");
                    i.putExtra("img", R.drawable.inter5);

                    startActivity(i);                }
                if(b[5].isPressed()){
                    Intent i = new Intent(InternshipsPage.this,InternshipCategory.class);
                    i.putExtra("name", "Media");
                    i.putExtra("img", R.drawable.inter6);


                    startActivity(i);                }
            }
        });
        }



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