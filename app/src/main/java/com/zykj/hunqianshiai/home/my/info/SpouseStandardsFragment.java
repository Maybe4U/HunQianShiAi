package com.zykj.hunqianshiai.home.my.info;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.select_city.SelectCityActivity;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 择偶标准
 * Created by xu on 2018/1/5.
 */

public class SpouseStandardsFragment extends BaseFragment implements BaseView<String> {

    @Bind(R.id.tv_content)
    TextView content;
    @Bind(R.id.tv_age)
    TextView tv_age;
    @Bind(R.id.tv_height)
    TextView tv_height;
    @Bind(R.id.tv_income)
    TextView tv_income;
    @Bind(R.id.tv_work_city)
    TextView tv_work_city;
    @Bind(R.id.tv_census)
    TextView tv_census;
    @Bind(R.id.tv_marryinfo)
    TextView tv_marryinfo;
    @Bind(R.id.tv_believe)
    TextView tv_believe;
    @Bind(R.id.tv_wine)
    TextView tv_wine;
    @Bind(R.id.tv_smoke)
    TextView tv_smoke;
    @Bind(R.id.tv_ischild)
    TextView tv_ischild;
    @Bind(R.id.tv_weight)
    TextView tv_weight;
    @Bind(R.id.tv_constellation)
    TextView tv_constellation;

    private BasePresenterImpl mPresenter;
    private PopupWindowAddInfo mPopupWindowAddInfo;
    private OptionsPickerView mPickerView;
    private Intent mIntent;
    private List<String> statureList;
    private List<String> ageList;
    private List<String> incomeList;
    private List<String> constellationList;
    private List<String> marriageList;
    private List<String> xin_yangList;
    private List<String> yin_jiuList;
    private List<String> xi_yanList;
    private List<String> zi_nvList;
    private List<String> ti_zhongList;
    private List<List<String>> mOptionsAgeItems;
    private List<List<String>> mOptionsStatureItems;
    public SpouseStandardsFragment() {
    }

    public static SpouseStandardsFragment getInstance() {
        return SpouseStandardsFragment.Instance.mSpouseStandardsFragment;
    }

    private static class Instance {
        static SpouseStandardsFragment mSpouseStandardsFragment = new SpouseStandardsFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_spouse_standards;
    }

    @Override
    public void initView() {

        //初始化身高和年龄数据
        initAgeAndStaInfo();

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.SPOUSE_STANDARDS_URL, mParams, BaseModel.DEFAULT_TYPE);

        String[] incomes = getResources().getStringArray(R.array.incomes);
        incomeList = Arrays.asList(incomes);
        String[] constellations = getResources().getStringArray(R.array.constellations);
        constellationList = Arrays.asList(constellations);
        String[] marriages = getResources().getStringArray(R.array.marriages);
        marriageList = Arrays.asList(marriages);

        String[] xin_yang = getResources().getStringArray(R.array.xin_yang);
        xin_yangList = Arrays.asList(xin_yang);

        String[] yin_jiu = getResources().getStringArray(R.array.yin_jiu);
        yin_jiuList = Arrays.asList(yin_jiu);

        String[] xi_yan = getResources().getStringArray(R.array.xi_yan);
        xi_yanList = Arrays.asList(xi_yan);

        String[] zi_nv = getResources().getStringArray(R.array.zi_nv);
        zi_nvList = Arrays.asList(zi_nv);

