package com.RSPL.MEDIA;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ngx.DebugLog;
import com.ngx.USBPrinter;

public class USBPrinterMain extends ActionBarActivity {


    public static USBPrinter UPrinter = USBPrinter.INSTANCE;
    private static FragmentManager fragMgr;
    private AppCompatActivity nm;
    private static final String cHomeStack = "home";
    private TextView tvStatus;

    public static SharedPreferences mSp;

    private String mConnectedDeviceName = "";
    public static final String title_connecting = "connecting...";
    public static final String title_connected_to = "connected: ";
    public static final String title_not_connected = "not connected";

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case USBPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case USBPrinter.STATE_CONNECTED:
                            tvStatus.setText(title_connected_to);

                            tvStatus.append(mConnectedDeviceName);
                            break;
					/*case USBPrinter.STATE_CONNECTING:
						tvStatus.setText(title_connecting);
						break;
					case USBPrinter.STATE_LISTEN:*/
                        case USBPrinter.STATE_NONE:
                            tvStatus.setText(title_not_connected);
                            break;

                        //Bellow status message should be shown in different textview.
                        case USBPrinter.ACTION_SERIALPORT_CONNECTED:
                            tvStatus.setText("Serial Port connected");
                            break;
                        case USBPrinter.ACTION_SERIALPORT_DISCONNECTED:
                            tvStatus.setText("Serial Port not connected");
                            break;
                        case USBPrinter.ACTION_SERIALPORT_NOT_SUPPORTED:
                            tvStatus.setText("Serial Port not supported");
                    }
                    break;
                case USBPrinter.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(
                            USBPrinter.DEVICE_NAME);
                    break;
                case USBPrinter.MESSAGE_STATUS:
                    tvStatus.setText(msg.getData().getString(
                            USBPrinter.STATUS_TEXT));
                    break;

//for weighing scale
			/*case UsbSerialService.MESSAGE_FROM_SERIAL_PORT:
				String data = msg.getData().getString(UsbSerialService.WEIGHT_TEXT);
				//UnicodeFragment.showWeight.setText(data);
				TalkEachOtherFragment.txtReadData.append(data);

				break;*/
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btp_main);
        tvStatus = (TextView) findViewById(R.id.tvstatus);


        mSp = PreferenceManager.getDefaultSharedPreferences(this);

        UPrinter.initServices(this, mHandler);

        UPrinter.initSerialPortServices();


        //nm = new UnicodeFragment();
        nm = new MediaMainScreen();


    }

    @Override
    public void onPause() {
        super.onPause();
        DebugLog.logTrace("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        DebugLog.logTrace("onStop");
        UPrinter.disconnectSerialPortService();
    }

    @Override
    public void onResume() {
        //	mBtp.onActivityResume();
        DebugLog.logTrace("onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.d("NGX","Destroy called");

        UPrinter.onActivityDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (fragMgr.getBackStackEntryCount()) {
                case 0:
                case 1:
                    finish();
                    break;
                case 2:
                    fragMgr.popBackStack();
                    break;
                default:
                    fragMgr.popBackStack();
                    break;
            }
            return true;
        }
        return false;
    }

}
