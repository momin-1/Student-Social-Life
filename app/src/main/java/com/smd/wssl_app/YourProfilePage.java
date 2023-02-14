package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class YourProfilePage extends AppCompatActivity {
FirebaseAuth mauth;
    ImageButton logout,back;
    ImageView profilepic;
    Integer ii=0;
    Integer bol=0;

    RecyclerView rv;
    Button update;
    List<ImageViewModel> ls;

    ImageViewAdapter adapter;
TextView sgrv;
    EditText name, aboutme;
    ImageButton home,football,groups,notifications,profile;
    FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_your_profile_page);

        mauth = FirebaseAuth.getInstance();
        Navbarfunctions();
        logout = findViewById(R.id.logout);
        sgrv = findViewById(R.id.SGrv);
        back = findViewById(R.id.back);
        update = findViewById(R.id.update);
        rv= findViewById(R.id.rvclubs);

        ls=new ArrayList<>();



        adapter=new ImageViewAdapter(ls,YourProfilePage.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(YourProfilePage.this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);
      ls.add(new ImageViewModel("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/images%2Fimage%3A256102?alt=media&token=0e9cff61-998e-466f-a6a8-318be59e1bc3"));
adapter.notifyDataSetChanged();
getimages();
getclubs();

name = findViewById(R.id.name);
aboutme  = findViewById(R.id.aboutme);
        getData();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateprofile();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profilepic = findViewById(R.id.profilepic);
        setdp();
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
        name = findViewById(R.id.name);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.requestFocus();
                name.setFocusableInTouchMode(true);

            }
        });

    }


    private void  getData(){
        DocumentReference docRef = db.collection("users").document(mauth.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve data
                    String namee = documentSnapshot.getString("name");
                        String aboutmee = documentSnapshot.getString("aboutme");
                    name.setText(namee);
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

    private void updateprofile(){

        DocumentReference docRef = db.collection("users").document(mauth.getUid());
        Map<String, Object> update = new HashMap<>();
        update.put("name", name.getText().toString());
        update.put("aboutme", aboutme.getText().toString());
        docRef.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Update successful
                Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error updating document
                    }
                });


    }

    public void Navbarfunctions(){

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

    private void setdp(){
        CollectionReference messagesRef =  db.collection("users");
        messagesRef.document(mauth.getUid())
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri imageUri = data.getData();
            profilepic.setImageURI(imageUri);

            // Upload the image to Firestore
            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            final StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());
            UploadTask uploadTask = imageRef.putFile(imageUri);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageURL = uri.toString();

                            // Add the image URL to the Firestore document
                            DocumentReference docRef = db.collection("users").document(mauth.getUid());
                            docRef.update("imgurl", imageURL);
                        }
                    });
                }
            });
        }
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

                        Query query = chatMembersRef.whereEqualTo("uid", mauth.getUid());
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


}