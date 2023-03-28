
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClubsPage extends AppCompatActivity {
ImageButton back;
CircleImageView c1,c2,c3,c4;
CircleImageView[] c;

    RecyclerView rv;
    EditText search_bar;

    Button search;

    List<ClubModel> ls;
    ClubsAdapter adapter;
    FirebaseFirestore db;
    FirebaseAuth mauth;
    ClubRecommendationSystem clubRecommendationSystem;
    int [] clubs = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    String [] club = {"Arts Club","Cyber Gaming Club","Finance Club","Computer Science Club","Engineering Club ","Politics Club ","UOWD Club","Football Club","Cricket Club","Basketball Club","Cars Club","Chess Club ","Outdoors Club","Movies Club","Photography Club","Anime Club","Sharing Club","Food Club","Literature Club","Language Club"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setContentView(R.layout.activity_clubs_page);
        Navbarfunctions();
        db = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();

        c = new CircleImageView[4];
        c[0] = findViewById(R.id.club_1);

        c[1]= findViewById(R.id.club_2);
        c[2] = findViewById(R.id.club_3);
        c[3] = findViewById(R.id.club_4);
        clubRecommendationSystem = new ClubRecommendationSystem();
        try {
            clubRecommendationSystem.suggest(clubs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
        String outt[] =    checker();
//            for (int i=0;i<outt.length;i++) {
//                Log.d("sugger", String.valueOf(outt[i]));
                updatesuggestions(outt);




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }



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


                                ls.add(new ClubModel(document.getString("club_name"),document.getString("members")+" Members",document.getString("interest"),document.getString("dp")));
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

    private void updatesuggestions(String[] title) {
        Log.d("pkpkpkk",title[0]);
        Log.d("pkpkpkk",title[1]);
        Log.d("pkpkpkk",title[2]);
        Log.d("pkpkpkk",title[3]);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        firestore.setFirestoreSettings(settings);
for(int p=0;p<title.length;p++){
        Query query = firestore.collection("clubs")
                .whereEqualTo("club_name", title[p]);
    int finalP = p;
    query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
               QuerySnapshot documentSnapshot = task.getResult();
                List<DocumentSnapshot> documents = documentSnapshot.getDocuments();
                int i=0;
                for (DocumentSnapshot document : documents) {
                    if(document.getString("dp")!=null) {
                        Log.d("pkpkpk", document.getString("dp"));
                        Log.d("pkpkpk", String.valueOf(finalP));

                            Picasso.get().load(document.getString("dp")).into(c[finalP]);


                    }
                    i++;
                }

                }
        });
}
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
    public void checke() throws Exception {
        Log.d("callere","recommendation called");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        for (int i=0;i<club.length;i++)
        {

            CollectionReference colRef = db.collection("groups").document(club[i]).collection("chatmembers");
            Query query = colRef.whereEqualTo("uid", currentUserId);
            int finalI = i;
            int finalI1 = i;
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {

                            Log.d("TAGGG", "User ID not found in chatmembers collection"+String.valueOf(finalI1));
//                        Toast.makeText(c, "User not found", Toast.LENGTH_SHORT).show();


                        } else {
                            Log.d("TAGGGED", "User ID found in chatmembers collection"+String.valueOf(finalI1));
//                        Toast.makeText(c, "User found", Toast.LENGTH_SHORT).show();
                            clubs[finalI]=1;
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
        }


        Log.d("opopop", "received");
    }


    public String[] checker() throws Exception {
        checke();

        for (int i=0;i<clubs.length;i++)
            Log.d("opopd", String.valueOf(clubs[i]));
//        wait(5);
        Log.d("opopop", "receiver");
        String [] suggestions = clubRecommendationSystem.suggest(clubs);

        Log.d("opopop", "receivers");

//        for (int i=0;i<suggestions.length;i++)
//            Log.d("sugger", String.valueOf(suggestions[i]));
return suggestions;
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