package com.example.masroor.cloud_hotel_project;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    final int RC_FBUI=1;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser firebaseUser;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);
        firebaseAuth=FirebaseAuth.getInstance();

        authStateListener=new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth fbAuth) {
                     if(fbAuth.getCurrentUser()!=null){
                         //signed in
                         //check who is signed in admin or user
                         //then
                         //launch appropriate activity
                         Toast.makeText(getApplicationContext(),"Signed in!",Toast.LENGTH_SHORT).show();
                         firebaseUser=firebaseAuth.getCurrentUser();
                         launchSignedInActivity();
                     }else{
                         //not signed in
                         button.setOnClickListener(new View.OnClickListener(){
                             @Override
                             public void onClick(View v) {
                                 Toast.makeText(getApplicationContext(),"NOT SIGNED IN!",Toast.LENGTH_SHORT).show();
                                 launchFirebaseUIFlow();
                             }
                         });
                     }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    public void launchFirebaseUIFlow(){
        startActivityForResult(
                AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build()))
                .build(),
                RC_FBUI
        );
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
            //launch simple user activity
            Toast.makeText(this,"Attempting to launch simple user activity!",Toast.LENGTH_SHORT).show();
        }
    }
}
