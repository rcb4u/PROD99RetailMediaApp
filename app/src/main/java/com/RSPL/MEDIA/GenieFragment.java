package com.RSPL.MEDIA;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.serialport.api.SerialPortHelper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.net.SocketTimeoutException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.text.TextUtils.isEmpty;

/**
 * A simple {@link Fragment} subclass.
 */
public class GenieFragment extends Fragment implements View.OnClickListener {

    private String test;
    private String formattedDate;
    private String access_token;
    private int success;
    EditText genieID, GenieAmount;
    TextView genieIDPrefix;
    Button geniePay, genieCancel;
    private String Genie_ID;
    private String GENIE_AMOUNT;
    private String GenieAccountNumber;
    private AlertDialog.Builder dialog;
    private AlertDialog geinedialog;
    private Response response;
    private String status;
    private String responsestring;
    private String genieTransactionId, merchantTxnStatusName,
            externalMerchantTransactionId, transactionAmount, invoiceNumber, transactionDateTime, merchantPgIdentifier, Message;
    private String failcode;
    private final int FIVE_SECONDS = 5000;
    private Handler mHandler;
    private String amount;
    private ProgressDialog dialogpr;
    private TextView Genie_test_Wallet_Tv, genie_amount_tv;
    private Waitingforstatus waitingforstatus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genie, container, false);
        Typeface ralewayFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway-Black.ttf");

        // QrImage = (ImageView) view.findViewById(R.id.QrimageView);
        if (geinedialog != null && geinedialog.isShowing()) {
            geinedialog.dismiss();
        }
        mHandler = new Handler();
        amount = getArguments().getString("GrandTotal");
        Genie_test_Wallet_Tv = (TextView) view.findViewById(R.id.Genie_test_Wallet_Tv);
        genie_amount_tv = (TextView) view.findViewById(R.id.genie_amount_tv);

        Genie_test_Wallet_Tv.setTypeface(ralewayFont);
        genie_amount_tv.setTypeface(ralewayFont);

        genieIDPrefix = (TextView) view.findViewById(R.id.Genie_test_Wallet_ID_Tv);
        genieID = (EditText) view.findViewById(R.id.Genie_test_Wallet_ID);
        GenieAmount = (EditText) view.findViewById(R.id.genie_total_amount);
        GenieAmount.setText(amount);
        geniePay = (Button) view.findViewById(R.id.genie_pay);
        genieCancel = (Button) view.findViewById(R.id.genie_cancel);
        geniePay.setOnClickListener(this);
        genieCancel.setOnClickListener(this);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = dateFormat.format(new Date()).toString();
        System.out.println(formattedDate);
        Long tsLong = System.currentTimeMillis() / 10;
        externalMerchantTransactionId = "99" + tsLong.toString();
        TokenGenerationProcess();
        return view;
    }
    public void TokenGenerationProcess() {
        class WaitingforResponse extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(),
                        "Waiting for Token Generation...", "Please Wait...", false, false);
                WindowManager.LayoutParams wmlp =
                        progressDialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                wmlp.height = 400;
                wmlp.width = 400;
                progressDialog.getWindow().setAttributes(wmlp);
            }

            @Override
            protected String doInBackground(Void... params) {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
                MediaType mediaType =
                        MediaType.parse("application/x-www-form-urlencoded");

                String json = "";
                RequestBody body = RequestBody.create(mediaType, json);
                Request request = new Request.Builder()

                        .url("https://ideabiz.lk/apicall/token?grant_type=password&username=99Retailstreet&password=99Retailstreet&scope=SANDBOX")
                        .addHeader("Content-Type",
                                "application/x-www-form-urlencoded")
                        .addHeader("Authorization", "Basic VFVzNDZrZHBoY0tvaUE2RmVlUDFVb3dVMVJzYTprT0hGV3dKbjREQ1UxUEVkOWNpdmxmdktjMFlh")
                        .post(body)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    test = response.body().string();
                    if (response.isSuccessful()) {
                        success = response.code();
                        System.out.println(test);
                        try {
                            JSONObject jsonObject = new JSONObject(test);
                            Log.e(" scope", "" + jsonObject.get("scope"));
                            Log.e(" token_type", "" +
                                    jsonObject.get("token_type"));
                            Log.e(" expires_in", "" +
                                    jsonObject.get("expires_in"));
                            Log.e(" refresh_token", "" +
                                    jsonObject.get("refresh_token"));
                            Log.e(" access_token", "" +
                                    jsonObject.get("access_token"));
                            Message = (String) jsonObject.get("refresh_token");

                            access_token = (String)
                                    jsonObject.get("access_token");

                            PersistenceManager.saveSessionId(getActivity(), access_token);
                            PersistenceManager.saveTime(getActivity(),
                                    String.valueOf(System.currentTimeMillis()));
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
                progressDialog.dismiss();
                //   CallingAPIProcess();
            }
        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute();
    }
    private void CallingAPIProcess() {
        class WaitingforResponse extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(),
                        "Transaction processing ...", "Please Wait...", false, false);

                WindowManager.LayoutParams wmlp =
                        progressDialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                wmlp.height = 400;
                wmlp.width = 400;
                progressDialog.getWindow().setAttributes(wmlp);
            }

            @Override
            protected String doInBackground(Void... params) {
                OkHttpClient client = null;

                client = new OkHttpClient.Builder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .build();
                MediaType mediaType = MediaType.parse("application/json");

                Log.e("Body", "" + bodyin());
                String json = bodyin();
                RequestBody body = RequestBody.create(mediaType, json);
                Request request = new Request.Builder()
                        // .url("https://services.axis.dialog.lk:4080/axipay/external/merchant/transaction/initiate/push")
                        // https://ideabiz.lk/apicall/geniepos/v1/axipay/external/merchant/transaction/initiate/qr
                        .url("https://ideabiz.lk/apicall/geniepos/v1/axipay/external/merchant/transaction/initiate/push\n")
                        .addHeader("content-type", "application/json")
                        .addHeader("X-IH-SECRETCODE", "4ecb71e43cda4910a9f651f7d4600e12")
                        // .addHeader("X-IH-SECRETCODE","063357ce07d845e3925278cb5da2c975")
                        .addHeader("X-IH-PSW", "Think100%")
                        .addHeader("Authorization", "Bearer " + access_token)
                        .post(body)
                        .build();

                try {
                    response = client.newCall(request).execute();
                    test = response.body().string();
                    if (response.isSuccessful()) {
                        System.out.println("Im in tst:: " + test);
                        System.out.println("response message ::   " +
                                response.code() + " : " + response.message());
                    } else {
                        System.out.println(response.code() + " : " +
                                response.message());
                    }

                } catch (SocketTimeoutException ex) {
                    ex.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
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
                    if (response.code() == 200) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(test);

                            Log.e("response", "" + jsonObject.get("response"));
                            responsestring = String.valueOf(jsonObject.get("response"));
                            JSONObject opt = new JSONObject(responsestring);
                            status = opt.getString("status");
                            Message = String.valueOf(opt.get("message"));
                            Log.e(" im before", status);
                            if (status.matches("success")) {
                                Log.e(" im in output", "" + status);
                                genieTransactionId = opt.getString("genieTransactionId");
                                writeInsertDataGenie(genieTransactionId, GenieAmount.getText().toString(), Message);
                                scheduleGenieStatus();
                            } else {
                                failcode = opt.getString("code");
                                Message = response.message();
                                OutPutMessage();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        android.widget.Toast.makeText(getActivity(), "No response from server.",
                                android.widget.Toast.LENGTH_SHORT).show();
                        Message = response.message();
                        OutPutMessage();
                    }
                } catch (NullPointerException e) {
                    android.widget.Toast.makeText(getActivity(), "No response from server.",
                            android.widget.Toast.LENGTH_SHORT).show();
                    Message = "No response from server.";
                    OutPutMessage();
                }
            }
        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute();
    }

    public String bodyin() {
        // GenieAccountNumber="+94715109942";
        //94765539702
        //below code for initialise push
        return "{  \n" +
                "   \"request\":{  \n" +
                "      \"genieAccountNumber\":\"" + GenieAccountNumber + "\",\n" +
                "      \"merchantPgIdentifier\":\"PG00008372\",\n" +
                "      \"externalMerchantTransactionId\":\"" + externalMerchantTransactionId + "\",\n" +
                "      \"chargeTotal\":" + GENIE_AMOUNT + ",\n" +
                "      \"counterId\":61,\n" +
                "      \"invoiceNumber\":\"1\",\n" +
                "      \"transactionDateTime\":\"" + formattedDate + "\",\n" +
                "      \"paymentReference\":\"reference\"\n" +
                "   }\n" +
                "}";
    }

    public void GenieStatuspayments() {
        try {
            if (dialogpr.isShowing()) {
                dialogpr.dismiss();
            }
            if (waitingforstatus != null && waitingforstatus.getStatus() != AsyncTask.Status.FINISHED)
                waitingforstatus.cancel(true);
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        } catch (Exception e) {

        }
    }

    //this for stop schedular after 2min customer has to complete before 2min
    public void stopschedularGenie() {

        final Handler nHandler = new Handler();
        nHandler.postDelayed(new Runnable() {
            public void run() {
                GenieStatuspayments();
                mHandler.removeCallbacksAndMessages(null);
            }
        }, 200000);
    }

    public void close_output_message_dialog() {
        final Handler nHandler = new Handler();
        nHandler.postDelayed(new Runnable() {
            public void run() {
                GenieStatuspayments();
                mHandler.removeCallbacksAndMessages(null);
                if (geinedialog != null && geinedialog.isShowing()) {
                    geinedialog.dismiss();
                }
            }
        }, FIVE_SECONDS);
    }

    public void scheduleGenieStatus() {
        stopschedularGenie();
        dialogpr = ProgressDialog.show(getActivity(),
                "Transaction Processing ...", "Please Wait...", false, false);
        WindowManager.LayoutParams wmlp =
                dialogpr.getWindow().getAttributes();
        wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        wmlp.height = 400;
        wmlp.width = 400;
        dialogpr.getWindow().setAttributes(wmlp);
        dialogpr.isShowing();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                waitingforstatus = new Waitingforstatus();
                waitingforstatus.execute();
                // get_Status_Of_Genie_TransactionProcess();
                //GenieStatuspayments();// this method will contain you almost-finished HTTP calls
                mHandler.postDelayed(this, FIVE_SECONDS);
            }
        }, FIVE_SECONDS);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.genie_pay:
                // GenieAccountNumber="+94715109942";
                Genie_ID = genieID.getText().toString();
                GENIE_AMOUNT = GenieAmount.getText().toString();
                if (isEmpty(Genie_ID)) {
                    genieID.setError("Please enter 9 digit FriMi ID/Mobile No.");
                } else if (isEmpty(GENIE_AMOUNT)) {
                    GenieAmount.setError("Amount can't be null");
                } else if (GenieAmount.getText().length() < 1) {
                    GenieAmount.setError("Amount can't be 0");
                } else if (genieID.getText().length() != 9) {
                    genieID.setError("Please enter 9 digit FriMi ID/Mobile No.");
                } else {
                    // writeInsertDatafrimi(FRIMI_TEST_WALLET_ID, FRIMI_AMOUNT);
                    GenieAccountNumber = genieIDPrefix.getText().toString() + Genie_ID;
                    GenieAccountNumber = GenieAccountNumber.replaceAll(" ", "");
                    GENIE_AMOUNT = GenieAmount.getText().toString();
                    CallingAPIProcess();
                }
                break;
            case R.id.genie_cancel:

                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;
        }
    }

    public void OutPutMessage() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View PayForGoodsProcess = inflater.inflate(R.layout.genieresponsedisplaylayout, null);
        dialog = new AlertDialog.Builder(getActivity());
        TextView Frim_Response_Message = (TextView) PayForGoodsProcess.findViewById(R.id.responseText);
        Button OKBtn = (Button) PayForGoodsProcess.findViewById(R.id.Okbutton);
        geinedialog = dialog.create();
        //geinedialog.setTitle("          PAY BY GENIE ...");
        try {
            String s = Message;
            // System.out.println();
            Frim_Response_Message.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        geinedialog.setView(PayForGoodsProcess);
        geinedialog.show();
        WindowManager.LayoutParams wmlp = geinedialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        wmlp.height = 700;
        wmlp.width = 380;

        geinedialog.getWindow().setAttributes(wmlp);

        geinedialog.setCanceledOnTouchOutside(false);

        OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // writeUpdateDatafrimi(FRIMI_TEST_WALLET_ID, FRIMI_AMOUNT);
                geinedialog.dismiss();
                if (dialogpr != null && dialogpr.isShowing()) {
                    dialogpr.dismiss();
                }
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(GenieFragment.this).commit();
                }
                if (Message != null) {
                    Message = "";
                }
            }
        });
    }

    // public void get_Status_Of_Genie_TransactionProcess() {
    public class Waitingforstatus extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /*     progressDialog = ProgressDialog.show(getActivity(),
                        "Transaction processing ...", "Please Wait...", false, false);

                WindowManager.LayoutParams wmlp =
                        progressDialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                wmlp.height = 400;
                wmlp.width = 400;
                progressDialog.getWindow().setAttributes(wmlp);*/
        }

        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = null;

            client = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();
            MediaType mediaType = MediaType.parse("application/json");

            Log.e("Body", "" + geniestatusbody());
            String json = geniestatusbody();
            RequestBody body = RequestBody.create(mediaType, json);
            Request request = new Request.Builder()
                    .url("https://ideabiz.lk/apicall/geniepos/v1/axipay/external/merchant/transaction/getstatus")
                    .addHeader("content-type", "application/json")
                    .addHeader("X-IH-SECRETCODE", "4ecb71e43cda4910a9f651f7d4600e12")
                    // .addHeader("X-IH-SECRETCODE","063357ce07d845e3925278cb5da2c975")
                    .addHeader("X-IH-PSW", "Think100%")
                    .addHeader("Authorization", "Bearer " + access_token)
                    .post(body)
                    .build();

            try {
                response = client.newCall(request).execute();
                test = response.body().string();
                if (response.isSuccessful()) {
                    System.out.println("Im in tst:: " + test);
                    System.out.println("response message ::   " +
                            response.code() + " : " + response.message());
                } else {
                    System.out.println(response.code() + " : " +
                            response.message());
                }

            } catch (SocketTimeoutException ex) {
                ex.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            waitingforstatus.cancel(true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressDialog.dismiss();
            try {
                if (response.code() == 200) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(test);

                        Log.e("response", "" + jsonObject.get("response"));
                        responsestring = String.valueOf(jsonObject.get("response"));
                        JSONObject opt = new JSONObject(responsestring);
                        status = opt.getString("status");
                        Log.e(" im before", status);
                        if (status.matches("success")) {
                            Log.e(" im in output", "" + status);
                            merchantTxnStatusName = opt.getString("merchantTxnStatusName");
                            externalMerchantTransactionId = opt.getString("externalMerchantTransactionId");
                            genieTransactionId = opt.getString("genieTransactionId");
                            transactionAmount = opt.getString("transactionAmount");
                            invoiceNumber = opt.getString("invoiceNumber");
                            transactionDateTime = opt.getString("transactionDateTime");
                            merchantPgIdentifier = opt.getString("merchantPgIdentifier");
                            Message = merchantTxnStatusName;
                            if (Message.matches("Payment Success")) {
                                mHandler.removeCallbacksAndMessages(null);
                                if (geinedialog != null && geinedialog.isShowing()) {
                                    geinedialog.dismiss();
                                    OutPutMessage();
                                } else {
                                    OutPutMessage();
                                }
                                close_output_message_dialog();
                            }
                            if (Message.matches("Payment Fail")) {
                                mHandler.removeCallbacksAndMessages(null);
                                if (geinedialog != null && geinedialog.isShowing()) {
                                    geinedialog.dismiss();
                                    OutPutMessage();
                                } else {
                                    OutPutMessage();
                                }
                                close_output_message_dialog();
                            }
                            if (Message.matches("Customer Reject")) {
                                mHandler.removeCallbacksAndMessages(null);
                                if (geinedialog != null && geinedialog.isShowing()) {
                                    geinedialog.dismiss();
                                    OutPutMessage();
                                } else {
                                    OutPutMessage();
                                }
                                close_output_message_dialog();
                            }
                            if (Message.matches("Merchant Cancel")) {
                                mHandler.removeCallbacksAndMessages(null);
                                if (geinedialog != null && geinedialog.isShowing()) {
                                    geinedialog.dismiss();
                                    OutPutMessage();
                                } else {
                                    OutPutMessage();
                                }
                                close_output_message_dialog();
                            }

                            //here i can write that timer after 5min this got stop with output or cancel by retailer
                            writeUpdateDataGenie(genieTransactionId, transactionAmount, Message);
                        } else {
                            Message = String.valueOf(opt.get("message"));
                            failcode = String.valueOf(opt.get("code"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    android.widget.Toast.makeText(getActivity(), "No response from server.",
                            android.widget.Toast.LENGTH_SHORT).show();
                    Message = response.message();
                    OutPutMessage();
                }
            } catch (NullPointerException e) {
                android.widget.Toast.makeText(getActivity(), "No response from server.",
                        android.widget.Toast.LENGTH_SHORT).show();
                Message = "No response from server.";
                OutPutMessage();
            }
        }
    }

    //}
    public String geniestatusbody() {
        //you can do it either external merchant transaction id or genie transaction id
       /* return "{  \n" +
                "   \"request\":{  \n" +
                "      \"merchantPgIdentifier\":\"PG00008372\",\n" +
                "      \"externalMerchantTransactionId\":\""+externalMerchantTransactionId+"\"\n" +
                "   }\n" +
                "}";*/

        return "{  \n" +
                "   \"request\":{  \n" +
                "      \"merchantPgIdentifier\":\"PG00008372\",\n" +
                "      \"genieTransactionId\":\"" + genieTransactionId + "\"\n" +
                "   }\n" +
                "}";
    }


    public void writeUpdateDataGenie(String genieTransactionId, String GENIE_AMOUNT, String Message) {
        try {
            SerialPortHelper UssPrinter = MediaMainScreen.mSerialPortHelper;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Method", "GenieUpdate");
            jsonObject.put("GENIEMOBILENO", GenieAccountNumber);
            jsonObject.put("GENIETRANID", genieTransactionId);
            jsonObject.put("GENIE_AMOUNT", GENIE_AMOUNT);
            jsonObject.put("MESSAGE", Message);
            jsonObject.put("EXTERNALMERTRANSID", externalMerchantTransactionId);
            jsonObject.put("DATE", formattedDate);
            UssPrinter.Write(String.valueOf(jsonObject));
            UssPrinter.Write(SerialPortHelper.EOT_COMMAND);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeInsertDataGenie(String genieTransactionId, String GENIE_AMOUNT, String Message) {
        try {
            SerialPortHelper UssPrinter = MediaMainScreen.mSerialPortHelper;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Method", "GenieInsert");
            jsonObject.put("GENIEMOBILENO", GenieAccountNumber);
            jsonObject.put("GENIETRANID", genieTransactionId);
            jsonObject.put("GENIE_AMOUNT", GENIE_AMOUNT);
            jsonObject.put("MESSAGE", Message);
            jsonObject.put("EXTERNALMERTRANSID", externalMerchantTransactionId);
            jsonObject.put("DATE", formattedDate);
            UssPrinter.Write(String.valueOf(jsonObject));
            UssPrinter.Write(SerialPortHelper.EOT_COMMAND);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}