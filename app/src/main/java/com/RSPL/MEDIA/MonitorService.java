package com.RSPL.MEDIA;
/**
 * Created by rspl-rajeev on 27/9/16.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by H149056 on 9/27/2016.
 */
public class MonitorService extends Service {
    TimerTask task = null;
    Timer timer = null;
    public static String Video = null;
    public static ArrayList<VideoDetails_Model> video_details_list = new ArrayList<>();

    @Nullable

    //not required
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        Log.e("#############BGG", "ONCREATE");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //make sure your videoDetails_list size is zero or not
        Log.e("#############", "onStartCommand && list size:" + video_details_list.size());
        //infinite timer task
        task = new TimerTask() {
            @Override
            public void run() {
                checkVideo();
            }
        };
        timer = new Timer();
        timer.schedule(task, 1000, 1000);
        return Service.START_STICKY;
    }

    public void checkVideo() {
        if (match()) {
            //video matched ,,send broadcast
            //send your unique video id(may be Video Uri)
            Log.e("------", "----->Video matched:" + MonitorService.Video);
            Intent in = new Intent();
            in.putExtra("VIDEO_ID", MonitorService.Video.toString());
            in.setAction("VIDEO_ID_BROADCASTER");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
        }
    }

    public boolean match() {
        boolean flag = false;
        //check your videos date time format
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss").format(Calendar.getInstance().getTime());
        Log.e("------", "------>current date and time:" + timeStamp);
        for (int index = 0; index < video_details_list.size(); index++) {
            if (video_details_list.get(index).startTime.equals(timeStamp)) {


                flag = true;
                MonitorService.Video = video_details_list.get(index).video;
                break;

            } else {
                flag = false;
            }
        }
        return flag;
    }


}
