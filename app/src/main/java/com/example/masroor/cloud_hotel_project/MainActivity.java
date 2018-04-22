package com.example.masroor.cloud_hotel_project;


import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    final int RC_FBUI=1;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button button;

    //for saving the user's uid in the Users node in db
    DatabaseReference dbRef_Users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);
        firebaseAuth=FirebaseAuth.getInstance();

        dbRef_Users= FirebaseDatabase.getInstance().getReference(DbReferencesStrings.USERS_ROOT);

//        authStateListener=new FirebaseAuth.AuthStateListener(){
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth fbAuth) {
//                     if(fbAuth.getCurrentUser()!=null){
//                         //signed in
//                         //check who is signed in admin or user
//                         //then
//                         //launch appropriate activity
//                         Toast.makeText(getApplicationContext(),"Signed in!",Toast.LENGTH_SHORT).show();
//                         firebaseUser=firebaseAuth.getCurrentUser();
//                         launchSignedInActivity();
//                     }else{
//                         //not signed in
//                         button.setOnClickListener(new View.OnClickListener(){
//                             @Override
//                             public void onClick(View v) {
//                                 Toast.makeText(getApplicationContext(),"NOT SIGNED IN!",Toast.LENGTH_SHORT).show();
//                                 launchFirebaseUIFlow();
//                             }
//                         });
//
//                     }
//            }
//        };

//        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser==null){
            //not signed in
            Log.i("Authentication","not signed in");
            Log.i("Authentication","launching firebaseui");

            launchFirebaseUIFlow();
        }
        if(firebaseUser!=null){
            //signed in
            launchSignedInActivity();
        }
    }

    public void launchFirebaseUIFlow(){
        startActivityForResult(
                AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.FacebookBuilder().build()))
                .build(),
                RC_FBUI
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_FBUI){
            if(resultCode==RESULT_OK){
                Toast.makeText(getApplicationContext(),"Signed in!",Toast.LENGTH_SHORT).show();
                firebaseUser=firebaseAuth.getCurrentUser();
                launchSignedInActivity();
            }
            else if(resultCode==RESULT_CANCELED){
                Log.i("Authentication","Sign in cancelled");
            }

        }
    }

    public void launchSignedInActivity(){
        //decide which activity to launch
        //admin's
        //or
        //user's activity

        if(firebaseUser.getUid().equals("277zxpzpF0YyHtILZbS9EhFnJaO2")){
            Toast.makeText(this,"Attempting to launch admin activity!",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(this,AdminMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else{
            // check if this user is stored in db
            // if not, store it
            // and launch user main activity
            // launch simple user activity
            ValueEventListener vel=new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        //means this user's user_uid does not exist in Users node of db
                        //so
                        //add this user to db at path
                        // -Users
                        //      -user_uid
                        String current_user_name=firebaseUser.getDisplayName();
                        String current_user_uid=firebaseUser.getUid();

                        Log.i("user name:",current_user_name);

                        HashMap<String,Object> user_details=new HashMap<>();
                        user_details.put(DbReferencesStrings.USER_UID,current_user_uid);
                        user_details.put(DbReferencesStrings.USER_NAME,current_user_name);

                        dbRef_Users.child(current_user_uid).setValue(user_details);
                        Log.i("user uid in main:",current_user_uid);
                        Toast.makeText(getApplicationContext(),
                                "Successfully added user details to db.",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            dbRef_Users.addListenerForSingleValueEvent(vel);

            //now launch user main activity
            Toast.makeText(this,"Attempting to launch simple user activity!",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,UserMainActivity.class);
            intent.putExtra(DbReferencesStrings.USER_UID,firebaseUser.getUid());
            intent.putExtra(DbReferencesStrings.USER_NAME,firebaseUser.getDisplayName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}