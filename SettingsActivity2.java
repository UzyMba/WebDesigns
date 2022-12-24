package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity2 extends AppCompatActivity {

    private CircleImageView setProfilePicture;
    private EditText username, status;
    private Button update, back;
    private String currentUserID, uName, Status,retrieveUsername, retrieveStatus;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();


        initializeViews();



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateSettings();

            }
        });

        retrieveUserInfo();



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToMainActivity();
            }
        });



    }

    private void updateSettings() {
        uName = username.getText().toString();
        Status = status.getText().toString();

        if(TextUtils.isEmpty(uName)){
            Toast.makeText(this, "Please create a username", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, String>profileMap = new HashMap<>();
                profileMap.put("uid", currentUserID);
                profileMap.put("name", uName);
                profileMap.put("status", Status);
            mRef.child("Users").child(currentUserID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SettingsActivity2.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(SettingsActivity2.this, "Error " + message, Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }public void sendToLoginActivity(){
        Intent login = new Intent(SettingsActivity2.this,Login.class);
        startActivity(login);
    }

    private void sendToMainActivity() {
        Intent j = new Intent(SettingsActivity2.this,MainActivity.class);
        j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(j);
    }

    private void initializeViews() {
        setProfilePicture = findViewById(R.id.profile_image);
        username = findViewById(R.id.set_profile_name);
        status = findViewById(R.id.set_status);
        update= findViewById(R.id.updateButton);
        back = findViewById(R.id.return_main);
    }

    private void retrieveUserInfo() {

        mRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if((snapshot.exists())&& (snapshot.hasChild("username")&&(snapshot.hasChild("image")))){
                    retrieveUsername = snapshot.child("username").getValue().toString();
                    retrieveStatus = snapshot.child("status").getValue().toString();

                    username.setText(retrieveUsername);
                    status.setText(retrieveStatus);
                }else  if((snapshot.exists())&& (snapshot.hasChild("username"))){
                    retrieveUsername = snapshot.child("username").getValue().toString();
                    retrieveStatus = snapshot.child("status").getValue().toString();

                    username.setText(retrieveUsername);
                    status.setText(retrieveStatus);
                }else{
                    Toast.makeText(SettingsActivity2.this, "Kindly update your profile", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}