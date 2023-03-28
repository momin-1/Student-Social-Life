package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.responses.feed.FeedTimelineResponse;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

public class YourProfilePage extends AppCompatActivity {
FirebaseAuth mauth;
    ImageButton logout,back;
    ImageView profilepic;
    String jjk;
    Integer ii=0;ArrayList<String> liked;
    ArrayList<String> suggests;
    int numCompleted = 0;
    Integer bol=0;
    List<String> dps;

    RecyclerView rv;
    EditText u,p;
    Button ai;
    AppCompatButton update;
    List<ImageViewModel> ls;

    ImageViewAdapter adapter;
TextView sgrv;
TextView uname;
    EditText name, aboutme;
    ImageButton home,football,groups,notifications,profile;
    FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       db = FirebaseFirestore.getInstance();

        setContentView(R.layout.activity_your_profile_page);
dps=new ArrayList<>(Arrays.asList());
        mauth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);

        Navbarfunctions();
        suggestions();
        u = findViewById(R.id.usernamee);
        p = findViewById(R.id.password);
        uname = findViewById(R.id.uname);
        sgrv = findViewById(R.id.SGrv);
        back = findViewById(R.id.back);
        update = findViewById(R.id.update);
        ai = findViewById(R.id.ai);
        rv= findViewById(R.id.rvclubs);

        ls=new ArrayList<>();

jjk = "";

        adapter=new ImageViewAdapter(ls,YourProfilePage.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(YourProfilePage.this,LinearLayoutManager.HORIZONTAL,false);
        rv.setLayoutManager(lm);
//      ls.add(new ImageViewModel("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/images%2Fimage%3A256102?alt=media&token=0e9cff61-998e-466f-a6a8-318be59e1bc3"));
//adapter.notifyDataSetChanged();

checkclubs();
getclubs();

name = findViewById(R.id.name);
aboutme  = findViewById(R.id.aboutme);
        getData();


        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Wait while we fetch data", Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                         jjk = integrateAI();

                        // post a message to the main thread's message queue when myFunction() is done
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                // execute your function on the main thread here
                                if(!isFinishing())

                                        if (jjk.equals("T"))
                                            Toast.makeText(getApplicationContext(), "Enter Details", Toast.LENGTH_SHORT).show();

                                   else if (jjk.equals("N"))
                                        Toast.makeText(getApplicationContext(), "Logging Error", Toast.LENGTH_SHORT).show();


                                      //  showCustomDialog(jjk);

                            }
                        });
                    }
                }).start();





//                String jjk =integrateAI();
//                android.os.SystemClock.sleep(20000);
//                showCustomDialog(jjk);

            }
        });

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
                showCustomDialog2();
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
                                   Picasso.get().load(R.drawable.uow_logo).into(profilepic);


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
                                        Log.d("abdabd","aaa"+groupId);
