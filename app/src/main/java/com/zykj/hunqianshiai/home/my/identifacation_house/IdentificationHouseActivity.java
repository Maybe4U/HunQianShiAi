package com.zykj.hunqianshiai.home.my.identifacation_house;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.home.my.PicsBean;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.seek.GetJsonDataUtil;
import com.zykj.hunqianshiai.seek.JsonBean;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.RxBus;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class IdentificationHouseActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.tv_select_buy)
    TextView selectBuy;
    @Bind(R.id.tv_car_trademark)
    TextView carTrademark;
    @Bind(R.id.tv_right)
    TextView right;
    @Bind(R.id.iv_house)
    ImageView house;
    @Bind(R.id.et_content)
    EditText et_content;

    private List<LocalMedia> selectList;
    private BasePresenterImpl mPresenter;
    private String mData;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private List<String> mStrings;
    private OptionsPickerView mPickerView;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_identification_house;
    }

    @Override
    protected void initView() {
        appBar("房产认证");
        right.setVisibility(View.VISIBLE);
        right.setText("提交");
        right.setOnClickListener(this);
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());

        String[] stringArray = getResources().getStringArray(R.array.buy_house);
        mStrings = Arrays.asList(stringArray);

        //解析城市文件
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 写子线程中的操作,解析省市区数据
                initJsonData();
            }
        }).start();
    }

    @OnClick({R.id.tv_select_buy, R.id.iv_house,R.id.rl_layout})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rl_layout:
                OptionsPickerView  pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        carTrademark.setText(options1Items.get(options1).getPickerViewText()
                                + options2Items.get(options1).get(options2)
                                + options3Items.get(options1).get(options2).get(options3));
                    }
                })

                        .setTitleText("城市选择")
                        .setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .setContentTextSize(20)
                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                        .setDividerColor(Color.BLACK)
                        .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
                pickerView.setPicker(options1Items, options2Items, options3Items);//三级选择器
                pickerView.show();
                break;
            case R.id.tv_select_buy://购买情况
                mPickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = mStrings.get(options1);
                        selectBuy.setText(text);

                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(mStrings);
                mPickerView.show();
//                PopupWindowBuyHouse popupWindowBuyHouse = new PopupWindowBuyHouse(this);
//                popupWindowBuyHouse.showAtLocation(carTrademark, Gravity.BOTTOM, 0, 0);
//                popupWindowBuyHouse.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        selectBuy.setText(object.toString());
//                    }
//                });
                break;
            case R.id.iv_house://证明
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                        .enableCrop(false)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.tv_right://

                String education = selectBuy.getText().toString().trim();
                if (education.equals("请选择")) {
                    toastShow("内容不完整");
                    return;
                }
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    toastShow("内容不完整");
                    return;
                }

                if (null == mData || TextUtils.isEmpty(mData)) {
                    toastShow("请重新选择房产证");
                    return;
                }
                String string = carTrademark.getText().toString();
                if (string.equals("请选择")) {
                    toastShow("请选择房产所在地");
                    return;
                }

                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("cat", 1);
                mParams.put("p1", education);
                mParams.put("p2", string);
                mParams.put("p3", mData);
                mParams.put("p4", content);
                mPresenter.getData(UrlContent.SET_IDENTIFICATION_URL, mParams, BaseModel.REFRESH_TYPE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    glide(selectList.get(0).getPath(), house, mOptions);
                    mParams.clear();

                    mParams.put("pic", new File(selectList.get(0).getCompressPath()));

                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.DEFAULT_TYPE);
                    break;
            }
        }
    }

    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (200 == baseBean.code) {
            UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
            mData = uploadBean.data;
        }
    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        toastShow(baseBean.message);
        if (200 == baseBean.code) {
            RxBus.getInstance().post(new PicsBean());
            finish();
        }
    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

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
}