        String[] ti_zhong = getResources().getStringArray(R.array.ti_zhong1);
        ti_zhongList = Arrays.asList(ti_zhong);
    }

    private void initAgeAndStaInfo() {
        List<String> list1 = new ArrayList<>();
        list1.clear();
        String[] statures = new String[62];
        statures[0] = "不限";
        for (int i = 1;i<=61;i++){
            statures[i] = 139 + i + "cm";
        }
        statureList = Arrays.asList(statures);

        mOptionsStatureItems = new ArrayList<>();
        for(int j=0;j<statureList.size();j++){
            list1.add(statureList.get(j));
        }
        for(int k=0;k<62;k++){
            mOptionsStatureItems.add(list1);
        }

        String[] ages = new String[84];
        ages[0] = "不限";
        for (int i = 1;i<=83;i++){
            ages[i] = 17 + i + "岁";
        }
        ageList = Arrays.asList(ages);
        mOptionsAgeItems = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list2.clear();
        for(int j=0;j<ageList.size();j++){
            list2.add(ageList.get(j));
        }
        for(int k=0;k<84;k++){
            mOptionsAgeItems.add(list2);
        }
    }

    @OnClick({R.id.tv_content, R.id.ll_age, R.id.ll_income, R.id.ll_height, R.id.ll_work_city, R.id.ll_census, R.id.ll_marryinfo,
            R.id.ll_believe, R.id.ll_wine, R.id.ll_smoke, R.id.ll_ischild, R.id.ll_weight, R.id.ll_constellation})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_content:
                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, content, "");
                mPopupWindowAddInfo.showAtLocation(content, Gravity.CENTER, 0, 0);
                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
                    @Override
                    public void onClickListener(Object object) {
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("info", object.toString());
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                });
                break;
            case R.id.ll_age://年龄
                mPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String str = null;
                        String s1 = ageList.get(options1);
                        String s2 = mOptionsAgeItems.get(options1).get(options2);
                        if(s1.equals("不限") || s2.equals("不限")){
                            str = "不限";
                            tv_age.setText(str);
                        }else if(s1.equals(s2)){
                            tv_age.setText(ageList.get(options1));
                        }else{
                            int i1 = Integer.parseInt(s1.replace("岁", ""));
                            int i2 = Integer.parseInt(s2.replace("岁", ""));

                            if(i1>i2){
                                tv_age.setText(s2 + "-" + s1);
                                str = s2 + "-" + s1;
                            }else {
                                tv_age.setText(s1 + "-" + s2);
                                str = s1 + "-" + s2;
                            }
                        }
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("age", str);
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(ageList,mOptionsAgeItems);
                mPickerView.show();
                break;
            case R.id.ll_height://身高
                mPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String str = null;
                        String s1 = statureList.get(options1);
                        String s2 = mOptionsStatureItems.get(options1).get(options2);

                        if(s1.equals("不限") || s2.equals("不限")){
                            str = "不限";
                            tv_height.setText(str);
                        }else if(s1.equals(s2)){
                            tv_height.setText(statureList.get(options1));
                        }else{
                            int i1 = Integer.parseInt(s1.replace("cm", ""));
                            int i2 = Integer.parseInt(s2.replace("cm", ""));
                            if(i1>i2){
                                tv_height.setText(s2 + "-" + s1);
                                str = s2 + "-" + s1;
                            }else {
                                tv_height.setText(s1 + "-" + s2);
                                str = s1 + "-" + s2;
                            }

                        }
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("height", str);
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(statureList,mOptionsStatureItems);
                mPickerView.show();
                break;
            case R.id.ll_income://年收入
                mPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = incomeList.get(options1);
                        tv_income.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("yearmoney", text);
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(incomeList);
                mPickerView.show();
                break;
            case R.id.ll_work_city://工作生活地
                mIntent = new Intent(mContext, SelectCityActivity.class);
                startActivityForResult(mIntent, 107);
                break;
            case R.id.ll_census://户籍
                mIntent = new Intent(mContext, SelectCityActivity.class);
                startActivityForResult(mIntent, 108);
                break;
            case R.id.ll_marryinfo://婚姻状况
                mPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = marriageList.get(options1);
                        tv_marryinfo.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("marryinfo", text);
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(marriageList);
                mPickerView.show();
                break;
            case R.id.ll_believe://信仰
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_believe, "");
//                mPopupWindowAddInfo.showAtLocation(content, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("believe", object.toString());
//                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });

                mPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = xin_yangList.get(options1);
                        tv_believe.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("believe", text);
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(xin_yangList);
                mPickerView.show();
                break;
            case R.id.ll_wine://饮酒
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_wine, "");
//                mPopupWindowAddInfo.showAtLocation(content, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("wine", object.toString());
//                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });
                mPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = yin_jiuList.get(options1);
                        tv_wine.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("wine", text);
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(yin_jiuList);
                mPickerView.show();
                break;
            case R.id.ll_smoke://吸烟
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_smoke, "");
//                mPopupWindowAddInfo.showAtLocation(content, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("smoke", object.toString());
//                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });

                mPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = xi_yanList.get(options1);
                        tv_smoke.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("smoke", text);
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(xi_yanList);
                mPickerView.show();
                break;
            case R.id.ll_ischild://有无子女
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_ischild, "");
//                mPopupWindowAddInfo.showAtLocation(content, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("ischild", object.toString());
//                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });

                mPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = zi_nvList.get(options1);
                        tv_ischild.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("ischild", text);
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(zi_nvList);
                mPickerView.show();
                break;
            case R.id.ll_weight://体重
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_weight, "kg");
//                mPopupWindowAddInfo.showAtLocation(content, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("weight", object.toString());
//                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });

                mPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = ti_zhongList.get(options1);
                        tv_weight.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("weight", text);
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(ti_zhongList);
                mPickerView.show();
                break;
            case R.id.ll_constellation://星座
                mPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = constellationList.get(options1);
                        tv_constellation.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("constellation", text);
                        mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(constellationList);
                mPickerView.show();
                break;
            default:
                break;
        }
    }

    @Override
    public void success(String bean) {
        SpouseStandardsBean spouseStandardsBean = JsonUtils.GsonToBean(bean, SpouseStandardsBean.class);
        SpouseStandardsBean.SpouseStandardsData data = spouseStandardsBean.data;
        content.setText(data.info);
        tv_age.setText(data.age);
        tv_height.setText(data.height);
        tv_income.setText(data.yearmoney);
        tv_work_city.setText(data.areaname);
        tv_census.setText(data.household);
        tv_marryinfo.setText(data.marryinfo);
        tv_believe.setText(data.believe);
        tv_wine.setText(data.wine);
        tv_smoke.setText(data.smoke);
        tv_ischild.setText(data.ischild);
        if (null != data.weight && !TextUtils.isEmpty(data.weight)) {
            tv_weight.setText(data.weight + "kg");
        }

        tv_constellation.setText(data.constellation);
    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        toastShow(baseBean.message);
    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 102) {
            switch (requestCode) {
                case 107:
                    String mCity_id = data.getStringExtra("city_id");
                    String city = data.getStringExtra("city");
                    tv_work_city.setText(city);
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("areaid", mCity_id);
                    mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    break;
                case 108:
                    String mCity_id1 = data.getStringExtra("city_id");
                    String city1 = data.getStringExtra("city");
                    tv_census.setText(city1);
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("household", mCity_id1);
                    mPresenter.getData(UrlContent.SET_SPOUSE_URL, mParams, BaseModel.REFRESH_TYPE);
                    break;
            }
        }
    }
}
