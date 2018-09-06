package com.RSPL.MEDIA;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteException;
import org.sqlite.database.sqlite.SQLiteStatement;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Config.Config;
import Config.ConfigItems;
import JSON.JSONParserSync;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Activity_Installation extends Activity implements OnClickListener, Callback<ConfigItems> {
    public EditText user, pass;
    String store_id;
    private Button bLogin;
    ProgressBar progress;
    ActionBar actionBar;

    private ArrayList list;

    private ArrayList<String> field4;
    private ArrayList<String> field6;
    private ArrayList<String> field11;
    private ArrayList<String> field14;
    private ArrayList<String> field16;
    private ArrayList<String> field37;
    private ArrayList<String> field38;
    private ArrayList<String> field39;
    private ArrayList<String> field42;
    private ArrayList<String> field52;

    private ArrayList<String> field60;


    private ArrayList<Config> data4;
    private ArrayList<Config> data6;

    private ArrayList<Config> data16;


    private ArrayList<Config> data52;

    private ArrayList<Config> data59;
    private ArrayList<String> field59;







  /*  String tablename_4 = "retail_ad_store_main";
    String tablename_6 = "retail_ad_ticker ";*//*******************************************//*
    String tablename_11 = "retail_cust";
    String tablename_14 = "retail_media";
    String tablename_16 = "retail_store";*//*******************************************//*
    String tablename_37 = "retail_videodata";*//*******************************************//*
    String tablename_38 = "retail_videodata_cont";*//*******************************************//*
    String tablename_39 = "retail_videodata_cont1";*//*******************************************//*
    String tablename_42 = "ad_ticker_main";*//*******************************************//*
    String tablename_52 = "retail_employees";
    String tablename_59 = "ad_main";*//*******************************************//*
    String tablename_60 = "retail_media_click";*/
    /*******************************************//*
     */


    String tablename_4 = "RETAIL_AD_STORE_MAIN";
    String tablename_6 = "RETAIL_AD_TICKER ";
    /*******************************************/
    String tablename_11 = "RETAIL_CUST";
    String tablename_14 = "RETAIL_MEDIA";
    String tablename_16 = "Retail_Store";
    /*******************************************/
    String tablename_37 = "RETAIL_VIDEODATA";
    /*******************************************/
    String tablename_38 = "RETAIL_VIDEODATA_CONT";
    /*******************************************/
    String tablename_39 = "RETAIL_VIDEODATA_CONT1";
    /*******************************************/
    String tablename_42 = "AD_TICKER_MAIN";
    /*******************************************/
    String tablename_52 = "RETAIL_EMPLOYEES";
    String tablename_59 = "AD_MAIN";
    /*******************************************/
    String tablename_60 = "RETAIL_MEDIA_CLICK";
    /*******************************************/


    SQLiteDatabase myDataBase;
    SQLiteStatement insertStmt;
    String INSERT;
    ProgressDialog loading;
    String id, JSON_STRING;

    public boolean checkDataBase() {
        Cursor c = null;
        String DB_PATH = "/data/data/" + Activity_Installation.class.getPackage().getName() + "/databases/";
        final String DATABASE_NAME = "Db";
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DATABASE_NAME;
            java.io.File f = new java.io.File(myPath);
            f.getParentFile().mkdirs();
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.execSQL("PRAGMA key='Anaconda'");
            c = checkDB.rawQuery("select * from RETAIL_STORE", null);
			/*File db_path = new File("/data/data/" + Activity_Installation.class.getPackage().getName() + "/databases/Db");
			db_path.getParentFile().mkdirs();
			checkDB = SQLiteDatabase.openOrCreateDatabase(db_path, null);          //Opens database in writable mode.
			myDataBase.execSQL("PRAGMA key='Anaconda'");
*/

        } catch (SQLiteException e) {


            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return c != null ? true : false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        System.loadLibrary("sqliteX");

        try {

            Boolean fortrue = checkDataBase();
            if (fortrue == true) {
                Intent intent = new Intent(getApplicationContext(), MediaMainScreen.class);
                startActivity(intent);
                return;
            } else {
                File db_path = new File("/data/data/" + Activity_Installation.class.getPackage().getName() + "/databases/Db");
                db_path.getParentFile().mkdirs();
                myDataBase = SQLiteDatabase.openOrCreateDatabase(db_path, null);          //Opens database in writable mode.
                myDataBase.execSQL("PRAGMA key='Anaconda'");
                setContentView(R.layout.activity_activity__installation);
                actionBar = getActionBar();
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9033")));
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setIcon(R.drawable.w);
                user = (EditText) findViewById(R.id.username);
                pass = (EditText) findViewById(R.id.password);
                bLogin = (Button) findViewById(R.id.login);
                bLogin.setOnClickListener(this);
                field4 = new ArrayList<>();
                field6 = new ArrayList<>();
                field11 = new ArrayList<>();
                field14 = new ArrayList<>();
                field16 = new ArrayList<>();
                field37 = new ArrayList<>();
                field38 = new ArrayList<>();
                field39 = new ArrayList<>();
                field42 = new ArrayList<>();
                field52 = new ArrayList<>();
                field59 = new ArrayList<>();
                field60 = new ArrayList<>();

                data4 = new ArrayList<>();
                data6 = new ArrayList<>();

                data16 = new ArrayList<>();

                data52 = new ArrayList<>();

                data59 = new ArrayList<>();


                getJSON();


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call<ConfigItems> call, Response<ConfigItems> response) {
        try {
            data16.addAll(response.body().Retail_Store);
            int rowindex = data16.size() / field16.size();
            list = new ArrayList<>();
            String data;

            for (int k = 0; k < data16.size() - 1; k += (field16.size() * rowindex) + 1) {
                for (int i = k; i < field16.size() * rowindex; i++) {
                    data = (data16.get(i).toString());
                    list.add(data);
                }

                insert(Activity_Installation.this, list, field16, tablename_16, 0);
            }
        } catch (Exception e) {
            Log.e("Exception in table 16", e.toString());
        }


        try {
            data59.addAll(response.body().Ad_Main);
            int rowindex = data59.size() / field59.size();
            list = new ArrayList<>();
            String data;

            for (int k = 0; k < data59.size() - 1; k += (field59.size() * rowindex) + 1) {
                for (int i = k; i < field59.size() * rowindex; i++) {
                    data = (data59.get(i).toString());
                    list.add(data);
                }

                insert(Activity_Installation.this, list, field59, tablename_59, 0);
            }
        } catch (Exception e) {
            Log.e("Exception in table 59", e.toString());
        }

        loading.dismiss();
        Intent intent = new Intent(getApplicationContext(), MediaMainScreen.class);
        startActivity(intent);

    }

    @Override
    public void onFailure(Call<ConfigItems> call, Throwable t) {
        Intent intent = new Intent(getApplicationContext(), Activity_Installation.class);
        startActivity(intent);

    }

    public void jsonparsed(String input) {
        try {
            JSONObject jsonObject4 = new JSONObject(input);
            JSONArray firsttitlearray4 = jsonObject4.getJSONArray(Config.TAG_ARRAY_TABLE_FOUR);
            for (int i = 0; i < firsttitlearray4.length(); i++) {
                JSONObject obj4 = (JSONObject) firsttitlearray4.get(i);
                id = obj4.getString(Config.TAG_FIELD);
                field4.add(id);
            }
            createDynamicDatabase(Activity_Installation.this, tablename_4, field4);
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        try {
            JSONObject jsonObject6 = new JSONObject(input);
            JSONArray firsttitlearray6 = jsonObject6.getJSONArray(Config.TAG_ARRAY_TABLE_SIX);
            for (int i = 0; i < firsttitlearray6.length(); i++) {
                JSONObject obj6 = (JSONObject) firsttitlearray6.get(i);
                id = obj6.getString(Config.TAG_FIELD);
                field6.add(id);
            }
            createDynamicDatabase(Activity_Installation.this, tablename_6, field6);
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

        try {
            JSONObject jsonObject11 = new JSONObject(input);
            JSONArray firsttitlearray11 = jsonObject11.getJSONArray(Config.TAG_ARRAY_TABLE_ELEVEN);
            for (int i = 0; i < firsttitlearray11.length(); i++) {
                JSONObject obj11 = (JSONObject) firsttitlearray11.get(i);
                id = obj11.getString(Config.TAG_FIELD);
                field11.add(id);
            }
            createDynamicDatabase(Activity_Installation.this, tablename_11, field11);
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }

        try {
            JSONObject jsonObject14 = new JSONObject(input);
            JSONArray firsttitlearray14 = jsonObject14.getJSONArray(Config.TAG_ARRAY_TABLE_FOURTEEN);
            for (int i = 0; i < firsttitlearray14.length(); i++) {
                JSONObject obj14 = (JSONObject) firsttitlearray14.get(i);
                id = obj14.getString(Config.TAG_FIELD);
                field14.add(id);
            }
            createDynamicDatabase(Activity_Installation.this, tablename_14, field14);
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        try {
            JSONObject jsonObject16 = new JSONObject(input);
            JSONArray firsttitlearray16 = jsonObject16.getJSONArray(Config.TAG_ARRAY_TABLE_SIXTEEN);
            for (int i = 0; i < firsttitlearray16.length(); i++) {
                JSONObject obj16 = (JSONObject) firsttitlearray16.get(i);
                id = obj16.getString(Config.TAG_FIELD);
                field16.add(id);
            }


            createDynamicDatabase(Activity_Installation.this, tablename_16, field16);
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        try {
            JSONObject jsonObject37 = new JSONObject(input);
            JSONArray firsttitlearray37 = jsonObject37.getJSONArray(Config.TAG_ARRAY_TABLE_37);
            for (int i = 0; i < firsttitlearray37.length(); i++) {
                JSONObject obj37 = (JSONObject) firsttitlearray37.get(i);
                id = obj37.getString(Config.TAG_FIELD);
                field37.add(id);
            }
            createDynamicDatabase(Activity_Installation.this, tablename_37, field37);
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        try {
            JSONObject jsonObject38 = new JSONObject(input);
            JSONArray firsttitlearray38 = jsonObject38.getJSONArray(Config.TAG_ARRAY_TABLE_38);
            for (int i = 0; i < firsttitlearray38.length(); i++) {
                JSONObject obj38 = (JSONObject) firsttitlearray38.get(i);
                id = obj38.getString(Config.TAG_FIELD);
                field38.add(id);
            }
            createDynamicDatabase(Activity_Installation.this, tablename_38, field38);
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        try {
            JSONObject jsonObject39 = new JSONObject(input);
            JSONArray firsttitlearray39 = jsonObject39.getJSONArray(Config.TAG_ARRAY_TABLE_39);
            for (int i = 0; i < firsttitlearray39.length(); i++) {
                JSONObject obj39 = (JSONObject) firsttitlearray39.get(i);
                id = obj39.getString(Config.TAG_FIELD);
                field39.add(id);
            }
            createDynamicDatabase(Activity_Installation.this, tablename_39, field39);
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        try {
            JSONObject jsonObject42 = new JSONObject(input);
            JSONArray firsttitlearray42 = jsonObject42.getJSONArray(Config.TAG_ARRAY_TABLE_42);
            for (int i = 0; i < firsttitlearray42.length(); i++) {
                JSONObject obj42 = (JSONObject) firsttitlearray42.get(i);
                id = obj42.getString(Config.TAG_FIELD);
                field42.add(id);
            }
            createDynamicDatabase(Activity_Installation.this, tablename_42, field42);
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        try {
            JSONObject jsonObject59 = new JSONObject(input);
            JSONArray firsttitlearray59 = jsonObject59.getJSONArray(Config.TAG_ARRAY_TABLE_59);
            for (int i = 0; i < firsttitlearray59.length(); i++) {
                JSONObject obj59 = (JSONObject) firsttitlearray59.get(i);
                id = obj59.getString(Config.TAG_FIELD);
                field59.add(id);
            }
            createDynamicDatabase(Activity_Installation.this, tablename_59, field59);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            Log.e("exception", e.toString());
        }
        try {
            JSONObject jsonObject60 = new JSONObject(input);
            JSONArray firsttitlearray60 = jsonObject60.getJSONArray(Config.TAG_ARRAY_TABLE_60);
            for (int i = 0; i < firsttitlearray60.length(); i++) {
                JSONObject obj60 = (JSONObject) firsttitlearray60.get(i);
                id = obj60.getString(Config.TAG_FIELD);
                field60.add(id);
            }
            createDynamicDatabase(Activity_Installation.this, tablename_60, field60);

        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        //SystemClock.sleep(10000);
        // SystemClock.sleep(10000);
        /*need to be uncomment*/

        Toast.makeText(getApplicationContext(), "Tables Created", Toast.LENGTH_LONG).show();
        loading = new ProgressDialog(Activity_Installation.this);
        loading.setMessage("Thanks  Patient...");
        loading.setIndeterminate(false);
        loading.setCancelable(false);
        loading.show();
        startDatabaseProgress();
        Intent intent = new Intent(getApplicationContext(), MediaMainScreen.class);
        startActivity(intent);

    }

    public void startDatabaseProgress() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 5; i++) {
                    final int value = i;
                    initializeDatabaseprogress();
                    progress.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.setProgress(value);
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    private void
    initializeDatabaseprogress() {
        try {
            PushDataBase();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void PushDataBase() {
        try {
            Process p;
            p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            //os.writeBytes("cp /sdcard/Music/Db /data/data/com.RSPL.MEDIA/databases/ \n");
            os.writeBytes("cp -rf /sdcard/Music/Db /data/data/com.RSPL.MEDIA/databases/ \n");
            os.writeBytes("");
            os.flush();
            p.waitFor();

        } catch (Exception e) {

        }

    }


    public void createDynamicDatabase(Context context, String tableName, ArrayList<String> title) {
        Log.i("INSIDE LoginDatabase", "****creatLoginDatabase*****");
        try {
            int i;
            String querryString;
            File db_path = new File("/data/data/" + Activity_Installation.class.getPackage().getName() + "/databases/Db");
            db_path.getParentFile().mkdirs();
            myDataBase = org.sqlite.database.sqlite.SQLiteDatabase.openOrCreateDatabase(db_path, null);          //Opens database in writable mode.
            myDataBase.execSQL("PRAGMA key='Anaconda'");
            querryString = title.get(0) + " NVARCHAR(30),";
            Log.d("**createDynamicDatabase", "in oncreate");
            for (i = 1; i < title.size() - 1; i++) {
                querryString += title.get(i);
                querryString += " NVARCHAR(30) ";
                querryString += ",";
            }
            querryString += title.get(i) + " NVARCHAR(30) ";
            querryString = "CREATE TABLE IF NOT EXISTS " + tableName + "(" + querryString + ");";
            System.out.println("Create Table Stmt : " + querryString);
            myDataBase.execSQL(querryString);
            myDataBase.close();

        } catch (android.database.SQLException ex) {
            Log.i("CreateDB Exception ", ex.getMessage());
        }
    }

    int insertion_index = 0;

    public void insert(Context context, ArrayList<String> array_vals, ArrayList<String> title, String TABLE_NAME, int row_index) {
        Log.d("Inside Insert", "Insertion starts for table name: " + TABLE_NAME);
        //myDataBase = context.openOrCreateDatabase("Db", Context.MODE_WORLD_WRITEABLE, null);         //Opens database in writable mode.
        File db_path = new File("/data/data/" + Activity_Installation.class.getPackage().getName() + "/databases/Db");
        db_path.getParentFile().mkdirs();
        myDataBase = org.sqlite.database.sqlite.SQLiteDatabase.openOrCreateDatabase(db_path, null);          //Opens database in writable mode.
        myDataBase.execSQL("PRAGMA key='Anaconda'");
        String titleString = null;
        // String markString = "";
        int i = 0, c = 0;
        titleString = title.get(0) + ",";


        String markString = array_vals.toString().replace("[", "").replace("]", "");


        for (i = 1; i < title.size() - 1; i++) {
            titleString += title.get(i);
            titleString += ",";
        }


        Log.d("**createDynamicDatabase", markString);


        INSERT = "insert or replace into " + TABLE_NAME + " values " + markString;
        Log.d("********", INSERT);
        insertion_index++;


        this.insertStmt = this.myDataBase.compileStatement(INSERT);
        insertStmt.executeInsert();
        myDataBase.close();


    }

    private void getJSON() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Activity_Installation.this, "Fetching Data", "Wait...", false, false);


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                jsonparsed(s);

            }

            @Override
            protected String doInBackground(Void... params) {
                JSONParserSync rh = new JSONParserSync();

                //Production link for structure download from google server change on 18 sept 2017
                String s = rh.sendGetRequest("http://35.201.138.23:8080/Download/FmcgStrDownload.jsp");

                // String s = rh.sendGetRequest("http://35.201.138.23:8080/Upload_Conn/Testing_Str_JSON.jsp");

                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }


    @Override
    public void onClick(View v) {

        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.login:
                store_id = user.getText().toString().trim();
                String password = pass.getText().toString().trim();

                final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).build();

                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();
                Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                        .baseUrl("http://35.201.138.23:8080/Download/")

                        //   .baseUrl("http://35.194.196.229:8080/")


                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                GettingApi stackOverflowAPI = retrofit.create(GettingApi.class);
                Call<ConfigItems> call1 = stackOverflowAPI.load(store_id, password, "DATA");
                call1.enqueue(this);
                loading = new ProgressDialog(Activity_Installation.this);
                loading.setMessage("Thanks for Being Patient...");
                loading.setIndeterminate(false);
                loading.setCancelable(false);
                loading.show();
                loading.dismiss();

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_master_screen007, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                help_installation();
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }

    }


    public void help_installation() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.alertimage);
        alertDialog.setTitle("                              STORE ID");
        alertDialog.setMessage("\n" +
                "1.Objective:\n" +
                "\n" +
                "Store Id is the unique identification number which is given to a particular store. It is needed only for the first time login along with the ONE TIME PASSWORD(OTP).\n" +
                "\n" +
                "2.Input Description:\n" +
                "\n" +
                "          a.Store Id: User has to enter store id.\n" +
                "          b.Password: User has to enter one time password.\n" +
                "\n" +
                "3.Field Description:\n" +
                "\n" +
                "   Login: User wil navigate to the login page.");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }

}

