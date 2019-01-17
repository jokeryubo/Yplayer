package com.yub.yplayer.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yub.yplayer.R;
import com.yub.yplayer.fragment.VideoFragment;
import com.yub.yplayer.utils.LogUtils;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "MainActivity--";
    FrameLayout mFrameLayout;
    VideoFragment videoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFrameLayout  = findViewById(R.id.frame);
        LogUtils.dLog(TAG,"onCreateonCreate ");
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
                .subscribe(permission -> {
                    if (permission.granted) {
                        // 用户已经同意该权限
                        if (permission.name .equals(Manifest.permission.READ_EXTERNAL_STORAGE) ){
                            videoFragment = new VideoFragment();
                            getSupportFragmentManager().beginTransaction().add(R.id.frame , videoFragment).commit();
                        }
                        Log.d(TAG, permission.name + " is granted.");

                    } else if (permission.shouldShowRequestPermissionRationale) {
                        // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                        Log.d(TAG, permission.name + " is denied. More info should be provided.");
                    } else {
                        // 用户拒绝了该权限，并且选中『不再询问』
                        Log.d(TAG, permission.name + " is denied.");
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoFragment.stopPlay();
    }
}
