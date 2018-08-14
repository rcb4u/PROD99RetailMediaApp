package com.RSPL.MEDIA;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.serialport.api.SerialPortDataReceived;
import android.serialport.api.SerialPortHelper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.RSPL.MEDIA.Doc990.DocMainActivity;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ngx.DebugLog;
import com.ngx.USBPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sqlite.database.sqlite.SQLiteDatabase;
import org.sqlite.database.sqlite.SQLiteStatement;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import Config.Config;
import Config.ConfigItems;
import JSON.JSONParserSync;
import io.fabric.sdk.android.Fabric;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MediaMainScreen extends AppCompatActivity
        implements Runnable, ConnectivityReceiver.ConnectivityReceiverListener,
        Callback<ConfigItems> {

    DBhelper db = new DBhelper(this);

    public static Bundle b = new Bundle();
    public String Message_Testing_Data_Transfer;

    public static final int MESSAGE_FROM_SERIAL_PORT = 0;
    private static final String TAG = "BluetoothChat";
    public static final String SERIAL_PORT_MESSAGE = "";
    public static SerialPortHelper mSerialPortHelper;
    private Timer scrollTimer = null;
    private int scrollPos = 0;
    private int scrollMax;
    public String Ad_Play, Store_id, Store_Media_Id, startdate, enddate, Str_store_id, Str_store_media_id, Cust_id, Ad_Play_Click, Store_OTP;
    private static String starttime = null, endtime;
    public int VIDEO_INCREMENT = 0;
    ArrayList<Uri> main_ad_video = new ArrayList<Uri>();

    public SimpleDateFormat timeFormat;
    public SimpleDateFormat Addatetime, Addatetimeforclick;
    ListView listView;
    public static USBPrinter UsPrinter = USBPrinter.INSTANCE;
    private TimerTask scrollerSchedule;
    String videoname;
    public static HorizontalScrollView hsv1;
    public static LinearLayout ll1;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static TextView GrandTotal;
    ArrayList<VideoDetails_Model> videoDetails_Db = new ArrayList<>();
    ArrayList<VideoDetails_Model> videoDetails_Local = new ArrayList<>();

    private VideoView mVideoView;
    TextView store_Name;
    private AudioManager mAudManager;
    private String mConnectedDeviceName = "";
    public static final String title_connecting = "connecting...";
    public static final String title_connected_to = "connected: ";
    public static final String title_not_connected = "not connected";
    //   public static  final String RETAILVIDEODATA="retail_videodata";
//    public static  final String RETALMEDIACLICK="retail_media_click";
    static SecondBillAdapter adapter;

    static RelativeLayout relativeVideoView;
    static LinearLayout linearBillView;
    static LinearLayout mcashlayout;
    static TextView discount, netpayable, customer_name;

    String position;
    String method;
    String quantity;
    String sprice;
    public TextView mOverlapTextview;

    private String touchOn = null;
    private static final String videoMailFlag = "0";
    private String AdPlayUniqueId = null;
    private SimpleDateFormat simpleDateFormat = null;


    AlertDialog.Builder alert;
    String StoreIDForVideo;
    public Button submitUsrRes, btnCancel;
    public EditText editMobileNo, editcustName;
    String enteredMobNumber, enteredUserName;


    private TextView mUpdateTextView, mDownloadTextView;
    File localFile;
    String updateMessage, mUpdateMessageDownload;
    String mDeleteVideoStatus, mDeleteVideoName;

    String id;
    String Storeid;
    String Password;
    String storeName;
    String updatevideomsg;
    String mUpdateVideoValue;
    // Remote Config keys
    private static final String LOADING_PHRASE_CONFIG_KEY = "loading_phrase";
    private static final String UPDATE_MESSAGE_KEY = "update_message";
    private static final String FINAL_STATUS_MESSAGE = "update_video_final";
    private static final String SecondTimeVideoDownload = "true";

    //Delete Videos
    private static final String Delete_Video_Status = "delete_videos_status";
    private static final String Delete_Video_Name = "delete_video_name";
    private static final String Delete_Video = "true";


    private ArrayList<String> field6;
    private ArrayList<Config> data6;
    String tablename_6 = "retail_ad_ticker ";
    SQLiteDatabase myDataBase;
    SQLiteStatement insertStmt;
    SQLiteStatement updateStmt;

    String INSERT;
    ProgressDialog loading;
    String JSON_STRING;
    private ArrayList list;

    String product;
    Float price;
    String count;
    Float total;

    private TextView tvProdName;
    private TextView tvProdPrice;
    private TextView tvProdCount;
    public TextView tvProdPriceTotal;

    String finalTotal;
    String salesprice;
    ImageButton mDataSyncBtn;
    Button btnListFiles;


    private static final String UPDATE_MESSAGE_KEY_TICKER = "update_ad_ticker";
    private static final String MEDIA_ID_KEY = "Media_Id_Ad_Ticker";

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    StorageReference storageRe = storage.getReferenceFromUrl("gs://media-ce642.appspot.com").child("Media.apk");
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


    String updateAdfromServer, storeidvalueserver;
    private String Addata;
    String versionName;
    PackageInfo pinfo;
    ProgressBar progress;
    String MediaId;
    String StoreName;


    private ArrayList<Config> data59;
    private ArrayList<String> field59;

    ArrayList<String> localFiles;
    String tablename_59 = "AD_MAIN";
    /*******************************************/
    String storeAddress;
    String ApkUpgrade;
    String Apk_Version;

    //unique id broadcast by service
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra("VIDEO_ID");

            Log.e("______________>>>", type);


            for (int i = 0; i < videoDetails_Local.size(); i++) {

                String uriString = String.valueOf(videoDetails_Local.get(i).video);


                int index = uriString.lastIndexOf('/');
                String video_name = uriString.substring(index + 1, uriString.length());
                Log.e("______________>>>", "[" + type + "]  [" + video_name + "]");

                if (type.equals(video_name)) {
                    Log.e("______________>>>", "matched at  " + i + "  ------->>" + videoDetails_Local.get(i).video);
                    mVideoView = (VideoView) findViewById(R.id.SecScrVV1);
                    mVideoView.setVideoURI(Uri.parse(videoDetails_Local.get(i).video));
                    break;
                }

            }


            mVideoView.start();


        }
    };

    public Handler Handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case MESSAGE_FROM_SERIAL_PORT:
                    final String storename = msg.getData().getString(SERIAL_PORT_MESSAGE);
                    Log.e("@@@@@@DATA", storename);


                    if (storename.matches("")) {
                        return;
                    }
                    try {
                        JSONObject js = new JSONObject(storename);
                        method = js.getString("Method");

                        if (method.matches("Add")) {
                            product = js.getString("Product");
                            price = Float.valueOf(js.getString("Price"));
                            count = js.getString("Count");
                            total = Float.valueOf(js.getString("Total"));

                            finalTotal = String.valueOf(total);
                            salesprice = String.valueOf(price);

                            tvProdName.setText(product);
                            tvProdPrice.setText(salesprice);
                            tvProdCount.setText(count);
                            tvProdPriceTotal.setText(finalTotal);
                            Log.e("%%%%%%Sales Price", salesprice);
                            Log.e("%%%%%%Total", finalTotal);
                        }


                    /*if(method.matches("T")){
                        String name=  js.getString("G");

                        tvProdPriceTotal.setText(name);
                    }*/


                        if (method.matches("ShowBill")) {


                            showBillAtBottom();

                        }

                        if (method.matches("FullScreen")) {

                            fullScreenVideo();

                        }


                        if (method.matches("SPrice")) {
                            sprice = js.getString("SPriceUpdate");
                            total = Float.valueOf(js.getString("Total"));
                            position = js.getString("Position");

                            finalTotal = String.valueOf(total);
                            salesprice = String.valueOf(sprice);


                            tvProdPrice.setText(salesprice);

                            tvProdPriceTotal.setText(finalTotal);

                            Log.e("%%%%Sales Price", "after update" + salesprice);

                            Log.e("%%%%%%Total", "after sales price update" + finalTotal);


                        }


                        if (method.matches("Delete")) {
                            position = js.getString("PositionDelete");
                            if (position != null) {
                                deleteSalesProductfromList(Integer.parseInt(position));

                                total = Float.valueOf(js.getString("Total"));
                                count = js.getString("Count");
                                product = js.getString("Product");
                                price = Float.valueOf(js.getString("Price"));

                                finalTotal = String.valueOf(total);
                                salesprice = String.valueOf(price);
                                tvProdPriceTotal.setText(finalTotal);
                                tvProdCount.setText(count);
                                tvProdName.setText(product);
                                tvProdPrice.setText(salesprice);

                                Log.e("%%%%%%Total", "after sales price update" + finalTotal);

                            }

                        }
                        if (method.matches("FullScreen")) {
                            fullScreenVideo();
                        }
                        if (method.matches("ShowBill")) {
                            String name = js.getString("STORENAME");
                            // showBill(name);
                            showBillAtBottom();
                        }
                        if (method.matches("Mcash")) {
                            total = Float.valueOf(js.getString("Total"));
                            finalTotal = String.valueOf(total);
                            tvProdPriceTotal.setText(finalTotal);
                            Log.e("%%GrandTotal", " grandtotal for mcash" + finalTotal);
                            mcashpayforgoods(finalTotal);
                        }

                        if (method.matches("Frimi")) {
                            total = Float.valueOf(js.getString("Total"));
                            finalTotal = String.valueOf(total);
                            tvProdPriceTotal.setText(finalTotal);
                            Log.e("%%GrandTotal", " grandtotal for mcash" + finalTotal);
                            Frimipayments(finalTotal);
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;

            }
        }
    };

    public void showBill(String name) {
        try {


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);

            layoutParams.weight = 80.0f;
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams1.weight = 20.0f;

            relativeVideoView.setLayoutParams(layoutParams);
            linearBillView.setLayoutParams(layoutParams1);


            //  store_Name.setText(name);

        } catch (Exception e) {

        }
    }

    public void mcashpayforgoods(String name) {
        try {

            com.RSPL.MEDIA.MediaMainScreen.b.putString("Current_name", "mcash");
            Bundle bundle = new Bundle();
            bundle.putString("GrandTotal", finalTotal);
            PayForGoodsActivity mcashfragment = new PayForGoodsActivity();
            FragmentManager fragmentManager3 = getFragmentManager();
            android.app.FragmentTransaction transaction3 = fragmentManager3.beginTransaction();
            transaction3.add(R.id.linearlayouts, mcashfragment, "ALT");
            mcashfragment.setArguments(bundle);
            transaction3.commit();
        } catch (Exception e) {

        }
    }


    public void Frimipayments(String name) {
        try {
            com.RSPL.MEDIA.MediaMainScreen.b.putString("Current_name", "frimi");
            Bundle bundle = new Bundle();
            bundle.putString("GrandTotal", finalTotal);
            FrimiActivity frimifragment = new FrimiActivity();
            FragmentManager fragmentManager3 = getFragmentManager();
            android.app.FragmentTransaction transaction3 = fragmentManager3.beginTransaction();
            transaction3.add(R.id.linearlayouts, frimifragment, "ALT");
            frimifragment.setArguments(bundle);
            transaction3.commit();
        } catch (Exception e) {

        }
    }


    public void showBillAtBottom() {
        try {


            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            layoutParams.weight = 90.0f;
            relativeVideoView.setLayoutParams(layoutParams);

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            layoutParams1.weight = 10.0f;
            linearBillView.setLayoutParams(layoutParams1);


        } catch (Exception e) {


        }
    }


    public static void setSummaryRow() {
        DecimalFormat f = new DecimalFormat("##.00");
        float Getval = adapter.getGrandTotal();
        Log.d("&&&&&&&&", "" + Getval);
        String GrandVal = f.format(Getval);

        // tvProdPriceTotal.setText(GrandVal);

        netpayable.setText(String.valueOf(Getval - Float.parseFloat(discount.getText().toString())));


        Log.d("@@@@@@@@@@", "" + GrandTotal.getText().toString());
        Log.d("Net@@@@@", "" + netpayable.getText().toString());


    }

    public static void setSummaryRows(Float dis) {
        DecimalFormat f = new DecimalFormat("##.00");
        float Getval = adapter.getGrandTotal();
        Log.d("&&&&&&&&", "" + Getval);
        String GrandVal = f.format(Getval);
        Float tot = 0.0f;
        tot = Float.valueOf(discount.getText().toString());
        tot += dis;
        GrandTotal.setText(GrandVal);
        discount.setText(String.valueOf(tot));
        netpayable.setText(String.valueOf(Getval - tot));

        Log.d("@@@@@@@@@@", "" + GrandTotal.getText().toString());
        Log.d("Net@@@@@", "" + netpayable.getText().toString());


    }

    @SuppressLint("HandlerLeak")
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case USBPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case USBPrinter.STATE_CONNECTED:
                            Log.d("Connected to", title_connected_to);
                            break;
					/*case USBPrinter.STATE_CONNECTING:
						tvStatus.setText(title_connecting);
						break;
					case USBPrinter.STATE_LISTEN:*/
                        case USBPrinter.STATE_NONE:
                            Log.d("Not Connected", "No Success");
                            break;

                        //Bellow status message should be shown in different textview.
                        case USBPrinter.ACTION_SERIALPORT_CONNECTED:
                            Log.d("Connected", "Success Serial Port");
                            break;
                        case USBPrinter.ACTION_SERIALPORT_DISCONNECTED:
                            Log.d("DisConnected", "Serial Port Disconnected");
                            break;
                        case USBPrinter.ACTION_SERIALPORT_NOT_SUPPORTED:
                            Log.d("Invalid", "Not Supported");
                    }
                    break;
                case USBPrinter.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(
                            USBPrinter.DEVICE_NAME);
                    break;
                case USBPrinter.MESSAGE_STATUS:
                    Log.d("Status", msg.getData().getString(
                            USBPrinter.STATUS_TEXT));
                    break;

