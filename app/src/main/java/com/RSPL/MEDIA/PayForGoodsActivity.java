package com.RSPL.MEDIA;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Fragment;

import com.ngx.USBPrinter;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayForGoodsActivity extends Fragment implements View.OnClickListener {

    int success;
    String Message, otps, merchantTransId, test, reference, responsecode;
    EditText Pincode, CustomerMobileNo, otpPayForGoods;
    TextView Transactionamt;
    Button Submit, btnOK, Cancel;
    String Transamt, custmerno, Retailermerchantid, RetailermobileNo, VendorMobileNo;
    AlertDialog PayforGoods;
    AlertDialog.Builder dialog;
    JSONObject jsonObject;
    String store_id;
    DBhelper db = new DBhelper(getActivity());
    ArrayList<String> WalletDetail;
    public static USBPrinter UsPrinter = USBPrinter.INSTANCE;
    TextPaint tp = new TextPaint();
    TextPaint tp32 = new TextPaint();
    TextPaint tp36 = new TextPaint();
    Typeface tff;
    String store, storeAddress, City, Storenumber, Str, AlternateNo, Footer, amountdiscount, Transid;
    String MRPisShown, FooterShown, Tele2Shown, PrintBill;
    ArrayList<String> store_name;
    int paybycashbillcopy;
    private String eol = "\n";
    private static final int PRINT_LEFT_MARGIN = 2;
    ArrayList<String> list;
    private static final String TAG_SUCCESS = "success";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_pay_for_goods, container, false);

        String s = getActivity().getIntent().getStringExtra("PayAmount");
        Transid = getActivity().getIntent().getStringExtra("TransID");
        //list=getIntent().getStringArrayListExtra("list");
        Pincode = (EditText) view.findViewById(R.id.pincode);
        Transactionamt = (TextView) view.findViewById(R.id.transactionamount);
        CustomerMobileNo = (EditText) view.findViewById(R.id.customermobilenumber);
        Submit = (Button) view.findViewById(R.id.cashOutSubmit);
        Cancel = (Button) view.findViewById(R.id.Cancelpayforgoods);
        Submit.setOnClickListener(this);
        Cancel.setOnClickListener(this);

        String amount = getArguments().getString("GrandTotal");
        Transactionamt.setText(amount);
        getActivity().setFinishOnTouchOutside(false);
        PersistenceManager.saveStoreId(getActivity(), db.getStoreid().toString().replace("[", "").replace("]", ""));
        store_id = PersistenceManager.getStoreId(getActivity());
        //  WalletDetail= db.getWalletDetail();
        Retailermerchantid = "AQUM00-5999";
        VendorMobileNo = "0711627783";

        /*Visibility value = db.getStorevisibility();
        MRPisShown = value.getMrpvisibility();
        FooterShown = value.getFootervisi();
        Tele2Shown = value.getTele2();
        PrintBill = value.getItemvisibilty();//bill print yes or not
        paybycashbillcopy = Integer.parseInt(value.getBillcopy());*/
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == Submit) {
            if (Pincode.getText().toString().equals("") || Transactionamt.getText().toString().equals("") || CustomerMobileNo.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Please fill the mandatory fields", Toast.LENGTH_SHORT).show();
                return;

            } else {
                Transamt = Transactionamt.getText().toString();
                custmerno = CustomerMobileNo.getText().toString();
                invoiceno();
                ValidateCustomerprocess();

            }
        } else if (v == Cancel) {
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();

        }
    }

    public void invoiceno() {
        Long Value = System.currentTimeMillis();
        String result = Long.toString(Value);
        String imei = "Mer-";
        Log.e("ImeiNo", imei.toString());
        String x_imei = imei.toString();
        String x1 = x_imei.replace("[", "").replace("]", "").concat(result);
        Log.e("X1_imei is :", x1);
        merchantTransId = x1;
    }

    public void ValidateCustomerprocess() {
        class WaitingforResponse extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(getActivity(), "Waiting for Response  Validate Customer...", "Please Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.e("Message", "" + success + "   " + reference);
                try {
                    if (responsecode.matches("2007")) {

                        CheckwalletbalanceDialog();
                    } else {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //update ui here
                                // display toast here
                                Toast.makeText(getActivity(), "" + Message, Toast.LENGTH_SHORT).show();
                            }
                        });
                        OutPutMessage();

                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                //  OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
                String json = ValidateCustomerJson();
                RequestBody body = RequestBody.create(mediaType, json);
                Request request = new Request.Builder()
                        //https://apphub.mobitel.lk/mobitelint/mapis/mcashexternalapi/validateCustomer
                        .url("https://apphubstg.mobitel.lk/intstg/sb/mcashexternalapi/validateCustomer")
                        .post(body)
                        //c6332897-79fa-4d3b-9194-26860843bbeb"
                        //
                        .addHeader("x-ibm-client-id", "66860d13-0b2d-4ef9-9c1e-34253b82646b")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
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
                        try {
                            jsonObject = new JSONObject(test);
                            Log.e(" transaction-amount ", "" + jsonObject.get("transaction-amount"));
                            Message = (String) jsonObject.get("response");
                            responsecode = (String) jsonObject.get("response-code");
                            reference = (String) jsonObject.get("reference");
                            //"transaction-amount":"250.0","response":"SOAP validation error","response-code":"500","merchant-transaction-id":"Mer-1495789549702"}
                            // {"transaction-type":"COUNTER_SALES","reference":"20170526185553511726","transaction-amount":"500.0","response":"OTP request send to customer, One Time Password(OTP) send to customer","response-code":"2007","merchant-transaction-id":"Mer-1495805183373"}
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("SocketTimeOut", "" + response);
                        System.out.println(test);
                        try {
                            jsonObject = new JSONObject(test);

                            Message = (String) jsonObject.get("httpMessage");

                            /*responsecode=(String)jsonObject.get("response-code");
                            reference= (String) jsonObject.get("reference");*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute();
    }

    @Override
    public String toString() {
        return Message;
    }

    private void CheckwalletbalanceDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View PayForGoodsProcess = inflater.inflate(R.layout.gettingotpcashout, null);
        dialog = new AlertDialog.Builder(getActivity());
        otpPayForGoods = (EditText) PayForGoodsProcess.findViewById(R.id.otp_cashOut);

        btnOK = (Button) PayForGoodsProcess.findViewById(R.id.cashOutOk);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otps = otpPayForGoods.getText().toString();
                PayforGoods.dismiss();
                PayForGoodsProcess();

            }
        });
        PayforGoods = dialog.create();
        PayforGoods.setView(PayForGoodsProcess);
        PayforGoods.setTitle("Enter OTP");

        PayforGoods.show();
        PayforGoods.setCanceledOnTouchOutside(false);
    }

    public void OutPutMessage() {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View PayForGoodsProcess = inflater.inflate(R.layout.responsedisplaylayout, null);
        dialog = new AlertDialog.Builder(getActivity());
        TextView responseText = (TextView) PayForGoodsProcess.findViewById(R.id.responseText);
        Button OKBtn = (Button) PayForGoodsProcess.findViewById(R.id.Okbutton);

        PayforGoods = dialog.create();
        PayforGoods.setTitle("          Response from Server");
        try {
            String s = Message;
            // System.out.println();
            responseText.setText(s.substring(s.lastIndexOf(',') + 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        PayforGoods.setView(PayForGoodsProcess);
        PayforGoods.show();
        PayforGoods.setCanceledOnTouchOutside(false);
        OKBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayforGoods.dismiss();
                // finish();
            }
        });

    }

    public void PayForGoodsProcess() {
        class WaitingforResponse extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            int success;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(getActivity(), "Waiting for Response PayForGoods...", "Please Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {

                    if (responsecode.matches("1000")) {

                        OutPutMessage();
                        db.McashDatainsert(store_id, Retailermerchantid, merchantTransId, Transactionamt.getText().toString(), CustomerMobileNo.getText().toString(), "PAYFORGOODS");
                        // Mcash_walletDetail();
                        Intent resultIntent = new Intent();
                        // TODO Add extras or a data URI to this intent as appropriate.
                        resultIntent.putExtra("ResponseCode", "1000");
                        getActivity().setResult(Activity.RESULT_OK, resultIntent);

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "" + Message, Toast.LENGTH_SHORT).show();
                            }
                        });
                        OutPutMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... params) {
                //OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
                String json = CashOutJson();
                RequestBody body = RequestBody.create(mediaType, json);
                Request request = new Request.Builder()
                        //https://apphub.mobitel.lk/mobitelint/mapis/mcashexternalapi/payForGoods
                        .url("https://apphubstg.mobitel.lk/intstg/sb/mcashexternalapi/payForGoods")
                        .post(body)
                        //production committed
                        //c6332897-79fa-4d3b-9194-26860843bbeb
                        .addHeader("x-ibm-client-id", "66860d13-0b2d-4ef9-9c1e-34253b82646b")
                        .addHeader("content-type", "application/json")
                        .addHeader("accept", "application/json")
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    test = response.body().string();
                    success = response.code();

                    if (response.isSuccessful()) {
                        success = response.code();
                        Headers responseHeaders = response.headers();
                        for (int i = 0; i < responseHeaders.size(); i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }

                        System.out.println(test);
                        try {
                            jsonObject = new JSONObject(test);
                            Log.e(" transaction-amount ", "" + jsonObject.get("transaction-amount"));

                            Message = (String) jsonObject.get("response");
                            responsecode = (String) jsonObject.get("response-code");
                            reference = (String) jsonObject.get("reference");
                            //"transaction-amount":"250.0","response":"SOAP validation error","response-code":"500","merchant-transaction-id":"Mer-1495789549702"}
                            // {"transaction-type":"COUNTER_SALES","reference":"20170526185553511726","transaction-amount":"500.0","response":"OTP request send to customer, One Time Password(OTP) send to customer","response-code":"2007","merchant-transaction-id":"Mer-1495805183373"}
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("SocketTimeOut", "" + response);
                        System.out.println(test);
                        try {
                            jsonObject = new JSONObject(test);

                            Message = (String) jsonObject.get("httpMessage");

                            /*responsecode=(String)jsonObject.get("response-code");
                            reference= (String) jsonObject.get("reference");*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

        }
        WaitingforResponse WaitingforResponse = new WaitingforResponse();
        WaitingforResponse.execute();

    }

    String ValidateCustomerJson() {
        // here for the testting im using pin is 1111 bt for the production pin is always 0000
        return " \n" +
                "{\n" +
                "  \"merchant-data\": {\n" +
                "    \"pin-code\": \"1111\",\n" +
                "    \"merchant-transaction-id\": \"" + merchantTransId + "\",\n" +
                "    \"merchant-id\": \"" + Retailermerchantid + "\",\n" +
                "    \"mobile-number\": \"" + VendorMobileNo + "\"\n" +
                "  },\n" +
                "  \"customer-data\": {\n" +
                "    \"note\": \"Validate COUNTER_PAYMENTS\",\n" +
                "    \"transaction-amount\":" + Transactionamt.getText().toString() + ",\n" +
                "    \"customer-mobile-number\": \"" + CustomerMobileNo.getText().toString() + "\",\n" +
                "    \"merchant-outlet-code\": \"01\"\n" +
                "  },\n" +
                "  \"validate-data\": {\n" +
                "    \"validate-for\": \"COUNTER_PAYMENTS\"\n" +
                "  }\n" +
                "}";


    }

    String CashOutJson() {
     /*   return " {\n" +
                "  \"merchant-data\": {\n" +
                "    \"pin-code\": \"'"+Pincode.getText().toString()+"'\",\n" +
                "    \"merchant-transaction-id\": \""+merchantTransId+"\",\n" +
                "    \"merchant-id\": \"ASIR00-5912\",\n" +
                "    \"mobile-number\": \"0712785148\"\n" +
                "  },\n" +
                "  \"transaction-data\": {\n" +
                "    \"note\": \"PAY for goods Process\",\n" +
                "    \"transaction-amount\":"+Transactionamt.getText().toString()+",\n" +
                "    \"customer-mobile-number\": \""+CustomerMobileNo.getText().toString()+"\",\n" +
                "    \"merchant-outlet-code\": \"01\"\n" +
                "  },\n" +
                "  \"otp-details\": {\n" +
                "    \"customer-otp\": \"'"+otps+"'\",\n" +
                "    \"reference\": \""+reference+"\"\n" +
                "  }\n" +
                "}";*/


        return "{\n" +
                "  \"merchant-data\": {\n" +
                "    \"pin-code\": \"" + Pincode.getText().toString() + "\",\n" +
                "    \"merchant-transaction-id\": \"" + merchantTransId + "\",\n" +
                "    \"merchant-id\": \"" + Retailermerchantid + "\",\n" +
                "    \"mobile-number\": \"" + VendorMobileNo + "\"\n" +
                "  },\n" +
                "  \"transaction-data\": {\n" +
                "    \"note\": \"PAY for goods Process\",\n" +
                "    \"transaction-amount\":" + Transactionamt.getText().toString() + ",\n" +
                "    \"customer-mobile-number\": \"" + CustomerMobileNo.getText().toString() + "\",\n" +
                "    \"merchant-outlet-code\": \"01\"\n" +
                "  },\n" +
                "  \"otp-data\": {\n" +
                "    \"customer-otp\": \"" + otps + "\",\n" +
                "    \"reference\": \"" + reference + "\"\n" +
                "  }\n" +
                "}";
    }
    ///////////////////////////////////////////////////////////////////////////////////////
   /* public void Mcash_walletDetail() {

        // final String pos_user = user2.getText().toString();
        PersistenceManager.saveStoreId(PayForGoodsActivity.this, db.getStoreid().toString().replace("[", "").replace("]", ""));
        String save_sales_store_id = PersistenceManager.getStoreId(PayForGoodsActivity.this);
        store_id = save_sales_store_id.substring(0, 10);
        final String merchantID=Retailermerchantid;
        final String MerchantTransID=merchantTransId;
        final String TransAmount=Transactionamt.getText().toString();
        final String CustomerNo=CustomerMobileNo.getText().toString();

        class UpdateActivitySalesbill extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            int success;

            @Override
            protected String doInBackground(Void... params) {

                try {
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put(Config.STORE_ID, store_id);
                    hashMap.put(Config.MERCHANT_ID,merchantID );
                    hashMap.put(Config.MERCHANT_TRANS_ID, MerchantTransID);
                    hashMap.put(Config.TRANS_AMOUNT, TransAmount);
                    hashMap.put(Config.CUST_MOBILE_NO, CustomerNo);
                    hashMap.put(Config.PAYMENT_MODE, "PAYFORGOODS");
                    hashMap.put(Config.S_FLAG, "1");
                    hashMap.put(Config.POS_USER,"User1");
                    JSONParserSync rh = new JSONParserSync();
                    // JSONObject s = rh.sendPostRequest("https://uldev.eu-gb.mybluemix.net/Pay_By_Cash.jsp", hashMap);
                    JSONObject s = rh.sendPostRequest(Config.SALES_BILL_MCASH, hashMap);

                    Log.d("Login attempt", "Mcash---->" + s.toString());

                    success = s.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Successfully !", "Mcash---->" + s.toString());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        UpdateActivitySalesbill updateActivitySalesbill = new UpdateActivitySalesbill();
        updateActivitySalesbill.execute();
    }
*/
////////////////////////////////////////////////////////////////////////////////////////


}

