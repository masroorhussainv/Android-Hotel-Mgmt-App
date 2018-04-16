package viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.masroor.cloud_hotel_project.R;

public class ManageRoomViewHolder extends RecyclerView.ViewHolder {

    private TextView textview_room_number,textview_room_price,textview_room_available;

    public ManageRoomViewHolder(View itemView) {
        super(itemView);

        textview_room_number=itemView.findViewById(R.id.textview_room_number);
        textview_room_price=itemView.findViewById(R.id.textview_room_price);
        textview_room_available=itemView.findViewById(R.id.textview_room_available);
    }

    public void populateManageRoomRow(String roomN,int roomP,boolean roomAvail){
        textview_room_number.setText(roomN);
        textview_room_price.setText(String.valueOf(roomP));
        if(roomAvail)
            textview_room_available.setText(R.string.Yes);
        else
            textview_room_available.setText(R.string.No);
    }
}
