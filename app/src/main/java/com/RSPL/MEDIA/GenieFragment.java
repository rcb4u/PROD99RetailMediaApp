package com.RSPL.MEDIA;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
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
    private String Message;
    private String access_token;
    private int success;
    private String externalMerchantTransactionId;
    EditText genieID, GenieAmount;
    TextView genieIDPrefix;
    Button geniePay, genieCancel;
    private String Genie_ID;
    private String GENIE_AMOUNT;
    private String GenieAccountNumber;
    private AlertDialog.Builder dialog;
    private AlertDialog geinedialog;
    private Response response;
    private String genieTransactionId;
    private String status;
    private String responsestring;
    private String qrEncryptedText;
    private ImageView QrImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genie, container, false);
        QrImage = (ImageView) view.findViewById(R.id.QrimageView);
       /* genieIDPrefix = (TextView) view.findViewById(R.id.Genie_test_Wallet_ID_Tv);
        genieID = (EditText) view.findViewById(R.id.Genie_test_Wallet_ID);
        GenieAmount = (EditText) view.findViewById(R.id.genie_total_amount);
        geniePay = (Button) view.findViewById(R.id.genie_pay);
        genieCancel = (Button) view.findViewById(R.id.genie_cancel);
        geniePay.setOnClickListener(this);
        genieCancel.setOnClickListener(this);*/
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
                CallingAPIProcess();
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
                        //https://ideabiz.lk/apicall/geniepos/v1/axipay/external/merchant/transaction/initiate/push
                        .url("https://ideabiz.lk/apicall/geniepos/v1/axipay/external/merchant/transaction/initiate/qr")
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
                            genieTransactionId = opt.getString("genieTransactionId");
                            qrEncryptedText = opt.getString("qrEncryptedText");

                            Log.e(" im before", status);
                            if (status.matches("success")) {
                                Log.e(" im in output", "" + status);
                                //   OutPutMessage();
                                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                try {
                                    BitMatrix bitMatrix = multiFormatWriter.encode(qrEncryptedText, BarcodeFormat.QR_CODE, 200, 200);
                                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                    QrImage.setImageBitmap(bitmap);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }

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
        return "{\n" +
                "   \"request\":{\n" +
                "      \"amount\":\"300\",\n" +
                "      \"merchantPgIdentifier\":\"PG00008372\",\n" +
                "      \"externalMerchantTransactionId\":\"" + externalMerchantTransactionId + "\",\n" +
                "      \"description\":\"this is a test Item\",\n" +
                "      \"invoiceNumber\":\"1234\",\n" +
                "      \"counterId\":61,\n" +
                "      \"transactionDateTime\":\"" + formattedDate + "\"\n" +
                "   }\n" +
                "}";

        // GenieAccountNumber="+94715109942";
        //below code for initialise push
    /*    return "{  \n" +
                "   \"request\":{  \n" +
                "      \"genieAccountNumber\":\"+94715109942\",\n" +
                "      \"merchantPgIdentifier\":\"PG00008372\",\n" +
                "      \"externalMerchantTransactionId\":\"" + externalMerchantTransactionId + "\",\n" +
                "      \"chargeTotal\":70,\n" +
                "      \"counterId\":61,\n" +
                "      \"invoiceNumber\":\"1\",\n" +
                "      \"transactionDateTime\":\"" + formattedDate + "\",\n" +
                "      \"paymentReference\":\"reference\"\n" +
                "   }\n" +
                "}";*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.genie_pay:
                // GenieAccountNumber="+94715109942";
                Genie_ID = genieID.getText().toString();
                GENIE_AMOUNT = GenieAmount.getText().toString();
                if (isEmpty(Genie_ID)) {
                    genieID.setError("Please enter 10 digit FriMi ID/Mobile No.");
                } else if (isEmpty(GENIE_AMOUNT)) {
                    GenieAmount.setError("Amount can't be null");
                } else if (GenieAmount.getText().length() < 1) {
                    GenieAmount.setError("Amount can't be 0");
                } else if (genieID.getText().length() != 10) {
                    genieID.setError("Please enter 10 digit FriMi ID/Mobile No.");
                } else {
                    // writeInsertDatafrimi(FRIMI_TEST_WALLET_ID, FRIMI_AMOUNT);
                    GenieAccountNumber = genieIDPrefix.getText().toString() + Genie_ID;
                    CallingAPIProcess();
                }
                break;
            case R.id.genie_cancel:
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                break;*/
        }
    }

    public void OutPutMessage() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View PayForGoodsProcess = inflater.inflate(R.layout.genieresponsedisplaylayout, null);
        dialog = new AlertDialog.Builder(getActivity());
        TextView Frim_Response_Message = (TextView) PayForGoodsProcess.findViewById(R.id.responseText);
        Button OKBtn = (Button) PayForGoodsProcess.findViewById(R.id.Okbutton);
        geinedialog = dialog.create();
        geinedialog.setTitle("          MAKING THINGS SIMPLE...");
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
        wmlp.width = 400;
        geinedialog.getWindow().setAttributes(wmlp);

        geinedialog.setCanceledOnTouchOutside(false);

        OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // writeUpdateDatafrimi(FRIMI_TEST_WALLET_ID, FRIMI_AMOUNT);
                geinedialog.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().remove(GenieFragment.this).commit();
            }
        });
    }

}