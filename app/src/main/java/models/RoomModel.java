package models;

import java.util.ArrayList;

public class RoomModel {

    private String room_number;
    private int room_price;
    private boolean room_available;
    private String booked_by_uid;
    private String booked_by_name;



    public RoomModel(String room_number, int price, boolean room_available, String booked_by_uid, String booked_by_name) {
        this.room_number = room_number;
        this.room_price = price;
        this.room_available = room_available;
        this.booked_by_uid = booked_by_uid;
        this.booked_by_name = booked_by_name;
    }

    public RoomModel() {
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
