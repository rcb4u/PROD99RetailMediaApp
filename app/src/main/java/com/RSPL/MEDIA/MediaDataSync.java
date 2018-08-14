package com.RSPL.MEDIA;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.app.Fragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaDataSync extends Fragment {

    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    private VideoView vidPreview;
    private Button btnUpload, btnCancel;
    long totalSize = 0;
    ProgressBar progressDialog;
    String response = "No connection";
    EditText password;
    TextView textView;
    DBhelper dBhelper;
    MediaStorePojo mediaStorePojo;
    // LogCat tag
    ImageButton error;
    BackgroundUploader uploader;
    Button btnMediaFiles;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_media_data_sync, container, false);

        txtPercentage = (TextView) view.findViewById(R.id.txtPercentage);
        btnUpload = (Button) view.findViewById(R.id.btnUpload);
        password = (EditText) view.findViewById(R.id.passwordDb);
        textView = (TextView) view.findViewById(R.id.pwText);
        error = (ImageButton) view.findViewById(R.id.error);
        btnCancel = (Button) view.findViewById(R.id.id_btn_cancel);
        btnMediaFiles = (Button) view.findViewById(R.id.btnmediafiles);
        dBhelper = new DBhelper(getActivity());
        mediaStorePojo = new MediaStorePojo();
        mediaStorePojo = dBhelper.getStoreDetails();

        // progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressDialog = (ProgressBar) view.findViewById(R.id.progressBar);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closefragment();

            }
        });

        btnMediaFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (password.getText().toString().equals("videodelete")) {

                    password.setVisibility(View.GONE);
                    textView.setText("");


                    ListFileFragment alertActivity = new ListFileFragment();
                    FragmentManager fragmentManager1 = getFragmentManager();
                    android.app.FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                    transaction1.replace(R.id.linearlayouts, alertActivity, "ALT");

                    transaction1.commit();
                } else {

                    password.setError("Invalid Password");
                }
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals("dbpull")) {
                    try {
                        password.setVisibility(View.GONE);
                        textView.setText("");
                        Process p;
                        p = Runtime.getRuntime().exec("su");


                        // Attempt to write a file to a root-only
                        DataOutputStream os = new DataOutputStream(p.getOutputStream());
                        os.writeBytes("cp /data/data/com.RSPL.MEDIA/databases/Db /sdcard/" + mediaStorePojo.getstoreId() + "\n");


                        // Close the terminal
                        os.writeBytes("exit\n");
                        os.flush();
                        p.waitFor();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    String sourceFileUri = "/mnt/sdcard/" + mediaStorePojo.getstoreId();
                    ;
                    String upLoadServerUri = "http://35.201.183.230/sync/mediadbsync";
                    // uploading the file to server
                    File sourceFile = new File(sourceFileUri);

                    uploader = new BackgroundUploader(upLoadServerUri, sourceFile);
                    uploader.execute();

                } else {
                    Toast.makeText(getActivity(), "Invalid Password", Toast.LENGTH_LONG).show();
                }

    /*    // Changing action bar background color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor(getResources().getString(
                        R.color.action_bar))));

        // Receiving the data from previous activity
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");

        // boolean flag to identify the media type, image or video
        boolean isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }*/


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


    class BackgroundUploader extends AsyncTask<Void, Integer, Void> implements DialogInterface.OnCancelListener {


        private String url;
        private File file;

        public BackgroundUploader(String url, File file) {
            this.url = url;
            this.file = file;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setProgress(0);

            // progressDialog.setMax((int) file.length());


        }

        @Override
        protected Void doInBackground(Void... v) {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = null;
            String fileName = file.getName();
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("POST");
                String boundary = "---------------------------boundary";
                String tail = "\r\n--" + boundary + "--\r\n";
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                connection.setDoOutput(true);

                String metadataPart = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"metadata\"\r\n\r\n"
                        + "" + "\r\n";

                String fileHeader1 = "--" + boundary + "\r\n"
                        + "Content-Disposition: form-data; name=\"file\"; filename=\""
                        + fileName + "\"\r\n"
                        + "Content-Type: application/octet-stream\r\n"
                        + "Content-Transfer-Encoding: binary\r\n";

                long fileLength = file.length() + tail.length();
                String fileHeader2 = "Content-length: " + fileLength + "\r\n";
                String fileHeader = fileHeader1 + fileHeader2 + "\r\n";
                String stringData = metadataPart + fileHeader;

                long requestLength = stringData.length() + fileLength;
                connection.setRequestProperty("Content-length", "" + requestLength);
                connection.setFixedLengthStreamingMode((int) requestLength);
                connection.connect();

                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(stringData);
                out.flush();

                int progress = 0;
                int bytesRead = 0;
                fileLength = file.length();
                byte buf[] = new byte[1024];
                BufferedInputStream bufInput = new BufferedInputStream(new FileInputStream(file));
                while ((bytesRead = bufInput.read(buf)) != -1) {
                    // write output
                    out.write(buf, 0, bytesRead);
                    out.flush();

                    progress += bytesRead;
                    // update progress bar
                    publishProgress((int) ((progress / (float) fileLength) * 100));


                }

                // Write closing boundary and close stream
                out.writeBytes(tail);
                out.flush();
                out.close();

                // Get server response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                Log.d("*******", String.valueOf(connection.getResponseCode()));
                response = connection.getResponseMessage();
                Log.d("************", connection.getResponseMessage());

            } catch (Exception e) {
                e.printStackTrace();
                response = String.valueOf(e);
                // Exception
            } finally {
                if (connection != null) connection.disconnect();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            progressDialog.setVisibility(View.VISIBLE);
            // updating progress bar value
            progressDialog.setProgress(progress[0]);

            txtPercentage.setText(String.valueOf(progress[0]) + " %");

            // Log.d("********", String.valueOf(progress[0]));

        }

        @Override
        protected void onPostExecute(Void v) {
            progressDialog.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnUpload.setVisibility(View.GONE);
            txtPercentage.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            if (response.equals("OK")) {
                textView.setText("Db Uploaded Succesfully");
            } else {
                textView.setText(response);
            }

        }

        @Override
        public void onCancel(DialogInterface dialog) {
            cancel(true);
            dialog.dismiss();
            uploader.cancel(true);
        }
    }


    private void closefragment() {
        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }


}