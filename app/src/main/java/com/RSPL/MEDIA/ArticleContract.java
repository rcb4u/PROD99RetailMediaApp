package com.RSPL.MEDIA;

import android.net.Uri;

public final class ArticleContract {

    // ContentProvider information
    public static final String CONTENT_AUTHORITY = "com.RSPL.MEDIA";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_RETAIL_MEDIA_CLICK = "retail_media_click";
    static final String PATH_RETAIL_VIDEODATA = "retail_videodata";
    static final String PATH_RETAIL_STORE = "retail_store";
    static final String PATH_CARGILL_BANK_DETAILS = "cargill_bank_details";

    static final String DB_NAME = "Db";

    public static abstract class RetailStore {

        public static final String TABLENAME_RETAIL_STORE = "retail_store";
        public static final String STORE_ID = "STORE_ID";
        public static final String STORE_MEDIA_ID = "STORE_MEDIA_ID";
        public static final String STR_NM = "STR_NM";
        public static final String ADD_1 = "ADD_1";
        public static final String CTY = "CTY";
        public static final String STR_CTR = "STR_CTR";
        public static final String ZIP = "ZIP";
        public static final String STR_CNTCT_NM = "STR_CNTCT_NM";
        public static final String TELE = "TELE";
        public static final String TELE_1 = "TELE_1";
        public static final String E_MAIL = "E_MAIL";
        public static final String TAN_NUMBER = "TAN_NUMBER";
        public static final String DSTR_NUMBER = "DSTR_NM";
        public static final String FOOTER = "FOOTER";
        public static final String FLAG = "FLAG";
        public static final String STR_IND_DESC = "STR_IND_DESC";
        public static final String RET_CLS_ID = "RET_CLS_ID";
        public static final String TEAM_MEMB = "TEAM_MEMB";
        public static final String STATUS = "STATUS";
        public static final String OTP = "OTP";
        public static final String USER = "USER";
        public static final String S_FLAG = "S_FLAG";
        public static final String S3_FLAG = "S3_FLAG";
        public static final String POS_USER = "POS_USER";
        public static final String M_FLAG = "M_FLAG";
        public static final String LKR = "LKr";

        // ContentProvider information for articles..............

        public static final Uri CONTENT_URI_RETAIL_STORE =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RETAIL_STORE).build();
        public static final String CONTENT_TYPE_RETAIL_STORE =
                "vnd.android.cursor.dir/" + CONTENT_URI_RETAIL_STORE + "/" + PATH_RETAIL_STORE;
        public static final String CONTENT_ITEM_TYPE_RETAIL_STORE =
                "vnd.android.cursor.item/" + CONTENT_URI_RETAIL_STORE + "/" + PATH_RETAIL_STORE;

    }

    public static abstract class RetailMedia {

        public static final String TABLENAME_RETAIL_MEDIA_CLICK = "retail_media_click";

        public static final String AD_PLAY = "AD_PLAY";
        public static final String STORE_MEDIA_ID = "STORE_MEDIA_ID";
        public static final String MOBILE_NO = "MOBILE_NO";
        public static final String CLICK = "CLICK";
        public static final String LAST_MODIFIED = "LAST_MODIFIED";
        public static final String S_FLAG = "S_FLAG";
        public static final String POS_USER = "POS_USER";
        public static final String M_FLAG = "M_FLAG";
        public static final String SLAVE_FLAG = "SLAVE_FLAG";
        public static final String CUSTOMER_NM = "CUSTOMER_NM";
        public static final String AD_PLAY_NAME = "AD_PLAY_NAME";
        public static final String VIDEO_MAIL_FLAG = "VIDEO_MAIL_FLAG";


        // ContentProvider information for articles..............

        public static final Uri CONTENT_URI_RETAIL_MEDIA_CLICK =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RETAIL_MEDIA_CLICK).build();

        public static final String CONTENT_TYPE_RETAIL_MEDIA_CLICK =
                "vnd.android.cursor.dir/" + CONTENT_URI_RETAIL_MEDIA_CLICK + "/" + PATH_RETAIL_MEDIA_CLICK;


        public static final String CONTENT_ITEM_TYPE_RETAIL_MEDIA_CLICK =
                "vnd.android.cursor.item/" + CONTENT_URI_RETAIL_MEDIA_CLICK + "/" + PATH_RETAIL_MEDIA_CLICK;
    }


    public static abstract class RetailVideodata {
        public static final String TABLENAME_RETAIL_VIDEODATA = "retail_videodata";
        public static final String AD_PLAY = "AD_PLAY";
        public static final String STORE_MEDIA_ID = "STORE_MEDIA_ID";
        public static final String STORE_ID = "STORE_ID";
        public static final String FILE_NAME = "FILE_NAME";
        public static final String STARTDATE = "STARTDATE";
        public static final String ENDDATE = "ENDDATE";
        public static final String STARTTIME = "STARTTIME";
        public static final String ENDTIME = "ENDTIME";
        public static final String CLICK = "CLICK";
        public static final String LAST_MODIFIED = "LAST_MODIFIED";
        public static final String S_FLAG = "S_FLAG";
        public static final String POS_USER = "POS_USER";
        public static final String M_FLAG = "M_FLAG";
        public static final String SLAVE_FLAG = "SLAVE_FLAG";


        public static final Uri CONTENT_URI_RETAIL_VIDEODATA =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RETAIL_VIDEODATA).build();
        public static final String CONTENT_TYPE_RETAIL_VIDEODATA =
                "vnd.android.cursor.dir/" + CONTENT_URI_RETAIL_VIDEODATA + "/" + PATH_RETAIL_VIDEODATA;
        public static final String CONTENT_ITEM_TYPE_RETAIL_VIDEODATA =
                "vnd.android.cursor.item/" + CONTENT_URI_RETAIL_VIDEODATA + "/" + PATH_RETAIL_VIDEODATA;
    }


    public static abstract class CargillBankDetails {

        public static final String TABLENAME_CARGILL_BANK_DETAILS = "cargill_bank_details";

        public static final String AD_PLAY = "AD_PLAY";
        public static final String STORE_MEDIA_ID = "STORE_MEDIA_ID";
        public static final String MOBILE_NO = "MOBILE_NO";
        public static final String S_FLAG = "S_FLAG";
        public static final String POS_USER = "POS_USER";
        public static final String M_FLAG = "M_FLAG";
        public static final String CUSTOMER_NM = "CUSTOMER_NM";
        public static final String AD_PLAY_NAME = "AD_PLAY_NAME";
        public static final String VIDEO_MAIL_FLAG = "VIDEO_MAIL_FLAG";
        public static final String COMPANY_NAME = "COMPANY_NAME";
        public static final String MONTHLY_SALARY = "MONTHLY_SALARY";
        public static final String LAST_MODIFIED = "LAST_MODIFIED";


        // ContentProvider information for articles..............

        public static final Uri CONTENT_URI_CARGILL_BANK_DETAILS =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARGILL_BANK_DETAILS).build();

        public static final String CONTENT_TYPE_CARGILL_BANK_DETAILS =
                "vnd.android.cursor.dir/" + CONTENT_URI_CARGILL_BANK_DETAILS + "/" + PATH_CARGILL_BANK_DETAILS;


        public static final String CONTENT_ITEM_TYPE_CARGILL_BANK_DETAILS =
                "vnd.android.cursor.item/" + CONTENT_URI_CARGILL_BANK_DETAILS + "/" + PATH_CARGILL_BANK_DETAILS;
    }


}