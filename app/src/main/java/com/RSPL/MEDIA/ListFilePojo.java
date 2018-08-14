package com.RSPL.MEDIA;

/**
 * Created by rspl-rajeev on 28/2/18.
 */

public class ListFilePojo {


    String FileName;

    public ListFilePojo(String location_Name) {
        FileName = location_Name;

    }


    public String getFileName() {
        return FileName;
    }


    public void setFileName(String fileName) {
        FileName = fileName;
    }


}
