package com.example.administrator.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by cai.jia on 2017/7/11 0011
 */

public class LocationService {

    public static final String WGS84 = "wgs84";
    public static final String GCJ02 = "gcj02";
    public static final String BD09LL = "bd09ll";

    public void getLocation(Context context,String coorType,final OnLocationListener callback) {
        final LocationClient client = new LocationClient(context);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(TextUtils.isEmpty(coorType)?GCJ02:coorType);  //bd09ll百度坐标系  //gcj02这个高德,阿里用的 ,wgs84//gps
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span=0;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(false);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(true);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        client.setLocOption(option);
        client.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation != null && bdLocation.getLocType() != BDLocation.TypeServerError
                        && bdLocation.getLocType() != BDLocation.TypeNetWorkException
                        && bdLocation.getLocType() != BDLocation.TypeCriteriaException) {
                    double latitude = bdLocation.getLatitude();
                    double longitude = bdLocation.getLongitude();
                    String addrStr = bdLocation.getAddrStr();
                    if (callback != null) {
                        callback.onLocationSuccess(latitude, longitude, addrStr, bdLocation);
                    }

                }else{
                    if (callback != null) {
                        callback.onLocationFailure();
                    }
                }
                client.stop();
                client.unRegisterLocationListener(this);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        client.start();
    }

    public void geoCoderMap(Context context,String coordType,double latitude, double longitude) {
        String webFormat = "http://api.map.baidu.com/geocoder?location=%s,%s&src=jetsun|jet&output=html&coord_type=%s";
        String nativeFormat = "baidumap://map/geocoder?src=jetsun|jet&location=%s,%s&coord_type=%s";
        String gdNativeFormat = "URL://uri.amap.com/marker?name=park&position=%s,%s&src=jetsun|jet&coordinate=%s&callnative=0";
        String nativeUri = String.format(nativeFormat, String.valueOf(latitude), String.valueOf(longitude),coordType);
        String webUrl = String.format(webFormat, String.valueOf(latitude), String.valueOf(longitude),coordType);
        openMap(context,webUrl,nativeUri);
    }

    public void geoCoderMap(Context context,String coordType, String address) {
        String webFormat = "http://api.map.baidu.com/geocoder?address=%s&src=jetsun|jet&output=html&coord_type=%s";
        String nativeFormat = "baidumap://map/geocoder?src=jetsun|jet&address=%s&coord_type=%s";
        String nativeUri = String.format(nativeFormat, address,coordType);
        String webUrl = String.format(webFormat, address,coordType);
        openMap(context,webUrl,nativeUri);
    }

    public void markerMap(Context context, double latitude, double longitude,String coordType,
                          String title,String content) {
        String webFormat = "http://api.map.baidu.com/geocoder?" +
                "location=%s,%s&src=jetsun|jet&output=html&title=%s&content=%s&coord_type=%s";
        String nativeFormat = "baidumap://map/show?center=%s,%s&coord_type=%s";
        String nativeUri = String.format(nativeFormat, String.valueOf(latitude),
                String.valueOf(longitude),coordType);
        String webUrl = String.format(webFormat, String.valueOf(latitude),
                String.valueOf(longitude), title, content,coordType);
        openMap(context,webUrl,nativeUri);
    }

    private void openMap(Context context,String webUrl, String nativeUri) {
        Intent intent = new Intent();
        intent.setData(Uri.parse(nativeUri == null ? "" : nativeUri));
        boolean isBdExists = intent.resolveActivity(context.getPackageManager()) != null;

       if(isBdExists){
            context.startActivity(intent);

        }else{
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(context, Uri.parse(webUrl));
        }
    }

    public interface OnLocationListener{

        void onLocationSuccess(double latitude, double longitude, String address, BDLocation location);

        void onLocationFailure();
    }
}
