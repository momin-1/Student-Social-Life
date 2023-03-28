package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatMembers extends AppCompatActivity {

    RecyclerView rv;
    ImageButton back;
    List<ChatMemberModel> ls;
    String uid;
    String t,i;
    String ret;
    String loggedname;
    FirebaseAuth mauth;
    ChatMembersAdapter adapter;
    FirebaseFirestore db;
    TextView num,chat_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_members);
        back = findViewById(R.id.back);
        num = findViewById(R.id.num);
        mauth = FirebaseAuth.getInstance();
        uid = mauth.getUid();
        db = FirebaseFirestore.getInstance();
        Bundle extras = getIntent().getExtras();
        chat_name = findViewById(R.id.chat_name);
        loggedname = extras.getString("chat_name");
        chat_name.setText(loggedname.toString());
        rv = findViewById(R.id.rv);
        ls=new ArrayList<>();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter=new ChatMembersAdapter(ls,ChatMembers.this);
        rv.setAdapter(adapter);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(ChatMembers.this);
        rv.setLayoutManager(lm);
//        ls.add(new ChatMemberModel("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/Capture.PNG?alt=media&token=02143959-4f2a-4810-ab94-d9b223e056cb","WSSL USER",
//                "student","online"));
//        ls.add(new ChatMemberModel("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/Capture.PNG?alt=media&token=02143959-4f2a-4810-ab94-d9b223e056cb","WSSL user",
//                "student","offline"));
//        ls.add(new ChatMemberModel("https://firebasestorage.googleapis.com/v0/b/w-app-46ce9.appspot.com/o/Capture.PNG?alt=media&token=02143959-4f2a-4810-ab94-d9b223e056cb","WSSL user",
//                "student","online"));
        showMembers();

        adapter.notifyDataSetChanged();




    }


    private void showMembers(){
        CollectionReference groupsRef = db.collection("groups");
        DocumentReference footballRef = groupsRef.document(loggedname);
        CollectionReference messagesRef = footballRef.collection("chatmembers");

        Query messagesQuery = messagesRef.orderBy("name", Query.Direction.ASCENDING);

        messagesQuery.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            Log.d("nono", "plplpl"+documentSnapshot.getId() + " => " + documentSnapshot.getData());
                            t= documentSnapshot.getString("name");

                            i = documentSnapshot.getString("dp");

                            ls.add(new ChatMemberModel(i,t,"student","offline",documentSnapshot.getString("uid")));
//                            Log.d("yess", "plplplplpl"+document.getString("status")+document.getString("email"));

//                            getstatus(documentSnapshot.getString("uid"));

                            adapter.notifyDataSetChanged();
num.setText(Integer.toString(adapter.getItemCount())+" Members");
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
    private void  getstatus(String uid)  {
        Task<DocumentSnapshot> task = db.collection("users").document(uid).get();

        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Do something with the document data
                        Log.d("yess", "plplpl"+document.getString("status")+document.getString("email"));

                        ret = document.getString("status");



                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }


                adapter.notifyDataSetChanged();


            }
        });


    }



}



