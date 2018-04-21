package com.example.masroor.cloud_hotel_project;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserMainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int PROFILE_IMAGE_PICKER_REQUEST_CODE = 123;
    public static final String BUTTON_PRESS = "button press";

    String current_user_name,current_user_uid;

    ImageView imageViewProfilePicture;
    TextView textViewUsername;
    Button btnUpdateProfilePicture,btnBookARoom,btnWriteAReview,btnLogout;
    Uri profile_picture_uri;

    FirebaseUser firebaseUser;
    DatabaseReference dbRef_profile_picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        imageViewProfilePicture=findViewById(R.id.imageview_profile_picture);
        textViewUsername=findViewById(R.id.textview_username);
        btnUpdateProfilePicture=findViewById(R.id.button_update_profile_picture);
        btnBookARoom=findViewById(R.id.button_book_room);
        btnWriteAReview=findViewById(R.id.button_write_review);
        btnLogout=findViewById(R.id.button_logout);

        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        assert bundle != null;
        current_user_uid=bundle.getString(DbReferencesStrings.USER_UID);
        current_user_name=bundle.getString(DbReferencesStrings.USER_NAME);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        dbRef_profile_picture=FirebaseDatabase.getInstance().getReference()
                .child(DbReferencesStrings.USERS_ROOT)
                .child(current_user_uid)
                .child(DbReferencesStrings.USER_PROFILE_PICTURE_URL);


        btnUpdateProfilePicture.setOnClickListener(this);
        btnBookARoom.setOnClickListener(this);
        btnWriteAReview.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        dbRef_profile_picture.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String url=dataSnapshot.child(DbReferencesStrings.USER_PROFILE_PICTURE_URL).getValue(String.class);

                String url=dataSnapshot.getValue(String.class);
                if(url!=null){
                    Picasso.get().load(url).into(imageViewProfilePicture);
                }
                else{
                    //set a default placeholder image for profile_picture
                    Picasso.get().load(R.drawable.ic_account_black_48dp);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        textViewUsername.setText(current_user_name);

    }

    @Override
    public void onClick(View v) {
        int pressed_button_id=v.getId();
        switch (pressed_button_id){
            case R.id.button_update_profile_picture:{
                Log.i(BUTTON_PRESS,"Update profile picture button pressed");
                launchImagePicker();
            }break;

            case R.id.button_book_room:{
                Intent i=new Intent(this,UserBookRoom.class);
                startActivity(i);
            }break;

            case R.id.button_write_review:{
                Intent intent=new Intent(this,UserWriteReviewActivity.class);
                startActivity(intent);

            }break;

            case R.id.button_logout:{
                logOut();
            }break;
        }
    }

    public void launchImagePicker(){
        Intent pickerIntent=new Intent(Intent.ACTION_PICK);
        pickerIntent.setType("image/*");
        startActivityForResult(pickerIntent, PROFILE_IMAGE_PICKER_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent receivedIntent) {
        super.onActivityResult(requestCode, resultCode, receivedIntent);

        switch (requestCode) {
            case PROFILE_IMAGE_PICKER_REQUEST_CODE: {
                if (resultCode == Activity.RESULT_OK) {
                    //get image uri
                    profile_picture_uri = receivedIntent.getData();
                //upload the profile picture to storage and push its url to profile_picture_url
                StorageReference user_profile_pictures_storage = FirebaseStorage.getInstance()
                        .getReference(DbReferencesStrings.STORAGE_USER_PROFILE_PICTURES);

                user_profile_pictures_storage.putFile(profile_picture_uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Toast.makeText(getApplicationContext(),
                                        "Uploaded profile picture to storage", Toast.LENGTH_SHORT).show();

                                //get its download url
                                String profile_picture_url = taskSnapshot.getDownloadUrl().toString();
                                DatabaseReference dbRef_profile_picture;
                                dbRef_profile_picture = FirebaseDatabase.getInstance().getReference()
                                        .child(DbReferencesStrings.USERS_ROOT)
                                        .child(current_user_uid).child(DbReferencesStrings.USER_PROFILE_PICTURE_URL);
                                Log.i("Current user uid:", current_user_uid);
                                //push the url to db
                                dbRef_profile_picture.setValue(profile_picture_url)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(),
                                                        "Uploaded profile picture's url stored in db!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                 }
            }
            break;
        }

    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
