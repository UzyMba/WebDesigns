package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private EditText email, password;
    private Button createAccount;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private TextView alreadyHaveAccount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InitializeIDs();
        mAuth=FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("please wait");

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToLogin();

            }
        });



        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CreateAccount();
            }
        });



    }

    private void sendToLogin() {
        Intent l = new Intent(Register.this,MainActivity.class);
        startActivity(l);
    }

    private void CreateAccount() {
        String user = email.getText().toString();
        String pass = password.getText().toString();

        if(TextUtils.isEmpty(user)){
            email.setError("Enter Email Address");
        }
        if(TextUtils.isEmpty(pass)){
            email.setError("Enter password");
        }

        else{
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(user, pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendToMainActivity();
                                Toast.makeText(Register.this, "Creation Successful", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                            else{
                                finish();
                                Toast.makeText(Register.this, "Creation not completed", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }
    }

    private void sendToMainActivity() {
        Intent k = new Intent(Register.this,MainActivity.class);
        startActivity(k);
    }

    private void InitializeIDs() {
        email= findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);
        createAccount = findViewById(R.id.register_button);
        alreadyHaveAccount = findViewById(R.id.already_have_an_account2);


    }


}