package com.RSPL.MEDIA.Doc990;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.RSPL.MEDIA.MediaMainScreen;
import com.RSPL.MEDIA.PersistenceManager;
import com.RSPL.MEDIA.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DocMainActivity extends Fragment {
    SearchWithoutName WaitingforResponse;
    String[] params;
    private JSONObject jsonObject, jsonObject1;
    private String test;
    private String Message;
    private int success;
    private String access_token;
    String doctorId, hospitalId, SpecializationToSend;
    Button search;
    Spinner hospitalsearch, specializationsearch;
    ImageButton error;
    AutoCompleteTextView doctorsearch;
    Button go;
    EditText datepicker;
    ArrayList<DoctorPojo> doctorPojos;
    ArrayList<Doctors> doctorPojoss;
    ArrayList<DoctorPojo.Hospital> hospitalPojos;
    ArrayList<DoctorPojo.Hospital.Specialization> specializationList;
    String from = "";
    String to = "";

    String userTypedString;
    String st;
    ProgressBar progressBar;
    RecyclerView recyclerChannel;
    private Calendar myCalendar;
    private int Year;
    private int Month;
    private int Day;
    private String date_saveee;
    HashMap<String, ArrayList> hospitalHashMap = new HashMap<>();
    HashMap<String, ArrayList> specializationHashMap = new HashMap<>();

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

                try {
                    Response response = client.newCall(request).execute();
                    test = response.body().string();
                    if (response.isSuccessful()) {
                        success = response.code();

                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++) {

                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }

                        System.out.println(test);
                        ///   {"scope":"default","token_type":"bearer","expires_in":3600,"refresh_token":"2d23c431f25e10e5abcd16bea931d0a","access_token":"b0bffddb2be384c53135cadf0f565c2"}
                        try {
                            jsonObject = new JSONObject(test);
                            Log.e(" scope", "" + jsonObject.get("scope"));
                            Log.e(" token_type", "" + jsonObject.get("token_type"));
                            Log.e(" expires_in", "" + jsonObject.get("expires_in"));
                            Log.e(" refresh_token", "" + jsonObject.get("refresh_token"));
                            Log.e(" access_token", "" + jsonObject.get("access_token"));
                            Message = (String) jsonObject.get("refresh_token");

                            access_token = (String) jsonObject.get("access_token");
                            PersistenceManager.saveSessionId(getActivity().getApplicationContext(), access_token);
                            PersistenceManager.saveTime(getActivity().getApplicationContext(), String.valueOf(System.currentTimeMillis()));
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

    class SearchWithoutName extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            // progressDialog = ProgressDialog.show(getActivity(), "Waiting for Response...", "Please Wait...", false, false);
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
                    .url("https://ideabiz.lk/apicall/docs/V2.0/hospital-doctors?hospital=" + params[1] + "&specialization=" + params[2] + "&from=" + params[3] + "&to=" + params[4] + "&page=0&offset=&doctor=" + params[0])

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

                        TokenGenerationProcess();
                    }

                    Headers responseHeaders = response.headers();
                    for (int i = 0; i < responseHeaders.size(); i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    try {
                        doctorPojos = new ArrayList<>();
                        doctorPojoss = new ArrayList<>();

                        jsonObject = new JSONObject(test);
                        from = params[3];
                        to = params[4];

                        String res = jsonObject.get("response").toString();
                        JSONObject jsonObjects = new JSONObject(res);
                        Log.e(" response", "" + jsonObject.get("response"));

                        JSONArray jsonArray = jsonObjects.getJSONArray("doctors");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            String title = jsonObject.getString("title");
                            String titleHospital = jsonObject.getString("hospitals");

                            ArrayList<DoctorPojo.Hospital> hospitalpojos1 = new ArrayList<>();
                            for (int k = 1; k < 100; k++) {
                                try {
                                    JSONObject mainObject = new JSONObject(titleHospital);
                                    JSONObject oneObject = mainObject.getJSONObject(String.valueOf(k));
                                    String hospi = oneObject.getString("name");
                                    String hospiId = oneObject.getString("id");

                                    JSONArray jsonSpecialization = oneObject.getJSONArray("specializations");

                                    ArrayList<DoctorPojo.Hospital.Specialization> specializationList1 = new ArrayList<>();


                                    for (int m = 0; m < jsonSpecialization.length(); m++) {

                                        JSONObject jsonSpec = (JSONObject) jsonSpecialization.get(m);
                                        String specId = jsonSpec.getString("id");
                                        String specName = jsonSpec.getString("name");
                                        Log.d("%%%%%%%%", id + "  " + specName);

                                        DoctorPojo.Hospital.Specialization specs = new DoctorPojo.Hospital.Specialization(specName, specId);

                                        specializationList1.add(specs);

                                    }

                                    DoctorPojo.Hospital hospitals = new DoctorPojo.Hospital(hospi, hospiId, specializationList1);
                                    hospitalpojos1.add(hospitals);

                                } catch (JSONException e1) {
                                    e1.printStackTrace();

                                }

                            }

                            String hospiId = null;
                            String specId = null;
                            ArrayList<Specializations> specializationList2 = new ArrayList<>();
                            DoctorPojo doctorPojo = new DoctorPojo(name, title, hospitalpojos1, id);
                            for (int l = 0; l < hospitalpojos1.size(); l++) {
                                hospiId = hospitalpojos1.get(l).getId().toString();
                                ArrayList<DoctorPojo.Hospital.Specialization> specsId = hospitalpojos1.get(l).getSpecialization();


                                String specName = specsId.toString().replace("[", "").replace("]", "").replace(",", " -");
                                for (int p = 0; p < specsId.size(); p++) {
                                    specId = hospitalpojos1.get(l).getSpecialization().get(p).getId();

                                    // Log.d("$$$$$$$$",hospiId + specId);

                                }
                                Specializations specializations = new Specializations(hospitalpojos1.get(l).getName(), specName, title, hospiId, specId, name, id);
                                specializationList2.add(specializations);
                            }
                            Doctors doctorPoj = new Doctors(title + " " + name, specializationList2, id);
                            doctorPojos.add(doctorPojo);
                            doctorPojoss.add(doctorPoj);


                        }
                        /*    */
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
            progressBar.setVisibility(View.INVISIBLE);
            //  progressDialog.dismiss();

            try {
                /*if (doctorPojos != null) {
                    DoctorsAdapter doctorsAdapter = new DoctorsAdapter(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, doctorPojos);
                    doctorsearch.setAdapter(doctorsAdapter);
                    doctorsAdapter.setDoctorList(doctorPojos);
                    doctorsAdapter.notifyDataSetChanged();
                }*/
                printMap(hospitalHashMap);
                printMap(specializationHashMap);

                if (doctorPojos != null) {
                    if (userTypedString.equals("")) {


                        ChannelAdapter sessionAdapter = new ChannelAdapter(doctorPojoss, from, to);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        recyclerChannel.setLayoutManager(linearLayoutManager);
                        recyclerChannel.setAdapter(sessionAdapter);


                    }
                    DoctorsAdapter doctorsAdapter = new DoctorsAdapter(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, doctorPojos);
                    doctorsearch.setAdapter(doctorsAdapter);
                    doctorsAdapter.setDoctorList(doctorPojos);
                    doctorsAdapter.notifyDataSetChanged();
                } else {
                    recyclerChannel.setVisibility(View.GONE);
                }
                Log.d(String.valueOf(doctorPojos.size()), String.valueOf(hospitalPojos.size()) + " " + String.valueOf(specializationList.size()));

            } catch (Exception e) {
            }
        }


    }


    class WaitingforResponse extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);


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

            Request request = new Request.Builder()
                    .url("https://ideabiz.lk/apicall/docs/V2.0/hospitals-doctors?hospitals=" + params[1] + "&specialization=" + params[2] + "&from=&to=&page=0&offset=&doctor=" + params[0])

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

                    Headers responseHeaders = response.headers();
                    for (int i = 0; i < responseHeaders.size(); i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    try {
                        doctorPojos = new ArrayList<>();


                        jsonObject = new JSONObject(test);

                        String res = jsonObject.get("response").toString();
                        JSONObject jsonObjects = new JSONObject(res);
                        Log.e(" response", "" + jsonObject.get("response"));

                        JSONArray jsonArray = jsonObjects.getJSONArray("doctors");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            String id = jsonObject.getString("id");
                            String name = jsonObject.getString("name");
                            String title = jsonObject.getString("title");
                            String titleHospital = jsonObject.getString("hospitals");
                            hospitalPojos = new ArrayList<>();

                            for (int k = 1; k < 100; k++) {
                                try {
                                    JSONObject mainObject = new JSONObject(titleHospital);
                                    JSONObject oneObject = mainObject.getJSONObject(String.valueOf(k));
                                    String hospi = oneObject.getString("name");
                                    String hospiId = oneObject.getString("id");


                                    JSONArray jsonSpecialization = oneObject.getJSONArray("specializations");

                                    specializationList = new ArrayList<>();

                                    for (int m = 0; m < jsonSpecialization.length(); m++) {
                                        JSONObject jsonSpec = (JSONObject) jsonSpecialization.get(m);
                                        String specId = jsonSpec.getString("id");
                                        String specName = jsonSpec.getString("name");

                                        DoctorPojo.Hospital.Specialization specs = new DoctorPojo.Hospital.Specialization(specName, specId);
                                        specializationList.add(specs);
                                    }
                                    DoctorPojo.Hospital hospitals = new DoctorPojo.Hospital(hospi, hospiId, specializationList);

                                    hospitalPojos.add(hospitals);


                                } catch (Exception e) {
                                    // e.printStackTrace();
                                }

                            }
                            DoctorPojo doctorPojo = new DoctorPojo(name, title, hospitalPojos, id);
                            doctorPojos.add(doctorPojo);


                        }
                        /*
                         */
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
            progressBar.setVisibility(View.INVISIBLE);

            try {
                if (doctorPojos != null) {
                    DoctorsAdapter doctorsAdapter = new DoctorsAdapter(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, doctorPojos);
                    doctorsearch.setAdapter(doctorsAdapter);
                    doctorsAdapter.setDoctorList(doctorPojos);
                    doctorsAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
            }
        }


    }

    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_welcome, container, false);
        final Calendar c = Calendar.getInstance();
        myCalendar = Calendar.getInstance();
        Year = c.get(Calendar.YEAR);
        Month = c.get(Calendar.MONTH) + 1;
        Day = c.get(Calendar.DAY_OF_MONTH);

        search = (Button) view.findViewById(R.id.searchbutton);
        datepicker = (EditText) view.findViewById(R.id.datepicker);
        doctorsearch = (AutoCompleteTextView) view.findViewById(R.id.doctorsearch);

        doctorsearch.didTouchFocusSelect();
        hospitalsearch = (Spinner) view.findViewById(R.id.hospitalsearch);
        specializationsearch = (Spinner) view.findViewById(R.id.specializationsearch);
        error = (ImageButton) view.findViewById(R.id.error);
        params = new String[5];
        progressBar = (ProgressBar) view.findViewById(R.id.pb);

        recyclerChannel = (RecyclerView) view.findViewById(R.id.recyclersearch);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        RecyclerView.ItemAnimator animator = recyclerChannel.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }


        recyclerChannel.setLayoutManager(layoutManager);


        st = PersistenceManager.getSessionId(getActivity());
        hospitalPojos = new ArrayList<>();
        specializationList = new ArrayList<>();
        datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        loadHospitals();


        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closefragment();

            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Log.d("#######", userTypedString);
                    if (WaitingforResponse != null) {
                        WaitingforResponse.cancel(true);
                    }
                    /*if (!userTypedString.equals("")) {


                        String doctor, hospital, specialization,date;
                        doctor = doctorsearch.getText().toString();
                        hospital = hospitalsearch.getSelectedItem().toString();
                        specialization = specializationsearch.getSelectedItem().toString();
                       // date=datepicker.getText().toString();

                        Log.d("*********", doctor + " " + hospital + " " + specialization);
                        Bundle intent = new Bundle();
                        intent.putString("DOCTOR", doctor);
                        intent.putString("SPECIALIZATION", SpecializationToSend);
                        intent.putString("HOSPITAL", hospital);
                        *//*intent.putString("DATE",date);*//*
                        intent.putString("DOCTOR_ID", doctorId);
                        intent.putString("HOSPITAL_ID", hospitalId);
                        Log.d("***&&(())******", doctorId + " " + hospitalId);

                        android.app.FragmentManager fragmentManager = getFragmentManager();
                        android.app.FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack("my_fragment");

                        doctorsearch.setText("");
                        DoctorSessions docSessions = new DoctorSessions();
                        docSessions.setArguments(intent);
                        transaction.replace(R.id.linearlayouts, docSessions, "Doc");

                        transaction.commit();

                    } else {*/
                    //  WaitingforResponse = new WaitingforResponse();
                    st = PersistenceManager.getSessionId(getActivity());
                    params = new String[5];

                    if (userTypedString.equals("")) {

                        params[0] = "0";
                    } else {
                        params[0] = userTypedString;
                    }
                    if (hospitalsearch.getSelectedItem().toString().equals("Any Hospital")) {
                        params[1] = "0";
                    } else {
                        params[1] = hospitalPojos.get(hospitalsearch.getSelectedItemPosition()).getId();
                    }
                    if (specializationsearch.getSelectedItem().toString().equals("Any Specialization")) {
                        params[2] = "0";
                    } else {
                        params[2] = specializationList.get(specializationsearch.getSelectedItemPosition()).getId();
                    }

                    if (!datepicker.getText().toString().equals("")) {

                        params[4] = datepicker.getText().toString();
                        params[3] = Year + "-" + Month + "-" + Day;
                    } else {

                        params[4] = "";
                        params[3] = "";
                    }

                    SearchWithoutName sea = new SearchWithoutName();
                    sea.execute(params);


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });
        doctorsearch.setThreshold(1);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                userTypedString = doctorsearch.getText().toString().trim();
                if (doctorsearch.isPerformingCompletion()) {
                    return;
                }
                if (userTypedString.equals("")) {
                    return;
                }
                if (userTypedString.length() > 3) {
                    WaitingforResponse = new SearchWithoutName();
                    st = PersistenceManager.getSessionId(getActivity());
                    params[2] = "0";
                    params[0] = userTypedString;
                    params[1] = "0";
                    params[4] = "";
                    params[3] = "";

                    WaitingforResponse.execute(params);
                }

            }
        };
        doctorsearch.addTextChangedListener(textWatcher);
        doctorsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final DoctorPojo result = (DoctorPojo) parent.getItemAtPosition(position);
                ArrayList<Doctors> doc = new ArrayList<Doctors>();
                ArrayList<DoctorPojo.Hospital> hos;
                ArrayList<Specializations> spec = new ArrayList<Specializations>();
                hos = result.getHospitals();
                String hospiId = null, specId = null;

                for (int l = 0; l < hos.size(); l++) {
                    hospiId = hos.get(l).getId().toString();
                    specId = hos.get(l).getSpecialization().toString();
                    Specializations specializations = new Specializations(hos.get(l).getName(), hos.get(l).getSpecialization().toString().replace("[", "").replace("]", ""), result.getTitle(), hospiId, specId, result.getName(), result.getId());
                    spec.add(specializations);

                }
                Doctors doctorPoj = new Doctors(result.getTitle().toString() + " " + result.getName().toString(), spec, result.getId().toString());

                doc.add(doctorPoj);


                ChannelAdapter sessionAdapter = new ChannelAdapter(doc, from, to);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                recyclerChannel.setLayoutManager(linearLayoutManager);
                recyclerChannel.setAdapter(sessionAdapter);
          /*      WaitingforResponse.cancel(true);
                recyclerChannel.setVisibility(View.GONE);
                progressBar.setVisibility(View.INVISIBLE);
                final DoctorPojo result = (DoctorPojo) parent.getItemAtPosition(position);

                doctorId = result.getId();


                ArrayList<DoctorPojo.Hospital> hoas;

                hoas = result.getHospitals();

                ArrayAdapter<DoctorPojo.Hospital> stringArrayAdapter =
                        new ArrayAdapter<DoctorPojo.Hospital>(getActivity(), android.R.layout.simple_dropdown_item_1line, hoas);
                hospitalsearch.setAdapter(stringArrayAdapter);

                hospitalsearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)

                    {
                        ArrayList<DoctorPojo.Hospital.Specialization> spec;
                        spec = result.getHospitals().get(position).getSpecialization();
                        hospitalId = result.getHospitals().get(position).getId();

                        SpecializationToSend = spec.toString().replace("[", "").replace("]", "").replace(",", " -");


                        ArrayAdapter<DoctorPojo.Hospital.Specialization> specializationArrayAdapter =
                                new ArrayAdapter<DoctorPojo.Hospital.Specialization>(getActivity(), android.R.layout.simple_dropdown_item_1line, spec);
                        specializationsearch.setAdapter(specializationArrayAdapter);
                    } // to close the onItemSelected

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/

            }
        });
        String time = PersistenceManager.getTime(getActivity());
      /*  if (time.equals("0")) {
            TokenGenerationProcess();
        }
        long difference = (System.currentTimeMillis() - Long.parseLong(time)) / 1000;
        if (difference >= 1500) {
            TokenGenerationProcess();
        }
*/
        //Log.e("Aaaaaaaaaaaaa", String.valueOf(difference));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("****", "Onstart");
        doctorsearch.setText("");
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
        Log.d("****", "OnDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("****", "OnDestroy");
    }

    public void loadHospitals() {
        class HospitalLoading extends AsyncTask<String, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Fetching Hospitals and Specializations");
                progressDialog.show();
                WindowManager.LayoutParams wmlp = progressDialog.getWindow().getAttributes();

                wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;

                wmlp.height = 200;
                wmlp.width = 400;

                progressDialog.getWindow().setAttributes(wmlp);

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
                        .url("https://ideabiz.lk/apicall/docs/V2.0/hospitals/")

                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .addHeader("access", "ebgdWu_er34")
                        .addHeader("authorization", String.format("Bearer %s", st))
                        .get()
                        .build();

                Request requestSpec = new Request.Builder()
                        .url("https://ideabiz.lk/apicall/docs/V2.0/specializations/")

                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .addHeader("access", "ebgdWu_er34")
                        .addHeader("authorization", String.format("Bearer %s", st))
                        .get()
                        .build();


                try {
                    Response response = client.newCall(request).execute();
                    Response response1 = client.newCall(requestSpec).execute();
                    String test = response.body().string();
                    String test1 = response1.body().string();

                    if (response.isSuccessful() && response1.isSuccessful()) {
                        int success = response.code();
                        if (success == 401) {

                            MediaMainScreen mediaMainScreen = new MediaMainScreen();
                            mediaMainScreen.TokenGenerationProcess();
                        }
                        System.out.println(test);


                        try {
                            doctorPojos = new ArrayList<>();
                            JSONObject jsonObject = new JSONObject(test);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            //      Log.e(" response",""+jsonObject.get("response"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjects = jsonArray.getJSONObject(i);
                                String name = jsonObjects.getString("name");
                                String id = jsonObjects.getString("id");
                                DoctorPojo.Hospital doctorPojo = new DoctorPojo.Hospital(name, id);
                                hospitalPojos.add(doctorPojo);
                            }
                            specializationList = new ArrayList<>();
                            JSONObject specjsonObject = new JSONObject(test1);
                            JSONArray specjsonArray = specjsonObject.getJSONArray("response");

                            for (int i = 0; i < specjsonArray.length(); i++) {
                                JSONObject jsonObjects = specjsonArray.getJSONObject(i);

                                String name = jsonObjects.getString("name");
                                String id = jsonObjects.getString("id");
                                DoctorPojo.Hospital.Specialization specialization = new DoctorPojo.Hospital.Specialization(name, id);
                                specializationList.add(specialization);
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
                    progressDialog.dismiss();

                    DoctorPojo.Hospital hospital = new DoctorPojo.Hospital("Any Hospital", "0");
                    hospitalPojos.add(0, hospital);
                    DoctorPojo.Hospital.Specialization specialization = new DoctorPojo.Hospital.Specialization("Any Specialization", "0");
                    specializationList.add(0, specialization);

                    ArrayAdapter<DoctorPojo.Hospital.Specialization> specializationArrayAdapter =
                            new ArrayAdapter<DoctorPojo.Hospital.Specialization>(getActivity(), android.R.layout.simple_dropdown_item_1line, specializationList);
                    specializationsearch.setAdapter(specializationArrayAdapter);

                    ArrayAdapter<DoctorPojo.Hospital> hospitalArrayAdapter =
                            new ArrayAdapter<DoctorPojo.Hospital>(getActivity(), android.R.layout.simple_dropdown_item_1line, hospitalPojos);
                    hospitalsearch.setAdapter(hospitalArrayAdapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        HospitalLoading hospitalLoadings = new HospitalLoading();
        hospitalLoadings.execute("");
    }

    private DatePickerDialog.OnDateSetListener date = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month,
                                      int day) {
                    // TODO Auto-generated method stub
                    validateDate(year, month + 1, day);
                }
            };


    public void validateDate(int year, int month, int day) {
        Date Todaydate = null, edate = null;

        String enddate = year + "-" + month + "-" + day;
        Log.e("########", "----------->" + enddate);

        String todaysdate = Year + "-" + Month + "-" + Day;
        //    String   demo =myCalendar.get(Calendar.YEAR)+"/"+ myCalendar.get(Calendar.MONTH)+"/"+myCalendar.get(Calendar.DAY_OF_MONTH);
        Log.e("########", "----------->" + todaysdate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            edate = sdf.parse(enddate);
            Todaydate = sdf.parse(todaysdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (edate.before(Todaydate)) {
            Toast.makeText(getActivity(), "Invalid date !!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Log.e("########", "----------->" + todaysdate);
            Log.e("########", "----------->" + edate);
            //   startDatePicked=true;
            int Mymonth = month;
            datepicker.setText(year + "-" + Mymonth + "-" + day);
            date_saveee = datepicker.getText().toString();
            /*if(mTempPositionBeforeCalenderDialog != -1 && mTempPositionBeforeCalenderDialog < list.size()) {
                int Mymonth=month;
                list.get(mTempPositionBeforeCalenderDialog).setExpdate(year+"/"+Mymonth +"/"+day);
                notifyDataSetChanged();
                mTempPositionBeforeCalenderDialog = -1;
            }*/
        }
    }

    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }


}
