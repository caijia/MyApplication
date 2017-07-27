package com.example.administrator.myapplication.ipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.myapplication.IMyAidlInterface;

/**
 * Created by cai.jia on 2017/7/27 0027
 */

public class MyService extends Service {

    IMyAidlInterface.Stub binder = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                               double aDouble, String aString) throws RemoteException {
            Log.d("server_client", "basicTypes");
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("server_client", "onBind");
        return binder;
    }

    @Override
    public void onCreate() {
        Log.d("server_client", "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("server_client", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("server_client", "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("server_client", "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        Log.d("server_client", "onRebind");
        super.onRebind(intent);
    }
}
