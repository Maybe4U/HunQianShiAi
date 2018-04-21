package com.zykj.hunqianshiai.home.message.people_nearby;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;

import java.util.ArrayList;
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

        mBmapView = (MapView)findViewById(R.id.bmapView);
        mBaiduMap = mBmapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        initPermission();

    }

    private void initPermission() {
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,1);
        }else {
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
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public void navigateTo(BDLocation bdLocation) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            Log.e("经纬度",bdLocation.getLatitude() +"," + bdLocation.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);

            /*判断baiduMap是已经移动到指定位置*/
            if (mBaiduMap.getLocationData()!=null)
                if (mBaiduMap.getLocationData().latitude==bdLocation.getLatitude()
                        &&mBaiduMap.getLocationData().longitude==bdLocation.getLongitude()){
                    isFirstLocate = false;
                }
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(bdLocation.getLatitude());
        locationBuilder.longitude(bdLocation.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude()));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation
                    || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
               navigateTo(bdLocation);
               TextView tv = (TextView)findViewById(R.id.position);
               tv.setText(bdLocation.getLatitude() + "" + bdLocation.getAddress() );
            }

            navigateTo(bdLocation);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            toastShow("必须同意以上权限才能使用该功能");
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    finish();
                }
                break;
        }
    }

    @Override
    public void success(String bean) {

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



    //
    //    //    private ImageView mIv_head;
    //    //    private TextView mTv_head_username;
    //    //    private ImageView mIv_isvip;
    //    //    private TextView tv_distance;
    //    //    private TextView mTv_userauth;
    //    //    private TextView mTv_areaname;
    //    //    private TextView mTv_yearmoney;
    //    //   private Bundle mBundle;
    //
    //    private MapView mMapView;
    //    private boolean isFirstLocate = true;
    //    private BaiduMap mBaiduMap;
    //    private StringBuilder currentPosition;
    //    private TextView positionTextView;
    //
    //    //    @Override
    //    //    protected void initMapView() {
    //    //        SDKInitializer.initialize(getApplicationContext());
    //    //    }
    //
    //    @Override
    //    protected int getContentViewX() {
    //        return R.layout.activity_nearby_location;
    //    }
    //
    //    @Override
    //    protected void initView() {
    //        appBar("附近人");
    //        //        rightImage.setVisibility(View.VISIBLE);
    //        //        rightImage.setImageResource(R.mipmap.fujin_shaixuan);
    //
    //        //mBundle = new Bundle();
    //        //mHeadView = LayoutInflater.from(this).inflate(R.layout.people_nearby_header, null);
    //        //        mIv_head = mHeadView.findViewById(R.id.iv_head);
    //        //        mTv_head_username = mHeadView.findViewById(R.id.tv_head_username);
    //        //        mIv_isvip = mHeadView.findViewById(R.id.iv_isvip);
    //        //        tv_distance = mHeadView.findViewById(R.id.tv_distance);
    //        //        mTv_userauth = mHeadView.findViewById(R.id.tv_userauth);
    //        //        mTv_areaname = mHeadView.findViewById(R.id.tv_areaname);
    //        //        mTv_yearmoney = mHeadView.findViewById(R.id.tv_yearmoney);
    //
    //        //        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    //        //        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    //        //        mRecyclerView.setLayoutManager(layoutManager);
    //        //
    //        //        showDialog();
    //        //
    //
    //
    //                mLocationClient = new LocationClient(getApplicationContext());
    //        //        //声明LocationClient类
    //                mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
    //                    @Override
    //                    public void onReceiveLocation(BDLocation bdLocation) {
    //                        double latitude = bdLocation.getLatitude();    //获取纬度信息
    //                        double longitude = bdLocation.getLongitude();    //获取经度信息
    //
    ////                        BasePresenterImpl presenter = new BasePresenterImpl(NearbyLocationActivity.this, new BaseModelImpl());
    ////                        mParams.clear();
    ////                        mParams.put("userid", UrlContent.USER_ID);
    ////                        mParams.put("lat", latitude);
    ////                        mParams.put("lng", longitude);
    ////                        mParams.put("page", 1);
    ////                        mParams.put("size", 15);
    ////
    ////                        presenter.getData(UrlContent.PEOPLE_NEARBY_URL, mParams, BaseModel.DEFAULT_TYPE);
    //                        mPosition.setText("经纬度：" + latitude +"/n" + longitude);
    //                    }
    //                });
    //        //
    //        //        LocationClientOption option = new LocationClientOption();
    //        //        //可选，设置定位模式，默认高精度
    //        //        //LocationMode.Hight_Accuracy：高精度；
    //        //        //LocationMode. Battery_Saving：低功耗；
    //        //        //LocationMode. Device_Sensors：仅使用设备；
    //        //        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
    //        //        //可选，设置是否使用gps，默认false
    //        //        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
    //        //        option.setOpenGps(true);
    //        //        mLocationClient.setLocOption(option);
    //        //        mLocationClient.start();
    //
    //        //mBaiduMap = mMapView.getMap();
    //        //mBaiduMap.setMyLocationEnabled(true);
    //        requestLocation();
    //    }
    //
    //    private void requestLocation() {
    //        initLocation();
    //        mLocationClient.start();
    //    }
    //
    //    private void initLocation() {
    //        LocationClientOption option = new LocationClientOption();
    //        //option.setScanSpan(3000);
    //        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
    //        option.setIsNeedAddress(true);
    //        mLocationClient.setLocOption(option);
    //    }
    //
    //    //    public void navigateTo(BDLocation bdLocation) {
    //    //        if (isFirstLocate) {
    //    //            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
    //    //            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
    //    //            mBaiduMap.animateMapStatus(update);
    //    //            update = MapStatusUpdateFactory.zoomTo(16f);
    //    //            mBaiduMap.animateMapStatus(update);
    //    //            isFirstLocate = false;
    //    //        }
    //    //        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
    //    //    locationBuilder.latitude(bdLocation.getLatitude());
    //    //        locationBuilder.longitude(bdLocation.getLongitude());
    //    //        MyLocationData locationData = locationBuilder.build();
    //    //        mBaiduMap.setMyLocationData(locationData);
    //    //    }
    //
    //    //    public class MyLocationListener implements BDLocationListener {
    //    //        @Override
    //    //        public void onReceiveLocation(BDLocation bdLocation) {
    //    //            currentPosition = new StringBuilder();
    //    //            currentPosition.append("纬度：").append(bdLocation.getLatitude()).append("\n");
    //    //            currentPosition.append("经线：").append(bdLocation.getLongitude()).append("\n");
    //    //            currentPosition.append("国家：").append(bdLocation.getCountry()).append("\n");
    //    //            currentPosition.append("省：").append(bdLocation.getProvince()).append("\n");
    //    //            currentPosition.append("市：").append(bdLocation.getCity()).append("\n");
    //    //            currentPosition.append("区：").append(bdLocation.getDistrict()).append("\n");
    //    //            currentPosition.append("街道：").append(bdLocation.getStreet()).append("\n");
    //    //            currentPosition.append("楼号：").append(bdLocation.getBuildingName()).append("\n");
    //    //            currentPosition.append("定位方式：");
    //    //            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
    //    //                currentPosition.append("GPS");
    //    //            } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
    //    //                currentPosition.append("网络");
    //    //            }
    //    //            runOnUiThread(new Runnable() {
    //    //                @Override
    //    //                public void run() {
    //    //                    //positionTextView.setText(currentPosition);
    //    //                }
    //    //            });
    //    //        }
    //    //    }
    //
    //    @Override
    //    public void success(String bean) {
    //        //        hideDialog();
    //        //        mLocationClient.stop();
    //        //        PeopleNearbyBean peopleNearbyBean = JsonUtils.GsonToBean(bean, PeopleNearbyBean.class);
    //        //        if (peopleNearbyBean.code != 200) {
    //        //            return;
    //        //        }
    //        //
    //        //        List<PeopleNearbyBean.Data> data = peopleNearbyBean.data;
    //        //
    //        //        final PeopleNearbyBean.Data info = data.get(0);
    //        //
    //        //        Glide.with(this)
    //        //                .load(info.headpic)
    //        //                .apply(BasesActivity.mCircleRequestOptions)
    //        //                .into(mIv_head);
    //        //        mTv_head_username.setText(info.username + "  " + info.age + "岁");
    //        //        if (info.isvip) {
    //        //            mIv_isvip.setVisibility(View.VISIBLE);
    //        //        } else {
    //        //            mIv_isvip.setVisibility(View.GONE);
    //        //        }
    //        //
    //        //        mTv_userauth.setText(info.userauth);
    //        //        mTv_areaname.setText(info.areaname);
    //        //        mTv_yearmoney.setText(info.yearmoney);
    //        //        if (null!=info.juli&& !TextUtils.isEmpty(info.juli)) {
    //        //            tv_distance.setText(info.juli + "米");
    //        //        }
    //        //
    //        //        data.remove(info);
    //        //
    //        //        PeopleNearbyAdapter peopleNearbyAdapter = new PeopleNearbyAdapter(data);
    //        //        mRecyclerView.setAdapter(peopleNearbyAdapter);
    //        //        peopleNearbyAdapter.addHeaderView(mHeadView);
    //        //
    //        //        mHeadView.setOnClickListener(new View.OnClickListener() {
    //        //            @Override
    //        //            public void onClick(View view) {
    //        //                mBundle.clear();
    //        //                mBundle.putString("userid", info.userid);
    //        //                openActivity(UserPageActivity.class, mBundle);
    //        //            }
    //        //        });
    //        //
    //        //        peopleNearbyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
    //        //            @Override
    //        //            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
    //        //                if (UrlContent.IS_MEMBER_KEY) {
    //        //                    List<PeopleNearbyBean.Data> data1 = adapter.getData();
    //        //                    String userid = data1.get(position).userid;
    //        //                    mBundle.clear();
    //        //                    mBundle.putString("userid", userid);
    //        //                    openActivity(UserPageActivity.class, mBundle);
    //        //                } else {
    //        //                    PopupWindowVip popupWindowVip = new PopupWindowVip(NearbyLocationActivity.this);
    //        //                    popupWindowVip.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
    //        //                }
    //        //
    //        //            }
    //        //        });
    //
    //    }
    //
    //    @Override
    //    public void refresh(String bean) {
    //
    //    }
    //
    //    @Override
    //    public void loadMore(String bean) {
    //
    //    }
    //
    //    @Override
    //    public void failed(String content) {
    //
    //    }
    //
    //
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
            //baiduMap.setMyLocationEnabled(false);
        }

}
