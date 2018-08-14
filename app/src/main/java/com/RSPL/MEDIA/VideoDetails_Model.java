package com.RSPL.MEDIA;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by rspl-rajeev on 27/9/16.
 */
public class VideoDetails_Model {
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String startTime;
    public String video;
    public String endTime;

    public VideoDetails_Model(String startTime, String video, String endTime) {
        this.startTime = startTime;
        this.video = video;
        this.endTime = endTime;
    }

    public VideoDetails_Model() {
    }


    public static void sort(ArrayList<VideoDetails_Model> list) {
        ArrayList<String> videos = new ArrayList<>();


        for (int i = 0; i < list.size(); i++) {
            String uriString = String.valueOf(list.get(i).video);

            Log.e("-------", "---------->>before sorting" + list.get(i).video);
            int index = uriString.lastIndexOf('/');
            String video_name = uriString.substring(index + 1, uriString.length());
            videos.add(video_name);
        }
        Collections.sort(videos);

        for (int i = 0; i < videos.size(); i++) {
            Log.e("-------", "---------->>after sorting" + videos.get(i));
        }
    }


}
