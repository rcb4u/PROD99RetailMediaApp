package com.RSPL.MEDIA.Doc990;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.RSPL.MEDIA.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

public class SpecHolder extends ChildViewHolder {

    private TextView childTextView;
    private TextView hospitalName;
    Button channel;


    public SpecHolder(View itemView) {
        super(itemView);
        childTextView = (TextView) itemView.findViewById(R.id.list_item_artist_name);
        hospitalName = (TextView) itemView.findViewById(R.id.list_item_hospital_name);
        channel = (Button) itemView.findViewById(R.id.channelbutton);

    }

    public void setArtistName(String name) {
        childTextView.setText(name);
    }

    public void setHospitalName(String name) {
        hospitalName.setText(name);
    }
}
