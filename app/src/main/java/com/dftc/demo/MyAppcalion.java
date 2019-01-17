package com.dftc.demo;

import android.app.Application;

import ren.yale.android.cachewebviewlib.WebViewCacheInterceptor;
import ren.yale.android.cachewebviewlib.WebViewCacheInterceptorInst;

/*
 * create by yubo on 2018/08/06
 */
public class MyAppcalion extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        WebViewCacheInterceptorInst.getInstance().init(new WebViewCacheInterceptor.Builder(this));
    }
}
