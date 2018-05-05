package com.zykj.hunqianshiai.login_register;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.camera.AppConstant;
import com.zykj.hunqianshiai.camera.CameraUtil;
import com.zykj.hunqianshiai.login_register.hobby_interest.HobbyInterestActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.TextUtil;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 人脸识别
 */
public class FaceRecognitionActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.iv_camera)
    ImageView camera;
    private BasePresenterImpl mPresenter;
    private String mImgPath;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_face_recognition;
    }

    @Override
    protected void initView() {
        appBar("人脸识别");
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
    }

    @OnClick({R.id.tv_submit, R.id.tv_skip, R.id.iv_camera})
    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()) {
            case R.id.tv_submit:

                if (null==mImgPath|| TextUtils.isEmpty(mImgPath)) {
                    toastShow("图片为空");
                    return;
                }
                showDialog();
                mParams.clear();
                mParams.put("img", new File(mImgPath));
                mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.DEFAULT_TYPE);

                break;
            case R.id.tv_skip:
                openActivity(HobbyInterestActivity.class);
                finish();
                break;
            case R.id.iv_camera://
                CameraUtil.getInstance().camera(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void success(String bean) {
        UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
        if (200 == uploadBean.code) {
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("p3", uploadBean.data);
            mPresenter.getData(UrlContent.NAME_AUTHENTICATION_URL, mParams, BaseModel.REFRESH_TYPE);
        }
    }

    @Override
    public void refresh(String bean) {
        hideDialog();
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (200 == baseBean.code) {

            openActivity(HobbyInterestActivity.class);
            finish();
        } else {
            toastShow(baseBean.message);
        }

    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {
        hideDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case AppConstant.REQUEST_CODE.CAMERA:
                    mImgPath = data.getStringExtra(AppConstant.KEY.IMG_PATH);
                    glide(mImgPath, camera, mOptions);
                    break;
            }
        }
    }
}
