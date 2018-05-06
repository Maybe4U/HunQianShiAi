package com.zykj.hunqianshiai.home.my.info;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.select_city.SelectCityActivity;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 个人资料
 * Created by xu on 2018/1/5.
 */

public class PersonageInfoFragment extends BaseFragment implements BaseView<String> {

    @Bind(R.id.iv_head)
    ImageView headView;
    @Bind(R.id.tv_username)
    TextView username;
    @Bind(R.id.tv_id)
    TextView id;
    @Bind(R.id.tv_birthday)
    TextView birthday;
    @Bind(R.id.tv_constellation)
    TextView constellation;
    @Bind(R.id.tv_height)
    TextView height;
    @Bind(R.id.tv_income)
    TextView income;
    @Bind(R.id.tv_work_city)
    TextView workCity;
    @Bind(R.id.tv_marry)
    TextView marry;
    @Bind(R.id.tv_nation)
    TextView nation;
    @Bind(R.id.tv_household)
    TextView household;
    @Bind(R.id.tv_marryinfo)
    TextView marryinfo;
    @Bind(R.id.tv_profession)
    TextView profession;
    @Bind(R.id.tv_believe)
    TextView tv_believe;
    @Bind(R.id.tv_wine)
    TextView tv_wine;
    @Bind(R.id.tv_bk1)
    TextView tv_bk1;
    @Bind(R.id.tv_familyrank)
    TextView tv_familyrank;
    @Bind(R.id.tv_bk2)
    TextView tv_bk2;
    @Bind(R.id.tv_weight)
    TextView tv_weight;
    @Bind(R.id.tv_wx)
    TextView tv_wx;
    @Bind(R.id.tv_generalize)
    TextView tv_generalize;

    private BasePresenterImpl mPresenter;
    private OptionsPickerView mPickerView;
    private PopupWindowAddInfo mPopupWindowAddInfo;
    private Intent mIntent;
    private List<String> incomeList;
    private List<String> constellationList;
    private List<String> marriageList;
    private List<String> mMarry_timeList;
    private List<LocalMedia> selectList;
    private List<String> min_zuList;
    private List<String> xin_yangList;
    private List<String> yin_jiuList;
    private List<String> xi_yanList;
    private List<String> zi_nvList;
    private List<String> shen_gaoList;
    private List<String> ti_zhongList;

    public PersonageInfoFragment() {
    }

    public static PersonageInfoFragment getInstance() {
        return PersonageInfoFragment.Instance.mPersonageInfoFragment;
    }

    private static class Instance {
        static PersonageInfoFragment mPersonageInfoFragment = new PersonageInfoFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_personage_info;
    }

    @Override
    public void initView() {
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.GET_INFO_URL, mParams, BaseModel.DEFAULT_TYPE);

        String[] incomes = getResources().getStringArray(R.array.incomes);
        incomeList = Arrays.asList(incomes);
        String[] constellations = getResources().getStringArray(R.array.constellations);
        constellationList = Arrays.asList(constellations);
        String[] marriages = getResources().getStringArray(R.array.marriages);
        marriageList = Arrays.asList(marriages);

        String[] marry_time = getResources().getStringArray(R.array.marry_time);
        mMarry_timeList = Arrays.asList(marry_time);

        String[] min_zu = getResources().getStringArray(R.array.min_zu);
        min_zuList = Arrays.asList(min_zu);

        String[] xin_yang = getResources().getStringArray(R.array.xin_yang);
        xin_yangList = Arrays.asList(xin_yang);

        String[] yin_jiu = getResources().getStringArray(R.array.yin_jiu);
        yin_jiuList = Arrays.asList(yin_jiu);

        String[] xi_yan = getResources().getStringArray(R.array.xi_yan);
        xi_yanList = Arrays.asList(xi_yan);

        String[] zi_nv = getResources().getStringArray(R.array.zi_nv);
        zi_nvList = Arrays.asList(zi_nv);

        String[] shen_gao = getResources().getStringArray(R.array.shen_gao);
        shen_gaoList = Arrays.asList(shen_gao);

        String[] ti_zhong = getResources().getStringArray(R.array.ti_zhong);
        ti_zhongList = Arrays.asList(ti_zhong);
    }

    @OnClick({R.id.ll_header, R.id.ll_nickname, R.id.ll_birthday, R.id.ll_constellation, R.id.ll_height, R.id.ll_income, R.id.ll_work_city,
            R.id.ll_marry, R.id.ll_nation, R.id.ll_household, R.id.ll_marryinfo, R.id.ll_profession, R.id.ll_believe, R.id.ll_wine, R.id.ll_bk1,
            R.id.ll_familyrank, R.id.ll_weight, R.id.ll_bk2, R.id.ll_wx})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_header://头像
