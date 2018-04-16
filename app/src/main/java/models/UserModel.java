package models;

public class UserModel {

    private String user_UID;
    private String name;
    private String profile_pic_URL;

    public String getUser_UID() {
        return user_UID;
    }

    public void setUser_UID(String user_UID) {
        this.user_UID = user_UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_pic_URL() {
        return profile_pic_URL;
    }

    public void setProfile_pic_URL(String profile_pic_URL) {
        this.profile_pic_URL = profile_pic_URL;
    }
}
