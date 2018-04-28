package com.zykj.hunqianshiai.home.my.pic_management;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

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
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.home.my.PicsBean;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.look_pic_video.VideoActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.RxBus;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 管理照片
 */
public class PicManagementActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.iv_head_view)
    ImageView iv_head_view;
    @Bind(R.id.iv_play)
    ImageView iv_play;

    private List<LocalMedia> mLocalMedia;
    private BasePresenterImpl mPresenter;
    private Bundle mExtras;
    private String mHeadpic;
    private String mVideo;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_pic_management;
    }

    @Override
    protected void initView() {
        mExtras = getIntent().getExtras();
        mHeadpic = mExtras.getString("headpic", "");
        mVideo = mExtras.getString("video", "");
        if (TextUtils.isEmpty(mVideo)) {
            glide(mHeadpic, iv_head_view, mOptions);
            iv_play.setVisibility(View.GONE);
        } else {
            glide(UrlContent.PIC_URL + mVideo, iv_head_view, mOptions);
            iv_play.setVisibility(View.VISIBLE);
        }
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
//        glide(mHeadpic, iv_head_view, mOptions);
    }

    @OnClick({R.id.iv_back, R.id.iv_change_pic, R.id.tv_video, R.id.iv_head_view, R.id.tv_finish})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_finish:
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("rdm", UrlContent.RDM);
                mParams.put("sign", UrlContent.SIGN);
                OkGo.<String>post(UrlContent.DELETE_VIDEO_URL)
                        .params(mParams)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                glide(mHeadpic, iv_head_view, mOptions);
                                iv_play.setVisibility(View.GONE);
                                mVideo = "";
                            }
                        });
                break;
            case R.id.iv_head_view:
                if (TextUtils.isEmpty(mVideo)) {
                    return;
                }
                mExtras.clear();
                mExtras.putString("video", mVideo);
                openActivity(VideoActivity.class, mExtras);
                break;
            case R.id.iv_change_pic://更换形象照
                mExtras.clear();
                mExtras.putString("headpic", mHeadpic);
                Intent intent = new Intent(this, MyPicActivity.class);
                intent.putExtras(mExtras);
                startActivityForResult(intent, 109);
                break;
            case R.id.tv_video:
                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofVideo())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
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
                        .compress(false)// 是否压缩 true or false
                        .videoQuality(0)
                        //.videoMaxSecond(7)
                        .videoMinSecond(3)
                        .recordVideoSecond(10)//视频秒数录制 默认60s int

//                        .selectionMedia(mLocalMedia)// 是否传入已选图片 List<LocalMedia> list
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
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
//                    glide(mLocalMedia.get(0).getPath(), iv_head_view, mOptions);
                    showDialog();
                    mParams.clear();
                    mParams.put("video", new File(mLocalMedia.get(0).getPath()));
                    mPresenter.getData(UrlContent.UPLOAD_VIDEO_URL, mParams, BaseModel.REFRESH_TYPE);

                    break;
                case 109:
                    mHeadpic = data.getStringExtra("mImgPath");
                    glide(UrlContent.PIC_URL + mHeadpic, iv_head_view, mOptions);
                    break;

            }
        }
    }

    @Override
    public void success(String bean) {

    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        hideDialog();
        if (baseBean.code == 200) {
            UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
            String data = uploadBean.data;
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("pics", data);
            mParams.put("type", 1);
            mPresenter.getData(UrlContent.ADD_VIDEO_URL, mParams, BaseModel.LOADING_MORE_TYPE);
        } else {
            toastShow(baseBean.message);

        }
    }

    @Override
    public void loadMore(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        toastShow(baseBean.message);
        if (baseBean.code == 200) {
            VideoBean videoBean = JsonUtils.GsonToBean(bean, VideoBean.class);
            mVideo = videoBean.data.video;
            glide(UrlContent.PIC_URL + mVideo, iv_head_view, mOptions);
            iv_play.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void failed(String content) {

    }

    @Override
    public void finish() {
        RxBus.getInstance().post(new PicsBean());
        super.finish();
    }
}
