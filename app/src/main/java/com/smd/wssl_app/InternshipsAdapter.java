package com.smd.wssl_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class InternshipsAdapter extends RecyclerView.Adapter<InternshipsAdapter.MyViewHolder> {
    List<InternshipModel> ls;
    Context c;
    FirebaseAuth mauth;

    public InternshipsAdapter(List<InternshipModel> ls, Context c) {
        this.ls = ls;
        this.c = c;
        mauth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(c).inflate(R.layout.row_internship,parent,false);
        return new InternshipsAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.name.setText(ls.get(position).getName());
        holder.pay.setText(ls.get(position).getPay());
        holder.address.setText(ls.get(position).getAddress());
        String users = ls.get(position).getAmount_of_users();
        holder.no_of_members.setText((users));
        check(ls.get(position).getName(),holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialog(ls.get(position).getName(),ls.get(position).getPay(),ls.get(position).getAddress(),ls.get(position).getLink());

            }
        });

    }

    public void filterlist(ArrayList<InternshipModel> filteredstalls) {
        ls = filteredstalls;
        notifyDataSetChanged();
    }




    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView img;
        TextView name,address,pay;
        TextView no_of_members;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            pay = itemView.findViewById(R.id.pay);
            no_of_members  = itemView.findViewById(R.id.no_of_members);


        }
    }


    private void showCustomDialog(String title,String pay,String address,String link) {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(c).create();
        LayoutInflater inflater = LayoutInflater.from(c);
        View dialogView = inflater.inflate(R.layout.apply_internship, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView textView = dialogView.findViewById(R.id.textView);
        String t1 = pay.split(":")[0];
        String t2 = pay.split(":")[1];

        textView.setText(
                "Title: "+title+"\nType: "+t1+"\nPay: "+t2+"\nAddress: "+address);


        Button button = dialogView.findViewById(R.id.confirm);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                add_to_db(title);
                inc(title);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                c.startActivity(intent);

            }
        });



    }


    private void inc(String title){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .build();
        firestore.setFirestoreSettings(settings);

        Query query = firestore.collection("internships")
                .whereEqualTo("title", title);
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
                                String fieldValue = snapshot.getString("interested");
                                int fieldValueInt = Integer.parseInt(fieldValue);
                                fieldValueInt++;
                                fieldValue = Integer.toString(fieldValueInt);
                                transaction.update(documentRef, "interested", fieldValue);
                                return null;
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // The transaction was successful.
                                Log.d("firer","---YES");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // The transaction failed.
                            }
                        });
                    }
                } else {
                    // The query failed.
                }
            }
        });

    }

    private void check(String title,InternshipsAdapter.MyViewHolder holder){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

// Get a reference to the "internships" collection
        CollectionReference internshipsRef = firestore.collection("internships");

// Query the "internships" collection for a document where the "title" field is equal to "CS"
        Query query = internshipsRef.whereEqualTo("title", title);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    // Get the first document from the query result
                    DocumentSnapshot parentDocument = task.getResult().getDocuments().get(0);

                    // Get a reference to the "members" subcollection of the parent document
                    CollectionReference membersRef = parentDocument.getReference().collection("members");

                    // Query the "members" subcollection for a document where the "uid" field is equal to 1
                    Query memberQuery = membersRef.whereEqualTo("uid", mauth.getUid());

                    memberQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                Log.d("Firestore", "Document with uid 1 already exists");
                                holder.itemView.setClickable(false);

                            } else {
                                Log.d("Firestore", "Document with uid 1 not found");
                            }
                        }
                    });
                } else {
                    Log.w("Firestore", "Parent document not found");
                }
            }
        });


    }

    private void add_to_db(String title){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Get a reference to the "internships" collection
        CollectionReference internshipsRef = db.collection("internships");

// Query the "internships" collection for a document where the "title" field is equal to "CS"
        Query query = internshipsRef.whereEqualTo("title",title );

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()) {
                    // Get the first document from the query result
                    DocumentSnapshot parentDocument = task.getResult().getDocuments().get(0);


                    // Get a reference to the "members" subcollection of the parent document
                    CollectionReference membersRef = parentDocument.getReference().collection("members");

                    // Create a Map to hold the data for the new document in the subcollection
                    Map<String, Object> newMember = new HashMap<>();

                    newMember.put("uid", mauth.getUid());


                    // Add the new document to the "members" subcollection
                    membersRef.add(newMember)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference memberRef) {
                                    Log.d("Firestore", "Member added with ID: " + memberRef.getId());
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("Firestore", "Error adding member", e);
                                }
                            });
                } else {
                    Log.w("Firestore", "Parent document not found");
                }
            }
        });


    }
}
