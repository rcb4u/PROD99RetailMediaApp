package com.RSPL.MEDIA;

import android.app.ProgressDialog;
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

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.security.ProviderInstaller;

import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class GenieFragment extends Fragment {

    private String test;
    private String formattedDate;
    private String Message;
    private String access_token;
    private int success;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genie, container, false);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = dateFormat.format(new Date()).toString();
        System.out.println(formattedDate);
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

                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++) {

                            System.out.println(responseHeaders.name(i)
                                    + ": " + responseHeaders.value(i));
                        }

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

    public X509TrustManager provideX509TrustManager() {
        try {
            TrustManagerFactory factory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);
            TrustManager[] trustManagers = factory.getTrustManagers();
            return (X509TrustManager) trustManagers[0];
        } catch (NoSuchAlgorithmException | KeyStoreException exception) {
            Log.e(getClass().getSimpleName(), "not trust manager available", exception);
        }

        return null;
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

           /*     client = new OkHttpClient.Builder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .build();*/
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
                TrustManagerFactory trustManagerFactory = null;
                try {
                    trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    trustManagerFactory.init((KeyStore) null);
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                }
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:"
                            + Arrays.toString(trustManagers));
                }
                X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

                SSLContext sslContext = null;
                try {
                    sslContext = SSLContext.getInstance("TLS");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                try {
                    sslContext.init(null, new TrustManager[]{trustManager}, null);
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                client = new OkHttpClient.Builder()
                        .connectTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .sslSocketFactory(sslSocketFactory, trustManager)
                        .build();

                MediaType mediaType = MediaType.parse("application/json");

                Log.e("Body", "" + bodyin());
                String json = bodyin();
                RequestBody body = RequestBody.create(mediaType, json);
                Request request = new Request.Builder()
                        // .url("https://services.axis.dialog.lk:4080/axipay/external/merchant/transaction/initiate/push")
                        .url("https://ideabiz.lk/apicall/geniepos/v1/axipay/external/merchant/transaction/initiate/push")
                        .addHeader("content-type", "application/json")
                        .addHeader("X-IH-SECRETCODE", "4ecb71e43cda4910a9f651f7d4600e12")
                        // .addHeader("X-IH-SECRETCODE","063357ce07d845e3925278cb5da2c975")
                        .addHeader("X-IH-PSW", "Think100%")
                        .addHeader("Authorization", "Bearer " + access_token)
                        .post(body)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
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
            }
        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute();
    }

    public String bodyin() {
        return "{  \n" +
                "   \"request\":{  \n" +
                "      \"genieAccountNumber\":\"+94715109942\",\n" +
                "      \"merchantPgIdentifier\":\"PG00008372\",\n" +
                "      \"externalMerchantTransactionId\":\"2567\",\n" +
                "      \"chargeTotal\":70,\n" +
                "      \"counterId\":61,\n" +
                "      \"invoiceNumber\":\"1\",\n" +
                "      \"transactionDateTime\":\"" + formattedDate + "\",\n" +
                "      \"paymentReference\":\"reference\"\n" +
                "   }\n" +
                "}";
    }
}
    /*SSLContext sslcontext = null;
                try {
                        ProviderInstaller.installIfNeeded(getActivity());
                        } catch (GooglePlayServicesRepairableException e) {
                        e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                        e.printStackTrace();
                        }
                        try {
                        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                        sslContext.init(null, null, null);
                        SSLEngine engine = sslContext.createSSLEngine();
                        } catch (NoSuchAlgorithmException e) {

                        } catch (KeyManagementException e) {
                        e.printStackTrace();
                        }
                        SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

                        HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
                        try {
                        URL l_url= null;
                        l_url = new URL("https://services.axis.dialog.lk:4080/axipay/external/merchant/transaction/initiate/push");
                        HttpsURLConnection urlConnection = (HttpsURLConnection) l_url.openConnection();
                        urlConnection.setRequestProperty("Content-Type", "application/json;");
                        urlConnection.setRequestProperty("X-IH-SECRETCODE","4ecb71e43cda4910a9f651f7d4600e12");
                        urlConnection.setRequestProperty("X-IH-PSW", "Think100%");
                        urlConnection.setRequestProperty("Authorization","Bearer "+access_token);
                        urlConnection.setSSLSocketFactory(NoSSLv3Factory);
                        urlConnection.setRequestMethod("POST");
                        OutputStream os = urlConnection.getOutputStream();
                        os.write(bodyin().getBytes());
                        os.flush();
                        if (urlConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        while ((line = reader.readLine()) != null) {
                        StringBuilder result = new StringBuilder();
                        result.append(line);
                        JSONObject readers = new JSONObject(result.toString());
                        Log.e("GetMovies", String.valueOf(readers));

                        }
                        }
                        }catch (MalformedURLException e) {
                        e.printStackTrace();
                        } catch (IOException e) {
                        e.printStackTrace();
                        } catch (JSONException e) {
                        e.printStackTrace();
                        }*/