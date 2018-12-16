package com.example.job.appcars;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.job.appcars.models.Booking;


import java.util.List;

public class BookingList extends ArrayAdapter<Booking> {
    private Activity context;
    List<Booking> bList;

    public BookingList(Activity context, List<Booking> bList) {
        super(context, R.layout.layout_booking_list, bList);
        this.context = context;
        this.bList = bList;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_booking_list, null, true);

        TextView textViewTime = (TextView) listViewItem.findViewById(R.id.textTime);
        TextView textViewUsername = (TextView) listViewItem.findViewById(R.id.textUsername);
        TextView textViewStatus = (TextView) listViewItem.findViewById(R.id.textStatus);

        Booking booking = bList.get(position);
        textViewTime.setText(booking.getTime());
        textViewUsername.setText(booking.getUid());
        textViewStatus.setText(booking.getStatus());

        return listViewItem;
    }

    private String checkUid(String uid) {
        String name = "";
        return name;
    }
}
