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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import models.RoomModel;
import viewholders.BookRoomViewHolder;
import viewholders.ManageRoomViewHolder;

public class UserBookRoom extends AppCompatActivity {

    RecyclerView recyclerview;
    DatabaseReference dbRef_Rooms;
    ChildEventListener roomsChildEventListener;
    FirebaseRecyclerAdapter<RoomModel, BookRoomViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_room);

        recyclerview=findViewById(R.id.recyclerview_book_room);
        dbRef_Rooms=FirebaseDatabase.getInstance().getReference(DbReferencesStrings.ROOMS_ROOT);

        roomsChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RoomModel room=dataSnapshot.getValue(RoomModel.class);

                Toast.makeText(getApplicationContext(),""+room.getRoom_number(),Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(),"received room data",Toast.LENGTH_SHORT).show();
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

            adapter=new FirebaseRecyclerAdapter<RoomModel, BookRoomViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull BookRoomViewHolder holder, int position, @NonNull RoomModel model) {

                HashMap<String, Object> map = model.getPicture_urls();
                String url=null;
                for (Map.Entry<String, Object> entry : map.entrySet())
                {
                    url=(String)entry.getValue();
                        break;
                }

                if(url!=null){
                    holder.populateBookRoomRow(
                            url,
                            model.getRoom_number(),
                            model.getRoom_price(),
                            model.isRoom_available()
                    );

                    Toast.makeText(getApplicationContext(),"Populating "+model.getRoom_number(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Error in getting image url", Toast.LENGTH_SHORT).show();
                }

            }

            @NonNull
            @Override
            public BookRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_book_room,parent,false);
                Toast.makeText(getApplicationContext(),"Creating view holder",Toast.LENGTH_SHORT).show();
                return new BookRoomViewHolder(v);
            }
        };
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
