package com.dftc.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private EditText mEditText;
    private myThread mThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mOnvifVideoUtil = OnvifVideoUtil.getInstance();
//        mkdir();
//        long l=mOnvifVideoUtil.initVideo(getSDPath() + "/" + "testSD/");
//        Log.i(TAG,"initVideo:"+l);
//        int i = mOnvifVideoUtil.addIPC(100,"e4:7d:5a:1c:22:4d","rtsp://192.168.2.78:8554/MainStream");
//        Log.i(TAG,"addIPC:"+i);
        mEditText = findViewById(R.id.edit);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mThread != null  ){
                    if (mThread.isAlive()){
                        mThread.interrupt();
                    }
                    mThread = new myThread();
                }else {
                    mThread = new myThread();
                }
                mThread.start();
            }
        });
        IntentFilter internetFilter = new IntentFilter();
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetChangeReceiver, internetFilter);// 网络变化监听
    }
    private BroadcastReceiver mNetChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            if (FileUtils.isNetworkAvailable(HotelActivity.this )){
//                if (currentFragment != FragmentType.quantity) {
//                    changeTitel(FragmentType.quantity);
//                    mTitleContainer.setVisibility(View.GONE);
//                    changeFragment(new QuantityFragment(), null);
//                }
//            }
            Log.i(TAG ,"mNetChangeReceiver");
        }
    };
    class myThread extends Thread{
        @Override
        public void run() {
            super.run();
            CommandExecution.CommandResult commandResult = CommandExecution.execCommand(mEditText.getText().toString(),false);
            Log.i("yub","successMsg:"+commandResult.successMsg +" errorMsg:"+commandResult.errorMsg +"  errorMsg:"+commandResult.errorMsg);
        }
    }
    private void mkdir(){
        String path  = getSDPath() + "/" + "testSD/";
        Log.d(TAG,"path:"+path);
        File file = new File(path);
        if (!file.exists()){
            file.mkdirs();
        }
    }

    public String getSDPath() {
        File sdDir = null;
        //判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取根目录
            Log.e("qq", "外部存储可用..." + sdDir.toString());
        }
        return sdDir.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
