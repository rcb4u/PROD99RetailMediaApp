package com.RSPL.MEDIA;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AutoUpgradeService extends Service

{

    private static final String LOADING_PHRASE_CONFIG_KEY = "loading_phrase";
    private static final String UPDATE_MESSAGE_KEY = "version_name";
    private static final String MEDIA_ID_KEY = "Media_Id";


    String updateMessage, storeidvalueserver;
    String versionName;
    PackageInfo pinfo;
    ProgressBar progress;
    String MediaId;
    String StoreName;


    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    StorageReference storageRe = storage.getReferenceFromUrl("gs://media-ce642.appspot.com").child("Media.apk");
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DBhelper db = new DBhelper(this);

    @Override
    public void onCreate() {
//        ref.child("UPDATE APK").child(MediaId+" : "+StoreName).child("ServerValue").child("Fetching..");
        fetchAppVersionValue();
    }

    /**
     * Fetch welcome message from server.
     */
    private void fetchAppVersionValue() {
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


                            ref.child("UPDATE APK").child(MediaId + " : " + StoreName).child("ServerValue").child("Value Fetched");

                            Log.e("SERVERRESPONSE", task.toString());


                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            ref.child("UPDATE APK").child(MediaId + " : " + StoreName).child("ServerValue").child("Value Not Fetched");


                            Log.e("SERVERRESPONSE", task.toString());
                        }
                        appUpdatecheckerDialog();
                    }
                });
        // [END fetch_config_with_callback]
    }

    private void appUpdatecheckerDialog() {
        try {
            pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pinfo.versionName;
            Log.e("@@@@@pack", versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        MediaStorePojo mStoreId = db.getStoreDetails();
        MediaId = mStoreId.getMediaid();
        StoreName = mStoreId.getStoreName();
        // [START get_config_values]
        updateMessage = mFirebaseRemoteConfig.getString(UPDATE_MESSAGE_KEY);

        storeidvalueserver = mFirebaseRemoteConfig.getString(MEDIA_ID_KEY);

        Log.e("VALUEFROMSERVER", updateMessage);
        Log.e("VALUEFROMDATABASE", MediaId);

        List<String> myList = new ArrayList<String>(Arrays.asList(storeidvalueserver.split(",")));


        for (int i = 0; i < myList.size(); i++) {
            String storeIdconfig = myList.get(i);
            Log.e("@@STOREIDVALUE", storeIdconfig);
            // [END get_config_values]


            try {

                if ((Float.parseFloat(updateMessage) > (Float.parseFloat(versionName))) && MediaId.matches(storeIdconfig))

                {
                    startDatabaseProgress();
                    getNewAPkDownloadServer();
                    ref.child("UPDATE APK").child(MediaId + " : " + StoreName).child("Status").setValue("0");


                } else {


                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    public void startDatabaseProgress() {
        // do something long
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

    // Simulating something timeconsuming
    private void initializeDatabaseprogress() {
        try {
            CopyDatabase();
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
// END OF COPY


    // DATABASE SCRIPT

    public void UnInstallApp() {

        try {
            Process p;
            p = Runtime.getRuntime().exec("su");

            // Attempt to write a file to a root-only
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("pm uninstall com.RSPL.MEDIA\n");
            os.writeBytes("pm install /mnt/internal_sd/ApkFolder/Media.apk\n ");
            os.writeBytes("am start -n com.RSPL.MEDIA/.Activity_Installation\n");
            os.writeBytes("");
            os.flush();
            p.waitFor();

        } catch (Exception e) {

        }

    }

    public void CopyDatabase() {

        try {
            Process p;
            p = Runtime.getRuntime().exec("su");

            // Attempt to write a file to a root-only
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("cp /data/data/com.RSPL.MEDIA/databases/Db /sdcard/Music/");

            // Close the terminal
            os.writeBytes("Db\n");
            os.flush();
            p.waitFor();

        } catch (Exception e) {

        }

    }

    private void getNewAPkDownloadServer() {
        class GetJSON extends AsyncTask<Void, Void, String> {

            ProgressDialog loading;

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
                File mydownload = new File(Environment.getExternalStorageDirectory(), "/ApkFolder");

                if (!mydownload.exists()) {
                    mydownload.mkdirs();
                }
                Log.e("Filedire", mydownload.getAbsolutePath());
                final File localFile = new File(mydownload, "Media.apk");

                storageRe.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        Log.e("@@SSSS", localFile.getAbsolutePath());
                        ref.child("UPDATE APK").child(MediaId + " : " + StoreName).child("Status").setValue("1");
                        SystemClock.sleep(30000);
                        UnInstallApp();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("firebase ", ";Exception Occur " + exception.toString());
                        ref.child("UPDATE APK").child(MediaId + " : " + StoreName).child("status").setValue(exception.toString());
                        // Handle any errors
                    }

                });

                return String.valueOf(localFile);
            }


        }

        GetJSON gj = new GetJSON();
        gj.execute();


    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service Completed or Stopped.", Toast.LENGTH_SHORT).show();

    }
}
