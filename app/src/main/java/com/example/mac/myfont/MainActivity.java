package com.example.mac.myfont;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.util.Log;




public class MainActivity extends AppCompatActivity {
    private TelephonyManager temgr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.READ_PHONE_NUMBERS,
                            Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS},1);
        }else{
            init();
        }

    }
    private void init(){
        temgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        String deviceid = temgr.getDeviceId();
        Log.i("test", deviceid);
        String n1 = temgr.getLine1Number();
        String n2 = temgr.getSimSerialNumber();
        String n3 = temgr.getSubscriberId();
        Log.i("test", "n1 = " + n1);
        Log.i("test", "n2 = " + n2);
        Log.i("test", "n3 = " + n3);
        temgr.listen(new MyCalllister() , PhoneStateListener.LISTEN_CALL_STATE);

        ContentResolver cor =  getContentResolver();
        Cursor c = cor.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER},null,null,null);
        while(c.moveToNext()){
            String name =c.getString(0);  //c.getString(c.getColumnIndex(""));
            String tel =c.getString(1);  //c.getString(c.getColumnIndex(""));
            Log.i("test","name:"+name);
            Log.i("test","tel:"+tel);

        }
    }

    private class MyCalllister extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i("test","IDLE" + incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i("test","RINGING"+ incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i("test","OFFHOOK"+ incomingNumber);
                    break;

            }
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            super.onServiceStateChanged(serviceState);
            Log.i("test","ServiceState"+ serviceState);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }
}
