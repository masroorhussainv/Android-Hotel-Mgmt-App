package com.example.masroor.cloud_hotel_project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import models.RoomModel;

public class AddNewRoomActivity extends AppCompatActivity {

    public static final String ROOM_PICTURE_URLS = "room_picture_urls";
    final int RC_PICK_PHOTO=2;

    ImageView imageView;
    EditText editText_room_num,editText_room_price,editText_room_capacity;
    Button btn_add_room;
    Uri imageUri;
    boolean uri_updated;

    private String uploaded_image_URL;

    //firebase related
    FirebaseStorage firebaseStorage;
    StorageReference storageRef_RoomPictures;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef_Rooms;  //insert a room object into this one
    DatabaseReference dbRef_Room_Picture_Urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_room);

        referViewElements();

        firebaseStorage=FirebaseStorage.getInstance();
        storageRef_RoomPictures=firebaseStorage.getReference("room_pictures");
        firebaseDatabase=FirebaseDatabase.getInstance();

       Picasso.get().load(R.drawable.image_plus).into(imageView);

        //image picker code
        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent pickerIntent=new Intent(Intent.ACTION_PICK);
                pickerIntent.setType("image/*");
                startActivityForResult(pickerIntent,RC_PICK_PHOTO);
            }
        });

        //add room button code
        btn_add_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    //input is valid
                    //upload the picked photo
                    addNewRoom();   //this will upload the picked photo and have the URL of the uploaded photo
                }
            }
        });
    }

    public  void referViewElements(){
        imageView=findViewById(R.id.imageView);
        editText_room_num=findViewById(R.id.editText_room_num);
        editText_room_price=findViewById(R.id.editText_room_price);
        editText_room_capacity=findViewById(R.id.editText_room_capacity);
        btn_add_room=findViewById(R.id.button_add_room);
    }


    public boolean validateInput(){
        if(TextUtils.isEmpty(editText_room_num.getText())){
            editText_room_num.setError("Enter Room Number");
            return false;
        }else{
            editText_room_num.setError(null);
        }
        if(TextUtils.isEmpty(editText_room_price.getText())){
            editText_room_price.setError("Enter Room Price");
            return false;
        }else{
            editText_room_price.setError(null);
        }
        if(TextUtils.isEmpty(editText_room_capacity.getText())){
            editText_room_capacity.setError("Enter Room Capacity");
            return false;
        }else{
            editText_room_price.setError(null);
        }
        if(!uri_updated){
            Toast.makeText(this,"Please choose an image for New Room!",Toast.LENGTH_SHORT).show();
            return false;
        }
        uri_updated=true;
        return true;
    }

    public void addNewRoom(){
        Toast.makeText(getApplicationContext(),"Uploading",Toast.LENGTH_SHORT).show();
        uploadRoomPicture();
    }

    public void uploadRoomData(){
        //get data from edit text fields
        String roomNumber = editText_room_num.getText().toString();
        int roomPrice = Integer.parseInt(editText_room_price.getText().toString());
        int roomCapacity=Integer.parseInt(editText_room_capacity.getText().toString());

        RoomModel room=new RoomModel(roomNumber,roomPrice,true,roomCapacity,null,null);

        dbRef_Rooms=firebaseDatabase.getReference("Rooms");
        dbRef_Rooms.child(roomNumber).setValue(room);

        dbRef_Room_Picture_Urls=firebaseDatabase.getReference("Rooms/"+roomNumber+"/picture_urls/");
        dbRef_Room_Picture_Urls.push().setValue(uploaded_image_URL);
    }


    public void uploadRoomPicture(){
        String rand_string=UUID.randomUUID().toString();

        String filename=rand_string+"_"+imageUri.getLastPathSegment();
        StorageReference newRef=storageRef_RoomPictures.child(filename);
        newRef.putFile(imageUri)
                //adding success listeners
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Image Upload successful!",Toast.LENGTH_LONG)
                                .show();
                        uploaded_image_URL=taskSnapshot.getDownloadUrl().toString();
//                        Toast.makeText(getApplicationContext(),uploaded_image_URL,Toast.LENGTH_SHORT).show();
                        uploadRoomData();
                        finish();
                    }
                })
                //adding failure listener
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Image Upload failed!",Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent receivedIntent) {
        super.onActivityResult(requestCode, resultCode, receivedIntent);

        switch (requestCode){
            case RC_PICK_PHOTO:{
                if(resultCode==Activity.RESULT_OK){
                    //get image uri
                    imageUri=receivedIntent.getData();
                    Picasso.get().load(imageUri).into(imageView);
                    uri_updated=true;
                }
            }
            break;
        }
    }
}