//                Intent intent = new Intent(mContext, CameraActivity.class);
//                startActivityForResult(intent, AppConstant.REQUEST_CODE.CAMERA);
//                CameraUtil.getInstance().camera((InfoActivity)mActivity);
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .maxSelectNum(6)// 最大图片选择数量 int
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
//                        .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.ll_nickname://用户名
                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, username, "");
                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
                    @Override
                    public void onClickListener(Object object) {
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("username", object.toString());
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                });
                break;
            case R.id.ll_birthday://生日
                //时间选择器
                TimePickerView pvTime = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        String format = new SimpleDateFormat("yyyy-MM-dd ").format(date);
                        birthday.setText(format);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("birthdate", format);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                        .setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();
                pvTime.show();
                break;
            case R.id.ll_constellation://星座
                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = constellationList.get(options1);
                        constellation.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("constellation", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(constellationList);
                mPickerView.show();
                break;
            case R.id.ll_height://身高
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, height, "cm");
//                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("height", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });

                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = shen_gaoList.get(options1);
                        height.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("height", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(shen_gaoList);
                mPickerView.show();
                break;
            case R.id.ll_income://年收入
                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = incomeList.get(options1);
                        income.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("yearmoney", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);

                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(incomeList);
                mPickerView.show();
                break;
            case R.id.ll_work_city://工作地
                mIntent = new Intent(mContext, SelectCityActivity.class);
                startActivityForResult(mIntent, 105);
                break;
            case R.id.ll_marry://期望结婚时间

                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = mMarry_timeList.get(options1);
                        marry.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("marrytime", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(mMarry_timeList);
                mPickerView.show();
                break;
            case R.id.ll_nation://民族
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, nation, "");
//                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("nation", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });
                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = min_zuList.get(options1);
                        nation.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("nation", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(min_zuList);
                mPickerView.show();
                break;
            case R.id.ll_household://户籍
                mIntent = new Intent(mContext, SelectCityActivity.class);
                startActivityForResult(mIntent, 106);
                break;
            case R.id.ll_marryinfo://婚姻状况

                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = marriageList.get(options1);
                        marryinfo.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("marryinfo", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(marriageList);
                mPickerView.show();
                break;
            case R.id.ll_profession://职业
                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, profession, "");
                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
                    @Override
                    public void onClickListener(Object object) {
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("zhiye", object.toString());
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                });
                break;
            case R.id.ll_believe://信仰
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_believe, "");
//                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("believe", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });
                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = xin_yangList.get(options1);
                        tv_believe.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("believe", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
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
//                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("wine", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });
                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = yin_jiuList.get(options1);
                        tv_wine.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("wine", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(yin_jiuList);
                mPickerView.show();
                break;
            case R.id.ll_bk1://吸烟
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_bk1, "");
//                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("smoke", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });

                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = xi_yanList.get(options1);
                        tv_bk1.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("smoke", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(xi_yanList);
                mPickerView.show();
                break;
            case R.id.ll_familyrank://家中排行
                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_familyrank, "");
                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
                    @Override
                    public void onClickListener(Object object) {
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("familyrank", object.toString());
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                });
                break;
            case R.id.ll_bk2://有无子女
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_bk2, "");
//                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("ischild", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });

                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = zi_nvList.get(options1);
                        tv_bk2.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("ischild", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
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
//                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("weight", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });
                mPickerView = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        String text = ti_zhongList.get(options1);
                        tv_weight.setText(text);
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("weight", text);
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                        .setSubmitColor(R.color.black)
                        .setCancelColor(R.color.black)//取消按钮文字颜色
                        .build();

                mPickerView.setPicker(ti_zhongList);
                mPickerView.show();
                break;
            case R.id.ll_wx://微信号
                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_wx, "");
                mPopupWindowAddInfo.showAtLocation(headView, Gravity.CENTER, 0, 0);
                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
                    @Override
                    public void onClickListener(Object object) {
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("wx", object.toString());
                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    mParams.clear();
                    mParams.put("pic", new File(selectList.get(0).getCompressPath()));
                    Glide.with(mContext)
                            .load(selectList.get(0).getCompressPath())
                            .apply(BasesActivity.mCircleRequestOptions)
                            .into(headView);
                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.LOADING_MORE_TYPE);
                    break;
            }
        }
//        if (resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case AppConstant.REQUEST_CODE.CAMERA:
//                    String mImgPath = data.getStringExtra(AppConstant.KEY.IMG_PATH);
//                    mParams.clear();
//                    mParams.put("pic", new File(mImgPath));
//                    Glide.with(mContext)
//                            .load(mImgPath)
//                            .apply(BasesActivity.mCircleRequestOptions)
//                            .into(headView);
//                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.LOADING_MORE_TYPE);
//                    break;
//            }
//        }
        if (resultCode == 102) {
            switch (requestCode) {
                case 105:
                    String mCity_id = data.getStringExtra("city_id");
                    String city = data.getStringExtra("city");
                    workCity.setText(city);
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("areaid", mCity_id);
                    mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    break;
                case 106:
                    String mCity_id1 = data.getStringExtra("city_id");
                    String city1 = data.getStringExtra("city");
                    household.setText(city1);
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("household", mCity_id1);
                    mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
                    break;
            }
        }
    }

    @Override
    public void success(String bean) {
        PersonageInfoBean personageInfoBean = JsonUtils.GsonToBean(bean, PersonageInfoBean.class);
        PersonageInfoBean.PersonageInfoData data = personageInfoBean.data;
        Glide.with(mContext)
                .load(data.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into(headView);
        username.setText(data.username);
        id.setText(data.userid);
        birthday.setText(data.birthdate);
        constellation.setText(data.constellation);
        height.setText(data.height + "cm");
        income.setText(data.yearmoney);
        workCity.setText(data.areaname);
        marry.setText(data.marrytime);
        nation.setText(data.nation);
        household.setText(data.household);
        marryinfo.setText(data.marryinfo);
        profession.setText(data.zhiye);
        tv_believe.setText(data.believe);
        tv_wine.setText(data.wine);
        tv_bk1.setText(data.smoke);
        tv_familyrank.setText(data.familyrank);
        tv_bk2.setText(data.ischild);
        if (null!=data.weight&& !TextUtils.isEmpty(data.weight)) {
            tv_weight.setText(data.weight + "kg");
        }

        tv_wx.setText(data.wx);
        tv_generalize.setText(data.ma);
    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        toastShow(baseBean.message);
    }

    @Override
    public void loadMore(String bean) {
        UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
        String mImg = uploadBean.data;
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("headpic", mImg);
        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
    }

    @Override
    public void failed(String content) {

    }

}
