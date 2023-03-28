package com.smd.wssl_app;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import okhttp3.MediaType;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignUpFragment extends Fragment {

Button button;
    int countt;
String num,email_full,password_full;
    EditText otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four;
EditText email,password;
String otp_full;
FirebaseAuth mauth;FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        mauth = FirebaseAuth.getInstance();
       if( mauth.getCurrentUser()!=null){
           if(mauth.getCurrentUser().isEmailVerified()){
           Intent i = new Intent(getContext(),HomeScreen.class);
           startActivity(i);}
       }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sign_up_fragment, container, false);
        // Inflate the layout for this fragment
        button = view.findViewById(R.id.signup);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        mauth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_full = email.getText().toString();
                password_full = password.getText().toString();
//                buttonSendEmail(email_full);
//                Toast.makeText(getContext(), "email sent", Toast.LENGTH_SHORT).show();
                if(!email_full.isEmpty() && !password_full.isEmpty() && email_full.contains("@") && password_full.length()>6){
                  if(email_full.contains("uowmail.edu.au") || email_full.contains("uowdubai.ac.ae")){
                    sendOTPEmail("+923098991009",123456);
                showCustomDialog();
                  }
//                  else{
//                      Toast.makeText(getContext(), "Out of Domain, can't sign you up", Toast.LENGTH_LONG).show();
//                  }

                }
                else{
                    Toast.makeText(getContext(), "Fill the fields properly", Toast.LENGTH_SHORT).show();

                }
            }
        });
        TextView textView = view.findViewById(R.id.already);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Select Sign In to continue", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }


    private void showCustomDialog() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.otp_1, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        Button button = dialogView.findViewById(R.id.ok);
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();


        Random rand = new Random();
        int randomInt = rand.nextInt(10000);

        num = String.format("%04d", randomInt);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();

              showCustomDialog3();
            }
        });



    }


    private void showCustomDialog2(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.otp_2, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        Button button = dialogView.findViewById(R.id.ok);
        otp_textbox_one = dialogView.findViewById(R.id.otp_edit_box1);
        otp_textbox_two = dialogView.findViewById(R.id.otp_edit_box2);
        otp_textbox_three = dialogView.findViewById(R.id.otp_edit_box3);
        otp_textbox_four = dialogView.findViewById(R.id.otp_edit_box4);

        EditText[] edit = {otp_textbox_one, otp_textbox_two, otp_textbox_three, otp_textbox_four};
        otp_textbox_one.addTextChangedListener(new GenericTextWatcher(otp_textbox_one, edit));
        otp_textbox_two.addTextChangedListener(new GenericTextWatcher(otp_textbox_two, edit));
        otp_textbox_three.addTextChangedListener(new GenericTextWatcher(otp_textbox_three, edit));
        otp_textbox_four.addTextChangedListener(new GenericTextWatcher(otp_textbox_four, edit));



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogBuilder.dismiss();
                showCustomDialog3();


            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }



    private void showCustomDialog3() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.otp_3, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




        Button button = dialogView.findViewById(R.id.accept);
        Button button2 = dialogView.findViewById(R.id.reject);
        TextView text2 = dialogView.findViewById(R.id.text2);

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showCustomDialog4();


            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
                                    Toast.makeText(getContext(), "Email Sent for verification", Toast.LENGTH_SHORT).show();


                registerUser(email_full,password_full);
                Intent i = new Intent(getContext(),Welcome.class);
                                        startActivity(i);


            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();


            }
        });



    }

    private void showCustomDialog4() {
        final AlertDialog dialogBuilder = new AlertDialog.Builder(getContext()).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.terms, null);
        dialogBuilder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




        ImageButton button = dialogView.findViewById(R.id.back);


        dialogBuilder.setView(dialogView);
        dialogBuilder.show();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();


            }
        });



    }



    private void registerUser(String email,String password){
        mauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(email.contains("@uowmail.edu.au"))
                    Toast.makeText(getContext(), "Successfully registered as Student", Toast.LENGTH_LONG).show();
                    else if(email.contains("@uow.edu.au"))
                        Toast.makeText(getContext(), "Successfully registered as Professor", Toast.LENGTH_LONG).show();
                    db = FirebaseFirestore.getInstance();

                    Verify(email_full);


                        // Get the user ID
                        FirebaseUser user = mauth.getCurrentUser();
                        String uid = user.getUid();

                        // Add the user ID as a document to Firestore
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("uid", uid);
                        userData.put("email", user.getEmail());
                        userData.put("password", password_full);
                        userData.put("name", "WSSL User");
                        userData.put("aboutme", "");
                        userData.put("imgurl", "");

                        db.collection("users").document(uid).set(userData)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        Log.d("fire", "DocumentSnapshot successfully written!");
//                                    Toast.makeText(getContext(), "You can now Sign In after Verification", Toast.LENGTH_LONG).show();
//                                        Intent i = new Intent(getContext(),HomeScreen.class);
//                                        startActivity(i);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("fire", "Error writing document", e);
                                    }
                                });



                }
            }
        })

                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Couldn't register  "+e.getMessage(), Toast.LENGTH_LONG).show();


            }
        });







    }

    private void Verify(String emaill){

        mauth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
//                Toast.makeText(getContext(), "Not SENT", Toast.LENGTH_SHORT).show();

            }
        });



    }


    private static final String MAILGUN_API_KEY = "75cd784d-efd9f9c4";
    private static final String MAILGUN_DOMAIN_NAME = "sandbox0f374a8b91a74b858b9a6f1e1e7f6291.mailgun.org";

    private void sendOTPEmail(String phone, int otp) {


        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

                Log.d("TAG", "---onVerificationCompleted:" + credential);

//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("TAG", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   PhoneAuthProvider.@NonNull ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("TAAAG", "---onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;


            }
        };



    }



}
