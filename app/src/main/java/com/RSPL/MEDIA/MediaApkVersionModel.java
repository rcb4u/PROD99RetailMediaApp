package com.RSPL.MEDIA;

/**
 * Created by rspl-rajeev on 29/3/18.
 */

public class MediaApkVersionModel {

    private String STORE_ID;
    private String STORE_NM;
    private String STORE_ADDRESS;
    private String APK_VERSION;
    private String APK_UPGRADE;


    public String getSTORE_ID() {
        return STORE_ID;
    }

    public void setSTORE_ID(String STORE_ID) {
        this.STORE_ID = STORE_ID;
    }

    public String getSTORE_NM() {
        return STORE_NM;
    }

    public void setSTORE_NM(String STORE_NM) {
        this.STORE_NM = STORE_NM;
    }

    public String getSTORE_ADDRESS() {
        return STORE_ADDRESS;
    }

    public void setSTORE_ADDRESS(String STORE_ADDRESS) {
        this.STORE_ADDRESS = STORE_ADDRESS;
    }

    public String getAPK_VERSION() {
        return APK_VERSION;
    }

    public void setAPK_VERSION(String APK_VERSION) {
        this.APK_VERSION = APK_VERSION;
    }

    public String getAPK_UPGRADE() {
        return APK_UPGRADE;
    }

    public void setAPK_UPGRADE(String APK_UPGRADE) {
        this.APK_UPGRADE = APK_UPGRADE;
    }
}