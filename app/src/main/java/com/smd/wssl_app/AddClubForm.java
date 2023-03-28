package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AddClubForm extends AppCompatActivity {
    EditText name,interests;
    Button add_club;
    ImageButton dp;
    FirebaseAuth mauth;
    Uri filepath;
    Uri filepathuri;
    FirebaseFirestore db;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_club_form);
        mauth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        name = findViewById(R.id.name);
        interests = findViewById(R.id.interests);
        add_club=findViewById(R.id.add_club);
        dp = findViewById(R.id.dp);
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        add_club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_to_db();
                finish();
            }
        });
    }

    private void add_to_db(){
        Map<String, Object> data = new HashMap<>();
        data.put("club_name", name.getText().toString());
        data.put("interest", interests.getText().toString());
        data.put("dp", filepathuri);
        data.put("members", "0");


        db.collection("clubs")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("adminnn", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Club Added Successfully", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("adminnn", "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "Error Adding Club", Toast.LENGTH_SHORT).show();

                    }
                });







        CollectionReference groupsRef = db.collection("groups");
        String customId = name.getText().toString(); // specify your custom ID here
        DocumentReference footballRef = groupsRef.document(customId);

        Map<String, Object> data2 = new HashMap<>();
//        data2.put("members", "0");



        footballRef.set(data2)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "Document with custom ID: " + customId + " created successfully");

                        CollectionReference messagesRef = footballRef.collection("messages");
                        CollectionReference chatMembersRef = footballRef.collection("chatmembers");

                        // add data to "messages" collection
                        messagesRef.add(new HashMap<String, Object>() {{
                            put("message1", "value1");
                            put("message2", "value2");
                        }});

                        // add data to "chatmembers" collection
                        chatMembersRef.add(new HashMap<String, Object>() {{
                            put("member1", "value1");
                            put("member2", "value2");
                        }});
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error creating document with custom ID: " + customId, e);
                    }
                });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            // Upload the image to Firebase Storage
            dp.setImageURI(filepath);
            uploadImage(filepath);


        }
    }

    private void uploadImage(Uri filePath) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        final StorageReference ref = storageRef.child("images/" + filePath.getLastPathSegment());
        ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the URL of the uploaded image
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Save the URL in Firestore
                                filepathuri = uri;
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure
                    }
                });
    }



}