package com.example.masroor.cloud_hotel_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
import viewholders.ManageRoomViewHolder;

public class ManageRoomsActivity extends AppCompatActivity {

    public static final String KEYS = "KEYS";
    public static final String ROOM_NUMBER = "room_number";
    public static final String ROOM_PRICE = "room_price";
    public static final String AVAILABLE = "available";
    RecyclerView rv;

    private DatabaseReference dbRef_Rooms;
    private ChildEventListener roomsChildEventListener;
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
        dbRef_Rooms=FirebaseDatabase.getInstance().getReference(DbReferencesStrings.ROOMS_ROOT);

        roomsChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RoomModel room=dataSnapshot.getValue(RoomModel.class);
                assert room != null;
                Log.i("Room_number",room.getRoom_number());
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


                //put data into viewholder object

                HashMap<String, Object> map = model.getPicture_urls();
                String url=null;
                for (Map.Entry<String, Object> entry : map.entrySet())
                {
                    url=(String)entry.getValue();
                    break;
                }

                if(url!=null) {
                    vh.populateManageRoomRow(
                            url,
                            model.getRoom_number(),
                            model.getRoom_price(),
                            model.getRoom_capacity(),
                            model.isRoom_available()
                    );
                }

                Log.i("populating: ",model.getRoom_number());
            }
            @NonNull
            @Override
            public ManageRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_manage_room,parent,false);
                //attach a click event listener on the manage button
                Button button_manage=view.findViewById(R.id.button_manage_room);
                button_manage.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //launch the 'manage single room' activity
                        Intent in=new Intent(getApplicationContext(),ManageSingleRoomActivity.class);

                        String room_number=((TextView)view.findViewById(R.id.textview_room_number)).getText().toString();
                        String available_str=((TextView)view.findViewById(R.id.textview_room_available)).getText().toString();
                        int room_price=Integer.parseInt(((TextView)view.findViewById(R.id.textview_room_price)).getText().toString());

                        boolean available;
                        if(available_str.equalsIgnoreCase("yes")){
                            available=true;
                        }else
                            available=false;

                        in.putExtra(ROOM_NUMBER,room_number);
                        in.putExtra(ROOM_PRICE,room_price);
                        in.putExtra(AVAILABLE,available);
                        startActivity(in);
                    }
                });

                return new ManageRoomViewHolder(view);
            }
        };

        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }
}