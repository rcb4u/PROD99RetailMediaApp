package com.RSPL.MEDIA;

/**
 * Created by rspl-richa on 10/05/2018.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CargillsBank extends Fragment {

    DBhelper db;
    String Str_store_media_id;
    public Button Submit, Cancel;
    public EditText name, mobileno, companyname;
    Spinner monthlysalary;
    String cargilusername, cargilmobilename, cargilcompanyname, cargilmonthlysalary;
    private String touchOn = null;
    private static final String videoMailFlag = "0";
    private String AdPlayUniqueId = null;
    private SimpleDateFormat simpleDateFormat = null;
    public static String currentvideoname = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_cargills_bank, container, false);

        String[] values =
                {"15,000 - 35,000", "35,000 - 60,000", "60,000 -1,00,000", "1,00,000 & above "};
        monthlysalary = (Spinner) rootView.findViewById(R.id.monthlysalary);
        ArrayAdapter<String> LTRadapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        LTRadapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // monthlysalary.setPrompt("Select Monthly salary");
        monthlysalary.setAdapter(LTRadapter);

        // return inflater.inflate(R.layout.activity_cargills_bank, container, false);


        db = new DBhelper(getActivity());
        Cancel = (Button) rootView.findViewById(R.id.cancel);
        Submit = (Button) rootView.findViewById(R.id.submit);
        name = (EditText) rootView.findViewById(R.id.name);
        mobileno = (EditText) rootView.findViewById(R.id.mobileno);
        companyname = (EditText) rootView.findViewById(R.id.companyname);


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


            Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (mobileno.getText().toString().equals("")) {
                        mobileno.setError("Please Fill Mobile Number");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        mobileno.startAnimation(shake);
                        return;

                    }

                    if (name.getText().toString().equals("")) {
                        name.setError("Please Fill Name");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        name.startAnimation(shake);
                        return;

                    }

                    if (companyname.getText().toString().equals("")) {
                        companyname.setError("Please Fill Company Name");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        companyname.startAnimation(shake);
                        return;

                    }


                    if (mobileno.length() < 9 || mobileno.length() > 10) {


                        mobileno.setError("Please Fill Valid Mobile Number");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        mobileno.startAnimation(shake);
                        return;

                    }


                    cargilusername = name.getText().toString();

                    if (!cargilusername.matches("[a-zA-Z ']+")) {

                        name.setError("Please Fill Valid Name");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        name.startAnimation(shake);

                        return;
                    }


                    cargilmobilename = mobileno.getText().toString();


                    if (db.CheckIsDataAlreadyInDBorNotinCargill(cargilmobilename, touchOn) == true) {


                        mobileno.setError("Your Number is Already registered with us");
                        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.alert_animation);
                        mobileno.startAnimation(shake);

                        return;

                    } else {


                        DBhelper dBhelper = new DBhelper(getActivity().getApplicationContext());
                        cargilmobilename = mobileno.getText().toString();
                        cargilusername = name.getText().toString();
                        cargilcompanyname = companyname.getText().toString();
                        cargilmonthlysalary = monthlysalary.getSelectedItem().toString();

                        dBhelper.insertCargilbankcustomer(AdPlayUniqueId, Str_store_media_id, cargilmobilename, cargilusername, touchOn, videoMailFlag, cargilcompanyname, cargilmonthlysalary);

                        Toast.makeText(getActivity().getApplicationContext(), "Thanks for showing interest", Toast.LENGTH_SHORT).show();
                        closefragment();

                    }
                }
            });


            Cancel.setOnClickListener(new View.OnClickListener() {
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
