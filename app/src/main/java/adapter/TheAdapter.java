package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.masroor.cloud_hotel_project.R;

import java.util.ArrayList;

import models.RoomModel;
import models.UserModel;
import viewholders.ManageRoomViewHolder;

public class TheAdapter extends RecyclerView.Adapter {

    private ArrayList<RoomModel> rooms_list;
    private ArrayList<UserModel> users_list;

    private int layout_id_to_use;

    public TheAdapter(ArrayList data_list, int layout_id) {
        switch (layout_id){
            case R.layout.itemview_manage_room:{
                this.rooms_list= data_list;
            }break;
        }
        this.layout_id_to_use=layout_id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (layout_id_to_use){
            case R.layout.itemview_manage_room:{
                View view= LayoutInflater.from(parent.getContext()).inflate(layout_id_to_use,parent,false);
                return new ManageRoomViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (layout_id_to_use){

            case R.layout.itemview_manage_room:{
                ManageRoomViewHolder vh=(ManageRoomViewHolder)holder;
                vh.populateManageRoomRow(rooms_list.get(position).getRoom_number(),
                    rooms_list.get(position).getRoom_price(),
                    rooms_list.get(position).isRoom_available());
            }
            break;
        }
    }

    @Override
    public int getItemCount() {
        switch (layout_id_to_use){
            case R.layout.itemview_manage_room:{
                return rooms_list.size();
            }

            default:{
                return 0;
            }
        }
    }
}
