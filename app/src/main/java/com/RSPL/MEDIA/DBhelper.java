package com.RSPL.MEDIA;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBhelper extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/" + Activity_Installation.class.getPackage().getName() + "/databases/";
    private static final String DATABASE_NAME = "Db";
    private static final int DATABASE_VERSION = 18;
    private SQLiteDatabase myDataBase;
//	private final Context mycontext;

    private static final boolean D = true;


    private final Context mycontext;


    public static final String STORE_ID = "STORE_ID";//Store table name
    public static final String STORE_NAME = "STR_NM";//Store table name


    public static final String STORE_EMAIL = "E_MAIL";//Store table name
    public static final String STORE_MOBILE = "TELE";//Store table name
    public static final String STORE_ZIP = "ZIP";//Store table name
    public static final String STORE_CONTACTNAME = "STR_CNTCT_NM";//Store table name
    public static final String STORE_CITY = "CTY";//Store table name
    public static final String STORE_COUNTRY = "STR_CTR";//Store table name
    public static final String STORE_ADDRESS1 = "ADD_1";//Store table name
    public static final String STORE_FOOTER = "FOOTER";//Store table name
    public static final String STORE_ALTER_MOBILE = "TELE_1";//Store table name

    public static final String COLUMN_AD_TEXT = "AD_TEXT";//Store table name


    public static final String COLUMN_AD_MAIN_ID = "AD_MAIN_ID";
    public static final String COLUMN_USER = "USER";
    public static final String COLUMN_AD_DESC = "AD_DESC";
    public static final String COLUMN_AD_FILE = "AD_FILE";
    public static final String COLUMN_AD_START_DT = "AD_STRT_DT";
    public static final String COLUMN_AD_END_DT = "AD_END_DT";
    public static final String COLUMN_STATUS = "STATUS";
    public static final String COLUMN_LAST_MODIFIED = "LAST_MODIFIED";
    public static final String COLUMN_S_FLAG = "S_FLAG";
    public static final String COLUMN_STORE_ID = "STORE_ID";

    public static final String COLUMN_POS_USER = "POS_USER";
    public static final String COLUMN_M_FLAG = "M_FLAG";
    public static final String COLUMN_SLAVE_FLAG = "SLAVE_FLAG";
    public static final String COLUMN_PHARMA_STORE = "PHARMA_STORE";
    public static final String COLUMN_FMCG_STORE = "FMCG_STORE";
    public static final String COLUMN_TOTALSTORE = "TOTAL_STORE";
    public static final String COLUMN_AD = "AD";
    public static final String COLUMN_COMMENT = "COMMENT";
    public static final String COLUMN_CREATION = "CREATION";
    public static final String COLUMN_E_MAIL = "E_MAIL";
    public static final String COLUMN_MOB_NO = "MOB_NO";

    public static final String COLUMN_RETAILER_MERCHANTID = "MER_ID";
    public static final String COLUMN_RETAILER_MOBILENO = "WALLET1";
    public static final String COLUMN_VENDOR_MOBILENO = "WALLET2";


    private static final String TAG = "MyActivity";

    @SuppressLint("NewApi")
    public DBhelper(Context context) {

        super(context, DB_PATH + DATABASE_NAME, null, DATABASE_VERSION);
        this.mycontext = context;
    }


    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase() {

        org.sqlite.database.sqlite.SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DATABASE_NAME;
            java.io.File f = new java.io.File(myPath);
            f.getParentFile().mkdirs();
            //checkDB = SQLiteDatabase.openDatabase(myPath, null, org.sqlite.database.sqlite.SQLiteDatabase.OPEN_READONLY);

            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        } catch (org.sqlite.database.sqlite.SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = mycontext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = org.sqlite.database.sqlite.SQLiteDatabase.openDatabase(myPath, null, org.sqlite.database.sqlite.SQLiteDatabase.OPEN_READONLY);
        myDataBase.execSQL("PRAGMA key='Anaconda'");

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onConfigure(org.sqlite.database.sqlite.SQLiteDatabase myDataBase) {
        myDataBase.execSQL("PRAGMA key='Anaconda'");
    }


    @Override
    public void onCreate(org.sqlite.database.sqlite.SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(org.sqlite.database.sqlite.SQLiteDatabase db, int oldversion, int newversion) {
        if (newversion > oldversion) {

            Log.e("####TABLE ALTER", "retail media click");

            //	db.execSQL("ALTER TABLE RETAIL_MEDIA_CLICK ADD VIDEO_MAIL_FLAG VARCHAR(30)");

			/*db.execSQL("ALTER TABLE retail_str_sales_master ADD COLUMN SAVETOTALBILLDISCOUNT VARCHAR (20)");
			db.execSQL("ALTER TABLE retail_str_sales_master ADD COLUMN SAVETOTALBILLAMOUNT VARCHAR (20)");
			db.execSQL("ALTER TABLE retail_str_sales_master ADD COLUMN SAVEFINALBILLAMOUNT VARCHAR (20)" );policeonline
			db.execSQL(  "ALTER TABLE retail_str_sales_master ADD COLUMN SAVERECEIVEDBILLAMOUNT VARCHAR (20)" );
			db.execSQL("ALTER TABLE retail_str_sales_master ADD COLUMN SAVEEXPECTEDBILLAMOUNT VARCHAR (20) ");*/

            //	db.execSQL("ALTER TABLE retail_str_stock_master ADD COLUMN ACTIVE VARCHAR (45) ");
        }
        onCreate(db);


    }


    //retail_videodata*************************************************************************************************

    public void insertVideoData(String ADPLAY, String STOREID, String STOREMEDIAID, String FILENAME, String STARTDATE, String ENDDATE, String STARTTME, String ENDTIME, String SFLAG) {
        try {
            Log.e("#########", "We Are Inside DataBase Class");
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            Log.e("########", "Ad_Play in database:" + ADPLAY);
            Log.e("########", "Store_id:" + STOREID);
            Log.e("########", "Store_Media_id:" + STOREMEDIAID);
            Log.e("#######", "File_Name in database:" + FILENAME);
            Log.e("#######", "Start Date in database:" + STARTDATE);
            Log.e("#######", "End Date in database:" + ENDDATE);
            Log.e("#######", "Start Time in database:" + STARTTME);
            Log.e("#######", "End Time in database:" + ENDTIME);
            Log.e("#######", "S_FLAG in database:" + SFLAG);

            cv.put("AD_PLAY", ADPLAY);
            cv.put("STORE_ID", STOREID);
            cv.put("STORE_MEDIA_ID", STOREMEDIAID);
            cv.put("FILE_NAME", FILENAME);
            cv.put("STARTDATE", STARTDATE);
            cv.put("ENDDATE", ENDDATE);
            cv.put("STARTTIME", STARTTME);
            cv.put("ENDTIME", ENDTIME);
            cv.put("S_FLAG", SFLAG);


            long result = db.insert("retail_videodata", null, cv);
            Log.e("Message", "############## data inserted and result is " + result);
            //db.close();
        } catch (Exception e) {
            Log.e("Message", "##############:" + e);
        }
        //db.close();
    }


    public ArrayList getAllAdTicker() {
        ArrayList getalladticker = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select AD_TEXT from retail_ad_ticker where STATUS Like 'A%'and Active Like 'Y%' ", null);
        if (res.moveToFirst())
            Log.e(TAG, "Get Ad_Text from retail_ad_ticker table");
        {
            do {
                getalladticker.add(res.getString(res.getColumnIndex(COLUMN_AD_TEXT)));

            } while (res.moveToNext());
        }
        db.close();
        return getalladticker;


    }

    public boolean CheckIsDataAlreadyInDBorNot(String Phone, String adName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[2];
        params[0] = Phone + "%";
        params[1] = adName + "%";
        String Query = ("select Mobile_No,AD_PLAY_NAME from retail_media_click where " + " Mobile_No like ? and AD_PLAY_NAME like ?");
        Cursor cursor = db.rawQuery(Query, params);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;

    }

    public boolean CheckIsDataAlreadyInDBorNotinCargill(String Phone, String adName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[2];
        params[0] = Phone + "%";
        params[1] = adName + "%";
        String Query = ("select MOBILE_NO,AD_PLAY_NAME from cargill_bank_details where " + " MOBILE_NO like ? and AD_PLAY_NAME like ?");
        Cursor cursor = db.rawQuery(Query, params);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;


    }

    public ArrayList<VideoDetails_Model> getVideos_Db(String date) {
        String[] params = new String[1];
        params[0] = date + "%";
        ArrayList<VideoDetails_Model> al_db = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor datacursor = db.rawQuery("Select * from retail_ad_store_main where AD_STRT_DT like ? ORDER BY AD_STRT_DT", params);
        if (datacursor.moveToFirst()) {
            do {
                String COLUMN_ADPLAY = datacursor.getString(5);
                String COLUMN_START_DATETIME = datacursor.getString(6);
                String COLUMN_END_DATETIME = datacursor.getString(7);

                VideoDetails_Model tempVideo = new VideoDetails_Model();
                tempVideo.video = COLUMN_ADPLAY;
                tempVideo.startTime = COLUMN_START_DATETIME;
                tempVideo.endTime = COLUMN_END_DATETIME;

                al_db.add(tempVideo);
            }
            while (datacursor.moveToNext());
        }
        return al_db;
    }


    public void insertUserResponse(String adPlay, String storeMediaId, String mobNumber, String clickName, String videoName, String videomailflag) {
        try {
            Log.e("#########", "We Are Inside DataBase Class");
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            Log.e("########", "Ad_Play in database:" + adPlay);
            Log.e("########", "Store_Media_id:" + storeMediaId);
            Log.e("#######", "Mobile Number:" + mobNumber);
            Log.e("#######", "User Name who clicked:" + clickName);
            Log.e("#######", "Video clicked:" + videoName);
            Log.e("#######", "Video Mail Flag:" + videomailflag);
            Log.e("#######", "CLICK:" + "0");
            Log.e("#######", "POS_USER:" + "0");

            cv.put("AD_PLAY", adPlay);
            cv.put("STORE_MEDIA_ID", storeMediaId);
            cv.put("MOBILE_NO", mobNumber);
            cv.put("CUSTOMER_NM", clickName);
            cv.put("S_FLAG", "0");
            cv.put("AD_PLAY_NAME", videoName);
            cv.put("M_FLAG", "0");
            cv.put("VIDEO_MAIL_FLAG", videomailflag);
            cv.put("CLICK", "0");
            cv.put("POS_USER", "0");


            long result = db.insert("retail_media_click", null, cv);
            Log.e("Message", "############## data inserted and result is " + result);
            db.close();
        } catch (Exception e) {
            Log.e("Message", "##############:" + e);
        }
        //db.close();
    }


    public void insertUseronODOC(String adPlay,
                                 String storeMediaId,
                                 String mobNumber,
                                 String clickName,
                                 String videoName,
                                 String videomailflag,
                                 String email) {
        try {
            Log.e("#########", "We Are Inside DataBase Class");
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            Log.e("########", "Ad_Play in database:" + adPlay);
            Log.e("########", "Store_Media_id:" + storeMediaId);
            Log.e("#######", "Mobile Number:" + mobNumber);
            Log.e("#######", "User Name who clicked:" + clickName);
            Log.e("#######", "Video clicked:" + videoName);
            Log.e("#######", "Video Mail Flag:" + videomailflag);
            Log.e("#######", "CLICK:" + "0");
            Log.e("#######", "EMAIL:" + email);

            cv.put("AD_PLAY", adPlay);
            cv.put("STORE_MEDIA_ID", storeMediaId);
            cv.put("MOBILE_NO", mobNumber);
            cv.put("CUSTOMER_NM", clickName);
            cv.put("S_FLAG", "0");
            cv.put("AD_PLAY_NAME", videoName);
            cv.put("M_FLAG", email);
            cv.put("VIDEO_MAIL_FLAG", videomailflag);
            cv.put("CLICK", "0");


            long result = db.insert("retail_media_click", null, cv);
            Log.e("Message", "############## data inserted and result is " + result);
            db.close();
        } catch (Exception e) {
            Log.e("Message", "##############:" + e);
        }
        //db.close();
    }

    public void insertCargilbankcustomer(String adPlay,
                                         String storeMediaId,
                                         String mobNumber,
                                         String customerName,
                                         String videoName,
                                         String videomailflag,
                                         String companyname, String monthlysalary) {
        try {
            Log.e("#########", "We Are Inside DataBase Class");
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            Log.e("########", "Ad_Play in database:" + adPlay);
            Log.e("########", "Store_Media_id:" + storeMediaId);
            Log.e("#######", "Mobile Number:" + mobNumber);
            Log.e("#######", "User Name who clicked:" + customerName);
            Log.e("#######", "Video clicked:" + videoName);
            Log.e("#######", "Video Mail Flag:" + videomailflag);
            Log.e("#######", "CompanyName:" + companyname);
            Log.e("#######", "MonthlySalary:" + monthlysalary);

            cv.put("AD_PLAY", adPlay);
            cv.put("STORE_MEDIA_ID", storeMediaId);
            cv.put("MOBILE_NO", mobNumber);
            cv.put("CUSTOMER_NM", customerName);
            cv.put("S_FLAG", "0");
            cv.put("AD_PLAY_NAME", videoName);
            cv.put("VIDEO_MAIL_FLAG", videomailflag);
            cv.put("COMPANY_NAME", companyname);
            cv.put("MONTHLY_SALARY", monthlysalary);


            long result = db.insert("cargill_bank_details", null, cv);
            Log.e("Message", "############## data inserted and result is " + result);
            db.close();
        } catch (Exception e) {
            Log.e("Message", "##############:" + e);
        }
        //db.close();
    }


    public ArrayList<String> retailMediaClick() {
        ArrayList<String> arraylist = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from retail_media_click where S_FLAG='0'", null);
        if (cur.moveToFirst()) {
            do {

                arraylist.add(cur.getString(cur.getColumnIndex("AD_PLAY")));
                arraylist.add(cur.getString(cur.getColumnIndex("STORE_MEDIA_ID")));
                arraylist.add(cur.getString(cur.getColumnIndex("MOBILE_NO")));
                arraylist.add(cur.getString(cur.getColumnIndex("CLICK")));
                arraylist.add(cur.getString(cur.getColumnIndex("S_FLAG")));
                arraylist.add(cur.getString(cur.getColumnIndex("POS_USER")));
                arraylist.add(cur.getString(cur.getColumnIndex("M_FLAG")));
                arraylist.add(cur.getString(cur.getColumnIndex("CUSTOMER_NM")));
                arraylist.add(cur.getString(cur.getColumnIndex("AD_PLAY_NAME")));
                arraylist.add(cur.getString(cur.getColumnIndex("VIDEO_MAIL_FLAG")));


                arraylist.add(";");


            } while (cur.moveToNext());
        }

        return arraylist;

    }


    public ArrayList<String> retailVideoData() {
        ArrayList<String> arraylist = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from retail_videodata where S_FLAG='0'", null);
        if (cur.moveToFirst()) {
            do {

                arraylist.add(cur.getString(cur.getColumnIndex("AD_PLAY")));
                arraylist.add(cur.getString(cur.getColumnIndex("STORE_MEDIA_ID")));
                arraylist.add(cur.getString(cur.getColumnIndex("STORE_ID")));
                arraylist.add(cur.getString(cur.getColumnIndex("FILE_NAME")));
                arraylist.add(cur.getString(cur.getColumnIndex("STARTDATE")));
                arraylist.add(cur.getString(cur.getColumnIndex("ENDDATE")));
                arraylist.add(cur.getString(cur.getColumnIndex("STARTTIME")));
                arraylist.add(cur.getString(cur.getColumnIndex("ENDTIME")));
                arraylist.add(cur.getString(cur.getColumnIndex("S_FLAG")));

                arraylist.add(";");


            } while (cur.moveToNext());
        }

        return arraylist;

    }

    public void deleteAlldata(String tableName) {

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + tableName + " where S_FLAG = '1'");
        db.close();

        Log.d("Data Deleted", tableName);


    }


    public void deleteAll(String tableName) {

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + tableName);
        db.close();

        Log.d("ALL Data Deleted", tableName);


    }


    public void alterTable(String tableName) {

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("alter table " + tableName + " add VIDEO_MAIL_FLAG nvarchar(30)");
        db.close();

        Log.e("Table Altered", tableName);


    }

    public MediaStorePojo getStoreDetails() {

        MediaStorePojo mediaStorePojo = null;

        SQLiteDatabase db = this.getReadableDatabase();
        //db.getVersion();
        //Log.e("version ", "Version number is " + db.getVersion());
        Cursor res = db.rawQuery("select * from retail_store", null);
        if (res.moveToFirst()) {
            //do {
            mediaStorePojo = new MediaStorePojo();
            mediaStorePojo.setStoreId(res.getString(res.getColumnIndex(STORE_ID)));
            mediaStorePojo.setMediaid(res.getString(res.getColumnIndex("STORE_MEDIA_ID")));
            mediaStorePojo.setOTP(res.getString(res.getColumnIndex("OTP")));
            mediaStorePojo.setStoreName(res.getString(res.getColumnIndex(STORE_NAME)));
            mediaStorePojo.setStoreemail(res.getString(res.getColumnIndex(STORE_EMAIL)));
            mediaStorePojo.setStoreTele(res.getString(res.getColumnIndex(STORE_MOBILE)));
            mediaStorePojo.setStorezip(res.getString(res.getColumnIndex(STORE_ZIP)));
            mediaStorePojo.setStorecity(res.getString(res.getColumnIndex(STORE_CITY)));
            mediaStorePojo.setStorecontactname(res.getString(res.getColumnIndex(STORE_CONTACTNAME)));
            mediaStorePojo.setStorecountry(res.getString(res.getColumnIndex(STORE_COUNTRY)));
            mediaStorePojo.setStoreAddress(res.getString(res.getColumnIndex(STORE_ADDRESS1)));
            mediaStorePojo.setFooter(res.getString(res.getColumnIndex(STORE_FOOTER)));
            mediaStorePojo.setAlterMobileNo(res.getString(res.getColumnIndex(STORE_ALTER_MOBILE)));
            //} while (res.moveToNext());
        }
        return mediaStorePojo;
    }


    public AdMainPojo getAdMainDetails() {

        AdMainPojo adMainPojo = null;

        SQLiteDatabase db = this.getReadableDatabase();
        //db.getVersion();
        //Log.e("version ", "Version number is " + db.getVersion());
        Cursor res = db.rawQuery("select * from ad_main", null);
        if (res.moveToFirst()) {
            //do {
            adMainPojo = new AdMainPojo();
            adMainPojo.setAd_Main_Id(res.getString(res.getColumnIndex(COLUMN_AD_MAIN_ID)));
            adMainPojo.setUser(res.getString(res.getColumnIndex(COLUMN_USER)));
            adMainPojo.setAd_Desc(res.getString(res.getColumnIndex(COLUMN_AD_DESC)));
            adMainPojo.setAd_File(res.getString(res.getColumnIndex(COLUMN_AD_FILE)));
            adMainPojo.setAd_Start_Dt(res.getString(res.getColumnIndex(COLUMN_AD_START_DT)));
            adMainPojo.setAd_End_Dt(res.getString(res.getColumnIndex(COLUMN_AD_END_DT)));
            adMainPojo.setStatus(res.getString(res.getColumnIndex(COLUMN_STATUS)));
            adMainPojo.setLast_Modified(res.getString(res.getColumnIndex(COLUMN_LAST_MODIFIED)));
            adMainPojo.setS_Flag(res.getString(res.getColumnIndex(COLUMN_S_FLAG)));
            adMainPojo.setStore_Id(res.getString(res.getColumnIndex(COLUMN_STORE_ID)));
            adMainPojo.setPos_User(res.getString(res.getColumnIndex(COLUMN_POS_USER)));
            adMainPojo.setM_Flag(res.getString(res.getColumnIndex(COLUMN_M_FLAG)));
            adMainPojo.setSlave_Flag(res.getString(res.getColumnIndex(COLUMN_SLAVE_FLAG)));
            adMainPojo.setPharma_Store(res.getString(res.getColumnIndex(COLUMN_PHARMA_STORE)));
            adMainPojo.setFMCG_Store(res.getString(res.getColumnIndex(COLUMN_FMCG_STORE)));
            adMainPojo.setTotal_Store(res.getString(res.getColumnIndex(COLUMN_TOTALSTORE)));
            adMainPojo.setAd(res.getString(res.getColumnIndex(COLUMN_AD)));
            adMainPojo.setComment(res.getString(res.getColumnIndex(COLUMN_COMMENT)));
            adMainPojo.setCreation(res.getString(res.getColumnIndex(COLUMN_CREATION)));
            adMainPojo.setE_Mail(res.getString(res.getColumnIndex(COLUMN_E_MAIL)));
            adMainPojo.setMob_No(res.getString(res.getColumnIndex(COLUMN_MOB_NO)));
        }
        db.close();
        return adMainPojo;
    }


    public ArrayList<String> getretailAdMainforDelete() {
        ArrayList<String> arraylist = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select AD_FILE from AD_MAIN where STATUS='CLOSE'", null);
        if (cur.moveToFirst()) {
            do {

                arraylist.add(cur.getString(cur.getColumnIndex(COLUMN_AD_FILE)));
            } while (cur.moveToNext());
        }
        db.close();

        return arraylist;


    }

    public void AdMainforDelete() {

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from AD_MAIN");
        db.close();

        Log.d("Deleted", "Add DELETED");


    }


    public ArrayList<String> getTableColumn() {
        ArrayList<String> arraylist = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from AD_MAIN", null);
        int count = cur.getColumnCount();

        do {
            for (int i = 0; i < count; i++) {
                String data = cur.getColumnName(i);
                arraylist.add(data);

            }
        }
        while (cur.isClosed());


        return arraylist;

    }


    public ArrayList<String> getStoreid() {
        ArrayList<String> getstoreid = new ArrayList<String>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from retail_store ", null);

            Log.e("Message", "############## data fetched  and result is " + res);
            if (res.moveToFirst())
                Log.e(TAG, "Get STORE_ID from retail_store table ");
            {
                do {
                    getstoreid.add(res.getString(res.getColumnIndex(STORE_ID)));
                } while (res.moveToNext());
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        return getstoreid;
    }

    public ArrayList<String> getWalletDetail() {
        ArrayList<String> prefixlist = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from ret_ticket_install_register", null);
        if (cursor.moveToFirst()) {
            do {
                prefixlist.add(cursor.getString(cursor.getColumnIndex(COLUMN_RETAILER_MERCHANTID)));
                prefixlist.add(cursor.getString(cursor.getColumnIndex(COLUMN_RETAILER_MOBILENO)));
                prefixlist.add(cursor.getString(cursor.getColumnIndex(COLUMN_VENDOR_MOBILENO)));


            } while (cursor.moveToNext());
        }
        return prefixlist;
    }

    public boolean McashDatainsert(String store_id, String Retailermerchantid, String merchantTransId, String Transactionamt, String CustomerMobileNo, String Paymode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("STORE_ID", store_id);
        contentValues.put("MERCHANT_ID", Retailermerchantid);
        contentValues.put("MERCHANT_TRANS_ID", merchantTransId);
        contentValues.put("TRANS_AMOUNT", Transactionamt);
        contentValues.put("CUST_MOBILE_NO", CustomerMobileNo);
        contentValues.put("PAYMENT_MODE", Paymode);
        contentValues.put("FLAG", "1");

        db.insert("retail_wallet_detail", null, contentValues);

	/*	String insert_customer = "insert into retail_cust ( CUST_ID , POS_USER , MOBILE_NO , NAME , E_MAIL , PASSWORD , CREDIT_CUST , CUST_ADD , M_FLAG,S_FLAG ) " +
				"values (" + "'" + id +  "'," + "'" + username +  "'," + "'" + customer.getCustomermobileno() +  "'," +  "'" + customer.getCustomername() +  "'," +  "'" + customer.getCustomeremail() +  "'," + "'" + customer.getCustomermobileno() +  "'," + "'" + customer.getCustomercredit() +  "'," + "'" + customer.getCustomeradress() +  "'," + "'I'," +"'0')";
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("Query", insert_customer);
			login.sendMessage(String.valueOf(jsonObject));
		}catch (Exception e){}*/
        return true;


    }


}
