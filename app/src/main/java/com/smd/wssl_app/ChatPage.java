package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChatPage extends AppCompatActivity {

TextView chat_name;
ImageButton back,leave;

String uid;
RecyclerView rv;
String img_url;
EditText message_bar;
Button sendmessage;

FirebaseAuth mauth;
    MessagesAdapter adapter;
FirebaseFirestore db;
List<ChatModel> ls;
    String loggedname,typee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        message_bar = findViewById(R.id.message_bar);
        back = findViewById(R.id.back);
        leave = findViewById(R.id.leave);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


rv = findViewById(R.id.rv);
mauth = FirebaseAuth.getInstance();
db  = FirebaseFirestore.getInstance();
        img_url= getimageurl();
        sendmessage = findViewById(R.id.sendmessage);
        Bundle extras = getIntent().getExtras();
         loggedname = extras.getString("chat_name");
         typee = extras.getString("type");
        chat_name = findViewById(R.id.chat_name);
        chat_name.setText(loggedname);

        Navbarfunctions();
        chat_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ChatMembers.class);
                i.putExtra("chat_name",loggedname);
                startActivity(i);
            }
        });


uid = mauth.getUid();

        ls=new ArrayList<>();


        adapter=new MessagesAdapter(ls,ChatPage.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(ChatPage.this);
        rv.setLayoutManager(lm);
//        ls.add(new ChatModel("dh53omFeQGT80I1HHD7zPI1XeuV2","hello gee","https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/Capture.PNG?alt=media&token=02143959-4f2a-4810-ab94-d9b223e056cb"));
//        ls.add(new ChatModel("dh53omFeQGT80I1HHD7zPIfXeuV2","okok",""));
        updatelist();
        CollectionReference parentRef = db.collection("groups");

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog();
            }
        });

        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(message_bar.getText().toString()!="")
                sendmessage(message_bar.getText().toString());


            }
        });



        adapter.notifyDataSetChanged();


    }

    private void sendmessage(String text){
        message_bar.setText("");
        Map<String, Object> data = new HashMap<>();
        data.put("sender_id", mauth.getUid());
        data.put("text", text);
        data.put("time", FieldValue.serverTimestamp());
        data.put("imgurl", img_url);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference groupsRef = db.collection("groups");
        DocumentReference footballRef = groupsRef.document(loggedname);






        CollectionReference messagesRef = footballRef.collection("messages");

        messagesRef.add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        Log.d("TAG", "Document added with ID:wioejfiewjoj " );
                        ls.add(new ChatModel(mauth.getUid(),text,img_url));

                        adapter.notifyDataSetChanged();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });


    }

    private void updatelist(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference groupsRef = db.collection("groups");
        DocumentReference footballRef = groupsRef.document(loggedname);
        CollectionReference messagesRef = footballRef.collection("messages");

        Query messagesQuery = messagesRef.orderBy("time", Query.Direction.ASCENDING);

        messagesQuery.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            Log.d("TAG", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                    String t= documentSnapshot.getString("text");
                    String ss= documentSnapshot.getString("sender_id");
                    String i = documentSnapshot.getString("imgurl");
                     ls.add(new ChatModel(ss,t,i));
                     adapter.notifyDataSetChanged();

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error getting documents", e);
                    }
                });

    }


    private String getimageurl(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference messagesRef = db.collection("users");

        messagesRef.document(mauth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                              img_url = documentSnapshot.getString("imgurl");
                                Log.d("TAGG", documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            } else {
                                Log.d("TAGG", "No such document");
                            }
                        } else {
                            Log.w("TAGG", "Error getting document", task.getException());
                        }
                    }
                });

return img_url;
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


    private void showCustomDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(ChatPage.this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.leave_chat, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        Button button = dialogView.findViewById(R.id.confirm);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                leave_group();

            }
        });



    }

    private  void leave_group(){
        db = FirebaseFirestore.getInstance();
        CollectionReference chatMembersRef = db.collection("groups").document(loggedname).collection("chatmembers");
        Query query = chatMembersRef.whereEqualTo("uid", mauth.getUid().toString());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Query successful
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    // Delete the document
                    documentSnapshot.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "-----document deleted");

                            // Document deleted successfully
                            Toast.makeText(getApplicationContext(), "Group Left Successfully", Toast.LENGTH_SHORT).show();
                            decc(loggedname);
                            finish();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error deleting document
                                }
                            });
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error executing query
                    }
                });





    }

    private void status(String status){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


// Get the document for the current user
        DocumentReference userRef = db.collection("users").document(currentUser.getUid());

// Create a map to hold the new fields
        Map<String, Object> newData = new HashMap<>();
        newData.put("status", status);


// Add the new fields to the document
        userRef.update(newData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "status:"+status, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // The fields could not be added to the document
                        Toast.makeText(getApplicationContext(),"aaaa"+ e, Toast.LENGTH_SHORT).show();

                    }
                });


    }


    private void decc(String title){
        Log.d("opop",typee);
        if(typee.equals("club")) {
            Log.d("taggg", title);
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .build();
            firestore.setFirestoreSettings(settings);

            Query query = firestore.collection("clubs")
                    .whereEqualTo("club_name", title);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            DocumentReference documentRef = document.getReference();
                            firestore.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(documentRef);
                                    String fieldValue = snapshot.getString("members");
                                    int fieldValueInt = Integer.parseInt(fieldValue);
                                    fieldValueInt--;
                                    fieldValue = Integer.toString(fieldValueInt);
                                    transaction.update(documentRef, "members", fieldValue);
                                    Log.d("firer", fieldValue);
                                    Log.d("opop",
                                            "cmonnn");

                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // The transaction was successful.
                                    Log.d("firer", "---YES");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // The transaction failed.
                                    Log.d("firer", "---no");

                                }
                            });
                        }
                    } else {
                        // The query failed.
                        Log.d("firer", "---no");

                    }
                }
            });
        }
        else  if(typee.equals("sg")) {
            Log.d("taggg", title);
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .build();
            firestore.setFirestoreSettings(settings);

            Query query = firestore.collection("subject-groups")
                    .whereEqualTo("name", title);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                        for (DocumentSnapshot document : documents) {
                            DocumentReference documentRef = document.getReference();
                            firestore.runTransaction(new Transaction.Function<Void>() {
                                @Override
                                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                                    DocumentSnapshot snapshot = transaction.get(documentRef);
                                    String fieldValue = snapshot.getString("amount_of_users");
                                    int fieldValueInt = Integer.parseInt(fieldValue);
                                    fieldValueInt--;
                                    fieldValue = Integer.toString(fieldValueInt);
                                    transaction.update(documentRef, "amount_of_users", fieldValue);
                                    Log.d("firer", fieldValue);
                                    Log.d("opop",
                                            "cmonnn");

                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // The transaction was successful.
                                    Log.d("firer", "---YES");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // The transaction failed.
                                    Log.d("firer", "---no");

                                }
                            });
                        }
                    } else {
                        // The query failed.
                        Log.d("firer", "---no");

                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
           status("offline");
        super.onDestroy();

    }

    @Override
    protected void onStart() {
        super.onStart();
         status("online");

    }


}