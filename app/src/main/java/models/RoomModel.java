package models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class RoomModel {

    private String room_number;
    private int room_price;
    private boolean room_available;
    private String booked_by_uid;
    private String booked_by_name;
    private int room_capacity;
    private HashMap<String,Object> picture_urls;
    public int getRoom_capacity() {
        return room_capacity;
    }

    public void setRoom_capacity(int room_capacity) {
        this.room_capacity = room_capacity;
    }

    public RoomModel(String room_number, int price, boolean room_available,int cap, String booked_by_uid, String booked_by_name) {
        this.room_number = room_number;
        this.room_price = price;
        this.room_available = room_available;
        this.booked_by_uid = booked_by_uid;
        this.booked_by_name = booked_by_name;
        this.room_capacity=cap;
    }

    public RoomModel() {
    }

    public HashMap<String, Object> getPicture_urls() {
        return picture_urls;
    }

    public void setPicture_urls(HashMap<String, Object> picture_urls) {
        this.picture_urls = picture_urls;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public int getRoom_price() {
        return room_price;
    }

    public void setRoom_price(int price) {
        this.room_price = price;
    }

    public boolean isRoom_available() {
        return room_available;
    }

    public void setRoom_available(boolean room_available) {
        this.room_available = room_available;
    }

    public String getBooked_by_uid() {
        return booked_by_uid;
    }

    public void setBooked_by_uid(String booked_by_uid) {
        this.booked_by_uid = booked_by_uid;
    }

    public String getBooked_by_name() {
        return booked_by_name;
    }

    public void setBooked_by_name(String booked_by_name) {
        this.booked_by_name = booked_by_name;
    }


}
