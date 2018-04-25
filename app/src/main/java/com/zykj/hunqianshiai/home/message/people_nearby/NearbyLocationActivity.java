package com.zykj.hunqianshiai.home.message.people_nearby;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.ImageUtils;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.L;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 新增附近人地图
 *
 * @author maybe
 * @time 2018-04-20
 */
public class NearbyLocationActivity extends BasesActivity implements BaseView<String> {
    private MapView mBmapView;
    private BaiduMap mBaiduMap;
    private boolean isFirstLocate = true;

    public LocationClient mLocationClient;
    private ImageView mIv_headPic;
    private Bundle mBundle;

    @Override
    protected void initMapView() {
        super.initMapView();
        mLocationClient = new LocationClient(NearbyLocationActivity.this);
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
    }

    @Override
    protected int getContentViewX() {
        return R.layout.activity_nearby_location;
    }

    @Override
    protected void initView() {
        appBar("附近人");

        mBmapView = (MapView) findViewById(R.id.bmapView);

        mIv_headPic = (ImageView)findViewById(R.id.iv_headPic);
        mBaiduMap = mBmapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        showDialog();
        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                double latitude = bdLocation.getLatitude();    //获取纬度信息
                double longitude = bdLocation.getLongitude();    //获取经度信息

                BasePresenterImpl presenter = new BasePresenterImpl(NearbyLocationActivity.this, new BaseModelImpl());
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("lat", latitude);
                mParams.put("lng", longitude);
                mParams.put("page", 1);
                mParams.put("size", 15);

                presenter.getData(UrlContent.PEOPLE_NEARBY_URL, mParams, BaseModel.DEFAULT_TYPE);
            }
        });

        initPermission();

    }

    private void initPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(3000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // 可选，默认gcj02，设置返回的定位结果坐标系
        //是不是设置的  bd09ll （百度的） 如果不写这个那么默认是  gcj02，那么就会有偏移
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public void navigateTo(BDLocation bdLocation) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);

            /*判断baiduMap是已经移动到指定位置*/
            if (mBaiduMap.getLocationData() != null)
                if (mBaiduMap.getLocationData().latitude == bdLocation.getLatitude()
                        && mBaiduMap.getLocationData().longitude == bdLocation.getLongitude()) {
                    isFirstLocate = false;
                }
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(bdLocation.getLatitude());
        locationBuilder.longitude(bdLocation.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(bdLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            toastShow("必须同意以上权限才能使用该功能");
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void success(String bean) {
        hideDialog();
        mLocationClient.stop();
        PeopleNearbyBean peopleNearbyBean = JsonUtils.GsonToBean(bean, PeopleNearbyBean.class);
        if (peopleNearbyBean.code != 200) {
            return;
        }

        List<PeopleNearbyBean.Data> data = peopleNearbyBean.data;

        for (PeopleNearbyBean.Data aData : data) {

            String headpic = aData.headpic;

            double longitude = Double.parseDouble(aData.lng);

            double latitude = Double.parseDouble(aData.lat);

            LatLng latLng = new LatLng(latitude, longitude);

            mBundle = new Bundle();
            mBundle.putString("headPic", headpic);
            mBundle.putString("userid",aData.userid);


            //在地图上显示附近人的所有点
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.other_loc);

            MarkerOptions options = new MarkerOptions().position(latLng).icon(bitmap).draggable(true);

            //MarkerOptions options = new MarkerOptions().position(latLng).icon().draggable(true);

            //在地图上添加Marker，并显示
            Marker marker = (Marker) mBaiduMap.addOverlay(options);
            marker.setToTop();
            marker.setExtraInfo(mBundle);


        }

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            private BitmapDescriptor mDescriptor;
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();

                //获取头像url
                String headPic = bundle.getString("headPic");
                //构建LatLng对象
                LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                //通过Glide获取头像图片对象
                Glide.with(NearbyLocationActivity.this)
                        .load(headPic)
                        .into(new SimpleTarget<Drawable>() {
                            @Override
                            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                                Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                                //将头像处理为统一尺寸
                                Bitmap bm = ImageUtils.setImgSize(bitmap, 100, 100);
                                //将头像设置为圆形
                                Bitmap roundBitmap = ImageUtils.toRoundBitmap(bm);
                                mDescriptor = BitmapDescriptorFactory.fromBitmap(roundBitmap);
                                //显示infoWindow
                                InfoWindow infoWindow = new InfoWindow(mDescriptor, latLng, -60, new InfoWindow.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick() {
                                        openActivity(UserPageActivity.class,bundle);
                                    }
                                });
                                mBaiduMap.showInfoWindow(infoWindow);
                            }
                        });
                return false;
            }
        });
    }

    @Override
    public void refresh(String bean) {

    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }

    protected void onPause() {
        super.onPause();
        mBmapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBmapView.onResume();
    }

    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mBmapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

}
