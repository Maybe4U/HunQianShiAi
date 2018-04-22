package com.zykj.hunqianshiai.home.dynamic.my_dynamic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.home.dynamic.DynamicFragment;
import com.zykj.hunqianshiai.home.my.PicsBean;
import com.zykj.hunqianshiai.home.my.pic_management.DeleteLocalPicsActivity;
import com.zykj.hunqianshiai.home.my.pic_management.DeletePicsActivity;
import com.zykj.hunqianshiai.inform.InformPicAdapter;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.seek.GetJsonDataUtil;
import com.zykj.hunqianshiai.seek.JsonBean;
import com.zykj.hunqianshiai.seek.SeekActivity;
import com.zykj.hunqianshiai.tools.JsonUtils;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 发布动态
 */
public class IssueDynamicActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.iv_add_pic)
    ImageView addPic;
    @Bind(R.id.tv_right)
    TextView right;
    @Bind(R.id.tv_text_count)
    TextView textCount;
    @Bind(R.id.recycler_pic)
    RecyclerView mRecyclerView;
    @Bind(R.id.et_content)
    EditText content;
    @Bind(R.id.iv_select_look)
    ImageView selectLook;
    @Bind(R.id.tv_set_look)
    TextView setLook;
    @Bind(R.id.tv_set_sit)
    TextView setSit;
    @Bind(R.id.iv_select_sit)
    ImageView selectSit;
    @Bind(R.id.check_synchronization)
    CheckBox mCheckBox;



    private List<LocalMedia> selectList = new ArrayList<>();
    private InformPicAdapter mInformPicAdapter;
    private LocationClient mLocationClient;
    private List<Poi> mPoiList;
    private OptionsPickerView mPickerView;
    private BasePresenterImpl mPresenter;
    private String mContents;
    private int look = 1;
    private ArrayList<String> mData = new ArrayList<>();


    @Override
    protected int getContentViewX() {

        return R.layout.activity_issue_dynamic;
    }

    @Override
    protected void initView() {
        appBar("发布动态");
        right.setVisibility(View.VISIBLE);
        right.setText("发布");
        right.setOnClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mInformPicAdapter = new InformPicAdapter(selectList);
        mRecyclerView.setAdapter(mInformPicAdapter);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());

        //限制输入字数
        content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(150)});
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = content.getText().toString().length();
                textCount.setText(length + "/150");
                if (length >= 500) {
                    toastShow("超出最大字数");
                }
            }
        });

        //移除选中的照片
        mInformPicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_delete_pic:
                        //selectList.remove(position);
                        adapter.remove(position);
                        break;
                        /*====================优化===================*/
                    case R.id.iv_inform_pic://提供打开预览图片并且删除的功能
                        if (!mData.isEmpty()){
                            mData.clear();
                        }
                        for (int i = 0; i < selectList.size(); i++) {
                            String path = selectList.get(i).getCompressPath();
                            mData.add(path);
                        }

                        Bundle mBundle = new Bundle();
                        mBundle.clear();
                        mBundle.putInt("position", position);
                        mBundle.putStringArrayList("pics", mData);
                        openActivity(DeleteLocalPicsActivity.class, mBundle,1);
                    default:
                        break;
                }

            }
        });

        baiduMap();
    }

    private void baiduMap() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
                //以下只列举部分获取地址相关的结果信息
                //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

                //                String addr = bdLocation.getAddrStr();    //获取详细地址信息
                //                String country = bdLocation.getCountry();    //获取国家
                //                String province = bdLocation.getProvince();    //获取省份
                //                String city = bdLocation.getCity();    //获取城市
                //                String district = bdLocation.getDistrict();    //获取区县
                //                String street = bdLocation.getStreet();    //获取街道信息
                mPoiList = bdLocation.getPoiList();

            }
        });

        LocationClientOption option = new LocationClientOption();
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);
        option.setIsNeedLocationPoiList(true);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.setLocOption(option);


        //mLocationClient为第二步初始化过的LocationClient对象
        //调用LocationClient的start()方法，便可发起定位请求
        mLocationClient.start();
    }

    @OnClick({R.id.iv_add_pic, R.id.rl_select_look, R.id.rl_sit})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_add_pic://添加照片
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .maxSelectNum(6)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(true)// 是否显示拍照按钮 true or false
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                        .enableCrop(false)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.rl_select_look://谁可以看
                PopupWindowSelectLook popupWindowSelectLook = new PopupWindowSelectLook(this);
                popupWindowSelectLook.showAtLocation(textCount, Gravity.BOTTOM, 0, 0);
                popupWindowSelectLook.setClickListener(new BasePopupWindow.ClickListener() {
                    @Override
                    public void onClickListener(Object object) {
                        String trim = object.toString().trim();
                        if (trim.equals("所有异性可见")) {
                            look = 4;
                        } else if (trim.equals("我心动的和心动我的可看")) {
                            look = 5;
                        } else if (trim.equals("我心动的可看")) {
                            look = 2;
                        } else if (trim.equals("心动我的可看")) {
                            look = 3;
                        }
                        setLook.setText(trim);
                        setLook.setVisibility(View.VISIBLE);
                        selectLook.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.rl_sit://位置
                if (null != mPoiList) {
                    final ArrayList<String> strings = new ArrayList<>();
                    for (int i = 0; i < mPoiList.size(); i++) {
                        strings.add(mPoiList.get(i).getName());
                    }
                    mPickerView = new OptionsPickerView
                            .Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            setSit.setText(strings.get(options1));
                            setSit.setVisibility(View.VISIBLE);
                            selectSit.setVisibility(View.GONE);
                        }
                    }).setTitleBgColor(0xffedbd5a)//标题背景颜色 Night mode
                            .setSubmitColor(R.color.black)
                            .setCancelColor(R.color.black)//取消按钮文字颜色
                            .build();

                    mPickerView.setPicker(strings);
                    mPickerView.show();
                }
                break;

            case R.id.tv_right://提交

                mContents = content.getText().toString().trim();
                if (TextUtils.isEmpty(mContents)) {
                    toastShow("说点什么吧");
                    return;
                }
                showDialog();
                if (null != selectList && !selectList.isEmpty()) {
                    mParams.clear();
                    for (int i = 0; i < selectList.size(); i++) {
                        mParams.put("pic" + i, new File(selectList.get(i).getCompressPath()));
                    }
                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.DEFAULT_TYPE);
                } else {
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("content", mContents);
                    mParams.put("is_play", 1);

                    if (setSit.getVisibility() == View.VISIBLE) {
                        mParams.put("address", setSit.getText().toString());
                    }

                    mParams.put("is_see", look);

                    mPresenter.getData(UrlContent.ISSUE_DYNAMIC_URL, mParams, BaseModel.REFRESH_TYPE);
                }

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
                    mInformPicAdapter.setNewData(selectList);

                    break;
                case 1:
                    ArrayList<Integer> list = data.getIntegerArrayListExtra("deleteList");
                    for (int i = 0; i < list.size(); i++) {
                        //Log.e("得到的要删除的位置", list.get(i) + "");
                        mInformPicAdapter.remove(list.get(i));
                    }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (null != mLocationClient) {
            mLocationClient.stop();
        }

        super.onDestroy();
    }

    @Override
    public void success(String bean) {
        //Log.e("success",bean);
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {
            toastShow("不支持的图片格式，请重新选择图片");
            return;
        }

        UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
        if (200 == uploadBean.code) {
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("content", mContents);
            mParams.put("img", uploadBean.data);

            if (mCheckBox.isChecked()) {
                mParams.put("is_play", 2);
            } else {
                mParams.put("is_play", 1);
            }

            if (setSit.getVisibility() == View.VISIBLE) {
                mParams.put("address", setSit.getText().toString());
            }

            mParams.put("is_see", look);

            mPresenter.getData(UrlContent.ISSUE_DYNAMIC_URL, mParams, BaseModel.REFRESH_TYPE);
        }
    }

    @Override
    public void refresh(String bean) {

        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);

        hideDialog();
        toastShow(baseBean.message);
        if (baseBean.code == 200) {
            setResult(104);

            /*===================优化====================*/
            //发布动态界面关闭时刷新动态页面内容
            mPresenter = new BasePresenterImpl(DynamicFragment.getInstance(), new BaseModelImpl());
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("page", 1);
            mParams.put("size", 6);
            mPresenter.getData(UrlContent.ALL_DYNAMIC_URL, mParams, BaseModel.REFRESH_TYPE);


            finish();
        }
    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }




}
