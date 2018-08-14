package com.RSPL.MEDIA;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by rspl-gourav on 15/12/17.
 */

public class AlertDialogfragment extends Fragment {

    DBhelper db;
    String Str_store_media_id;
    public Button submitUsrRes, btnCancel;
    public EditText editMobileNo, editcustName;
    String enteredMobNumber, enteredUserName;

    private String touchOn = null;
    private static final String videoMailFlag = "0";
    private String AdPlayUniqueId = null;
    private SimpleDateFormat simpleDateFormat = null;
    public static String currentvideoname = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.custom_popup, container, false);


        db = new DBhelper(getActivity());
        btnCancel = (Button) rootView.findViewById(R.id.id_btn_cancel);
        submitUsrRes = (Button) rootView.findViewById(R.id.button_submit);
        editMobileNo = (EditText) rootView.findViewById(R.id.edit_customermobile);
        editcustName = (EditText) rootView.findViewById(R.id.edit_customername);

        Calendar calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyyMMDDHHmmssmm");
        AdPlayUniqueId = simpleDateFormat.format(calendar.getTimeInMillis());
        Log.e("@@@@@@@@AD_PlayID:", "--------->" + AdPlayUniqueId);

        try {


            MediaStorePojo mStoreId = db.getStoreDetails();
            Str_store_media_id = mStoreId.getMediaid();

            currentvideoname = MediaMainScreen.b.getString("Current_name");

            touchOn = currentvideoname.toString().trim();

            Log.d("current Video", touchOn);


            submitUsrRes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (editMobileNo.getText().toString().equals("")) {
                        /* Toast.makeText(getApplicationContext(), "Please Fill Mobile Number and Name", Toast.LENGTH_SHORT).show();*/
                        editMobileNo.setError("Please Fill Mobile Number");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        editMobileNo.startAnimation(shake);
                        return;

                    }

                    if (editMobileNo.length() < 9 || editMobileNo.length() > 10) {


                        editMobileNo.setError("Please Fill Valid Mobile Number");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        editMobileNo.startAnimation(shake);
                        return;

                    }
                    if (editcustName.getText().toString().equals("")) {

                        editcustName.setError("Please Fill Name");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        editcustName.startAnimation(shake);

                        return;
                    }
                    enteredUserName = editcustName.getText().toString();

                    if (!enteredUserName.matches("[a-zA-Z ']+")) {

                        editcustName.setError("Please Fill Valid Name");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        editcustName.startAnimation(shake);

                        return;
                    }


                    enteredMobNumber = editMobileNo.getText().toString();


                    if (db.CheckIsDataAlreadyInDBorNot(enteredMobNumber, touchOn) == true) {


                        editMobileNo.setError("Your Number is Already registered with us");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        editMobileNo.startAnimation(shake);

                        return;

                    } else {


                        DBhelper dBhelper = new DBhelper(getActivity().getApplicationContext());
                        enteredMobNumber = editMobileNo.getText().toString();
                        enteredUserName = editcustName.getText().toString();

                        dBhelper.insertUserResponse(AdPlayUniqueId, Str_store_media_id, enteredMobNumber, enteredUserName, touchOn, videoMailFlag);

                        Toast.makeText(getActivity().getApplicationContext(), "Thanks for showing interest", Toast.LENGTH_SHORT).show();
                        closefragment();


                    }
                }
            });


            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    closefragment();

                }
            });


        } catch (Exception e) {

            e.printStackTrace();
        }


        return rootView;
    }


    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();


    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("****", "Onstart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("****", "OnResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("****", "OnPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("****", "OnStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        closefragment();
        Log.d("****", "OnDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("****", "OnDestroy");
    }

}
