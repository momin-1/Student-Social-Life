package com.smd.wssl_app;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInFragment extends Fragment {
    Button button;
    EditText email,password;
FirebaseAuth mauth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


mauth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.sign_in_fragment, container, false);
        // Inflate the layout for this fragment
        button = view.findViewById(R.id.signin);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        TextView textView = view.findViewById(R.id.already);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String email_full =   email.getText().toString();
              String password_full =   password.getText().toString();
              if(!email_full.isEmpty() && !password_full.isEmpty())
              siginUser(email_full,password_full);
              else
                  Toast.makeText(getContext(), "Fill the fields", Toast.LENGTH_SHORT).show();


            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Select Sign Up to continue", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }


    private  void siginUser(String email,String password) {



            mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
//                    Toast.makeText(getContext(), "Signing in"+mauth.getCurrentUser().get, Toast.LENGTH_LONG).show();
                        if (mauth.getCurrentUser().getUid().contains("LFBmnV7bLVPEeJ5W0BKdLReyeew2")) {
                            Intent i = new Intent(getContext(), AdminHomePage.class);
                            startActivity(i);
                        } else {
                            if(mauth.getCurrentUser().isEmailVerified()) {
                                if (email.contains("@uowmail.edu.au"))
                                    Toast.makeText(getContext(), "Signing in as Student", Toast.LENGTH_LONG).show();
                                else if (email.contains("@uow.edu.au"))
                                    Toast.makeText(getContext(), "Signing in as Professor", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(getContext(), HomeScreen.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getContext(), "Can't Verify Email", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {

                    Toast.makeText(getContext(), "Couldn't Sign in " + e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

    }


}