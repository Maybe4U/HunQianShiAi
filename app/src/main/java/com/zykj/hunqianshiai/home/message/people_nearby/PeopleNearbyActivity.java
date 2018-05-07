package com.zykj.hunqianshiai.home.message.people_nearby;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.chat.PopupWindowVip;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.user_home.UserPageActivity;

import java.util.List;

import butterknife.Bind;

/**
 * 附近人
 */
public class PeopleNearbyActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.recycler_nearby)
    RecyclerView mRecyclerView;
    @Bind(R.id.iv_right_share)
    ImageView rightImage;
    @Bind(R.id.fab_nearby)
    FloatingActionButton fab_nearby;

    private LocationClient mLocationClient;
    private View mHeadView;
    private ImageView mIv_head;
    private TextView mTv_head_username;
    private ImageView mIv_isvip;
    private TextView tv_distance;
    private TextView mTv_userauth;
    private TextView mTv_areaname;
    private TextView mTv_yearmoney;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_people_nearby;
    }

    @Override
    protected void initView() {
        appBar("附近人");
//        rightImage.setVisibility(View.VISIBLE);
//        rightImage.setImageResource(R.mipmap.fujin_shaixuan);

        mBundle = new Bundle();
        mHeadView = LayoutInflater.from(this).inflate(R.layout.people_nearby_header, null);
        mIv_head = mHeadView.findViewById(R.id.iv_head);
        mTv_head_username = mHeadView.findViewById(R.id.tv_head_username);
        mIv_isvip = mHeadView.findViewById(R.id.iv_isvip);
        tv_distance = mHeadView.findViewById(R.id.tv_distance);
        mTv_userauth = mHeadView.findViewById(R.id.tv_userauth);
        mTv_areaname = mHeadView.findViewById(R.id.tv_areaname);
        mTv_yearmoney = mHeadView.findViewById(R.id.tv_yearmoney);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        showDialog();

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                double latitude = bdLocation.getLatitude();    //获取纬度信息
                double longitude = bdLocation.getLongitude();    //获取经度信息

                BasePresenterImpl presenter = new BasePresenterImpl(PeopleNearbyActivity.this, new BaseModelImpl());
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("lat", latitude);
                mParams.put("lng", longitude);
                mParams.put("page", 1);
                mParams.put("size", 15);

                presenter.getData(UrlContent.PEOPLE_NEARBY_URL, mParams, BaseModel.DEFAULT_TYPE);

            }
        });

        LocationClientOption option = new LocationClientOption();
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();



        fab_nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity(NearbyLocationActivity.class);
            }
        });
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

        final PeopleNearbyBean.Data info = data.get(0);

        Glide.with(this)
                .load(info.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into(mIv_head);
        mTv_head_username.setText(info.username + "  " + info.age + "岁");
        if (info.isvip) {
            mIv_isvip.setVisibility(View.VISIBLE);
        } else {
            mIv_isvip.setVisibility(View.GONE);
        }
        if(info.userauth.equals("未认证")){
            mTv_userauth.setTextColor(Color.BLACK);
        }
        mTv_userauth.setText(info.userauth);
        mTv_areaname.setText(info.areaname);
        mTv_yearmoney.setText(info.yearmoney);
        if (null!=info.juli&& !TextUtils.isEmpty(info.juli)) {
            tv_distance.setText(info.juli + "米");
        }

        data.remove(info);

        PeopleNearbyAdapter peopleNearbyAdapter = new PeopleNearbyAdapter(data);
        mRecyclerView.setAdapter(peopleNearbyAdapter);
        peopleNearbyAdapter.addHeaderView(mHeadView);

        mHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBundle.clear();
                mBundle.putString("userid", info.userid);
                openActivity(UserPageActivity.class, mBundle);
            }
        });

        peopleNearbyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                if (UrlContent.IS_MEMBER_KEY) {
//                    List<PeopleNearbyBean.Data> data1 = adapter.getData();
//                    String userid = data1.get(position).userid;
//                    mBundle.clear();
//                    mBundle.putString("userid", userid);
//                    openActivity(UserPageActivity.class, mBundle);
//                } else {
//                    PopupWindowVip popupWindowVip = new PopupWindowVip(PeopleNearbyActivity.this);
//                    popupWindowVip.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
//                }
                /*=====================修改==================*/
                //取消只有会员才能查看
                List<PeopleNearbyBean.Data> data1 = adapter.getData();
                String userid = data1.get(position).userid;
                mBundle.clear();
                mBundle.putString("userid", userid);
                openActivity(UserPageActivity.class, mBundle);
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
}
