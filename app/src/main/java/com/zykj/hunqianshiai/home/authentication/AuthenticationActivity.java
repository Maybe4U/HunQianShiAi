package com.zykj.hunqianshiai.home.authentication;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.my.PicsBean;
import com.zykj.hunqianshiai.login_register.FaceRecognitionActivity;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.RxBus;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 实名认证
 */
public class AuthenticationActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.iv_front)
    ImageView front1;
    @Bind(R.id.iv_front2)
    ImageView front2;
    @Bind(R.id.tv_skip)
    TextView tv_skip;
    private List<LocalMedia> mLocalMedia;
    private List<LocalMedia> mLocalMedia1;
    private BasePresenterImpl mPresenter;
    private String mP2;
    private String mP1;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_name_authentication;
    }

    @Override
    protected void initView() {
        appBar("实名认证");
        tv_skip.setVisibility(View.GONE);
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
    }

    @OnClick({R.id.tv_submit, R.id.tv_skip, R.id.iv_front, R.id.iv_front2})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_submit://提交
                if (null == mP1 || TextUtils.isEmpty(mP1)) {
                    toastShow("照片上传失败，请重新选择照片");
                    return;
                }
                if (null == mP2 || TextUtils.isEmpty(mP2)) {
                    toastShow("照片上传失败，请重新选择照片");
                    return;
                }
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("p1", mP1);
                mParams.put("p2", mP2);
                mPresenter.getData(UrlContent.NAME_AUTHENTICATION_URL, mParams, BaseModel.DEFAULT_TYPE);
                break;
            case R.id.tv_skip://跳过
                openActivity(FaceRecognitionActivity.class);
                finish();
                break;
            case R.id.iv_front:
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .maxSelectNum(8)// 最大图片选择数量 int
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
            case R.id.iv_front2:
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .maxSelectNum(8)// 最大图片选择数量 int
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
                        .forResult(199);//结果回调onActivityResult code
                break;
            default:
                break;
        }
    }

    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code == 200) {
            RxBus.getInstance().post(new PicsBean());
            finish();
        } else {
            toastShow("认证失败");
        }

    }

    @Override
    public void refresh(String bean) {
        UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
        if (uploadBean.code == 200) {
            mP1 = uploadBean.data;
        }

    }

    @Override
    public void loadMore(String bean) {
        UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
        if (uploadBean.code == 200) {
            mP2 = uploadBean.data;
        }
    }

    @Override
    public void failed(String content) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    mLocalMedia = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    glide(mLocalMedia.get(0).getPath(), front1, mOptions);
                    //选择完上传，服务器规定
                    mParams.clear();
                    mParams.put("img", new File(mLocalMedia.get(0).getPath()));
                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.REFRESH_TYPE);
                    break;
                case 199:
                    // 图片选择结果回调
                    mLocalMedia1 = PictureSelector.obtainMultipleResult(data);
                    glide(mLocalMedia1.get(0).getPath(), front2, mOptions);
                    //选择完上传，服务器规定
                    mParams.clear();
                    mParams.put("img", new File(mLocalMedia1.get(0).getPath()));
                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.LOADING_MORE_TYPE);
                    break;
            }
        }
    }
}
