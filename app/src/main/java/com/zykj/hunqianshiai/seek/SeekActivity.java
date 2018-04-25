package com.zykj.hunqianshiai.seek;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.chat.PopupWindowVip;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.select_city.SelectCityActivity;
import com.zykj.hunqianshiai.tools.ButtonUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 筛选
 */
public class SeekActivity extends BasesActivity implements TextView.OnEditorActionListener {

    @Bind(R.id.tv_stature)
    TextView tvStature;
    @Bind(R.id.tv_age)
    TextView tvAge;
    @Bind(R.id.tv_income)
    TextView tvIncome;
    @Bind(R.id.tv_work_city)
    TextView tvWorkCity;
    @Bind(R.id.tv_home_city)
    TextView tvHomeCity;
    @Bind(R.id.tv_education)
    TextView tvEducation;
    @Bind(R.id.tv_constellation)
    TextView tvConstellation;
    @Bind(R.id.tv_zodiac)
    TextView tvZodiac;
    @Bind(R.id.tv_marriage)
    TextView tvMarriage;
    @Bind(R.id.et_id)
    EditText et_id;
    @Bind(R.id.tv_hasChild)
    TextView mTvHasChild;
    @Bind(R.id.tv_belief)
    TextView mTvBelief;
    @Bind(R.id.tv_house)
    TextView mTvHouse;
    @Bind(R.id.tv_car)
    TextView mTvCar;
    @Bind(R.id.tv_occupation)
    TextView mTvOccupation;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private OptionsPickerView mPickerView;
    private List<String> statureList;
    private List<String> ageList;
    private List<String> incomeList;
    private List<String> constellationList;
    private List<String> marriageList;
    private List<String> educationList;
    private List<String> zodiacList;
    //有无子女列表
    private List<String> childrenList;
    //有无信仰列表
    private List<String> beliefsList;
    //有无房产列表
    private List<String> housesList;
    //有无车辆列表
    private List<String> carsList;


    private Bundle mBundle;
    private Intent mIntent;
    private String mCity_id;
    private String mCity_id1;

    private ArrayList<HeightBean> mHeightBeans = new ArrayList<>();
    private ArrayList<ArrayList<String>> height = new ArrayList<>();

    @Override
    protected int getContentViewX() {
        return R.layout.activity_seek;
    }

    @Override
    protected void initView() {
        appBar("筛选");

        mBundle = new Bundle();

        et_id.setOnEditorActionListener(this);


        String[] statures = getResources().getStringArray(R.array.statures);
        statureList = Arrays.asList(statures);
        String[] ages = getResources().getStringArray(R.array.ages);
        ageList = Arrays.asList(ages);
        String[] incomes = getResources().getStringArray(R.array.incomes);
        incomeList = Arrays.asList(incomes);
        String[] constellations = getResources().getStringArray(R.array.constellations);
        constellationList = Arrays.asList(constellations);
        String[] marriages = getResources().getStringArray(R.array.marriages);
        marriageList = Arrays.asList(marriages);
        String[] educations = getResources().getStringArray(R.array.educations);
        educationList = Arrays.asList(educations);
        String[] zodiacs = getResources().getStringArray(R.array.zodiacs);
        zodiacList = Arrays.asList(zodiacs);

        /*==================添加=====================*/
        //子女
        String[] children = getResources().getStringArray(R.array.zi_nv);
        childrenList = Arrays.asList(children);
        //信仰
        String[] beliefs = getResources().getStringArray(R.array.xin_yang);
        beliefsList = Arrays.asList(beliefs);
        //房产
        String[] houses = getResources().getStringArray(R.array.buy_house);
        housesList = Arrays.asList(houses);
        //购车
        String[] cars = getResources().getStringArray(R.array.buy_car);
        carsList = Arrays.asList(cars);

        //解析城市文件
        //        new Thread(new Runnable() {
        //            @Override
        //            public void run() {
        //                // 写子线程中的操作,解析省市区数据
        ////                initJsonData();
        //
        //                for (int i = 140; i < 201; i++) {
        //                    mHeightBeans.add(new HeightBean((i - 140) + "", i + ""));
        //                    ArrayList<String> strings = new ArrayList<>();
        //                    for (int j = i; j < 201; j++) {
        //                        strings.add(j + "");
        //                    }
        //                    height.add(strings);
        //                }
        //            }
        //        }).start();
    }

