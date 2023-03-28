package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MemberProfile extends AppCompatActivity {


    FirebaseAuth mauth;
    ImageButton back;
    ImageView profilepic;
    String jjk;
    Integer ii=0;
    ArrayList<String> liked;
    ArrayList<String> suggests;
    Integer bol=0;

    RecyclerView rv;
    EditText u,p;

    List<ImageViewModel> ls;

    ImageViewAdapter adapter;
    TextView sgrv;
    TextView uname;
    EditText name, aboutme;
    ImageButton home,football,groups,notifications,profile;
    FirebaseFirestore db;
    String memberuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_profile);

        Bundle extras = getIntent().getExtras();
        memberuid= extras.getString("uid");

        mauth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();


        u = findViewById(R.id.usernamee);
        p = findViewById(R.id.password);
        uname = findViewById(R.id.uname);
        sgrv = findViewById(R.id.SGrv);
        back = findViewById(R.id.back);
        rv= findViewById(R.id.rvclubs);
        profilepic = findViewById(R.id.profilepic);

        ls=new ArrayList<>();

        jjk = "";

        adapter=new ImageViewAdapter(ls,MemberProfile.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(MemberProfile.this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);
        ls.add(new ImageViewModel("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/images%2Fimage%3A256102?alt=media&token=0e9cff61-998e-466f-a6a8-318be59e1bc3"));
        adapter.notifyDataSetChanged();
        getimages();
        getclubs();
        setdp();

        name = findViewById(R.id.name);
        aboutme  = findViewById(R.id.aboutme);
        getData();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }


    private void  getData(){
        DocumentReference docRef = db.collection("users").document(memberuid);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve data
                    String namee = documentSnapshot.getString("name");
                    String aboutmee = documentSnapshot.getString("aboutme");
                    String una = documentSnapshot.getString("email");
                    name.setText(namee);
                    uname.setText(una.substring(0, una.length() - 10));

                    int iend = una.indexOf("@"); //this finds the first occurrence of "."
//in string thus giving you the index of where it is in the string

// Now iend can be -1, if lets say the string had no "." at all in it i.e. no "." is found.
//So check and account for it.

                    String subString;
                    if (iend != -1)
                    {
                        subString= una.substring(0 , iend); //this will give abc
                        uname.setText(subString);

                    }


                    if(!aboutmee.isEmpty())
                        aboutme.setText(aboutmee);
                    else
                        aboutme.setText("Write something here... ");


                } else {
                    // Document does not exist
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error retrieving document
                    }
                });


    }


    private void getclubs(){
        CollectionReference groupsRef = db.collection("groups");
        ArrayList<String> groupids = new ArrayList<>();
        groupsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot groupDocument : task.getResult()) {
                        CollectionReference chatMembersRef = groupDocument.getReference().collection("chatmembers");

                        Query query = chatMembersRef.whereEqualTo("uid", memberuid);
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> innerTask) {
                                if (innerTask.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : innerTask.getResult()) {
                                        String groupId = groupDocument.getId();
//                                        groupids.add(groupId);
                                        // Do something with the group ID
                                        Log.d("abd","aaa"+groupId);
//                                        groupids.add(groupId);
                                        sgrv.setText(sgrv.getText()+ groupId+"\n");




                                    }
//                                    Toast.makeText(getApplicationContext(), "hayyeee", Toast.LENGTH_SHORT).show();




                                } else {
                                    // Handle the error
                                }
                            }
                        });
                    }



                } else {
                    // Handle the error
                }
            }
        });

    }


    private void getimages(){


        CollectionReference collectionRef = db.collection("clubs");



        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String fieldValue = document.getString("dp");
                        // Do something with the field value
                        ls.add(new ImageViewModel(fieldValue));
                        adapter.notifyDataSetChanged();

                    }
                } else {
                    // Handle the error
                }
            }
        });

    }


    private void setdp(){
        CollectionReference messagesRef =  db.collection("users");
        messagesRef.document(memberuid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Log.d("TAG", documentSnapshot.getId() + " => " + documentSnapshot.getData());

                                String img = documentSnapshot.getString("imgurl");
                                if(!img.isEmpty())
                                    Picasso.get().load(img).into(profilepic);
                                else
                                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/images%2Fimage%3A256102?alt=media&token=0e9cff61-998e-466f-a6a8-318be59e1bc3").into(profilepic);


                            } else {
                                Log.d("TAG", "No such document");
                            }
                        } else {
                            Log.w("TAG", "Error getting document", task.getException());
                        }
                    }
                });
    }

}