//for weighing scale
			/*case UsbSerialService.MESSAGE_FROM_SERIAL_PORT:
				String data = msg.getData().getString(UsbSerialService.WEIGHT_TEXT);
				//UnicodeFragment.showWeight.setText(data);
				TalkEachOtherFragment.txtReadData.append(data);

				break;*/
                default:
                    break;
            }
        }
    };

    public Boolean Connects() {


        return mSerialPortHelper.OpenSerialPort();
    }

    //  private String data="";
    int index = 0;
    byte[] processedData;
    List<Byte> dummyData = new ArrayList<>();
    boolean keepProcessing = true;
    private String serialPortData = "";
    private boolean isSerialPortConnected = false;
    static int countRev = 0, countSend = 0;
    ImageButton mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen);
        System.loadLibrary("sqliteX");
        tvProdName = (TextView) findViewById(R.id.tv_idProdName);
        tvProdPrice = (TextView) findViewById(R.id.tv_idProdPrice);
        tvProdCount = (TextView) findViewById(R.id.tv_idProdCount);
        tvProdPriceTotal = (TextView) findViewById(R.id.tv_idProdPriceTotal);
        mDataSyncBtn = (ImageButton) findViewById(R.id.dbPull);
        mButton = (ImageButton) findViewById(R.id.testingimage11);

        linearBillView = (LinearLayout) findViewById(R.id.id_linear_billview);


        mSerialPortHelper = new SerialPortHelper();
        if (!isSerialPortConnected) {
            if ((isSerialPortConnected = Connects())) {
            } else {
            }
        }
        read();

        mSerialPortHelper.mSerialPort.setOnserialportDataReceived(new SerialPortDataReceived() {


            @Override
            public void onDataReceivedListener(final byte[] buffer, int size) {
                Log.d("*****", "HEllo");
                for (int ind = 0; ind < size; ind++) {
                    if (buffer[ind] == SerialPortHelper.EOT) {
                        Log.i("NGX", "EOT Reached");
                        keepProcessing = false;
                        break;
                    } else {
                        String s = new String(new byte[]{buffer[ind]});
                        Log.i("NGX", "String Added - " + s);
                        dummyData.add(buffer[ind]);
                    }
                }
                if (!keepProcessing) {
                    Log.i("NGX", "KeepProcessing Stopped");
                    if (!dummyData.isEmpty()) {
                        processedData = new byte[dummyData.size()];
                        for (byte a : dummyData) {
                            String s = new String(new byte[]{a});
                            Log.i("NGX", "String Retrieved - " + s);
                            processedData[index++] = a;
                        }
                        serialPortData = new String(processedData);
                        countRev += processedData.length;
                        Log.i("NGX", "Data Received : " + serialPortData);
                        notifyHandler(serialPortData);
                        dummyData.clear();
                        processedData = null;
                    }
                    index = 0;
                    keepProcessing = true;
                }
            }
        });


        if (UsPrinter.getSerialPortState() == USBPrinter.ACTION_SERIALPORT_DISCONNECTED
                || UsPrinter.getSerialPortState() == USBPrinter.ACTION_SERIALPORT_NOT_SUPPORTED) {
            Connect();

        }

        field59 = new ArrayList<>();
        localFiles = new ArrayList<>();
        data59 = new ArrayList<>();

        field59 = db.getTableColumn();
        Log.e("Table Column", field59.toString());

        try {

            MediaStorePojo mStoreId = db.getStoreDetails();
            Storeid = mStoreId.getstoreId();
            Store_Media_Id = mStoreId.getMediaid();
            Password = mStoreId.getOTP();
            storeName = mStoreId.getStoreName();
            storeAddress = mStoreId.getStoreAddress();
            Store_OTP = mStoreId.getOTP();
            Log.e("StoreID :", Storeid);
            Log.e("StoreOTP :", Password);
            Log.e("StoreName :", storeName);

            Fabric.with(this, new Crashlytics());
            Crashlytics.setUserIdentifier(Storeid + "   " + storeName);


            mVideoView = (VideoView) findViewById(R.id.SecScrVV1);

            /* Button logo1 = (Button) findViewById(R.id.testingimage1);*/
            ImageButton logo3 = (ImageButton) findViewById(R.id.testingimage3);
            ImageButton logo10 = (ImageButton) findViewById(R.id.testingimage10);

          /*  ImageButton oDoc = (ImageButton) findViewById(R.id.btnodoc);

            oDoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    com.RSPL.MEDIA.MediaMainScreen.b.putString("Current_name", "mcash");

                    PayForGoodsActivity mcashfragment = new PayForGoodsActivity();
                    FragmentManager fragmentManager3 = getFragmentManager();
                    android.app.FragmentTransaction transaction3 = fragmentManager3.beginTransaction();
                    transaction3.add(R.id.linearlayouts,mcashfragment,"ALT");
                    transaction3.commit();

                }
            });*/


           /* logo1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    com.RSPL.MEDIA.MediaMainScreen.b.putString("Current_name", videoname);

                    AlertDialogfragment alertActivity = new AlertDialogfragment();
                    FragmentManager fragmentManager2 = getFragmentManager();
                    android.app.FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                    transaction2.add(R.id.linearlayouts, alertActivity, "ALT");
                    transaction2.commit();


                }
            });*/


            // hardcode data transfer abhigyan.....

           /* mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Frimipayments("2000");
                    writeDatafrimi();
                }
            });*/

            //////////////////////////////////


            logo3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    com.RSPL.MEDIA.MediaMainScreen.b.putString("Current_name", "CARGILL BANK");

                    CargillsBank cargillsBank = new CargillsBank();
                    FragmentManager fragmentManager2 = getFragmentManager();
                    android.app.FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                    transaction2.add(R.id.linearlayouts, cargillsBank, "ALT");
                    transaction2.commit();
                }
            });

            logo10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   /* TokenGenerationProcess();
                    DocMainActivity docMainActivity = new DocMainActivity();
                    FragmentManager fragmentManager = getFragmentManager();
                    android.app.FragmentTransaction transaction = fragmentManager. beginTransaction();
                    transaction.add(R.id.linearlayouts,docMainActivity,"Doc");
                    transaction.commit()*/
                    ;
                    GenieFragment frimifragment = new GenieFragment();
                    android.support.v4.app.FragmentManager fragmentManager4 = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction transaction4 = fragmentManager4.beginTransaction();
                    transaction4.add(R.id.linearlayouts, frimifragment, "ALT");

                    transaction4.commit();
                }
            });

            getFiles();
            // getJSON();
            ApkUpgrade = "Yes";
            try {
                PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
                Apk_Version = pInfo.versionName;

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            getApkVersionData(Store_Media_Id, storeName, storeAddress, Apk_Version, ApkUpgrade);

            mDataSyncBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   /* Intent btnUploaddbpull=new Intent(MediaMainScreen.this,MediaDataSync.class);
                    startActivity(btnUploaddbpull);
*/
                    MediaDataSync alertActivity = new MediaDataSync();
                    FragmentManager fragmentManager1 = getFragmentManager();
                    android.app.FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                    transaction1.replace(R.id.linearlayouts, alertActivity, "ALT");
                    transaction1.commit();


                }
            });


            if (field59 != null) {
                dataDownloadAdMain(Storeid, Password);
            }


            PerformClick();
            Intent intent = new Intent();
            intent.setAction("com.RSPL.MEDIA.AutoUpgradeService");
            startService(intent);
            init_local();
            startVideo();
            fullScreenVideo();
            deleteFiles();
            hideNav();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        return true;
    }


    private void fetchVideoData() {
        mUpdateVideoValue = mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_KEY);

        long cacheExpiration = 3600;
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {


                            Toast.makeText(MediaMainScreen.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(MediaMainScreen.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        CheckForNewUpdate();
                    }
                });

    }


    private void CheckForNewUpdate() {
        try {

            updateMessage = mFirebaseRemoteConfig.getString(UPDATE_MESSAGE_KEY);
            mUpdateMessageDownload = mFirebaseRemoteConfig.getString(FINAL_STATUS_MESSAGE);

            mDeleteVideoName = mFirebaseRemoteConfig.getString(Delete_Video_Name);
            //Delete Videos
            ref.child("DELETE VIDEO").child(Storeid + " : " + storeName + "    FILE NAME : " + mDeleteVideoName.replace(".", "").replace("_", "")).child("Status").setValue("Not Deleted");
            mDeleteVideoStatus = mFirebaseRemoteConfig.getString(Delete_Video_Status);

            List<String> myList = new ArrayList<String>(Arrays.asList(updateMessage.split(",")));
            for (int i = 0; i < myList.size(); i++) {
                StoreIDForVideo = myList.get(i);
                Log.e("@@STOREIDVALUE", StoreIDForVideo);
                if (Storeid.matches(StoreIDForVideo) && SecondTimeVideoDownload.matches(mUpdateMessageDownload)) {

                    ref.child("Videos").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            updatevideomsg = dataSnapshot.child("Video").getValue(String.class).toString();
                            ref.child("DOWNLOAD VIDEO").child(Storeid + " : " + storeName).child("Status").setValue("0");
                            getNewVideoFireBase(updatevideomsg);
                            Log.e("FILE FOUND", Storeid);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else if (SecondTimeVideoDownload.matches(mUpdateMessageDownload)) {
                    Log.e("FILENOT FOUND", Storeid);
                } else if (Storeid.matches(StoreIDForVideo) && Delete_Video.matches(mDeleteVideoStatus)) {
                    File rootPath = new File(Environment.getExternalStorageDirectory(), "1464772267/MainAd");
                    File localFile = new File(rootPath, mDeleteVideoName);
                    localFile.delete();
                    Log.e("StoreIDForVideo", StoreIDForVideo);
                    Log.e("StoreID", Storeid);


                    ref.child("DELETE VIDEO").child(Storeid + " : " + storeName + "    FILE NAME : " + mDeleteVideoName.replace(".", "").replace("_", "")).child("Status").setValue("Deleted");
                    Log.e("filename", mDeleteVideoName);

                    reStartMachineAfterVideoDelete();


                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void reStartMachineAfterVideoDelete() throws IOException, InterruptedException {

        Process p;
        p = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(p.getOutputStream());
        os.writeBytes("su -c reboot\n");
        os.flush();
        p.waitFor();


    }

    @Override
    public synchronized void onResume() {
        super.onResume();


        init_local();
        startVideo();
        fullScreenVideo();


        Log.e(TAG, "+ ON RESUME +");


    }

    public void AddSalesProducttoList(Sales result) {
        try {
            if (adapter == null) {
                Log.e("&&&&&&&&", "Product Adapter was null and HENCE Creating.....");
                adapter = new SecondBillAdapter(getApplicationContext()
                        , android.R.layout.simple_dropdown_item_1line, new ArrayList<Sales>());
                listView.setAdapter(adapter);
                /* ScrollviewSales.getListViewSize(listView);*/
            }
            int pos = adapter.addProductToList(result);
            Log.i("&&&&&&&&", "Adding " + result + " to Product List..");
            adapter.notifyDataSetChanged();
            setSummaryRow();
            listView.smoothScrollToPosition(pos);
        } catch (Exception e) {
        }
    }

    public void deleteSalesProductfromList(int result) {
        try {


            int pos = adapter.removeProductFromList(result);
            Log.i("&&&&&&&&", "Removing " + result + " From list..");
            adapter.notifyDataSetChanged();
            setSummaryRow();
            listView.smoothScrollToPosition(pos);


        } catch (Exception e) {
        }
    }

    public void updateQuantity(int position, int quan) {
        try {


            int pos = adapter.updateQuantityAtPosition(position, quan);
            adapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(pos);
            setSummaryRow();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSPrice(int position, int sp) {
        try {


            int pos = adapter.updateSPriceAtPosition(position, sp);
            adapter.notifyDataSetChanged();
            listView.smoothScrollToPosition(pos);
            setSummaryRow();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        DebugLog.logTrace("onStop");
        UsPrinter.disconnectSerialPortService();
    }

    public void setStore(String store) {

        store_Name.setText(store);
    }

    public void read() {
        if (isSerialPortConnected) {
            Log.i("NGX", "Read btn clicked");
            mSerialPortHelper.Read();
        }
    }

    public void Connect() {
        try {

            UsPrinter.connectSerialPort();
            Log.d("SuccessFull", "Connection");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyHandler(String message) {
        Log.d("NGX", message);
        if (Handler != null) {
            Message msg = mHandler.obtainMessage(MESSAGE_FROM_SERIAL_PORT);
            Bundle bundle = new Bundle();
            bundle.putString(SERIAL_PORT_MESSAGE, message);
            msg.setData(bundle);
            Handler.sendMessage(msg);
        }
    }

    @Override
    public void run() {

    }


    private class ReadThread extends Thread {
        private AtomicBoolean working;
        private Handler mHandler;

        ReadThread(Handler pHandler) {
            working = new AtomicBoolean(true);
            if (pHandler != null)
                mHandler = pHandler;
        }

        private void notifyHandler(String message) {
            Log.d("NGX", message);
            if (mHandler != null) {
                Message msg = mHandler.obtainMessage(MESSAGE_FROM_SERIAL_PORT);
                Bundle bundle = new Bundle();
                bundle.putString(SERIAL_PORT_MESSAGE, message);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }
        }

        boolean keepRunning = true;
        List<Byte> byteList = new ArrayList<>();
        int i = 0;
        byte[] data;
        String stri;
        byte[] dummyData;

        @Override
        public void run() {
            while (working.get()) {
                try {
                    while (keepRunning) {
                        data = UsPrinter.readDataFromSerialPort();
                        if (data != null) {
                            for (byte a : data
                                    ) {
                                if (a == (byte) 0x03) {
                                    keepRunning = false;
                                    break;
                                }
                                Log.i("NGX", "byte added");
                                byteList.add(a);
                            }
                        }
                    }
                    if (!keepRunning) {
                        if (!byteList.isEmpty()) {
                            // Log.i("NGX","list lenght "+ byteList.size());

                            dummyData = new byte[byteList.size()];

                            for (byte b : byteList
                                    ) {

                                dummyData[i++] = b;

                            }
                            i = 0;
                            byteList.clear();
                            ;

                            stri = new String(dummyData);
                            //txtReadData.append(str);
                            Log.d("Received", stri);
                            Log.d("Reading", stri);


                            notifyHandler(stri);

                        }
                        keepRunning = true;
                        Log.i("NGX", "SET TRUE FOR KEEPRUNNING");
                        Thread.currentThread().sleep(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }


        public void stopThread() {
            working.set(false);
            Log.i("NGX", "Data Read stopped");
        }

    }

    public void fullScreenVideo() {
        try {


            tvProdName.setText("PRODUCT_NAME");
            tvProdPrice.setText("P_PRICE");
            tvProdCount.setText("COUNT");
            tvProdPriceTotal.setText("TOTAL");


        } catch (Exception e) {
        }
    }

    public static void clearTotal() {

        GrandTotal.setText("0.0");
        discount.setText("0.0");
        netpayable.setText("0.0");
        customer_name.setText("");
        if (adapter != null) {
            adapter.clearAllRows();
        }
      /*  else if(salesReturnAdapter!=null){
            salesReturnAdapter.clearAllRows();
        }
        else if(secondSalesReturnAdapterWithotInvoiceNumber!=null){
            secondSalesReturnAdapterWithotInvoiceNumber.clearAllRows();
        }*/
        else {
        }

    }

    /*=========================================================================================*/
    public void init_local() {

        File videodir = new File(Environment.getExternalStorageDirectory() + "/1464772267" + "/MainAd");
        Log.e("####******########", videodir.toString());

        if (videodir.exists() && videodir.isDirectory()) {
            final File[] files = videodir.listFiles();

            videoDetails_Local.clear();

            if (files != null) {
                for (File file : files) {
                    if (file != null) {
                        if (file.isDirectory()) {  // it is a folder...
                        } else {  // it is a file...

                            VideoDetails_Model tempObj = new VideoDetails_Model();

                            tempObj.video = file.toString();

                            Log.e("@@@myAll before files", file.getName());

                            //  Collections.shuffle(videoDetails_Local);

                            videoDetails_Local.add(tempObj);
/////////////////////////////////ad sequence jimmy///////////////////////////////////////////////////////
                            Collections.sort(videoDetails_Local, new Comparator<VideoDetails_Model>() {
                                @Override
                                public int compare(VideoDetails_Model videoDetails_model, VideoDetails_Model t1) {
                                    return videoDetails_model.video.compareTo(t1.video);
                                }
                            });

                            Log.e("@@@after suffle files", file.getName());


                        }
                    }
                }
            }

        } else {
            videodir.mkdir();
        }
    }


    public void startVideo() {

        try {

            Log.e("startVideo", "Called");

            Date date = new Date();
            final CharSequence s = DateFormat.format("yyyy-MM-dd ", date.getTime());
            final Calendar c1 = Calendar.getInstance();
            timeFormat = new SimpleDateFormat("HH:mm:ss");
            Addatetime = new SimpleDateFormat("yyyyMMDDHHmmssmm");
            MediaStorePojo mStoreId = db.getStoreDetails();
            Str_store_id = mStoreId.getstoreId();
            Str_store_media_id = mStoreId.getMediaid();


            mVideoView = (VideoView) findViewById(R.id.SecScrVV1);

            insertVideoName();


            mVideoView.start();

            startdate = s.toString();
            starttime = timeFormat.format(c1.getTime());
            Ad_Play = Addatetime.format(c1.getTimeInMillis());

            Store_id = Str_store_id;
            Str_store_media_id =

                    Store_Media_Id = Str_store_media_id;

            // call setOnPreparedListener for set default mute option........
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {

                    mp.setVolume(0, 0);
                }
            });


            mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Calendar c2 = Calendar.getInstance();
                    enddate = s.toString();
                    endtime = timeFormat.format(c2.getTimeInMillis());
                    String SFlag = "0";
                    //video details insert from here...

                    db.insertVideoData(Ad_Play, Store_id, Store_Media_Id, videoname, startdate, enddate, starttime, endtime, SFlag);

                    VIDEO_INCREMENT++;
                    if (VIDEO_INCREMENT < videoDetails_Local.size()) {
                        insertVideoName();
                        mVideoView.start();
                        Calendar c3 = Calendar.getInstance();
                        Ad_Play = Addatetime.format(c3.getTimeInMillis());
                        startdate = s.toString();
                        enddate = s.toString();
                        starttime = timeFormat.format(c3.getTime());
                        endtime = timeFormat.format(c3.getTime());


                    } else {
                        VIDEO_INCREMENT = 0;
                        insertVideoName();
                        mVideoView.start();
                        Calendar c4 = Calendar.getInstance();
                        startdate = s.toString();
                        Ad_Play = Addatetime.format(c4.getTimeInMillis());
                        starttime = timeFormat.format(c4.getTime());
                        enddate = s.toString();
                        endtime = timeFormat.format(c4.getTime());
                    }
                }
            });

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Video not found", Toast.LENGTH_SHORT).show();
        }


    }

    public void insertVideoName() {
        mVideoView.setVideoURI(Uri.parse(videoDetails_Local.get(VIDEO_INCREMENT).video));

        Uri strvideo = Uri.parse(videoDetails_Local.get(VIDEO_INCREMENT).video);
        String strVideoName = strvideo.getLastPathSegment();
        videoname = strVideoName.substring(0, strVideoName.length() - 4);
        Log.e("Ad Name After Trim", videoname);

        if (videoname.startsWith("T") || videoname.startsWith("V") || videoname.startsWith("Th") || videoname.startsWith("M")) {
           /* mOverlapTextview.setVisibility(View.VISIBLE);
            Animation a = AnimationUtils.loadAnimation(this, R.anim.offer_animation);
            mOverlapTextview.startAnimation(a);
            mOverlapTextview.setText("Are you interested? Touch Here");*/
            mVideoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    com.RSPL.MEDIA.MediaMainScreen.b.putString("Current_name", videoname);


                    AlertDialogfragment alertActivity = new AlertDialogfragment();
                    FragmentManager fragmentManager2 = getFragmentManager();
                    android.app.FragmentTransaction transaction2 = fragmentManager2.beginTransaction();
                    transaction2.replace(R.id.linearlayouts, alertActivity, "ALT");
                    transaction2.commit();

                    return false;
                }
            });
        } else {

           /* mOverlapTextview.setVisibility(View.INVISIBLE);
            mOverlapTextview.clearAnimation();*/
            mVideoView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }

    }


    public void createVideoFolder() {

        //  main_ad_video folder Name Specify Here
        File videodir = new File(Environment.getExternalStorageDirectory() + "/1464772267" + "/MainAd");
        Log.e("####******########", videodir.toString());
        final File[] filest = videodir.listFiles();
        if (videodir.exists() && videodir.isDirectory()) {
            final File[] files = videodir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file != null) {
                        if (file.isDirectory()) {  // it is a folder...
                        } else {  // it is a file...
                            main_ad_video.add(Uri.fromFile(file));
                        }
                    }
                }
            }

        } else {
            videodir.mkdir();
        }


    }


    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {

            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                mAudManager.abandonAudioFocus(afChangeListener);
            }
        }
    };


    public void startAutoScrolling() {
        if (scrollTimer == null) {
            scrollTimer = new Timer();
            final Runnable Timer_Tick = new Runnable() {
                public void run() {
                    moveScrollView();
                    // hsv1.smoothScrollTo(ll1.getChildAt(11).getLeft(), 0);

                }
            };

            if (scrollerSchedule != null) {
                scrollerSchedule.cancel();
                scrollerSchedule = null;
            }
            scrollerSchedule = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(Timer_Tick);
                }
            };

            scrollTimer.schedule(scrollerSchedule, 10, 10);
        }
    }


    public void moveScrollView() {
        scrollPos = (int) (hsv1.getScrollX() + 1.0);
        if (scrollPos >= scrollMax) {
            scrollPos = 0;

        }
        hsv1.scrollTo(scrollPos, 0);


    }


    public void stopAutoScrolling() {
        if (scrollTimer != null) {
            scrollTimer.cancel();
            scrollTimer = null;
        }
    }

    public void getScrollMaxAmount() {
        int actualWidth = (ll1.getMeasuredWidth() - 2050);
        scrollMax = actualWidth;
    }

    public void PerformClick() {
        class ConnectionSerial extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Connect();
                read();

            }

            @Override
            protected String doInBackground(Void... params) {
                UsPrinter.initServices(getApplicationContext(), mHandler);
                UsPrinter.initSerialPortServices();


                return null;
            }
        }

        ConnectionSerial updateShell = new ConnectionSerial();
        updateShell.execute();

    }

    /*http://99sync.eu-gb.mybluemix.net/Retail_Video_Data.jsp*/


   /* public void uploadVideoDetails() {


        class UpdateProduct extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            int success;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // loading = ProgressDialog.show(ActivityProduct.this, "UPDATING...", "Wait...", false, false);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //  loading.dismiss();
                // Toast.makeText(ActivityProduct.this, s, Toast.LENGTH_LONG).show();

            }
            @Override
            protected String doInBackground(Void... params) {
                try{


                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(Config.RETAIL_VIDEODATA_STORE_ID,Store_id);
                    hashMap.put(Config.RETAIL_VIDEODATA_MEDIA_ID,Store_Media_Id);
                    hashMap.put(Config.RETAIL_VIDEODATA_AD_PLAY,Ad_Play);
                    hashMap.put(Config.RETAIL_VIDEODATA_FILE_NAME,videoname);
                    hashMap.put(Config.RETAIL_VIDEODATA_START_TIME,starttime);
                    hashMap.put(Config.RETAIL_VIDEODATA_END_TIME,endtime);
                    hashMap.put(Config.RETAIL_VIDEODATA_START_DATE,startdate);
                    hashMap.put(Config.RETAIL_VIDEODATA_END_DATE,enddate);

                    Log.e("video",videoname);
                    Log.e("start",starttime);
                    Log.e("end",endtime);

                    Log.e("-->>UPLOAD_STORE_ID",Store_id);
                    Log.e("-->>UPLOAD_MEDIA_ID",Store_Media_Id);
                    Log.e("-->>UPLOAD_AD_PLAY",Ad_Play);
                    Log.e("-->>UPLOAD_FILE_NAME",videoname);
                    Log.e("-->>UPLOAD_START_TIME",starttime);
                    Log.e("-->>UPLOAD_END_TIME",endtime);
                    Log.e("-->>UPLOAD_START_DATE",startdate);
                    Log.e("-->>UPLOAD_END_DATE",enddate);


                    JSONParserSync rh = new JSONParserSync();

                    // For Development
                    JSONObject s = rh.sendPostRequest("http://35.201.138.23:8080/Upload_Conn/Retail_Video_Data.jsp",hashMap);

                    Log.d("Login attempt", s.toString());

                    // success tag for json
                    success = s.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("VIDEO DATA Successfully","SENT"+ s.toString());
                        return s.getString(TAG_MESSAGE);
                    } else {

                        return s.getString(TAG_MESSAGE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;            }
        }
        UpdateProduct updateproduct = new UpdateProduct();
        updateproduct.execute();
    }*/

    public void hideNav() {

        try {
            Process p;
            p = Runtime.getRuntime().exec("su");

            // Attempt to write a file to a root-only
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("service call activity 42 s16 com.android.systemui\n");

            // Close the terminal
            os.writeBytes("exit\n");
            os.flush();
            p.waitFor();

        } catch (Exception e) {

        }

    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        int color;
        if (isConnected) {
            String message = "Good! Connected to Internet";
            Log.d("Message", message);
            uploadData();
        } else {
            String messages = "Sorry! Not connected to internet";
            Log.d("Message", messages);
            color = Color.RED;
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) throws InterruptedException {
        showSnack(isConnected);
    }


    public void uploadData() {
        class uploadmediaData extends AsyncTask<Void, Void, String> {
            ArrayList<String> get_retail_videodata = new ArrayList<>();
            ArrayList<String> get_retail_media_click = new ArrayList<>();


            ProgressDialog loading;
            int success;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                get_retail_videodata = db.retailVideoData();
                get_retail_media_click = db.retailMediaClick();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(Void... params) {


                HashMap<String, String> uploadRetailVideoData = new HashMap<>();
                HashMap<String, String> uploadRetailMediaClick = new HashMap<>();

                StringBuilder sb = new StringBuilder(128);
                for (String value : get_retail_videodata) {
                    if (sb.length() > 0) {
                        sb.append("|");
                    }
                    sb.append(value);
                }
                sb.insert(0, "|");

                StringBuilder sb2 = new StringBuilder(128);
                for (String value : get_retail_media_click) {
                    if (sb2.length() > 0) {
                        sb2.append("|");
                    }
                    sb2.append(value);
                }
                sb2.insert(0, "|");


                SQLiteDatabase DataBase;
                File db_path = new File("/data/data/" + Activity_Installation.class.getPackage().getName() + "/databases/Db");
                db_path.getParentFile().mkdirs();
                DataBase = SQLiteDatabase.openOrCreateDatabase(db_path, null);         //Opens database in writable mode.
                DataBase.execSQL("PRAGMA key='Anaconda'");

                uploadRetailVideoData.put("retail_videodata", String.valueOf(sb));
                uploadRetailMediaClick.put("retail_media_click", String.valueOf(sb2));


                Log.d("*****", String.valueOf(sb2));
                JSONParserSync jsonParserSync2 = new JSONParserSync();
                JSONObject s2 = jsonParserSync2.sendPostRequest("http://35.201.138.23:8080/Upload_Conn/Retail_Media_Click_Pipline.jsp", uploadRetailMediaClick);
                try {
                    String succes = s2.getString("success");
                    Log.d("Resposnse", String.valueOf(succes) + " " + "retail_media_click");
                    if (succes.equals("1")) {
                        DataBase.execSQL("update retail_media_click set S_FLAG='1'");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Log.d("*****", String.valueOf(sb));
                JSONParserSync jsonParserSync = new JSONParserSync();
                JSONObject s = jsonParserSync.sendPostRequest("http://35.201.138.23:8080/Upload_Conn/Retail_Video_Data_Pipline.jsp", uploadRetailVideoData);
                try {
                    String succes = s.getString("success");
                    if (succes.equals("1")) {
                        Log.d("Resposnse", succes + " " + "retail_videodata");
                        DataBase.execSQL("update retail_videodata set S_FLAG='1'");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                DataBase.close();
                return null;
            }
        }
        uploadmediaData upload = new uploadmediaData();
        upload.execute();
    }



   /* public void AlertDialog() {

        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.custom_popup, null);
        alert = new AlertDialog.Builder(this);

        alert.setView(alertLayout);
        final AlertDialog dialog = alert.create();

        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
       // mVideoView.pause();


        touchOn=videoname.toString().trim();

        Log.d("@@@@VIDEO NAME OnTOUCH",touchOn);

        Calendar calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyyMMDDHHmmssmm");
        AdPlayUniqueId = simpleDateFormat.format(calendar.getTimeInMillis());
        Log.e("@@@@@@@@AD_PlayID:","--------->"+AdPlayUniqueId);


        //Custom Dialog hide automatically after 30 seconds......
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    mVideoView.start();
                }
            }
        };

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 30000);//  30  seconds define here
        btnCancel = (Button)alertLayout. findViewById(R.id.id_btn_cancel);
        submitUsrRes = (Button) alertLayout.findViewById(R.id.button_submit);
        editMobileNo = (EditText)alertLayout.findViewById(R.id.edit_customermobile);
        editcustName = (EditText)alertLayout.findViewById(R.id.edit_customername);


        submitUsrRes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (editMobileNo.getText().toString().equals("")) {

                       *//* Toast.makeText(getApplicationContext(), "Please Fill Mobile Number and Name", Toast.LENGTH_SHORT).show();*//*
                    editMobileNo.setError("Please Fill Mobile Number");
                    Animation shake = AnimationUtils.loadAnimation(MediaMainScreen.this, R.anim.alert_animation);
                    editMobileNo.startAnimation(shake);

                    return;
                }

                if(editMobileNo.length() < 9 || editMobileNo.length() > 10){


                    editMobileNo.setError("Please Fill Valid Mobile Number");
                    Animation shake = AnimationUtils.loadAnimation(MediaMainScreen.this, R.anim.alert_animation);
                    editMobileNo.startAnimation(shake);
                    return;

                }
                if (editcustName.getText().toString().equals("")) {

                    editcustName.setError("Please Fill Name");
                    Animation shake = AnimationUtils.loadAnimation(MediaMainScreen.this, R.anim.alert_animation);
                    editcustName.startAnimation(shake);

                    return;
                }
                enteredUserName=editcustName.getText().toString();

                if(!enteredUserName.matches("[a-zA-Z ']+")){

                    editcustName.setError("Please Fill Valid Name");
                    Animation shake = AnimationUtils.loadAnimation(MediaMainScreen.this, R.anim.alert_animation);
                    editcustName.startAnimation(shake);

                    return;}


                enteredMobNumber=editMobileNo.getText().toString();



                if(db.CheckIsDataAlreadyInDBorNot(enteredMobNumber,touchOn)== true){


                    editMobileNo.setError("Your Number is Already registered with us");
                    Animation shake = AnimationUtils.loadAnimation(MediaMainScreen.this, R.anim.alert_animation);
                    editMobileNo.startAnimation(shake);


                    return;

                }

                else {
                    DBhelper dBhelper= new DBhelper(getApplicationContext());
                    enteredMobNumber=editMobileNo.getText().toString();
                    enteredUserName=editcustName.getText().toString();

                    dBhelper.insertUserResponse(AdPlayUniqueId,Str_store_media_id,enteredMobNumber,enteredUserName,touchOn,videoMailFlag);
                    //uploadUserResponse();

                    Toast.makeText(getApplicationContext(), "Thanks for showing interest", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    mVideoView.start();

                }}
        });




        //  dialog.setCanceledOnTouchOutside(false);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.start();

                dialog.dismiss();

            }
        });


        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.RIGHT|Gravity.CENTER_VERTICAL;

        wmlp.height=450;
        wmlp.width=400;

        dialog.getWindow().setAttributes(wmlp);



    }
*/


    public void DeleteReceiver() {

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("WrongConstant") int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                Intent intent = new Intent();
                intent.setAction("com.RSPL.MEDIA.UpdateMediaDbReceiver");
                sendBroadcast(intent);

            case Calendar.TUESDAY:
                Intent intent1 = new Intent();
                intent1.setAction("com.RSPL.MEDIA.UpdateMediaDbReceiver");
                sendBroadcast(intent1);

        }

    }

    private void getNewVideoFireBase(final String name) {
        class GetJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }

            @Override
            protected String doInBackground(Void... params) {

                StorageReference videostorage = storageRe.child("Videos").child(name);

                File rootPath = new File(Environment.getExternalStorageDirectory(), "1464772267/MainAd");
                if (!rootPath.exists()) {
                    rootPath.mkdirs();
                }

                final File localFile = new File(rootPath, name);

                videostorage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.e("firebase Video", "Video Downloaded" + localFile.toString());
                        ref.child("DOWNLOAD VIDEO").child(Storeid + " : " + storeName).child("Status").setValue("1");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("firebase ", "Cannot Download Video" + exception.toString());
                        ref.child(Storeid).child("Status").setValue(exception.toString());

                    }
                });
                return String.valueOf(localFile);
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();

    }

    public void getFiles() {
        File mfile = new File("/sdcard/1464772267/MainAd");
        File[] list = mfile.listFiles();

        System.out.println("No.of Files :" + mfile.listFiles().length);
        for (int i = 0; i < mfile.listFiles().length; i++) {
            if (list[i].isHidden()) ;
            Log.d("------->", " List of Files.. " + list[i].getName());

            localFiles.add(list[i].getName());
            Log.e("Local Files", localFiles.toString());
        }
    }

    public void ApplicationStatus() {
        ref.child("APPLICATION STATUS").child(Storeid + " : " + storeName).child("Status").setValue("1");
    }


    public void checkforUpdate() {
        // Create Remote Config Setting to enable developer mode.
        // Fetching configs from the server is normally limited to 5 requests per hour.
        // Enabling developer mode allows many more requests to be made per hour, so developers
        // can test different config values during development.
        // [START enable_dev_mode]
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        // [END enable_dev_mode]

        // Set default Remote Config values. In general you should have in app defaults for all
        // values that you may configure using Remote Config later on. The idea is that you
        // use the in app defaults and when you need to adjust those defaults, you set an updated
        // value in the App Manager console. Then the next time you application fetches from the
        // server, the updated value will be used. You can set defaults via an xml file like done
        // here or you can set defaults inline by using one of the other setDefaults methods.S
        // [START set_default_values]
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        // [END set_default_values]
        //  String welcomemessageserver = mFirebaseRemoteConfig.getString(LOADING_PHRASE_CONFIG_KEY);

        long cacheExpiration = 3600; // 1 hour in seconds.
        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        // [START fetch_config_with_callback]
        // cacheExpirationSeconds is set to cacheExpiration here, indicating that any previously
        // fetched and cached config would be considered expired because it would have been fetched
        // more than cacheExpiration seconds ago. Thus the next fetch would go to the server unless
        // throttling is in progress. The default expiration duration is 43200 (12 hours).
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Log.e("SERVERRESPONSE", task.toString());

                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Log.e("SERVERRESPONSE", task.toString());
                        }
                        MediaStorePojo mStoreId = db.getStoreDetails();
                        MediaId = mStoreId.getMediaid();
                        updateAdfromServer = mFirebaseRemoteConfig.getString(UPDATE_MESSAGE_KEY_TICKER);
                        storeidvalueserver = mFirebaseRemoteConfig.getString(MEDIA_ID_KEY);
                        Log.e("####updateAdfromServer", updateAdfromServer);

                        Addata = updateAdfromServer.toString().trim();

                        Log.e("####Addata", Addata);

                        Log.e("VALUEFROMDATABASE", MediaId);
                    }
                });
        // [END fetch_config_with_callback]
    }

    public void dataDownloadAdMain(String StoreId, String Otp) {
        Log.e("*********", "ADMAINDATADOWNLOAD");

        // store_id=user.getText().toString().trim();
        //  String password = pass.getText().toString().trim();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS).build();
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl("http://35.201.138.23:8080/Download/")
                //   .baseUrl("http://35.194.196.229:8080/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        AdMainDataApi stackOverflowAPI = retrofit.create(AdMainDataApi.class);
        Call<ConfigItems> call1 = stackOverflowAPI.adMainData(StoreId, Otp, "DATA");
        call1.enqueue(this);


    }

    @Override
    public void onResponse(Call<ConfigItems> call, Response<ConfigItems> response) {
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

                insert(MediaMainScreen.this, list, field59, tablename_59, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e("Exception in table 59", e.getMessage());
        }


    }

    @Override
    public void onFailure(Call<ConfigItems> call, Throwable t) {
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
        Log.d("**createDynamicDatabase", "in oncreate");
        INSERT = "insert or replace into " + TABLE_NAME + " values " + markString;
        Log.d("Data in ad Main", "insert or replace into " + TABLE_NAME + " values " + markString);
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
                loading = ProgressDialog.show(MediaMainScreen.this, "Fetching Data", "Wait...", false, false);
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
                String s = rh.sendGetRequest("http://35.201.138.23:8080/Upload_Conn/Testing_Str_JSON.jsp");

                //String s = rh.sendGetRequest("http://35.194.196.229:8080/media_mysql_testing/Testing_Str_JSON.jsp");

                //      String s = rh.sendGetRequest("http://99sync.eu-gb.mybluemix.net/Testing_Str_JSON.jsp");
                return s;

            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    public void jsonparsed(String input) {
        try {
            JSONObject jsonObject59 = new JSONObject(input);
            JSONArray firsttitlearray59 = jsonObject59.getJSONArray(Config.TAG_ARRAY_TABLE_59);
            for (int i = 0; i < firsttitlearray59.length(); i++) {
                JSONObject obj59 = (JSONObject) firsttitlearray59.get(i);
                id = obj59.getString(Config.TAG_FIELD);
                field59.add(id);
            }
            createDynamicDatabase(MediaMainScreen.this, tablename_59, field59);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (android.database.SQLException e) {
            Log.e("exception", e.toString());
        }

        //Toast.makeText(getApplicationContext(), "Tables Created", Toast.LENGTH_LONG).show();
        loading = new ProgressDialog(MediaMainScreen.this);
        loading.setMessage("Thanks  Patient...");
        loading.setIndeterminate(false);
        loading.setCancelable(false);

        loading.show();
        loading.dismiss();
    }

    public void deleteFiles() {
        ArrayList<String> arrayList = db.getretailAdMainforDelete();
        arrayList.retainAll(localFiles);
        Log.e("&&Delete file name", arrayList.toString());
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList != null) {

                File mfile = new File("/sdcard/1464772267/MainAd");
                File file = new File(mfile, arrayList.get(i));
                file.delete();
                Log.e("File Deleted", arrayList.get(i));
            }
            uploadDeleteStatus(Store_Media_Id, storeName, storeAddress, arrayList.get(i));
        }


        db.AdMainforDelete();
    }

    public void uploadDeleteStatus(final String store_Media_Id,
                                   final String storeName,
                                   final String StoreAddress,
                                   final String AdFileName) {


        class UpdateProduct extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            int success;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // loading = ProgressDialog.show(ActivityProduct.this, "UPDATING...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //  loading.dismiss();
                // Toast.makeText(ActivityProduct.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("Store_Media_Id", store_Media_Id);
                    hashMap.put("Store_Name", storeName);
                    hashMap.put("Store_Address", StoreAddress);
                    hashMap.put("Ad_Name", AdFileName);

                    JSONParserSync rh = new JSONParserSync();
                    JSONObject s = rh.sendPostRequest("http://35.201.138.23:8080/Media_Delete/Delete_Report.jsp", hashMap);


                    Log.d("Login attempt", s.toString());

                    // success tag for json
                    success = s.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Response Sent.!", s.toString());
                        return s.getString(TAG_MESSAGE);
                    } else {
                        return s.getString(TAG_MESSAGE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }
        UpdateProduct updateproduct = new UpdateProduct();
        updateproduct.execute();
    }

    private void getApkVersionData(String store_id, String store_Nm, String store_Address, String apk_Version, String apkUpgrade) {

        Log.e("Store_id", store_id);
        Log.e("Store_Nm", store_Nm);
        Log.e("Store_Address", store_Address);
        Log.e("ApkUpgrade", apkUpgrade);
        Log.e("Apk_Version", apk_Version);


        Retrofit.Builder retrofit = new Retrofit.Builder()
                .baseUrl("http://35.201.138.23:8080/Operation/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit1 = retrofit.build();

        MediaApkVersion apkVersion = retrofit1.create(MediaApkVersion.class);

        Call<MediaApkVersionModel> versionCall = apkVersion.loadMediaVersion(store_id, store_Nm, store_Address, apk_Version, apkUpgrade, "DATA");

        versionCall.enqueue(new Callback<MediaApkVersionModel>() {
            @Override
            public void onResponse(Call<MediaApkVersionModel> call, Response<MediaApkVersionModel> response) {
                Log.d("Operation Success", response.message());
            }

            @Override
            public void onFailure(Call<MediaApkVersionModel> call, Throwable t) {
                Log.d("Operation", "Failure");
            }
        });


    }

    public void TokenGenerationProcess() {
        class WaitingforResponse extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //  progressDialog = ProgressDialog.show(getActivity().getBaseContext(), "Waiting for Token Generation...", "Please Wait...", false, false);
            }

            @Override
            protected String doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                String json = "";
                RequestBody body = RequestBody.create(mediaType, json);
               /* https://ideabiz.lk/apicall/token?grant_type=password&username=99R_User&password=99R_User&scope=SANDBOX

                https://ideabiz.lk/apicall/token?grant_type=refresh_token&refresh_token="+Message+"&scope=SANDBOX */
                // Message="d6a5be5f5455c87c21442ba3d5201c8c";
                Request request = new Request.Builder()
                        .url("https://ideabiz.lk/apicall/token?grant_type=password&username=99R_User&password=99R_User&scope=PRODUCTION")

                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("authorization", "Bearer ZnhWM0Uzb2xuRjBld2RFb2liTjBZMlpRMWVvYTppQUZWM1VsVjd1VTVFS1VTdEpaTWU2Z3VaR0Vh")
                        .post(body)
                        .build();
//Bearer aUFVMWFRd2Vpb2txQUI2VTFkajBFZWRkTTBZYTpEMzZfbU9GM2ZlYkFEaWRiMGp0ZUZCN2xGUUlh")


                try {
                    okhttp3.Response response = client.newCall(request).execute();
                    String test = response.body().string();
                    if (response.isSuccessful()) {
                        int success = response.code();

                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++) {

                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }

                        System.out.println(test);
                        ///   {"scope":"default","token_type":"bearer","expires_in":3600,"refresh_token":"2d23c431f25e10e5abcd16bea931d0a","access_token":"b0bffddb2be384c53135cadf0f565c2"}
                        try {
                            JSONObject jsonObject = new JSONObject(test);
                            Log.e(" scope", "" + jsonObject.get("scope"));
                            Log.e(" token_type", "" + jsonObject.get("token_type"));
                            Log.e(" expires_in", "" + jsonObject.get("expires_in"));
                            Log.e(" refresh_token", "" + jsonObject.get("refresh_token"));
                            Log.e(" access_token", "" + jsonObject.get("access_token"));
                            // Message = (String) jsonObject.get("refresh_token");

                            String access_token = (String) jsonObject.get("access_token");
                            String refresh_token = (String) jsonObject.get("refresh_token");
                            PersistenceManager.saveRefreshToken(getApplicationContext(), refresh_token);
                            PersistenceManager.saveSessionId(getApplicationContext(), access_token);
                            PersistenceManager.saveTime(getApplicationContext(), String.valueOf(System.currentTimeMillis()));
                            // reference= (String) jsonObject.get("Refno");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println(test);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                // progressDialog.dismiss();
            }


        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute();

    }

// hardcode data transfer


    public void writeDatafrimi() {
        try {

            SerialPortHelper UssPrinter = MediaMainScreen.mSerialPortHelper;
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("FrimiSuccess", Message_Testing_Data_Transfer);

            UssPrinter.Write(String.valueOf(jsonObject));
            UssPrinter.Write(SerialPortHelper.EOT_COMMAND);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


