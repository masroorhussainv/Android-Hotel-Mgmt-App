package viewholders;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.masroor.cloud_hotel_project.DbReferencesStrings;
import com.example.masroor.cloud_hotel_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewViewHolder extends RecyclerView.ViewHolder {

    private TextView textview_reviewer_name,textview_comment;
    private RatingBar ratingBar;

//    final private DatabaseReference dbRef_Reviews= FirebaseDatabase.getInstance().getReference()
//            .child(DbReferencesStrings.REVIEWS_ROOT);
//
//    private String current_user_uid;

    public Button getDeleteButton(){
        return (Button)itemView.findViewById(R.id.button_delete_comment);
    }

    public ReviewViewHolder(View itemView) {
        super(itemView);
        textview_reviewer_name=itemView.findViewById(R.id.textview_reviewer_name);
        textview_comment=itemView.findViewById(R.id.textview_comment);
        ratingBar=itemView.findViewById(R.id.ratingBar);

        //get the button "delete comment"'s reference

//
//        btn_delete_comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //delete this comment
//                //make reference to this comment
//
//                //at this point viewholder will have been created
//                //and
//                //onBindViewHolder will set the current_user_uid
//                //lets see whether the button event fires before
//                //current_user_uid
//                //is set or not
//                DatabaseReference dbRef_comment=
//                        dbRef_Reviews
//                            .child(current_user_uid)
//                            .child(DbReferencesStrings.REVIEW_COMMENT);
//
//                dbRef_comment.removeValue();
//                Snackbar snackbar=Snackbar
//                        .make(v.getRootView(),"Comment Deleted Successfully.",Snackbar.LENGTH_SHORT);
//                snackbar.show();
//            }
//        });
    }

    public void populateReviewViewHolder(String name,int rating,String comment){
//        current_user_uid=uid;
        textview_reviewer_name.setText(name);
        ratingBar.setRating(rating);
        textview_comment.setText(comment);
    }
}
