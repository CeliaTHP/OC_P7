package com.openclassrooms.oc_p7;

import androidx.multidex.MultiDexApplication;


public class MyApplication extends MultiDexApplication {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initNotificationService();

    }

    public static MyApplication getInstance() {
        return MyApplication.instance;
    }

    private void initNotificationService() {

    }

}
