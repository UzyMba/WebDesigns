package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private TextView user1, user2;
    private EditText editText;
    private ImageButton imageButton;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private String currentGroup, currentUserID, currentUserName, currentDate, currentTime, chat;
    private DatabaseReference mRef, groupNameRef, GroupMessageKeyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        currentGroup = getIntent().getExtras().get("GroupName").toString();
        Toast.makeText(this, currentGroup, Toast.LENGTH_SHORT).show();


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        mRef = FirebaseDatabase.getInstance().getReference().child("User");
        groupNameRef = FirebaseDatabase.getInstance().getReference().child("GroupName");

        InitializeFields();
        GetUserInfo();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendMessageInfoToDatabase();

                editText.setText("");
                getSupportActionBar().setTitle(currentGroup);


            }
        });










        groupNameRef.child(currentGroup).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    DisplayMessages(snapshot);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DisplayMessages(DataSnapshot snapshot) {
        Iterator iterator = snapshot.getChildren().iterator();
        while (iterator.hasNext()){
            String chatDate = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatName = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String) ((DataSnapshot)iterator.next()).getValue();
            user1.append(chatName + "\n" + chatMessage + "\n" + chatDate + "   " + chatTime + "\n\n\n");
        }
    }

    private void SendMessageInfoToDatabase() {
        chat = editText.getText().toString();
        String messageKey = groupNameRef.push().getKey();

        if(TextUtils.isEmpty(chat)){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }else{
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM DD, YYYY");
            currentDate = currentDateFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm a");
            currentTime = currentTimeFormat.format(calForTime.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            groupNameRef.updateChildren(groupMessageKey);

            GroupMessageKeyRef = groupNameRef.child(messageKey);

            HashMap<String, Object> messageInfo = new HashMap<>();
            messageInfo.put("name", currentGroup);
            messageInfo.put("Message", chat);
            messageInfo.put("Date", currentDate);
            messageInfo.put("Time", currentTime);
            GroupMessageKeyRef.updateChildren(messageInfo);
        }
    }

    private void InitializeFields() {

        mToolbar = findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Group Info");

        scrollView = findViewById(R.id.my_scroll_view);
        user1 = findViewById(R.id.user1_chat);
        user2 = findViewById(R.id.user2_chat);
        editText = findViewById(R.id.input_chat);
        imageButton = findViewById(R.id.send_icon);

    }
    public void GetUserInfo(){
        mRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    currentUserName = snapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}