package models;

public class UserModel {

    private String user_UID;
    private String user_name;
    private String profile_pic_URL;

    public String getUser_UID() {
        return user_UID;
    }

    public void setUser_UID(String user_UID) {
        this.user_UID = user_UID;
    }

    public UserModel(String user_UID, String user_name, String profile_pic_URL) {
        this.user_UID = user_UID;
        this.user_name = user_name;
        this.profile_pic_URL = profile_pic_URL;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProfile_pic_URL() {
        return profile_pic_URL;
    }

    public void setProfile_pic_URL(String profile_pic_URL) {
        this.profile_pic_URL = profile_pic_URL;
    }
}
