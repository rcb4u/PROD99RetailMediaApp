package com.RSPL.MEDIA.Doc990;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.serialport.api.SerialPortHelper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.RSPL.MEDIA.MediaMainScreen;
import com.RSPL.MEDIA.PersistenceManager;
import com.RSPL.MEDIA.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AppointmentConfirm extends Fragment {

    String payment_id, ref_id, referenceapp, username, phonenumber, nicapp, total, next, namehosp, doc, spec, datee, timee, email, pass, title, local, country;
    Button continueforpayment;
    TextView passport, hospitalappoint, doctor, speciality, specialnote, patientno, patientname, phone, nic, reference, amount, time, date, emailapp, pattitle;
    ImageButton error;
    String doccharge, docvatcharge, hospitalcharge, bookingcharge;
    TextView doctorcharges, docchargesvat, hospitalcharges, bookingcharges;
    ProgressDialog progressBar;
    private String st;
    String paymentname;
    String title_id;
    String pay_response, pay_amount, pay_success;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_appointment_confirm, container, false);
        st = PersistenceManager.getSessionId(getActivity().getApplicationContext());


        doctorcharges = (TextView) view.findViewById(R.id.doctorcharges);
        docchargesvat = (TextView) view.findViewById(R.id.doctorchargesvat);
        hospitalcharges = (TextView) view.findViewById(R.id.hospitalcharges);
        bookingcharges = (TextView) view.findViewById(R.id.bookingcharges);

        error = (ImageButton) view.findViewById(R.id.error);
        continueforpayment = (Button) view.findViewById(R.id.continueforpay);
        hospitalappoint = (TextView) view.findViewById(R.id.appointhospital);
        doctor = (TextView) view.findViewById(R.id.drNameappoint);
        speciality = (TextView) view.findViewById(R.id.specializationappoint);
        specialnote = (TextView) view.findViewById(R.id.specialnote);
        time = (TextView) view.findViewById(R.id.timesessionappoint);
        date = (TextView) view.findViewById(R.id.datesessionappoint);
        patientno = (TextView) view.findViewById(R.id.patientnoappoint);
        patientname = (TextView) view.findViewById(R.id.patientname);
        phone = (TextView) view.findViewById(R.id.phoneappoint);
        reference = (TextView) view.findViewById(R.id.reference);
        amount = (TextView) view.findViewById(R.id.totalcharges);
        nic = (TextView) view.findViewById(R.id.nicappoint);
        emailapp = (TextView) view.findViewById(R.id.email);
        passport = (TextView) view.findViewById(R.id.passportno);
        pattitle = (TextView) view.findViewById(R.id.patienttitle);


        Bundle intent = this.getArguments();

        //   if (intent != null) {
      /*      docvatcharge = intent.getString("doctorvat");
       if(docvatcharge =="")
       {
            doccharge = intent.getString("doctorcharges");
            //
            hospitalcharge = intent.getString("hospitalcharge");
            bookingcharge = intent.getString("bookingcharges");
            referenceapp = intent.getString("reference");
            ref_id = intent.getString("ref_id");
            username = intent.getString("name");
            phonenumber = intent.getString("phone");
            nicapp = intent.getString("nic");
            total = intent.getString("total");
            next = intent.getString("nextPatient");
            namehosp = intent.getString("hospitals");
            doc = intent.getString("doctorName");
            spec = intent.getString("specialization");
            datee = intent.getString("date");
            timee = intent.getString("time");
            email = intent.getString("email");
            pass = intent.getString("passport");
            title = intent.getString("title");
            title_id = intent.getString("title_id");
            local = intent.getString("local");
            country = intent.getString("country");
       }
       else{*/
        doccharge = intent.getString("doctorcharges");
        docvatcharge = intent.getString("doctorvat");
        hospitalcharge = intent.getString("hospitalcharge");
        bookingcharge = intent.getString("bookingcharges");
        referenceapp = intent.getString("reference");
        ref_id = intent.getString("ref_id");
        username = intent.getString("name");
        phonenumber = intent.getString("phone");
        nicapp = intent.getString("nic");
        total = intent.getString("total");
        next = intent.getString("nextPatient");
        namehosp = intent.getString("hospitals");
        doc = intent.getString("doctorName");
        spec = intent.getString("specialization");
        datee = intent.getString("date");
        timee = intent.getString("time");
        email = intent.getString("email");
        pass = intent.getString("passport");
        title = intent.getString("title");
        title_id = intent.getString("title_id");
        local = intent.getString("local");
        country = intent.getString("country");

        //  }
        //  }

        try {

            if (docvatcharge == null) {
                patientname.setText(username);
                patientno.setText(next);
                phone.setText(phonenumber);
                nic.setText(nicapp);
                reference.setText(referenceapp);
                hospitalappoint.setText(namehosp);
                doctor.setText(doc);
                speciality.setText(spec);
                time.setText(timee);
                date.setText(datee);
                emailapp.setText(email);
                passport.setText(pass);
                pattitle.setText(title);
                doctorcharges.setText(doccharge);
                //docchargesvat.setText("0.00");
                hospitalcharges.setText(hospitalcharge);
                bookingcharges.setText(bookingcharge);
                Log.e("testChargeswithoutVat1", "doccharge" + " " + doccharge + "  " + " " + "hospitalcharge" + "  " + hospitalcharge + "" + "bookingcharge" + " " + bookingcharge);
                amount.setText(String.valueOf(Double.valueOf(doccharge) + Double.valueOf(hospitalcharge) + Double.valueOf(bookingcharge)));
            } else {
                patientname.setText(username);
                patientno.setText(next);
                phone.setText(phonenumber);
                nic.setText(nicapp);
                reference.setText(referenceapp);
                hospitalappoint.setText(namehosp);
                doctor.setText(doc);
                speciality.setText(spec);
                time.setText(timee);
                date.setText(datee);
                emailapp.setText(email);
                passport.setText(pass);
                pattitle.setText(title);
                doctorcharges.setText(doccharge);
                docchargesvat.setText(docvatcharge);
                hospitalcharges.setText(hospitalcharge);
                bookingcharges.setText(bookingcharge);
                Log.e("testchargeswithVAT2", "doccharge" + " " + doccharge + "  " + "docvatcharge" + "  " + docvatcharge + " " + "hospitalcharge" + "  " + hospitalcharge + "" + "bookingcharge" + " " + bookingcharge);
                amount.setText(String.valueOf(Double.valueOf(doccharge) + Double.valueOf(docvatcharge) + Double.valueOf(hospitalcharge) + Double.valueOf(bookingcharge)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        continueforpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    android.app.FragmentManager fragmentManager = getFragmentManager();
                    android.app.FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack("my_fragmen");
                    Successful successful = new Successful();
                    transaction.replace(R.id.linearlayouts, successful, "Doc");
                    transaction.commit();
                    PaymentProcess("");

                } catch (Exception e) {
                }
            }
        });

        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                errorfragment();
            }
        });
        return view;
    }

    public void writeData() {
        try {

            SerialPortHelper UssPrinter = MediaMainScreen.mSerialPortHelper;
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("Reference", referenceapp);
            jsonObject.put("Referid", ref_id);
            jsonObject.put("Title", title);
            jsonObject.put("Name", username);
            jsonObject.put("Nic", nicapp);
            jsonObject.put("Total", amount.getText().toString());
            jsonObject.put("Hospital", namehosp);
            jsonObject.put("NextPatient", next);
            jsonObject.put("Phone", phonenumber);
            jsonObject.put("DoctorName", doc);
            jsonObject.put("specialization", spec);
            jsonObject.put("Date", datee);
            jsonObject.put("Time", timee);
            jsonObject.put("Email", email);
            jsonObject.put("Paymentsuccess", "1");
            jsonObject.put("Passport", pass);
            jsonObject.put("AccessToken", st);
            jsonObject.put("PaymentId", payment_id);
            jsonObject.put("PaymentName", paymentname);
            jsonObject.put("bookingType", "1");
            jsonObject.put("PaymentResponse", "success");
            jsonObject.put("Active", "Y");


            UssPrinter.Write(String.valueOf(jsonObject));
            UssPrinter.Write(SerialPortHelper.EOT_COMMAND);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void errorfragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }


    public void PaymentProcess(final String userTyped) {
        class WaitingforResponse extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(">>>>>>>>", userTyped);
                progressBar = new ProgressDialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                progressBar.setTitle("Loading");
                progressBar.setMessage("Continue for Payment Gateway");
                progressBar.show();
                WindowManager.LayoutParams wmlp = progressBar.getWindow().getAttributes();
                wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                wmlp.height = 200;
                wmlp.width = 400;
                progressBar.getWindow().setAttributes(wmlp);
            }

            @Override
            public String doInBackground(String... params) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(1000, TimeUnit.SECONDS)
                        .writeTimeout(1000, TimeUnit.SECONDS)
                        .readTimeout(6000, TimeUnit.SECONDS)
                        .build();
                MediaType mediaType = MediaType.parse("application/json");

                String json = "";
                RequestBody bodyupdate = new FormBody.Builder().add("title", title_id).
                        add("name", username).add("phone", phonenumber).add("nic", nicapp).
                        add("reference", referenceapp).add("bookingType", "1").
                        add("local", local).
                        add("country", country).
                        add("passport", pass).
                        add("email", email).build();


                Request requestUpdate = new Request.Builder()
                        .url("https://ideabiz.lk/apicall/docs/V2.0/appointments/" + ref_id + "/update")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .addHeader("access", "ebgdWu_er34")
                        .addHeader("authorization", String.format("Bearer %s", st))
                        .post(bodyupdate)
                        .build();


                Log.d("reference_id******", ref_id);

                Request requestPay = new Request.Builder()
                        .url("https://ideabiz.lk/apicall/docs/V2.0/payment-methods")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .addHeader("access", "ebgdWu_er34")
                        .addHeader("authorization", String.format("Bearer %s", st))
                        .get()
                        .build();

                try {

                    Response responsepay = client.newCall(requestPay).execute();
                    Response responseupdate = client.newCall(requestUpdate).execute();

                    String test = responsepay.body().string();
                    String test1 = responseupdate.body().string();
                    if (responsepay.isSuccessful() && responseupdate.isSuccessful()) {
                        int success = responsepay.code();
                        if (success == 401) {
                            MediaMainScreen mediaMainScreen = new MediaMainScreen();
                            mediaMainScreen.TokenGenerationProcess();
                        }

                    }
                    try {

                        JSONObject jsonObject = new JSONObject(test);
                        JSONArray jsonArray = jsonObject.getJSONArray("response");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjects = jsonArray.getJSONObject(i);
                            payment_id = jsonObjects.getString("id");
                            paymentname = jsonObjects.getString("name");
                            Log.d("payment****method", paymentname);
                        }
                        JSONObject updatejsonObject = new JSONObject(test1);
                        String res = updatejsonObject.get("response").toString();
                        Log.e(" responseeeeessss", "" + res);
                        JSONObject resp = new JSONObject(res);
                        String appoint = resp.getString("appointment");
                        JSONObject appointment = new JSONObject(appoint);
                        String payments = appointment.getString("payments");
                        JSONObject payme = new JSONObject(payments);

                        Iterator<String> iterator = payme.keys();
                        Log.e("pay_", payme.toString());
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            Log.d("key********   ", "" + key);
                            JSONObject jsonobj = payme.getJSONObject(key);

                            pay_response = jsonobj.getString("type");
                            pay_amount = jsonobj.getString("amount");
                            pay_success = jsonobj.getString("success");

                            Log.e("pay_  pay_success", pay_response + "    " + pay_success);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.dismiss();
                writeData();
            }
        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute(userTyped);

    }

}



