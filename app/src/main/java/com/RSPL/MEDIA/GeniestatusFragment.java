package com.RSPL.MEDIA;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeniestatusFragment extends Fragment {
    private Response response;
    private String test;
    private String responsestring;
    private String status;
    private String genieTransactionId, merchantTxnStatusName,
            externalMerchantTransactionId, transactionAmount, invoiceNumber, transactionDateTime, merchantPgIdentifier, Message;
    private String failcode;
    private String access_token;

    public GeniestatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_geniestatus, container, false);
        access_token = getArguments().getString("access_token");
        genieTransactionId = getArguments().getString("genieTransactionId");
        externalMerchantTransactionId = getArguments().getString("externalMerchantTransactionId");

        get_Status_Of_Genie_TransactionProcess();
        return view;
    }

    private void get_Status_Of_Genie_TransactionProcess() {
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
                        // OutPutMessage();
                    }
                } catch (NullPointerException e) {
                    android.widget.Toast.makeText(getActivity(), "No response from server.",
                            android.widget.Toast.LENGTH_SHORT).show();
                    Message = "No response from server.";
                    //OutPutMessage();
                }
            }
        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute();
    }

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
}
