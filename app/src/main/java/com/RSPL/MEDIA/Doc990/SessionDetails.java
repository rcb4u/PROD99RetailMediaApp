package com.RSPL.MEDIA.Doc990;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.RSPL.MEDIA.MediaMainScreen;
import com.RSPL.MEDIA.PersistenceManager;
import com.RSPL.MEDIA.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.RSPL.MEDIA.R.id.foreignradio;
import static com.RSPL.MEDIA.R.id.localradio;

public class SessionDetails extends Fragment {

    private String test;
    String id;
    ImageButton back;
    private int success;
    String sessionAt, nextPatient, canBook, doctoDetails, totallocalcharge, totaloverseascharge, doctorName, title, hospital, nameHospital, specNAme, countryid, countryname;
    TextView tvdate, tvtime, tvnextPatient, tvcanBook, tvdoctoDetails, tvdoctorName, tvnamehospital, totalamount;
    String username, phone, nic, passport, reference, nationality, total;
    String doctorcharges, hospitalcharge, bookingchargr, doctorvat, Totalcharges;
    String patientname, patientphone, patientnic, patientemail, patienttitle;
    EditText name, phn, nicdetail, passportdetail, emailadd;
    Button submit;
    RadioButton local, foreign;
    RadioGroup radioGroup;
    Spinner title1;
    private String st;
    ImageButton error;
    Spinner spintitle;
    ArrayList<Titlepojo> titlepojos;
    String ref_id;
    private int statussessiondetail;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_session_details, container, false);
        st = PersistenceManager.getSessionId(getActivity().getApplicationContext());
        Log.e("Aaaaaaaaaaaaa", st.toString());

        Titles(id);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            id = bundle.getString("ID");
            Log.d("************", id);
            DoctorlistProcess(id);


        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Sorry No Sessions Found", Toast.LENGTH_LONG).show();
        }

        spintitle = (Spinner) view.findViewById(R.id.spintitle);
        error = (ImageButton) view.findViewById(R.id.error);
        back = (ImageButton) view.findViewById(R.id.back);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        tvdate = (TextView) view.findViewById(R.id.datesession);
        tvtime = (TextView) view.findViewById(R.id.timesession);
        tvnextPatient = (TextView) view.findViewById(R.id.nextsession);
        tvcanBook = (TextView) view.findViewById(R.id.availablesession);
        tvdoctoDetails = (TextView) view.findViewById(R.id.specialization);
        tvdoctorName = (TextView) view.findViewById(R.id.drName);
        tvnamehospital = (TextView) view.findViewById(R.id.hospitals);
        submit = (Button) view.findViewById(R.id.continuebutton);
        local = (RadioButton) view.findViewById(R.id.localradio);
        foreign = (RadioButton) view.findViewById(foreignradio);
        //totalamount = (TextView) view.findViewById(R.id.totalamount);
        name = (EditText) view.findViewById(R.id.username);
        phn = (EditText) view.findViewById(R.id.phoneno);
        nicdetail = (EditText) view.findViewById(R.id.nic);
        passportdetail = (EditText) view.findViewById(R.id.passport);
        emailadd = (EditText) view.findViewById(R.id.email);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == localradio) {
                    nationality = "1";
                    countryid = "0";
                    // totalamount.setText(totallocalcharge);
                    total = totallocalcharge;
                    passport = "";
                }
                if (checkedId == foreignradio) {
                    nationality = "0";
                    countryid = "200";
                    // totalamount.setText(totaloverseascharge);
                    total = totaloverseascharge;


                }
            }
        });


        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closefragment();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack("my_fragmn", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (!emailadd.getText().toString().equals("")) {
                        if (!emailadd.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
                            emailadd.setError("Email Not Valid");
                            return;
                        }
                    }
                    if (local.isChecked() && nicdetail.getText().toString().length() < 10 || nicdetail.getText().toString().length() > 12) {
                        nicdetail.setError("Nic Must Be 10 or 12 Characters");
                        return;
                    }
                    if (foreign.isChecked() && passportdetail.getText().toString().equals("")) {
                        passportdetail.setError("Passport cannot left empty");
                        return;
                    }
                    if (phn.getText().toString().length() < 9) {
                        phn.setError("Phone Must Be 9 or 10 Digits");
                        return;
                    }
                    if (name.getText().toString().equals("")) {
                        name.setError("Please fill mandatory details");
                        return;
                    }
                    if (name.getText().toString().length() <= 2) {
                        name.setError("Name Should Be more than 2 characters");
                        return;
                    } else {
                        Submitsession("");
                        patientname = name.getText().toString();
                        patientnic = nicdetail.getText().toString();
                        patientphone = phn.getText().toString();
                        patientemail = emailadd.getText().toString();
                        patienttitle = titlepojos.get(spintitle.getSelectedItemPosition()).getId();
                    }


                } catch (Exception e) {
                }

            }
        });


        return view;
    }

    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }


    public void Submitsession(final String userTyped) {
        class WaitingforResponse extends AsyncTask<String, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(">>>>>>>>", userTyped);
                passport = passportdetail.getText().toString();
                progressDialog = new ProgressDialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Confirming Your Appointment");
                progressDialog.show();
                WindowManager.LayoutParams wmlp = progressDialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                wmlp.height = 200;
                wmlp.width = 400;
                progressDialog.getWindow().setAttributes(wmlp);
            }

            @Override
            public String doInBackground(String... params) {
                try {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(1000, TimeUnit.SECONDS)
                            .writeTimeout(1000, TimeUnit.SECONDS)
                            .readTimeout(6000, TimeUnit.SECONDS)
                            .build();
                    MediaType mediaType = MediaType.parse("application/json");

                    String json = "";
                    RequestBody body = new FormBody.Builder().add("title", patienttitle).
                            add("name", patientname)
                            .add("phone", patientphone)
                            .add("nic", patientnic).
                                    add("session", id)
                            .add("bookingType", "1")
                            .add("local", nationality)
                            .add("passport", passport)
                            .add("email", patientemail)
                            .add("country", countryid).build();

                    Request request = new Request.Builder()
                            .url("https://ideabiz.lk/apicall/docs/V2.0/appointments/store")

                            .addHeader("content-type", "application/json")
                            .addHeader("accept", "application/json")
                            .addHeader("access", "ebgdWu_er34")
                            .addHeader("authorization", String.format("Bearer %s", st))
                            .post(body)
                            .build();
                    Log.d("%%%%%%%%%%%%%", params[0]);

                    try {
                        Response response = client.newCall(request).execute();
                        test = response.body().string();
                        success = response.code();
                        if (response.isSuccessful()) {
                            System.out.println(test);
                            if (success == 401) {
                                MediaMainScreen mediaMainScreen = new MediaMainScreen();
                                mediaMainScreen.TokenGenerationProcess();
                            }
                            Headers responseHeaders = response.headers();
                            for (int i = 0; i < responseHeaders.size(); i++) {
                                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                            }
                            JSONObject jsonObject = new JSONObject(test);
                            statussessiondetail = jsonObject.getInt("status");
                            String res = jsonObject.get("response").toString();
                            JSONObject jsonObjects = new JSONObject(res);
                            Log.e(" response", "" + jsonObject.get("response"));
                            String appointment = jsonObjects.getString("appointment");
                            JSONObject datas = null;
                            JSONObject resObject = new JSONObject(appointment);
                            String charges = resObject.getString("charges");
                            JSONObject resObject1 = new JSONObject(charges);
                            Iterator<String> iterator = resObject1.keys();
                            HashMap<String, String> chargess = new HashMap<>();

                            while (iterator.hasNext()) {
                                String key = iterator.next();
                                Log.d("*******", key);// this will be your date
                                datas = resObject1.getJSONObject(key);// And this is your data
                                if (datas.has("taxes")) {
                                    String taxes = datas.getString("taxes");
                                    JSONObject tax = new JSONObject(taxes);
                                    Iterator<String> keys = tax.keys();
                                    String keyss = keys.next();
                                    JSONObject doccharges = tax.getJSONObject(keyss);

                                    doctorvat = doccharges.getString("amount");
                                    String rate = doccharges.getString("rate");
                                    String namevat = doccharges.getString("name");
                                }
                                String id = datas.getString("id");
                                String doctorcharg = datas.getString("value");
                                String name = datas.getString("name");
                                chargess.put(name, doctorcharg);
                            }
                            String agent = chargess.get("Agent Booking Charges");
                            hospitalcharge = chargess.get("Hospital Charges");
                            bookingchargr = chargess.get("Booking Charges");
                            doctorcharges = chargess.get("Doctor Charges");
                            Log.d("$$$$$$charges", doctorcharges + " " + bookingchargr + hospitalcharge + agent + "   " + "doctorvat" + " " + doctorvat);

                            JSONObject jsonAppointment = new JSONObject(appointment);
                            reference = jsonAppointment.getString("reference");
                            ref_id = jsonAppointment.getString("id");
                            username = jsonAppointment.getString("name");
                            phone = jsonAppointment.getString("phone");
                            nic = jsonAppointment.getString("nic");
                        } else {
                            System.out.println(test);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();

                try {
                    if (foreign.isChecked() && passportdetail.getText().toString().equals("")) {
                        passportdetail.setError("Passport required for foreign patients");
                        return;
                    }

                    Bundle intent = new Bundle();
                    if (statussessiondetail == 100) {
                        if (doctorvat == "") {
                            intent.putString("doctorcharges", doctorcharges);
                            //intent.putString("doctorvat", doctorvat);
                            intent.putString("hospitalcharge", hospitalcharge);
                            intent.putString("bookingcharges", bookingchargr);
                            intent.putString("reference", reference);
                            intent.putString("ref_id", ref_id);
                            intent.putString("name", username);
                            intent.putString("phone", phone);
                            intent.putString("nic", nic);
                            intent.putString("total", total);
                            intent.putString("nextPatient", nextPatient);
                            intent.putString("hospitals", nameHospital);
                            intent.putString("doctorName", doctorName);
                            intent.putString("specialization", specNAme);
                            intent.putString("time", tvtime.getText().toString());
                            intent.putString("date", tvdate.getText().toString());
                            intent.putString("email", emailadd.getText().toString());
                            intent.putString("passport", passportdetail.getText().toString());
                            intent.putString("title", spintitle.getSelectedItem().toString());
                            intent.putString("title_id", patienttitle);
                            intent.putString("local", nationality);
                            intent.putString("country", countryid);
                        } else {
                            intent.putString("doctorcharges", doctorcharges);
                            intent.putString("doctorvat", doctorvat);
                            intent.putString("hospitalcharge", hospitalcharge);
                            intent.putString("bookingcharges", bookingchargr);
                            intent.putString("reference", reference);
                            intent.putString("ref_id", ref_id);
                            intent.putString("name", username);
                            intent.putString("phone", phone);
                            intent.putString("nic", nic);
                            intent.putString("total", total);
                            intent.putString("nextPatient", nextPatient);
                            intent.putString("hospitals", nameHospital);
                            intent.putString("doctorName", doctorName);
                            intent.putString("specialization", specNAme);
                            intent.putString("time", tvtime.getText().toString());
                            intent.putString("date", tvdate.getText().toString());
                            intent.putString("email", emailadd.getText().toString());
                            intent.putString("passport", passportdetail.getText().toString());
                            intent.putString("title", spintitle.getSelectedItem().toString());
                            intent.putString("title_id", patienttitle);
                            intent.putString("local", nationality);
                            intent.putString("country", countryid);
                        }
                    } else {

                        //   Toast.makeText(getContext(), "Parameter having validation errors", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // intent.putString("Amount",);

                   /* if(doctorcharges==null ||doctorcharges=="")
                    {   doctorcharges="0.00";
                        intent.putString("doctorcharges", doctorcharges);}
                    else{
                        intent.putString("doctorcharges", doctorcharges);
                    }

                    if(doctorvat==null ||doctorvat==""){
                        doctorvat="0.00";
                        intent.putString("doctorvat", doctorvat);
                    }
                    else{
                        intent.putString("doctorvat", doctorvat);
                    }*/
                    AppointmentConfirm sessionDetails = new AppointmentConfirm();
                    FragmentManager fm = getFragmentManager();
                    sessionDetails.setArguments(intent);
                    fm.beginTransaction().replace(R.id.linearlayouts, sessionDetails).commit();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute(userTyped);

    }


    public void DoctorlistProcess(final String userTyped) {
        class WaitingforResponse extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(">>>>>>>>", userTyped);
                // progressDialog = ProgressDialog.show(DocMainActivity.this, "Waiting for Response Cash In...", "Please Wait...", false, false);
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
                RequestBody body = RequestBody.create(mediaType, json);
                /* https://ideabiz.lk/apicall/token?grant_type=password&username=99R_User&password=99R_User&scope=SANDBOX
                 */
                // Message="246f17de5bb981ff4784035c1609f98";
                Request request = new Request.Builder()
                        .url("https://ideabiz.lk/apicall/docs/V2.0/doctor-sessions/" + params[0])

                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .addHeader("access", "ebgdWu_er34")
                        .addHeader("authorization", String.format("Bearer %s", st))
                        .get()
                        .build();
                Log.d("%%%%%%%%%%%%%", params[0]);

                try {
                    Response response = client.newCall(request).execute();
                    test = response.body().string();
                    if (response.isSuccessful()) {
                        success = response.code();
                        if (success == 401) {
                            MediaMainScreen mediaMainScreen = new MediaMainScreen();
                            mediaMainScreen.TokenGenerationProcess();
                        }

                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }
                        JSONObject jsonObject = new JSONObject(test);
                        String res = jsonObject.get("response").toString();
                        JSONObject jsonObjects = new JSONObject(res);
                        Log.e(" response", "" + jsonObject.get("response"));
                        sessionAt = jsonObjects.getString("sessionAt");
                        nextPatient = jsonObjects.getString("nextPatient");
                        canBook = jsonObjects.getString("canBook");
                        doctoDetails = jsonObjects.getString("doctor");
                        totallocalcharge = jsonObjects.getString("totalLocalCharge");
                        totaloverseascharge = jsonObjects.getString("totalOverseasCharge");
                        JSONObject jsonDoctor = new JSONObject(doctoDetails);
                        title = jsonDoctor.getString("title");
                        doctorName = jsonDoctor.getString("name");
                        doctorName = title + doctorName;
                        JSONArray jsonArray = jsonDoctor.getJSONArray("specializations");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonSpec = (JSONObject) jsonArray.get(i);
                            specNAme = jsonSpec.getString("name");
                            System.out.println(specNAme);
                        }
                        hospital = jsonObjects.getString("hospital");
                        JSONObject hospitalDetails = new JSONObject(hospital);
                        nameHospital = hospitalDetails.getString("name");
                        Log.e("&&&&&&&", nameHospital);


                    } else {
                        System.out.println("Error");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /*String dateInString = "2013-11-01 00:00:00";
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");*/

                try {
                    Date date = formatter.parse(sessionAt);
                    DateFormat out = new SimpleDateFormat("dd MMM yyyy");
                    tvdate.setText(out.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String[] splitStr = sessionAt.split("\\s+");
                String time = splitStr[1];
                tvtime.setText(time);
                tvnextPatient.setText(nextPatient);
                tvcanBook.setText(canBook);
                if (canBook.equals("true")) {
                    tvcanBook.setText("Available");
                } else {
                    tvcanBook.setText("NA");
                }
                tvdoctoDetails.setText(specNAme);
                tvdoctorName.setText(doctorName);
                tvnamehospital.setText(nameHospital);
                //  totalamount.setText(totallocalcharge);
                total = totallocalcharge;
                nationality = "1";
                countryid = "0";
            }
        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute(userTyped);
    }

    public void Titles(final String userTyped) {
        class WaitingforResponse extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                Log.d(">>>>>>>>", userTyped);
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
                RequestBody body = RequestBody.create(mediaType, json);

                Request request = new Request.Builder()
                        .url("https://ideabiz.lk/apicall/docs/V2.0/titles/")

                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .addHeader("access", "ebgdWu_er34")
                        .addHeader("authorization", String.format("Bearer %s", st))
                        .get()
                        .build();
//                Log.d("%%%%%%%%%%%%%", params[0]);

                try {
                    Response response = client.newCall(request).execute();
                    String test = response.body().string();

                    if (response.isSuccessful()) {
                        int success = response.code();
                        if (success == 401) {
                            MediaMainScreen mediaMainScreen = new MediaMainScreen();
                            mediaMainScreen.TokenGenerationProcess();
                        }
                        System.out.println(test);

                        try {
                            titlepojos = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(test);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                String id = jsonObjects.getString("id");
                                String name = jsonObjects.getString("name");

                                Titlepojo titlepojo = new Titlepojo(id, name);
                                titlepojos.add(titlepojo);
                            }

                        } catch (Exception e) {


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


                try {
                    // titlepojos = (ArrayList<Titlepojo>) getArguments().getSerializable("TITLES");

                    if (titlepojos != null) {
                        ArrayAdapter<Titlepojo> titleArrayAdapter =
                                new ArrayAdapter<Titlepojo>(getActivity(), android.R.layout.simple_spinner_dropdown_item, titlepojos);
                        spintitle.setAdapter(titleArrayAdapter);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute(userTyped);

    }

}