    @OnClick({R.id.et_id, R.id.rl_stature, R.id.rl_age, R.id.rl_income, R.id.rl_work_city, R.id.rl_home_city,
            R.id.rl_education, R.id.rl_constellation, R.id.rl_zodiac, R.id.rl_marriage, R.id.tv_reset,
            R.id.tv_affirm, R.id.rl_hasChild, R.id.rl_belief, R.id.rl_car, R.id.rl_house,R.id.rl_occupation})
    @Override
    public void onClick(View view) {
        super.onClick(view);


        switch (view.getId()) {
            case R.id.et_id:

                break;
            case R.id.rl_stature://身高
                showOption(tvStature,statureList);
                break;
            case R.id.rl_age://年龄
                showOption(tvAge,ageList);
                break;
            case R.id.rl_income://年收入
                showOption(tvIncome,incomeList);
                break;
            case R.id.rl_work_city://工作生活在
                if (UrlContent.IS_MEMBER_KEY) {
                    mIntent = new Intent(this, SelectCityActivity.class);
                    startActivityForResult(mIntent, 111);
                } else {
                    PopupWindowVip popupWindowVip = new PopupWindowVip(this);
                    popupWindowVip.showAtLocation(et_id, Gravity.CENTER, 0, 0);
                }

                //                mPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                //                    @SuppressLint("SetTextI18n")
                //                    @Override
                //                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //                        //返回的分别是三个级别的选中位置
                //                        tvWorkCity.setText(options1Items.get(options1).getPickerViewText()
                //                                + options2Items.get(options1).get(options2)
                //                                + options3Items.get(options1).get(options2).get(options3));
                //                    }
                //                })
                //
                //                        .setTitleText("城市选择")
                //                        .setDividerColor(Color.BLACK)
                //                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                //                        .setContentTextSize(20)
                //                        .build();
                //
                //        /*pvOptions.setPicker(options1Items);//一级选择器
                //        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
                //                mPickerView.setPicker(options1Items, options2Items, options3Items);//三级选择器
                //                mPickerView.show();
                break;
            case R.id.rl_home_city://户籍
                if (UrlContent.IS_MEMBER_KEY) {
                    mIntent = new Intent(this, SelectCityActivity.class);
                    startActivityForResult(mIntent, 112);
                } else {
                    PopupWindowVip popupWindowVip = new PopupWindowVip(this);
                    popupWindowVip.showAtLocation(et_id, Gravity.CENTER, 0, 0);
                }

                //                mPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                //                    @SuppressLint("SetTextI18n")
                //                    @Override
                //                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //                        //返回的分别是三个级别的选中位置
                //                        tvHomeCity.setText(options1Items.get(options1).getPickerViewText()
                //                                + options2Items.get(options1).get(options2)
                //                                + options3Items.get(options1).get(options2).get(options3));
                //                    }
                //                })
                //
                //                        .setTitleText("城市选择")
                //                        .setDividerColor(Color.BLACK)
                //                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                //                        .setContentTextSize(20)
                //                        .build();
                //
                //        /*pvOptions.setPicker(options1Items);//一级选择器
                //        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
                //                mPickerView.setPicker(options1Items, options2Items, options3Items);//三级选择器
                //                mPickerView.show();
                break;
            case R.id.rl_education://学历
                showOption(tvEducation,educationList);
                break;
            case R.id.rl_constellation://星座
                showOption(tvConstellation,constellationList);
                break;
            case R.id.rl_zodiac://属相
                showOption(tvZodiac,zodiacList);
                break;
            case R.id.rl_marriage://婚史
                showOption(tvMarriage,marriageList);
                break;
            case R.id.rl_hasChild://有无子女
                showOption(mTvHasChild,childrenList);
                break;
            case R.id.rl_belief://有无信仰
                showOption(mTvBelief,beliefsList);
                break;
            case R.id.rl_house://有无房产
                showOption(mTvHouse,housesList);
                break;
            case R.id.rl_car://有无购车
                showOption(mTvCar,carsList);
                break;
            case R.id.rl_occupation://有无职业

                String occupation = mTvOccupation.getText().toString().trim();
                break;

            case R.id.tv_reset://重置
                tvStature.setText("不限");
                tvAge.setText("不限");
                tvIncome.setText("不限");
                tvWorkCity.setText("不限");
                tvHomeCity.setText("不限");
                tvEducation.setText("不限");
                tvConstellation.setText("不限");
                tvZodiac.setText("不限");
                tvMarriage.setText("不限");
                break;
            case R.id.tv_affirm://确认

                mBundle.clear();
                String nicknameOrId = et_id.getText().toString().trim();
                /*==================优化=====================*/
                //点击确认执行两种情况
                //1.如果当前搜索框为空，则按照筛选条件搜索
                //2.如果当前搜索框不为空，则优先查找搜索框内容
                if (nicknameOrId.isEmpty()) {
                    specialSeek();
                    openActivity(VipSearchActivity.class, mBundle);
                } else {
                    mBundle.putString("searchid", nicknameOrId);
                    openActivity(SearchResultActivity.class, mBundle);
                }

                break;
            default:
                break;
        }
    }

