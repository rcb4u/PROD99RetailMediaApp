package com.RSPL.MEDIA;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class UpdateMediaDbReceiver extends BroadcastReceiver {

    public static final String TABLE_NAME1 = "retail_videodata";
    public static final String TABLE_NAME2 = "retail_media_click";


    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            DBhelper db = new DBhelper(context);

            db.deleteAlldata(TABLE_NAME1);
            db.deleteAlldata(TABLE_NAME2);
            //Toast.makeText(context, "DATA DELETED", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

        }
        //Toast.makeText(context, "DATA DELETED", Toast.LENGTH_SHORT).show();

    }
}
