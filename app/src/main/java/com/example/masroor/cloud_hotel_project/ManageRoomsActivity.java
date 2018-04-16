package com.example.masroor.cloud_hotel_project;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


import java.util.ArrayList;

import models.RoomModel;
import viewholders.ManageRoomViewHolder;

public class ManageRoomsActivity extends AppCompatActivity {

    RecyclerView rv;

    private DatabaseReference dbRef_Rooms;
    private ChildEventListener roomsChildEventListener;
    private ArrayList<RoomModel> rooms_list;
    private FirebaseRecyclerAdapter<RoomModel, ManageRoomViewHolder> adapter;


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_rooms);

        rv=findViewById(R.id.recyclerView);
        dbRef_Rooms=FirebaseDatabase.getInstance().getReference("Rooms");
        rooms_list=new ArrayList<>();

        roomsChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RoomModel room=dataSnapshot.getValue(RoomModel.class);
                Log.i("Room_number",room.getRoom_number());
                rooms_list.add(room);
//                Toast.makeText(getApplicationContext(),"added room",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        Query query = FirebaseDatabase.getInstance()
                .getReference("Rooms");

        query.addChildEventListener(roomsChildEventListener);

        FirebaseRecyclerOptions<RoomModel> options =
                new FirebaseRecyclerOptions.Builder<RoomModel>()
                        .setQuery(query, RoomModel.class)
                        .build();


        adapter=new FirebaseRecyclerAdapter<RoomModel, ManageRoomViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ManageRoomViewHolder vh, int position, @NonNull RoomModel model) {
                vh.populateManageRoomRow(model.getRoom_number(),
                        model.getRoom_price(),
                        model.isRoom_available());
                Log.i("populating: ",model.getRoom_number());
            }

            @NonNull
            @Override
            public ManageRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_manage_room,parent,false);
                return new ManageRoomViewHolder(view);
            }

            @Override
            public int getItemCount() {
                return rooms_list.size();
            }
        };

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
//        rv.setAdapter(adapter);
//        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}
