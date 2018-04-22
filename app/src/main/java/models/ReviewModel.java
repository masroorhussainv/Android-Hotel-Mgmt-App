package models;

public class ReviewModel {
    private int rating;
    private String comment;
    private String reviewer_uid;
    private String reviewer_name;

    ReviewModel(){

    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReviewer_uid() {
        return reviewer_uid;
    }

    public void setReviewer_uid(String reviewer_uid) {
        this.reviewer_uid = reviewer_uid;
    }

    public String getReviewer_name() {
        return reviewer_name;
    }

    public void setReviewer_name(String reviewer_name) {
        this.reviewer_name = reviewer_name;
    }
}
