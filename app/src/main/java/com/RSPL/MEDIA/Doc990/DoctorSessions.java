package com.RSPL.MEDIA.Doc990;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.RSPL.MEDIA.MediaMainScreen;
import com.RSPL.MEDIA.PersistenceManager;
import com.RSPL.MEDIA.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctorSessions extends Fragment {

    TextView doctor, specialization, hospital, emptyview;
    RecyclerView recyclerView;
    ArrayList<SessionPojo> sessionPojos;
    String doctor1, specialization1, specId, hospital1, doctorId, hospitalId, frmdate, title;
    String todate;
    ImageButton back;
    private String st;
    ProgressDialog progressBar;
    ImageButton error;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_doctor_sessions, container, false);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        doctor = (TextView) view.findViewById(R.id.doctorname);
        specialization = (TextView) view.findViewById(R.id.specializationname);
        hospital = (TextView) view.findViewById(R.id.hospitalname);
        emptyview = (TextView) view.findViewById(R.id.EmptyView);
        back = (ImageButton) view.findViewById(R.id.back);
        error = (ImageButton) view.findViewById(R.id.error);
        sessionPojos = new ArrayList<>();
        st = PersistenceManager.getSessionId(getActivity().getApplicationContext());
        Log.e("Aaaaaaaaaaaaa", st.toString());

        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Bundle bundle = this.getArguments();
        title = bundle.getString("title");
        doctor1 = bundle.getString("DOCTOR");
        specialization1 = bundle.getString("SPECIALIZATION");
        specId = bundle.getString("SPECIALIZATION_ID");

        hospital1 = bundle.getString("HOSPITAL");
        doctorId = bundle.getString("DOCTOR_ID");
        hospitalId = bundle.getString("HOSPITAL_ID");
        frmdate = bundle.getString("FROM_DATE");
        todate = bundle.getString("TO_DATE");
        Log.d("^^^^^^^^", frmdate + todate);


        doctor.setText(title + doctor1);
        specialization.setText(specialization1);
        hospital.setText(hospital1);

        DoctorlistProcess("");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack("my_fragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });


        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closefragment();

            }
        });

        return view;
    }

    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    public void DoctorlistProcess(final String userTyped) {
        class WaitingforResponse extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d(">>>>>>>>", userTyped);
                progressBar = new ProgressDialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                progressBar.setTitle("Loading");
                progressBar.setMessage("Getting Sessions");
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
                RequestBody body = RequestBody.create(mediaType, json);
                /* https://ideabiz.lk/apicall/token?grant_type=password&username=99R_User&password=99R_User&scope=SANDBOX
                 */
                // Message="246f17de5bb981ff4784035c1609f98";
                Request request = new Request.Builder()
                        .url("https://ideabiz.lk/apicall/docs/V2.0/doctor-sessions?hospital=" + hospitalId + "&doctor=" + doctorId + "&from=" + frmdate + "&to=" + todate + "&specialization=" + specId)

                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .addHeader("access", "ebgdWu_er34")
                        .addHeader("authorization", String.format("Bearer %s", st))
                        .get()
                        .build();
                Log.d("%%%%%%%%%%%%%", params[0]);
                try {
                    Response response = client.newCall(request).execute();
                    String test = response.body().string();
                    if (response.isSuccessful()) {
                        int success = response.code();
                        if (success == 401) {
                            MediaMainScreen mediaMainScreen = new MediaMainScreen();
                            mediaMainScreen.TokenGenerationProcess();
                        }

                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }
                        System.out.println(test);
                        JSONObject js = new JSONObject(test);
                        String res = js.get("response").toString();

                        JSONObject jsonObjects = new JSONObject(res);

                        String sessions = jsonObjects.getString("sessions");
                        JSONObject jsonSession = new JSONObject(sessions);
                        JSONArray jsonSessionArray = jsonSession.getJSONArray(specialization1);
                        for (int i = 0; i < jsonSessionArray.length(); i++) {
                            JSONObject jsonObject = (JSONObject) jsonSessionArray.get(i);
                            String id = jsonObject.getString("id");
                            Log.d(":::::::::", id);
                            String canbook = jsonObject.getString("canBook");
                            String status = jsonObject.getString("status");
                            String time = jsonObject.getString("sessionAt");
                            String nextpatient = jsonObject.getString("nextPatient");
                            SessionPojo sessionPojo = new SessionPojo(canbook, status, time, nextpatient, id);
                            sessionPojos.add(sessionPojo);
                        }
                        System.out.println(sessions);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressBar.dismiss();

                if (!sessionPojos.isEmpty()) {
                    SessionAdapter sessionAdapter = new SessionAdapter(getActivity().getApplicationContext(), sessionPojos);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(sessionAdapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyview.setVisibility(View.VISIBLE);
                    emptyview.setText("No Session Found");

                }
            }


        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute(userTyped);

    }
}
