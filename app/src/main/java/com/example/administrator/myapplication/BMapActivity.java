package com.example.administrator.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.baidu.location.BDLocation;

/**
 * Created by cai.jia on 2017/7/11 0011
 */

public class BMapActivity extends AppCompatActivity {

    private LocationService locationService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);
        locationService = new LocationService();
    }

    public void goMap(View view) {
        locationService.getLocation(this.getApplicationContext(),LocationService.BD09LL,new LocationService.OnLocationListener() {
            @Override
            public void onLocationSuccess(double latitude, double longitude, String address, BDLocation location) {
                System.out.println("latitude:"+latitude);
                System.out.println("longitude:"+longitude);
                System.out.println("addrStr:"+address);
                locationService.geoCoderMap(BMapActivity.this, LocationService.BD09LL,latitude,longitude);
            }

            @Override
            public void onLocationFailure() {
                System.out.println("onLocationFailure");
            }
        });
    }
}
