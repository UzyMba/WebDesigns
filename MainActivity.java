package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabsAccessorAdapter myTabsAccessorAdapter;
    private FirebaseUser currentUser;
    private FirebaseAuth oAuth;
    private DatabaseReference mRef;
    private FirebaseDatabase firebaseDatabase;
    private String groupName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        oAuth = FirebaseAuth.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();
        currentUser = oAuth.getCurrentUser();







        mToolbar=findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("ChatUp");


        myViewPager=findViewById(R.id.main_tabs_pager);
        myTabLayout=findViewById(R.id.main_tabs);
        myTabsAccessorAdapter = new TabsAccessorAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(myTabsAccessorAdapter);

        myTabLayout.setupWithViewPager(myViewPager);




    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser ==null){
            sendToLoginActivity();
        }else{
            verifyUserExistence();
        }

    }


    private void verifyUserExistence() {
        String currentUserID = currentUser.getUid();

        mRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("name").exists()){
                    Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                }else {
                    sendToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendToSettingsActivity() {
        Intent n = new Intent(MainActivity.this,SettingsActivity2.class);
        n.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(n);
        finish();
    }

    private void returnToLogin() {
        Intent i = new Intent(MainActivity.this,Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.main_create_group_option){

            RequestGroup();
        }
        if(item.getItemId() == R.id.main_logout_option){

            sendToLoginActivity();
        }
        if(item.getItemId() == R.id.main_update_profile){
            goToProfileSettings();
        }
        if(item.getItemId() == R.id.main_set_profilePicture){
            goToProfileSettings();
        }

        return true;
    }

    private void sendToLoginActivity() {
        Intent n = new Intent(MainActivity.this, SettingsActivity2.class);
        n.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(n);
    }

    private void RequestGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this,R.style.AlertDialog);
        builder.setTitle("Enter Group Name");
        final EditText enterGroupName = new EditText(MainActivity.this);
        enterGroupName.setHint("eg ChatUp");
        builder.setView(enterGroupName);


        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                groupName = enterGroupName.getText().toString();
                if(TextUtils.isEmpty(groupName)){
                    Toast.makeText(MainActivity.this, "Enter group name", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Created successfully", Toast.LENGTH_SHORT).show();
                    GroupName(groupName);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void GroupName(String groupName) {
        mRef.child("GroupName").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, groupName  + "is successfully created", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Group not created", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void goToProfileSettings() {
        Intent m = new Intent(MainActivity.this, SettingsActivity2.class);
        startActivity(m);
    }

}