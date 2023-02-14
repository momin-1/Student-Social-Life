package com.smd.wssl_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SGAdapter extends RecyclerView.Adapter<SGAdapter.MyViewHolder> {
    List<SGModel> ls;
    Context c;
    FirebaseAuth mauth;


    public SGAdapter(List<SGModel> ls, Context c) {
        this.ls = ls;
        this.c = c;
        mauth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(c).inflate(R.layout.row_subject_groups,parent,false);
        return new SGAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(ls.get(position).getName());
        holder.longname.setText(ls.get(position).getLongname());
        holder.no_of_members.setText(ls.get(position).getAmount_of_users());

        check(ls.get(position).getName(),holder);

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.join.getText()=="Joined"){
                    Intent i = new Intent(c,ChatPage.class);
                    i.putExtra("chat_name",ls.get(position).getName());
                    c.startActivity(i);}
            }
        });

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.join.getText()=="Joined"){

                    Intent i = new Intent(c,ChatPage.class);
                    i.putExtra("chat_name",ls.get(position).getName());
                    c.startActivity(i);}
            }
        });

        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.join.getText()!="Joined"){
                    //add user to club
                    getData(ls.get(position).getName().toString());
                    holder.join.setText("Joined");
                    holder.join.setBackgroundResource(R.color.gray);
                }

            }
        });

    }

    public void filterlist(ArrayList<SGModel> filteredstalls) {
        ls = filteredstalls;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,longname;
        AppCompatButton join;
        TextView no_of_members;
CircleImageView img;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            longname = itemView.findViewById(R.id.longname);
            join = itemView.findViewById(R.id.join);
img = itemView.findViewById(R.id.profile_image);
            no_of_members  = itemView.findViewById(R.id.no_of_members);


        }
    }


    private void getData(String club_name){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference docRef = firestore.collection("users").document(mauth.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Object user = documentSnapshot.getData();
                    Log.d("TAG", "User: " + user);

                    addmember(club_name,documentSnapshot.get("name").toString(),documentSnapshot.get("imgurl").toString());

                } else {
                    Log.d("TAG", "No such document");
                }
            }
        });
    }


    private void addmember(String club_name,String user_name,String dp){
        Map<String, Object> data = new HashMap<>();
        data.put("uid", mauth.getUid());
        data.put("dp", dp);
        data.put("name",user_name );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference groupsRef = db.collection("groups");
        DocumentReference footballRef = groupsRef.document(club_name);




        CollectionReference messagesRef = footballRef.collection("chatmembers");

        messagesRef.add(data)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        Log.d("TAG", "Document added with ID in chatmembers " );

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error adding document", e);
                    }
                });

    }


    private void check(String club_name, SGAdapter.MyViewHolder holder){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference colRef = db.collection("groups").document(club_name).collection("chatmembers");
        Query query = colRef.whereEqualTo("uid", currentUserId);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().isEmpty()) {

                        Log.d("TAG", "User ID not found in chatmembers collection");
//                        Toast.makeText(c, "User not found", Toast.LENGTH_SHORT).show();

                    } else {
                        holder.join.setText("Joined");
                        holder.join.setBackgroundResource(R.color.gray);
                        Log.d("TAG", "User ID found in chatmembers collection");
//                        Toast.makeText(c, "User found", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
