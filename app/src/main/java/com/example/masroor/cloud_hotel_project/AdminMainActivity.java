package com.example.masroor.cloud_hotel_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AdminMainActivity extends AppCompatActivity {

    Button btn_add_room,btn_manage_rooms,btn_manage_users,btn_manage_reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        initializeButtonViews();

        btn_add_room.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //launch add room activity
                Intent intent=new Intent(getApplicationContext(),AddNewRoomActivity.class);
                startActivity(intent);
            }
        });

        btn_manage_rooms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //launch manage rooms activity
                Intent intent=new Intent(getApplicationContext(),ManageRoomsActivity.class);
                startActivity(intent);
            }
        });

        btn_manage_users.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //launch manage_users activity
            }
        });

        btn_manage_reviews.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //launch manage_reviews ativity
            }
        });


    }

    public void initializeButtonViews(){
        btn_add_room=findViewById(R.id.button_add_room);
        btn_manage_rooms=findViewById(R.id.button_manage_rooms);
        btn_manage_users=findViewById(R.id.button_manage_users);
        btn_manage_reviews=findViewById(R.id.button_manage_reviews);
    }

}
