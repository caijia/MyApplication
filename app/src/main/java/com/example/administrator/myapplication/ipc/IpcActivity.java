package com.example.administrator.myapplication.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.administrator.myapplication.IMyAidlInterface;
import com.example.administrator.myapplication.R;

/**
 * Created by cai.jia on 2017/7/27 0027
 */

public class IpcActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bindBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipc_service_activity);
        bindBtn = (Button) findViewById(R.id.bind_btn);
        bindBtn.setOnClickListener(this);
    }

    private IMyAidlInterface aidlInterface;

    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            aidlInterface = IMyAidlInterface.Stub.asInterface(service);
            try {
                aidlInterface.basicTypes(1, 11, false, 1.2f, 1.22, "hah");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void onClick(View v) {
        Intent i = new Intent("com.example.administrator.myapplication.IMyAidlInterface");
        i.setPackage("com.example.administrator.myapplication");
        bindService(i, connection, Context.BIND_AUTO_CREATE);
    }
}
