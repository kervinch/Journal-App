package com.absolutely.self;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.JournalAPI;

public class CreateAccountActivity extends AppCompatActivity {
    private Button loginButton;
    private Button createButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firestore connections
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Users");

    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        firebaseAuth = FirebaseAuth.getInstance();

        createButton = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.create_progress);
        emailEditText = findViewById(R.id.create_email);
        passwordEditText = findViewById(R.id.create_password);
        usernameEditText = findViewById(R.id.create_username);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    //User is already logged in
                } else {
                    //User is not logged in

                }
            }
        };

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(emailEditText.getText().toString()) && !TextUtils.isEmpty(passwordEditText.getText().toString()) && !TextUtils.isEmpty(usernameEditText.getText().toString())) {

                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    String username = usernameEditText.getText().toString().trim();
                    createUserEmailAccount(email, password, username);
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Empty Fields Not Allowed", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void createUserEmailAccount(String email, String password, final String username) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //We take user to AddJournalActivity
                        currentUser = firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        final String currentUserId = currentUser.getUid();

                        //Create a user map so we can create a user in the user collection
                        Map<String, String> userObj = new HashMap<>();
                        userObj.put("userId", currentUserId);
                        userObj.put("username", username);

                        //Save to our firestore db
                        collectionReference.add(userObj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (Objects.requireNonNull(task.getResult()).exists()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            String name = task.getResult().getString("username");
                                            JournalAPI journalAPI = JournalAPI.getInstance(); //Global API
                                            journalAPI.setUserId(currentUserId);
                                            journalAPI.setUsername(name);
                                            Intent intent = new Intent(CreateAccountActivity.this, PostJournalActivity.class);
                                            intent.putExtra("username", name);
                                            intent.putExtra("userId", currentUserId);
                                            startActivity(intent);
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    } else {
                        //Something went wrong
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        } else {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

}
