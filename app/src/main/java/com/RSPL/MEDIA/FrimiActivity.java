package com.RSPL.MEDIA;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.serialport.api.SerialPortHelper;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.text.TextUtils.isEmpty;

public class FrimiActivity extends Fragment {

    EditText Frimi_Test_Wallet_id;
    EditText Frimi_amount;
    Button PAY, Cancel;
    private String Request_id;
    private String formattedDate;
    String FRIMI_TEST_WALLET_ID, FRIMI_AMOUNT, FRIMI_TOTAL_AMOUNT;
    AlertDialog FRIMI_R;
    AlertDialog.Builder dialog;
    private String Message;
    private String test;
    public String data, TXN_CODE, FRIMI_TXN_REF_NO, MERCHANT_REF_NO, DISCOUNT_AMOUNT, DESCRIPTION;
    private String amount;
    Response response;
    private String CurrentDate;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_frimi, container, false);

        //Frimi_mobile=(EditText)findViewById(R.id.frimi_mobile_no);
        Frimi_amount = (EditText) view.findViewById(R.id.frimi_total_amount);
        Frimi_amount.setEnabled(false);
        Frimi_Test_Wallet_id = (EditText) view.findViewById(R.id.frimi_test_Wallet_ID);
        // Frimi_notyetdecided=(EditText)findViewById(R.id.frimi_not_yet_decided);

        amount = getArguments().getString("GrandTotal");
        if (amount != null) {
            Frimi_amount.setText(String.format("%,.2f", Float.valueOf(amount)));
        } else {
            Frimi_amount.setText("0.00");
        }
        PAY = (Button) view.findViewById(R.id.frimi_pay);
        Cancel = (Button) view.findViewById(R.id.frimi_cancel);
        TokenGenerationProcess();
        Long tsLong = System.currentTimeMillis() / 10;
        Request_id = "12" + tsLong.toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy hh:mm:ss");
        formattedDate = dateFormat.format(new Date()).toString();
        System.out.println(formattedDate);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        CurrentDate = dateFormat1.format(new Date()).toString();
        System.out.println(CurrentDate);

        PAY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Datavalidation();
                } catch (Exception e) {
                }
                //  CallingAPIProcess ();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getFragmentManager().beginTransaction().remove(FrimiActivity.this).commit();
            }
        });
        return view;
    }

    private void CallingAPIProcess() {
        class WaitingforResponse extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "You're about to FriMi ...", "Please Wait...", false, false);

                WindowManager.LayoutParams wmlp = progressDialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                wmlp.height = 400;
                wmlp.width = 400;
                progressDialog.setIcon(R.drawable.frimi_logo);
                progressDialog.getWindow().setAttributes(wmlp);
            }

            @Override
            protected String doInBackground(Void... params) {
                OkHttpClient client = null;
                try {
                    client = new OkHttpClient.Builder()
                            .connectTimeout(300, TimeUnit.SECONDS)
                            .writeTimeout(300, TimeUnit.SECONDS)
                            .readTimeout(300, TimeUnit.SECONDS)
                            .sslSocketFactory(new Tls12SocketFactory(), provideX509TrustManager())
                            .build();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                MediaType mediaType = MediaType.parse("application/json");

                Log.e("getval", "" + bodyin());
                String json = bodyin();
                RequestBody body = RequestBody.create(mediaType, json);
                Request request = new Request.Builder()

                        // https://uatapi.nationstrust.com:8243/ntb/sense/1.0.0/common
                        .url("https://uatapi.nationstrust.com:8243/ntb/sense/1.0.0/common")

                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("authorization", "Bearer " + PersistenceManager.getSessionId(getActivity()))
                        .post(body)
                        .build();
                try {
                    response = client.newCall(request).execute();
                    test = response.body().string();
                    if (response.isSuccessful()) {
                        System.out.println("Im in tst:: " + test);
                        System.out.println("response message ::   " + response.code() + " : " + response.message());
                    } else {
                        System.out.println(response.code() + " : " + response.message());
                    }
                } catch (SocketTimeoutException ex) {
                    ex.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (EOFException e) {
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

                            Log.e(" tid", "" + jsonObject.get("tid"));
                            Log.e(" request_id", "" + jsonObject.get("request_id"));
                            Log.e(" app_id", "" + jsonObject.get("app_id"));
                            Log.e(" module_id", "" + jsonObject.get("module_id"));
                            Log.e(" req_type_id", "" + jsonObject.get("req_type_id"));
                            Log.e(" date_time", "" + jsonObject.get("date_time"));
                            Log.e(" sender_id", "" + jsonObject.get("sender_id"));
                            //    Log.e(" txn_code", "" + jsonObject.get("txn_code"));
                            Log.e(" body", "" + jsonObject.get("body"));
                            String output = decodeBase64(String.valueOf(jsonObject.get("body")));
                            Log.e(" body", "" + output);
                            JSONObject opt = new JSONObject(output);

                            String val = opt.getString("txn_code");
                            Message = String.valueOf(opt.get("description"));
                            Log.e(" im before", val);
                            if (val.matches("00")) {
                                Log.e(" im in output", "" + val);
                                OutPutMessage();
                                //return result.complete('success');
                            } else if (val.matches("-1")) {
                                Log.e(" im in output", "" + val);
                                OutPutMessage();
                            } else {
                                Log.e(" im in output", "" + val);
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

    public X509TrustManager provideX509TrustManager() {
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);
            TrustManager[] trustManagers = factory.getTrustManagers();
            return (X509TrustManager) trustManagers[0];
        } catch (NoSuchAlgorithmException | KeyStoreException exception) {
            Log.e(getClass().getSimpleName(), "not trust manager available", exception);
        }

        return null;
    }

    public void TokenGenerationProcess() {
        class WaitingforResponse extends AsyncTask<Void, Void, String> {
            ProgressDialog progressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = ProgressDialog.show(getActivity(), "Waiting for Token Generation...", "Please Wait...", false, false);
                WindowManager.LayoutParams wmlp = progressDialog.getWindow().getAttributes();
                wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                wmlp.height = 400;
                wmlp.width = 400;
                progressDialog.setIcon(R.drawable.frimi_logo);
                progressDialog.getWindow().setAttributes(wmlp);
            }

            @Override
            protected String doInBackground(Void... params) {
                OkHttpClient client = null;
                try {
                    client = new OkHttpClient.Builder()
                            .connectTimeout(120, TimeUnit.SECONDS)
                            .writeTimeout(120, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            .sslSocketFactory(new Tls12SocketFactory(), provideX509TrustManager())
                            .build();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials");
                Request request = new Request.Builder()
                        .url("https://uatapi.nationstrust.com:8243/token")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("authorization", "Basic N000SDNmU3RtVERuZmZ1R0JNMlBGR1FXdmtFYTpEQUFJcEprVUhjdXBwcEx4dkRPSkFYZjNwMmth")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String test = response.body().string();
                    if (response.isSuccessful()) {
                        System.out.println(test);
                    } else {
                        System.out.println(response.code() + " : " + response.message());
                    }

                    JSONObject jsonObject = new JSONObject(test);
                    Log.e(" scope", "" + jsonObject.get("scope"));
                    Log.e(" token_type", "" + jsonObject.get("token_type"));
                    Log.e(" expires_in", "" + jsonObject.get("expires_in"));
                    Log.e(" access_token", "" + jsonObject.get("access_token"));
                    // Message = (String) jsonObject.get("refresh_token");

                    String access_token = (String) jsonObject.get("access_token");

                    //PersistenceManager.saveRefreshToken(getApplicationContext(), refresh_token);
                    PersistenceManager.saveSessionId(getActivity(), access_token);
                    PersistenceManager.saveTime(getActivity(), String.valueOf(System.currentTimeMillis()));
                    // reference= (String) jsonObject.get("Refno");


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
            }
        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute();
    }

    public String inputbody() {

//        String FRIMI_MOBILE = Frimi_mobile.getText().toString();
        FRIMI_TEST_WALLET_ID = Frimi_Test_Wallet_id.getText().toString();
        FRIMI_AMOUNT = amount;

        return "{\n" +
                "\"frimi_id\":\"" + FRIMI_TEST_WALLET_ID + "\",\n" +
                "\"merchant_ref_no\":\"1100312\",\n" +
                "\"txn_amount\":\"" + FRIMI_AMOUNT + "\",\n" +
                "\"txn_currency_code\":\"144\",\n" +
                "\"mid\":\"917404519\",\n" +
                "\"mobile_no\":\"\",\n" +
                "\"discount_amount\":\"0.00\",\n" +
                "\"description\":\"Testing transaction\",\n" +
                "\"custom_field_01\":\"\",\n" +
                "\"custom_field_02\":\"\"}";
    }

    public String outputbody(String str) {
        byte[] bytesEncoded = new byte[0];
        try {
            bytesEncoded = Base64.encode(str.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("encoded value is " + new String(bytesEncoded));

        // Decode data on other side, by processing encoded data
        return new String(bytesEncoded);
    }

    public String bodyin() {
       /* return "                         \"tid\":\"11111160\",\n" +
                "                        \"request_id\":\"0125489748\",\n" +
                "                        \"app_id\":\"FriMi\",\n" +
                "                        \"module_id\":\"WAM\",\n" +
                "                        \"req_type_id\":\"001\",\n" +
                "                        \"date_time\":\"29-Jun-2018 00:30:30\",\n" +
                "                        \"sender_id\":\"\" ";*/
        return "{\n" +
                "\"tid\":\"11111160\",\n" +
                "\"request_id\":\"" + Request_id + "\",\n" +
                "\"app_id\":\"FriMi\",\n" +
                "\"module_id\":\"WAM\",\n" +
                "\"req_type_id\":\"001\",\n" +
                "\"date_time\":\"" + formattedDate + "\",\n" +
                "\"sender_id\":\"\",\n" +
                "\"body\":\"" + outputbody(inputbody()) + "\"}";
    }

    private String decodeBase64(String coded) {
        byte[] valueDecoded = new byte[0];
        try {
            valueDecoded = Base64.decode(coded.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
        }
        return new String(valueDecoded);
    }

    /// Frimi Amount pay....................................
    public String Datavalidation() {
        FRIMI_TEST_WALLET_ID = Frimi_Test_Wallet_id.getText().toString();
        FRIMI_AMOUNT = Frimi_amount.getText().toString();
        // Frimi_amount.getText().toString().startsWith("1");

        if (isEmpty(FRIMI_TEST_WALLET_ID)) {
            Frimi_Test_Wallet_id.setError("Pleaae enter 10 digit FriMi ID/Mobile No.");
            //Toast tost = Toast.makeText(this, "Enter mobile", Toast.LENGTH_LONG);
            //tost.show();
        } else if (isEmpty(FRIMI_AMOUNT)) {
            Frimi_amount.setError("Amount can't ne null");
        } else if (Frimi_amount.getText().length() < 1) {
            Frimi_amount.setError("Amount can't be 0");
        } else if (Frimi_Test_Wallet_id.getText().length() != 10) {
            Frimi_Test_Wallet_id.setError("Pleaae enter 10 digit FriMi ID/Mobile No.");
        } else {
            writeInsertDatafrimi(FRIMI_TEST_WALLET_ID, FRIMI_AMOUNT);
            CallingAPIProcess();
            //Toast tost = Toast.makeText(this, "Enter mobile", Toast.LENGTH_LONG);
        }
        return null;
    }

    public void OutPutMessage() {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View PayForGoodsProcess = inflater.inflate(R.layout.responsedisplaylayout, null);
        dialog = new AlertDialog.Builder(getActivity());
        TextView Frim_Response_Message = (TextView) PayForGoodsProcess.findViewById(R.id.responseText);
        Button OKBtn = (Button) PayForGoodsProcess.findViewById(R.id.Okbutton);
        FRIMI_R = dialog.create();
        FRIMI_R.setTitle("          MAKING THINGS SIMPLE...");
        try {
            String s = Message;
            // System.out.println();
            Frim_Response_Message.setText(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        FRIMI_R.setView(PayForGoodsProcess);
        FRIMI_R.show();
        WindowManager.LayoutParams wmlp = FRIMI_R.getWindow().getAttributes();
        wmlp.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        wmlp.height = 700;
        wmlp.width = 400;
        FRIMI_R.getWindow().setAttributes(wmlp);

        FRIMI_R.setCanceledOnTouchOutside(false);

        OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(test);
                    // data =decodeBase64(String.valueOf(jsonObject.get("body")));
                    String output = decodeBase64(String.valueOf(jsonObject.get("body")));
                    Log.e(" body", "" + output);
                    JSONObject opt = new JSONObject(output);

                    TXN_CODE = opt.getString("txn_code");
                    FRIMI_TXN_REF_NO = opt.getString("frimi_txn_ref_no");
                    MERCHANT_REF_NO = opt.getString("merchant_ref_no");
                    DISCOUNT_AMOUNT = opt.getString("discount_amount");
                    DESCRIPTION = opt.getString("description");

                    Log.e(" data send to pos", "" + TXN_CODE);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                writeUpdateDatafrimi(FRIMI_TEST_WALLET_ID, FRIMI_AMOUNT);
                FRIMI_R.dismiss();
                getActivity().getFragmentManager().beginTransaction().remove(FrimiActivity.this).commit();
            }
        });
    }

    public void writeUpdateDatafrimi(String FRIMI_TEST_WALLET_ID, String FRIMI_AMOUNT) {
        try {
            SerialPortHelper UssPrinter = MediaMainScreen.mSerialPortHelper;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Method", "FriMi");
            jsonObject.put("Request_id", Request_id);
            jsonObject.put("frimi_id", FRIMI_TEST_WALLET_ID);
            jsonObject.put("FriMi_Amount", FRIMI_AMOUNT);
            jsonObject.put("TXN_CODE", TXN_CODE);
            jsonObject.put("FRIMI_TXN_REF_NO", FRIMI_TXN_REF_NO);
            jsonObject.put("MERCHANT_REF_NO", MERCHANT_REF_NO);
            jsonObject.put("DISCOUNT_AMOUNT", DISCOUNT_AMOUNT);
            jsonObject.put("DESCRIPTION", DESCRIPTION);
            jsonObject.put("DATE", CurrentDate);
            UssPrinter.Write(String.valueOf(jsonObject));
            UssPrinter.Write(SerialPortHelper.EOT_COMMAND);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeInsertDatafrimi(String FRIMI_TEST_WALLET_ID, String FRIMI_AMOUNT) {
        try {
            SerialPortHelper UssPrinter = MediaMainScreen.mSerialPortHelper;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Method", "FriMiInsert");
            jsonObject.put("Request_id", Request_id);
            jsonObject.put("frimi_id", FRIMI_TEST_WALLET_ID);
            jsonObject.put("FriMi_Amount", FRIMI_AMOUNT);
            jsonObject.put("status", "Pending");
            jsonObject.put("DATE", CurrentDate);
            UssPrinter.Write(String.valueOf(jsonObject));
            UssPrinter.Write(SerialPortHelper.EOT_COMMAND);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}