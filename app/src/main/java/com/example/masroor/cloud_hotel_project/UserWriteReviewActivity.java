package com.example.masroor.cloud_hotel_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class UserWriteReviewActivity extends AppCompatActivity {

    String current_user_name,current_user_uid,reviewComment;
    int rating=4;

    RatingBar ratingBar;
    EditText editTextComments;
    Button btnSubmitReview;

    //Firebase related
    DatabaseReference dbRef_reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_write_review);

        current_user_uid= Objects.requireNonNull(
                FirebaseAuth.getInstance().getCurrentUser()).getUid();
        current_user_name= Objects.requireNonNull(
                FirebaseAuth.getInstance().getCurrentUser()).getDisplayName();

        ratingBar=findViewById(R.id.ratingBar);
        editTextComments=findViewById(R.id.editText_comments);
        btnSubmitReview=findViewById(R.id.button_submit_review);

        dbRef_reviews= FirebaseDatabase.getInstance().getReference().child(DbReferencesStrings.REVIEWS_ROOT);

        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateReview()){

                    //prepare review data for writing into database
                    HashMap<String,Object> map=new HashMap<>();
                    map.put(DbReferencesStrings.REVIEWER_UID,current_user_uid);
                    map.put(DbReferencesStrings.REVIEWER_NAME,current_user_name);
                    map.put(DbReferencesStrings.REVIEW_RATING,rating);
                    map.put(DbReferencesStrings.REVIEW_COMMENT,reviewComment);

                    //write the review to the database
                    dbRef_reviews
                            .child(current_user_uid)
                            .setValue(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Review Added!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please write a comment first",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean validateReview(){
        if(!TextUtils.isEmpty(editTextComments.getText())){
            reviewComment=editTextComments.getText().toString();
            rating=(int)ratingBar.getRating();
            return true;
        }
        return false;
    }
}
