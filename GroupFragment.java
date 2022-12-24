package com.example.whatsapp;

import static com.google.firebase.database.core.RepoManager.clear;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GroupFragment extends Fragment {
    private  View view;
    private ListView list_view;
    private ArrayAdapter arrayadapter;
    private ArrayList<String>arrayList = new ArrayList<>();
    private DatabaseReference mRef;
    private String currentChatter;


    public GroupFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_group, container, false);
        arrayadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrayList);
        list_view = view.findViewById(R.id.list_view);
        list_view.setAdapter(arrayadapter);



        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                currentChatter = adapterView.getItemAtPosition(position).toString();
                Intent openGroupChat = new Intent (getContext(),GroupChatActivity.class);
                openGroupChat.putExtra("GroupName", currentChatter);
                startActivity(openGroupChat);
            }
        });

        mRef = FirebaseDatabase.getInstance().getReference();
        ShowGroup();



        return view;


}

    private void ShowGroup() {
        mRef.child("GroupName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Set<String> set = new HashSet<String>();
                Iterator iterator = snapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                arrayList.clear();
                arrayList.addAll(set);
                arrayadapter.notifyDataSetChanged();



            }





            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}