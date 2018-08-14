package com.RSPL.MEDIA;

/**
 * Created by rspl-rajeev on 28/2/18.
 */

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import JSON.JSONParserSync;


public class ListFileFragment extends Fragment implements View.OnClickListener {


    private File file;

    ListView mListView;


    Button btnShowCheckedItems;
    Button btnCancel;

    ArrayList<ListFilePojo> fileNameLists;

    MultiSelectionAdapter<ListFilePojo> mAdapter;
    DBhelper db = new DBhelper(getActivity());
    private String Store_Media_Id;
    private String storeName;
    private String storeAddress;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_listfiles, container, false);
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        MediaStorePojo mStoreId = db.getStoreDetails();

        Store_Media_Id = mStoreId.getMediaid();
        storeName = mStoreId.getStoreName();
        storeAddress = mStoreId.getStoreAddress();
        Log.e("STORE_MEDIA_ID", Store_Media_Id);
        Log.e("STORE NAME", storeName);
        Log.e("STORE ADD", storeAddress);


        mListView = (ListView) view.findViewById(R.id.filelistview);
        btnShowCheckedItems = (Button) view.findViewById(R.id.btnIddeleteFiles);
        btnCancel = (Button) view.findViewById(R.id.id_cancel);
        file = Environment.getExternalStorageDirectory();
        getFiles();
        fileNameLists = new ArrayList<>();
        if (GetFiles("/sdcard/1464772267/MainAd") != null) {
            ArrayList<String> fileNameList = GetFiles("/sdcard/1464772267/MainAd");
            for (String s : fileNameList) {
                ListFilePojo listFilePojo = new ListFilePojo(s);
                fileNameLists.add(listFilePojo);

            }


            mAdapter = new MultiSelectionAdapter<ListFilePojo>(getActivity(), fileNameLists);
            mListView.setAdapter(mAdapter);
        } else {

            Toast.makeText(getActivity(), "NO file Found", Toast.LENGTH_LONG).show();
        }


        btnShowCheckedItems.setOnClickListener(this);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                closefragment();

            }
        });


        return view;

    }


    public ArrayList<String> GetFiles(String directorypath) {
        ArrayList<String> Myfiles = new ArrayList<String>();
        File f = new File(directorypath);
        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0) {
            return null;
        } else {
            for (int i = 0; i < files.length; i++)
                Myfiles.add(files[i].getName());
        }
        return Myfiles;
    }

    public void getFiles() {
        File mfile = new File("/sdcard/1464772267/MainAd");
        File[] list = mfile.listFiles();

        System.out.println("No.of Files :" + mfile.listFiles().length);
        for (int i = 0; i < mfile.listFiles().length; i++) {
            if (list[i].isHidden()) ;
            Log.d("------->", " List of Files.. " + list[i].getName());
        }


    }


    @Override
    public void onClick(View view) {


        if (mAdapter != null) {


            ArrayList<ListFilePojo> mArrayProducts = mAdapter.getCheckedItems();


            for (int i = 0; i < mArrayProducts.size(); i++) {


                Toast.makeText(getActivity(), "Selected Items: " + mArrayProducts.get(i).getFileName(), Toast.LENGTH_LONG).show();


                File mfile = new File("/sdcard/1464772267/MainAd");

                File file = new File(mfile, mArrayProducts.get(i).getFileName());
                file.delete();
                uploadDeleteStatus(Store_Media_Id, storeName, storeAddress, mArrayProducts.get(i).getFileName());
                Intent intent = new Intent(getActivity(), MediaMainScreen.class);
                startActivity(intent);
            }


        }
    }

    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }

    public void uploadDeleteStatus(final String store_Media_Id,
                                   final String storeName,
                                   final String StoreAddress,
                                   final String AdFileName) {


        class UpdateProduct extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;
            int success;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // loading = ProgressDialog.show(ActivityProduct.this, "UPDATING...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //  loading.dismiss();
                // Toast.makeText(ActivityProduct.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    HashMap<String, String> hashMap = new HashMap<>();

                    hashMap.put("Store_Media_Id", store_Media_Id);
                    hashMap.put("Store_Name", storeName);
                    hashMap.put("Store_Address", StoreAddress);
                    hashMap.put("Ad_Name", AdFileName);

                    Log.e("DELETE , Store_Media_Id", store_Media_Id);
                    Log.e("Store_Name", storeName);
                    Log.e("Store_Address", StoreAddress);
                    Log.e("Ad_Name", AdFileName);


                    JSONParserSync rh = new JSONParserSync();
                    JSONObject s = rh.sendPostRequest("http://35.201.138.23:8080/Media_Delete/Delete_Report.jsp", hashMap);


                    Log.d("Login attempt", s.toString());

                    // success tag for json
                    success = s.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Log.d("Response Sent.!", s.toString());


                        return s.getString(TAG_MESSAGE);
                    } else {

                        return s.getString(TAG_MESSAGE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        }
        UpdateProduct updateproduct = new UpdateProduct();
        updateproduct.execute();
    }
}