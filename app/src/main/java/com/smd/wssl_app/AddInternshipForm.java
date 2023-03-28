package com.smd.wssl_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class AddInternshipForm extends AppCompatActivity {

    EditText title,type,salary,jobfamily,location,description,link;
    Button add_internship;
    FirebaseAuth mauth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_internship_form);
        mauth=FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        title = findViewById(R.id.title);
        type = findViewById(R.id.type);
        salary = findViewById(R.id.salary);
        jobfamily = findViewById(R.id.jobfamily);
        location = findViewById(R.id.location);
        description = findViewById(R.id.description);
        add_internship = findViewById(R.id.add_internship);
        link = findViewById(R.id.link);

        add_internship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_to_db();
                finish();
            }
        });




    }
    private void add_to_db(){
        Map<String, Object> data = new HashMap<>();
        data.put("title", title.getText().toString());
        data.put("type", type.getText().toString());
        data.put("salary", "$"+salary.getText().toString());
        data.put("job-family", jobfamily.getText().toString());
        data.put("location", location.getText().toString());
        data.put("description", description.getText().toString());
        data.put("link", link.getText().toString());
        data.put("interested", "0");

        db.collection("internships")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("adminnn", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Internship Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("adminnn", "Error adding document", e);
                        Toast.makeText(getApplicationContext(), "Error Adding Internship", Toast.LENGTH_SHORT).show();

                    }
                });




    }

}