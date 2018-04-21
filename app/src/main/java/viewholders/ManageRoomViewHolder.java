package viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.masroor.cloud_hotel_project.R;
import com.squareup.picasso.Picasso;

public class ManageRoomViewHolder extends RecyclerView.ViewHolder {

    ImageView image;

    private TextView textview_room_number,textview_room_price,textview_room_available,textview_room_capacity;

    public ManageRoomViewHolder(View itemView) {
        super(itemView);

        textview_room_number=itemView.findViewById(R.id.textview_room_number);
        textview_room_price=itemView.findViewById(R.id.textview_room_price);
        textview_room_available=itemView.findViewById(R.id.textview_room_available);
        textview_room_capacity=itemView.findViewById(R.id.textview_room_capacity);
        image=itemView.findViewById(R.id.imageview_room_image);
    }

    public void populateManageRoomRow(String url,String roomN,int roomP,int roomCap,boolean roomAvail){

        Picasso.get().load(url).into(image);

        textview_room_number.setText(roomN);
        textview_room_price.setText(String.valueOf(roomP));
        textview_room_capacity.setText(String.valueOf(roomCap));
        if(roomAvail)
            textview_room_available.setText(R.string.Yes);
        else
            textview_room_available.setText(R.string.No);
    }
}
