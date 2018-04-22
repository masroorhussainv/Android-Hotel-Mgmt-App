package com.example.masroor.cloud_hotel_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ManageSingleRoomActivity extends AppCompatActivity {

    public static final String DB_REF_DEBUG_TAG = "DB-REF";
    EditText editText_room_price,editText_room_capacity;
    RadioGroup radioGroup;
    RadioButton radioButton_yes,radioButton_no;
    Button button_update_room_details,button_delete_room;

    boolean current_availability_status,update_price,update_capacity,update_availability;
    String room_number;

    //firebase related
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef_root,dbRef_price,dbRef_availability,dbRef_capacity;

    //variables for updated data
    int updated_price,updated_capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_single_room);
        editText_room_price=findViewById(R.id.edittext_room_price);
        editText_room_capacity=findViewById(R.id.edittext_room_capacity);
        radioGroup=findViewById(R.id.radiogroup);
        radioButton_yes=findViewById(R.id.radiobutton_yes);
        radioButton_no=findViewById(R.id.radiobutton_no);
        button_update_room_details=findViewById(R.id.button_update_room_details);
        button_delete_room=findViewById(R.id.button_delete_room);

        firebaseDatabase=FirebaseDatabase.getInstance();
        dbRef_root=firebaseDatabase.getReference();

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        room_number=(String)bundle.get(ManageRoomsActivity.ROOM_NUMBER);
        current_availability_status=(boolean)bundle.get(ManageRoomsActivity.AVAILABLE);


        // listener for button 'update room details'
        button_update_room_details.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                validateInput();
                Log.i("LOG_FIREBASE_UPLOAD",update_capacity+"."+update_price+"."+update_availability);
                Log.i(DB_REF_DEBUG_TAG,dbRef_root.toString());

                if(update_capacity){
                    //means we've to update capacity of the room
                    dbRef_capacity=firebaseDatabase.getReference()
                            .child(DbReferencesStrings.ROOMS_ROOT)
                            .child(room_number)
                            .child(DbReferencesStrings.ROOM_CAPACITY);
                    updateRoomCapacity();
                }
                if(update_price){
                    //means we've to update room's price
                    dbRef_price=firebaseDatabase.getReference()
                            .child(DbReferencesStrings.ROOMS_ROOT)
                            .child(room_number)
                            .child(DbReferencesStrings.ROOM_PRICE);
                    updateRoomPrice();
                }
                if(update_availability){
                    //means we're to update availability status
                    dbRef_availability=firebaseDatabase.getReference()
                            .child(DbReferencesStrings.ROOMS_ROOT)
                            .child(room_number)
                            .child(DbReferencesStrings.ROOM_AVAILABLE);
                    updateRoomAvailability();
                }
            }
        });


        //listener for 'Delete Room' button
        button_delete_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef_root
                    .child(DbReferencesStrings.ROOMS_ROOT)
                    .child(room_number)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Room deleted successfully!",Toast.LENGTH_SHORT)
                                    .show();
                            finish();
                        }
                    });
            }
        });
    }


    public void updateRoomCapacity(){
        dbRef_capacity.setValue(updated_capacity).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Capacity updated",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateRoomPrice(){
        dbRef_price.setValue(updated_price).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Price updated",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateRoomAvailability(){
        dbRef_availability.setValue(!current_availability_status).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    //change current status of availability
                    current_availability_status=!current_availability_status;
                    Toast.makeText(getApplicationContext(),"Availability updated",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void validateInput(){
        if(TextUtils.isEmpty(editText_room_capacity.getText())){
            update_capacity=false;
        }else{
            updated_capacity=Integer.parseInt(editText_room_capacity.getText().toString());
            update_capacity=true;
        }
        if(TextUtils.isEmpty(editText_room_price.getText())){
            update_price= false;
        }else{
            updated_price=Integer.parseInt(editText_room_price.getText().toString());
            update_price= true;
        }
        if(radioButton_yes.isChecked() && !current_availability_status){
            update_availability=true;
        }
        else if(radioButton_no.isChecked() && current_availability_status){
            update_availability=true;
        }
    }
}
