package com.example.masroor.cloud_hotel_project;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import models.RoomModel;
import viewholders.BookRoomViewHolder;
import viewholders.ManageRoomViewHolder;

public class UserBookRoom extends AppCompatActivity {

    RecyclerView recyclerview;
    //firebase
    final DatabaseReference dbRef_Rooms=FirebaseDatabase.getInstance()
            .getReference(DbReferencesStrings.ROOMS_ROOT);
    ChildEventListener roomsChildEventListener;
    FirebaseRecyclerAdapter<RoomModel, BookRoomViewHolder> adapter;

    final DatabaseReference dbRef_Bookings=FirebaseDatabase.getInstance()
            .getReference(DbReferencesStrings.ROOM_BOOKINGS_ROOT);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_room);

        recyclerview=findViewById(R.id.recyclerview_book_room);

        roomsChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                RoomModel room=dataSnapshot.getValue(RoomModel.class);

                if(room==null){
                    Toast.makeText(getApplicationContext(),"room model null",Toast.LENGTH_SHORT).show();
                }

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

        Toast.makeText(getApplicationContext(),"constructing",Toast.LENGTH_SHORT).show();

        FirebaseRecyclerOptions<RoomModel> options =
                new FirebaseRecyclerOptions.Builder<RoomModel>()
                        .setQuery(query, RoomModel.class)
                        .build();

            adapter=new FirebaseRecyclerAdapter<RoomModel, BookRoomViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull BookRoomViewHolder holder, int position, @NonNull RoomModel model) {

                Toast.makeText(getApplicationContext(),""+
                                model.getRoom_number()+
                                model.getRoom_price()+
                                model.getRoom_capacity()+
                                model.isRoom_available(),
                                Toast.LENGTH_LONG);

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
                            model.getRoom_capacity(),
                            model.isRoom_available()
                    );

                    Toast.makeText(getApplicationContext(),"Populating "+model.getRoom_number(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Error in getting image url", Toast.LENGTH_SHORT).show();
                }

            }

            @NonNull
            @Override
            public BookRoomViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
                final View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_book_room,parent,false);
                Toast.makeText(getApplicationContext(),"Creating view holder",Toast.LENGTH_SHORT).show();

                //add book a room logic here to the button
                v.findViewById(R.id.button_manage_room)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String snackbar_msg;

                                //firstly
                                // check whether the room is available or not

                                String avail=((TextView)v.findViewById(R.id.textview_room_available)).getText().toString();

                                if(avail.equalsIgnoreCase("yes")){
                                    //this room can be booked

                                    //database    structure is like
                                    //-Bookings
                                    //      -booked_by_uid
                                    //      -booked_by_name
                                    //get uid and name of the current user

                                    String roomN=((TextView)
                                            v.findViewById(R.id.textview_room_number)).getText().toString();
                                    String current_user_uid=
                                            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                    String current_user_name=
                                            Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

                                    HashMap<String,Object> map=new HashMap<>();
                                    map.put(DbReferencesStrings.BOOKED_BY_UID,current_user_uid);
                                    map.put(DbReferencesStrings.BOOKED_BY_NAME,current_user_name);

                                    //put the booking record in the database
                                    //and

                                    dbRef_Bookings
                                            .child(roomN)
                                            .setValue(map);

                                    //update the room object's availability
                                    dbRef_Rooms
                                            .child(roomN)
                                            .child(DbReferencesStrings.ROOM_AVAILABLE)
                                            .setValue(false);

                                    snackbar_msg="You've successfully booked room number: "+roomN;
                                }
                                else{
                                    //room is already booked
                                    //this booking cannot be done
                                    snackbar_msg="This room cannot be booked!";

//                                    Toast.makeText(getApplicationContext(),
//                                            "This room is already booked!",Toast.LENGTH_SHORT).show();
                                }

                                Snackbar snackbar=Snackbar
                                        .make(parent,snackbar_msg,Snackbar.LENGTH_SHORT);
                                // get snackbar view
                                View snackbarView = snackbar.getView();
                                // change snackbar text color
                                int snackbarTextId = android.support.design.R.id.snackbar_text;
                                TextView textView = snackbarView.findViewById(snackbarTextId);
                                textView.setTextColor(getResources().getColor(R.color.yellow));
                                snackbar.show();
                            }
                        });
                return new BookRoomViewHolder(v);
            }
        };
        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL,false));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
