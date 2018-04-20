package com.zykj.hunqianshiai.activities.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.dynamic.my_dynamic.IssueDynamicActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.seek.GetJsonDataUtil;
import com.zykj.hunqianshiai.seek.JsonBean;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.TextUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 活动报名
 */
public class ActivitiesApplyActivity extends BasesActivity implements BaseView<String> {

    private Bundle mBundle;
    @Bind(R.id.et_name)
    EditText et_name;
    @Bind(R.id.et_phone)
    EditText et_phone;
    @Bind(R.id.et_sit)
    EditText et_sit;
    @Bind(R.id.tv_set_sit)
    TextView setSit;
    @Bind(R.id.iv_select_sit)
    ImageView selectSit;
    /*===================省市区JSON数据解析相关 begin====================*/
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private Thread thread;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    //解析省市区数据标志位  完成为true
    private boolean isLoaded = false;
    /*===================省市区JSON数据解析相关 end====================*/

    private BasePresenterImpl mPresenter;
    private String mActid;
    private String mCost;
    private LocationClient mLocationClient;
    private OptionsPickerView mPickerView;
    private List<Poi> mPoiList;


    @Override
    protected int getContentViewX() {
        return R.layout.activity_activities_apply;
    }

    @Override
    protected void initView() {
        appBar("活动报名");
        mBundle = getIntent().getExtras();
        mActid = mBundle.getString("actid");
        mCost = mBundle.getString("cost");
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());

        //初始化省市区数据
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
    }

    /*===================省市区JSON数据解析====================*/
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(ActivitiesApplyActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @OnClick({R.id.tv_cancel, R.id.tv_affirm, R.id.rl_sit})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.rl_sit:
                //选择省市区地址
                if (isLoaded) {
                    showPickerView();
                }
                break;
            case R.id.tv_affirm:
                String trim = et_name.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    toastShow("姓名不能为空");
                    return;
                }
                String trim1 = et_phone.getText().toString().trim();
                if (!TextUtil.isMobile(trim1)) {
                    toastShow("手机号码不正确");
                    return;
                }
                mBundle.putString("name", trim);
                mBundle.putString("phone", trim1);

                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("p1", mActid);
                mParams.put("phone", trim1);
                mParams.put("name", trim);
                String trim2 = et_sit.getText().toString().trim();
                if (!TextUtils.isEmpty(trim2)) {
                    mParams.put("address", trim2);
                }

                mPresenter.getData(UrlContent.JOIN_ACTIVITY_URL, mParams, BaseModel.DEFAULT_TYPE);

                break;
        }
    }

    @Override
    public void success(String bean) {
        ActivitiesApplyBean activitiesApplyBean = JsonUtils.GsonToBean(bean, ActivitiesApplyBean.class);
        mBundle.putString("id", activitiesApplyBean.data.id);
        Log.e("活动报名",activitiesApplyBean.data.phone);
        if (mCost.equals("0")) {
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("actid", mActid);
            mPresenter.getData(UrlContent.ACTIVITIES_NOPAY_URL, mParams, BaseModel.REFRESH_TYPE);
        } else {
            openActivity(ActivitiesPayActivity.class, mBundle);
        }

    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        toastShow(baseBean.message);
        if (baseBean.code == 200) {
            finish();
        }
    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }


    /*===================省市区选择框====================*/

    private void showPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerView
                .Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                et_sit.setText(tx);
                selectSit.setVisibility(View.GONE);
            }
        }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                .setSubmitColor(R.color.black)
                .setCancelColor(R.color.black)//取消按钮文字颜色
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    /*===================省市区JSON数据解析====================*/
    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    /*===================省市区JSON数据解析====================*/
    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }
}
