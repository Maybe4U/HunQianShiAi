package com.zykj.hunqianshiai.home.my.identification_car;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
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
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.RxBus;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 车辆认证
 */
public class IdentificationCarActivity extends BasesActivity implements BaseView<String>{

    @Bind(R.id.tv_select_buy)
    TextView selectBuy;
    @Bind(R.id.tv_car_trademark)
    TextView carTrademark;
    @Bind(R.id.tv_right)
    TextView right;
    @Bind(R.id.iv_car)
    ImageView car;
    @Bind(R.id.et_content)
    EditText et_content;

    private List<LocalMedia> selectList;
    private BasePresenterImpl mPresenter;
    private String mData;
    private List<String> mStrings;
    private OptionsPickerView mPickerView;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_identification_car;
    }

    @Override
    protected void initView() {
        appBar("车辆认证");
        right.setVisibility(View.VISIBLE);
        right.setText("提交");
        right.setOnClickListener(this);
        String[] stringArray = getResources().getStringArray(R.array.buy_car);
        mStrings = Arrays.asList(stringArray);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
    }

    @OnClick({R.id.tv_select_buy, R.id.iv_car})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_select_buy://购买情况
                mPickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
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
//                PopupWindowBuyCar popupWindowBuyCar = new PopupWindowBuyCar(this);
//                popupWindowBuyCar.showAtLocation(carTrademark, Gravity.BOTTOM, 0, 0);
//                popupWindowBuyCar.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        selectBuy.setText(object.toString());
//                    }
//                });
                break;
            case R.id.iv_car://证明
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
                    toastShow("请重新选择行驶证");
                    return;
                }

                String trim = carTrademark.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    toastShow("请填写车辆品牌");
                    return;
                }

                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("cat", 1);
                mParams.put("p1", education);
                mParams.put("p2", trim);
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
                    glide(selectList.get(0).getPath(), car, mOptions);
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
}