    private void hideSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * method desc.  :显示选择框
     * params        :
     * return        :
     */
    private void showOption(TextView tv,List<String> list) {
        //隐藏软键盘
        hideSoftInput();

        if(tv.equals(tvStature) || tv.equals(tvAge) || tv.equals(tvIncome)){
            showPickerView(tv, list);
        }else if (UrlContent.IS_MEMBER_KEY) {
            showPickerView(tv, list);
        } else {
            PopupWindowVip popupWindowVip = new PopupWindowVip(this);
            popupWindowVip.showAtLocation(et_id, Gravity.CENTER, 0, 0);
        }
    }

    private void showPickerView(TextView tv, List<String> list) {
        mPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tv.setText(list.get(options1));

            }
        }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                .setSubmitColor(R.color.black)
                .setCancelColor(R.color.black)//取消按钮文字颜色
                .build();

        mPickerView.setPicker(list);
        mPickerView.show();
    }

    /*==================优化=====================*/
    //搜索条件 包括基本筛选和高级筛选条件
    private void specialSeek() {
        String stature = tvStature.getText().toString();
        //身高
        if (!stature.equals("不限")) {
            String cm = stature.replace("cm", "");
            String replace = cm.replace("以下", "");
            String replace1 = replace.replace("以上", "");
            String[] split = replace1.split("-");
            if (split.length > 1) {
                mBundle.putString("p1", split[0]);
                mBundle.putString("p2", split[1]);

            } else {
                if (split[0].equals("150")) {
                    mBundle.putString("p2", split[0]);
                } else {
                    mBundle.putString("p1", split[0]);
                }
            }
        }
        //年龄
        String age = tvAge.getText().toString();
        if (!age.equals("不限")) {
            //                    String cm = age.replace("cm", "");
            String replace = age.replace("以下", "");
            String replace1 = replace.replace("以上", "");
            String[] split = replace1.split("-");
            if (split.length > 1) {
                mBundle.putString("p3", split[0]);
                mBundle.putString("p4", split[1]);

            } else {
                if (split[0].equals("20")) {
                    mBundle.putString("p4", split[0]);
                } else {
                    mBundle.putString("p3", split[0]);
                }

            }
        }
        //收入
        String income = tvIncome.getText().toString();
        if (!income.equals("不限")) {
            mBundle.putString("p5", income);
        }

        //工作城市
        String workCity = tvWorkCity.getText().toString();
        if (!workCity.equals("不限")) {
            mBundle.putString("p10", mCity_id);
        }

        //户籍
        String homeCity = tvHomeCity.getText().toString();
        if (!homeCity.equals("不限")) {
            mBundle.putString("p11", mCity_id1);
        }

        //学历
        String education = tvEducation.getText().toString();
        if (!education.equals("不限")) {
            mBundle.putString("p12", education);
        }

        //星座
        String constellation = tvConstellation.getText().toString();
        if (!education.equals("不限")) {
            mBundle.putString("p13", constellation);
        }

        //
        String zodiac = tvZodiac.getText().toString();
        if (!education.equals("不限")) {
            mBundle.putString("p14", zodiac);
        }
        //婚史
        String marriage = tvMarriage.getText().toString();
        if (!marriage.equals("不限")) {
            mBundle.putString("p14", marriage);
        }
        //有无子女
        String hasChild = mTvHasChild.getText().toString();
        if (!hasChild.equals("不限")) {
            mBundle.putString("P15", hasChild);
        }
        //信仰
        String belief = mTvBelief.getText().toString();
        if (!belief.equals("不限")) {
            mBundle.putString("p16", belief);
        }
        //房产情况
        String house = mTvHouse.getText().toString();
        if (!house.equals("不限")) {
            mBundle.putString("p17", house);
        }
        //购车情况
        String car = mTvCar.getText().toString();
        if (!car.equals("不限")) {
            mBundle.putString("p18", car);
        }
        //职业
        String occupation = mTvOccupation.getText().toString();
        if (!occupation.equals("不限")) {
            mBundle.putString("p19", occupation);
        }
    }

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


    }


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

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

        //防止误触
        if(!ButtonUtils.isFastDoubleClick()){
            if (i == EditorInfo.IME_ACTION_DONE || i == EditorInfo.IME_ACTION_SEND || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                //让mPasswordEdit获取输入焦点
                et_id.requestFocus();
                String id = et_id.getText().toString().trim();

                if (!TextUtils.isEmpty(id)) {
                    mBundle.clear();
                    mBundle.putString("searchid", id);
                    openActivity(SearchResultActivity.class, mBundle);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 102) {
            switch (requestCode) {
                case 111:
                    mCity_id = data.getStringExtra("city_id");
                    String city = data.getStringExtra("city");
                    tvWorkCity.setText(city);
                    break;
                case 112:
                    mCity_id1 = data.getStringExtra("city_id");
                    String city1 = data.getStringExtra("city");
                    tvHomeCity.setText(city1);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