//                                        groupids.add(groupId);
                                        if(!groupId.toLowerCase().contains("club"))
                                        sgrv.setText(sgrv.getText()+ groupId+"\n");
                                        sgrv.setPaintFlags(sgrv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);




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



    private String integrateAI(){
if(!u.getText().toString().isEmpty() && !p.getText().toString().isEmpty()){
    String username = u.getText().toString();
    String password = p.getText().toString();

Log.d("uiuiui",username+"-11 11-"+password);

//  Create a new Instagram client with the user's credentials
                liked = new ArrayList<String>();
                int i=0;
                IGClient client = null;

                try {
                    client = IGClient.builder()
                            .username(username)
                            .password(password)
                            .login();
                } catch (IGLoginException e) {
                    Log.d("logged","logger");
                    e.printStackTrace();
return "N";

                }
                Log.d("logged",client.toString());
                for (FeedTimelineResponse response: client.getActions().timeline().feed()) {
                    Log.d("sook",response.toString());
//                   Log.d("sook",response.toString().substring(0,response.toString().indexOf("type1")));


                    liked.add(response.toString());

                    Log.d("ook2","num");
                    i++;
                    if(i==3)
                        break;
//                 for (String res :  (response.toString().split("' '"))){
//                     Log.d("feedD",res);
//                     if(res.startsWith("#"))
//                         liked.add(res.substring(1));
//
//                 }

                }
                
                String jjk="";
//                Log.d("resullt","fsdf");
                Log.d("resulter",liked.toString());
                for(int k=0;k<liked.size();k++)
                    for(int j=0;j<suggests.size();j++){
                        if(liked.get(k).toLowerCase().contains(suggests.get(j)))
                        {
//                            Toast.makeText(getApplicationContext(), "Club Suggested:"+suggests.get(j)+" Club", Toast.LENGTH_SHORT).show();
                        Log.d("cmonman","yes suggested"+j+liked.size());
                        jjk = suggests.get(j);
                        k=liked.size();
                        break;
                        }
                    }


return jjk;}
else{
    return "T";
}
    }

    private void getimages(List<String> dpp){


        CollectionReference collectionRef = db.collection("clubs");



        collectionRef.whereIn("club_name", dps)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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




//        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                               if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        String fieldValue = document.getString("dp");
//                        // Do something with the field value
//                        ls.add(new ImageViewModel(fieldValue));
//                        adapter.notifyDataSetChanged();
//
//                    }
//                } else {
//                    // Handle the error
//                }
//            }
//        });

    }


    private void suggestions()
    {
        db = FirebaseFirestore.getInstance();
        mauth = FirebaseAuth.getInstance();
suggests = new ArrayList<>();
        db.collection("clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String[] splited = document.getString("club_name").split("\\s+");
suggests.add(splited[0].toLowerCase());

                            }
                        } else {
                            Log.d("fire", "Error getting documents: ", task.getException());
                        }
                    }
                });



    }

    private void showCustomDialog(String c) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(YourProfilePage.this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.club_suggestion, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        Button button = dialogView.findViewById(R.id.ok);
        TextView tv= dialogView.findViewById(R.id.textView);
        if(c.isEmpty()){
            tv.setText("Couldn't find any clubs for you :(");
        }
        if(!c.equals("") || !c.isEmpty() || !c.equals(" ") || !c.equals("N") || !c.equals("T") ) {
            Log.d("uiuiui", "o"+c+"o");
            tv.setText("You have been suggested a Club: " + c + " Club");
        }else
            tv.setText("Couldn't find any clubs for you :(");

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                Intent i =new Intent(getApplicationContext(),ClubsPage.class);
                startActivity(i);

            }
        });



    }

    private void showCustomDialog2() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(YourProfilePage.this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.logout_prompt, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        Button button = dialogView.findViewById(R.id.ok);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                mauth.signOut();
                Intent i =new Intent(getApplicationContext(),SignUpLogin.class);
                startActivity(i);
                finish();


            }
        });



    }

 public void checkclubs(){

// Replace "groups" with the name of your top-level collection
     CollectionReference groupsRef = db.collection("groups");

// Replace "your_uid_value" with the UID value you want to query for
     String uidValue = mauth.getUid();

     groupsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
         @Override
         public void onSuccess(QuerySnapshot querySnapshot) {
             int numIterations = querySnapshot.size();

             for (DocumentSnapshot groupDocumentSnapshot : querySnapshot) {
                 String groupId = groupDocumentSnapshot.getId();

                 // Query the chatmembers subcollection of the current group
                 CollectionReference chatMembersRef = groupDocumentSnapshot.getReference().collection("chatmembers");
                 Query query = chatMembersRef.whereEqualTo("uid", uidValue);

                 query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot querySnapshot) {
                         for (DocumentSnapshot chatMemberDocumentSnapshot : querySnapshot) {
                             String chatMemberId = groupDocumentSnapshot.getId();
                             // Use the group ID and chat member ID as needed
                             if(chatMemberId.toLowerCase().contains("club") && !dps.contains(chatMemberId)) {
                                 dps.add(chatMemberId);
                                 Log.d("abdulll", "done " + chatMemberId + ": ");

                             }
                         }
                         numCompleted++;
                         if (numCompleted == numIterations) {
                             if(!dps.isEmpty())
                             getimages(dps);
                             else
                                 rv.setVisibility(View.INVISIBLE);

                         }



                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Log.e("TAG", "Error querying for chatmembers in group " + groupId + ": " + e);
                     }
                 });
             }
         }
     }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
             Log.e("TAG", "Error querying for groups: " + e);
         }
     });

 }

}