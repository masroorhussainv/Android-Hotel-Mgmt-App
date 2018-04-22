package com.example.masroor.cloud_hotel_project;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import models.ReviewModel;
import models.RoomModel;
import viewholders.ReviewViewHolder;

public class AdminManageReviews extends AppCompatActivity {

    RecyclerView recyclerView;

    //firebase
    final private DatabaseReference dbRef_Reviews= FirebaseDatabase.getInstance().getReference()
            .child(DbReferencesStrings.REVIEWS_ROOT);

    private FirebaseRecyclerAdapter<ReviewModel, ReviewViewHolder> adapter;
    private ChildEventListener reviewsChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_reviews);

        recyclerView=findViewById(R.id.recyclerview_reviews);

        reviewsChildEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
        Query query=FirebaseDatabase.getInstance()
                .getReference(DbReferencesStrings.REVIEWS_ROOT);
        query.addChildEventListener(reviewsChildEventListener);

        FirebaseRecyclerOptions<ReviewModel> options =
                new FirebaseRecyclerOptions.Builder<ReviewModel>()
                        .setQuery(query, ReviewModel.class)
                        .build();

        adapter=new FirebaseRecyclerAdapter<ReviewModel, ReviewViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ReviewViewHolder holder, int position, @NonNull final ReviewModel model) {

                holder.populateReviewViewHolder(
                        model.getReviewer_name(),
                        model.getRating(),
                        model.getComment());
                Log.i("uid",""+model.getReviewer_uid());

                holder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //delete this comment
                        //make reference to this comment

                        //at this point viewholder will have been created
                        //and
                        //onBindViewHolder will set the current_user_uid
                        //lets see whether the button event fires before
                        //current_user_uid
                        //is set or not
                        Log.i("uid",""+model.getReviewer_uid());

                        DatabaseReference dbRef_comment=
                                FirebaseDatabase.getInstance().getReference()
                                        .child(DbReferencesStrings.REVIEWS_ROOT)
                                        .child(model.getReviewer_uid())
                                        .child(DbReferencesStrings.REVIEW_COMMENT);

                        dbRef_comment.removeValue();
                        Snackbar snackbar=Snackbar
                                .make(v.getRootView(),"Comment Deleted Successfully.",Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });

            }

            @NonNull
            @Override
            public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_review,parent,false);

                //add listener for "delete comment"
//                Button btn_delete=view.findViewById(R.id.button_delete_comment);
//
//                btn_delete.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //delete this comment
//                                //make reference to this comment
//                                String current_user_uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                DatabaseReference dbRef_comment=dbRef_Reviews
//                                        .child(current_user_uid)
//                                        .child(DbReferencesStrings.REVIEW_COMMENT);
//                                dbRef_comment.removeValue();
//                                Snackbar snackbar=Snackbar
//                                        .make(v.getRootView(),"Comment Deleted Successfully.",Snackbar.LENGTH_SHORT);
//                                snackbar.show();
//                            }
//                        });

                return new ReviewViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
