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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {


    private FirebaseAuth nAuth;
    private EditText eUser;
    private EditText ePass;
    private Button eLogin;
    private ProgressDialog progressDialog1;
    private TextView forgotPassword;
    private TextView register;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nAuth = FirebaseAuth.getInstance();
        eUser = findViewById(R.id.login_email);
        ePass = findViewById(R.id.login_password);
        eLogin = findViewById(R.id.login_button);
        forgotPassword=findViewById(R.id.forgotPassword);
        register = findViewById(R.id.do_not_have_an_account);


        progressDialog1 = new ProgressDialog(this);
        progressDialog1.setTitle("Logging in");
        progressDialog1.setMessage("Please wait");
        
        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginToActivity();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToRegister();
            }
        });

    }

    private void loginToActivity() {
        String email1 = eUser.getText().toString();
        String pass1 = ePass.getText().toString();
        if(TextUtils.isEmpty(email1)){
            eUser.setError("Enter your registered email");
        }if(TextUtils.isEmpty(pass1)){
            ePass.setError("Enter your registered password");
        }else{
            progressDialog1.show();
            nAuth.signInWithEmailAndPassword(email1,pass1)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendUserToMainActivity();
                                Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                progressDialog1.dismiss();


                            }else{
                                Toast.makeText(Login.this, "Enter valid login details", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void sendUserToMainActivity(){
        Intent j = new Intent(Login.this,MainActivity.class);
        j.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(j);

    }
    public void sendUserToRegister(){
        Intent k = new Intent(Login.this,Register.class);
        k.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(k);

    }

}