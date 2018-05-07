package com.zykj.hunqianshiai.home.home.face;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
import com.zykj.hunqianshiai.camera.BitmapUtils;
import com.zykj.hunqianshiai.camera.CameraUtil;
import com.zykj.hunqianshiai.home.home.FaceActivity;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FacialActivity extends BasesActivity implements SurfaceHolder.Callback, BaseView<String> {

    @Bind(R.id.surface_view)
    SurfaceView mSurfaceView;
    @Bind(R.id.iv_take)
    ImageView take;
    @Bind(R.id.tv_right)
    TextView tv_right;
    @Bind(R.id.tv_chongx)
    TextView tv_chongx;

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int screenWidth;
    private int screenHeight;
    private int cameraPosition = 1;//1代表前置摄像头，0代表后置摄像头
    private String mImg_path;
    private BasePresenterImpl mPresenter;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_facial;
    }

    @Override
    protected void initView() {
        appBar("人脸匹配");
        //        tv_right.setVisibility(View.VISIBLE);
        //        tv_right.setOnClickListener(this);
        //        tv_right.setText("确定");

        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        mBundle = new Bundle();
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null) {
            mCamera = getCamera(cameraPosition);
            if (mHolder != null && mCamera != null) {
                startPreview(mCamera, mHolder);
            }
        }
    }

    /**
     * 获取Camera实例
     *
     * @return
     */
    private Camera getCamera(int id) {
        Camera camera = null;
        try {
            camera = Camera.open(id);
        } catch (Exception e) {
            toastShow("相机权限未获得");
        }
        return camera;
    }


    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            //这里要设置相机的一些参数，下面会详细说下
            //            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            //亲测的一个方法 基本覆盖所有手机 将预览矫正
            CameraUtil.getInstance().setCameraDisplayOrientation(this, cameraPosition, camera);
            //            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (Exception e) {
            toastShow("请在设置中心打开相关权限");
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startPreview(mCamera, surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        if(mCamera != null){
            mCamera.stopPreview();
            startPreview(mCamera, surfaceHolder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void captrue() {
        if(mCamera == null){
            return;
        }
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Bitmap saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(1, bitmap);

                saveBitmap = Bitmap.createScaledBitmap(saveBitmap, screenWidth, screenHeight, true);

                mImg_path = getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath() +
                        File.separator + System.currentTimeMillis() + ".jpeg";
                BitmapUtils.saveJPGE_After(FacialActivity.this, saveBitmap, mImg_path, 100);

                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }

                if (!saveBitmap.isRecycled()) {
                    saveBitmap.recycle();
                }
                if (null != mImg_path && !TextUtils.isEmpty(mImg_path)) {
                    mParams.clear();
                    mParams.put("pic", new File(mImg_path));
                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.DEFAULT_TYPE);
                }
                //                Intent intent = new Intent();
                //                intent.putExtra(AppConstant.KEY.IMG_PATH, img_path);
                //                setResult(AppConstant.RESULT_CODE.RESULT_OK, intent);
                //                finish();

                //这里打印宽高 就能看到 CameraUtil.getInstance().getPropPictureSize(parameters.getSupportedPictureSizes(), 200);
                // 这设置的最小宽度影响返回图片的大小 所以这里一般这是1000左右把我觉得
                //                Log.d("bitmapWidth==", bitmap.getWidth() + "");
                //                Log.d("bitmapHeight==", bitmap.getHeight() + "");
            }
        });
    }

    @OnClick({R.id.iv_take, R.id.iv_fanzhuan, R.id.iv_xc, R.id.tv_chongx})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_right:
                if (null != mImg_path && !TextUtils.isEmpty(mImg_path)) {
                    mParams.clear();
                    mParams.put("pic", new File(mImg_path));
                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.DEFAULT_TYPE);
                }
                break;
            case R.id.tv_chongx:
                tv_chongx.setVisibility(View.GONE);
                releaseCamera();
                onResume();
                break;
            case R.id.iv_take:
                showDialog();
                tv_chongx.setVisibility(View.VISIBLE);
                captrue();
                break;
            case R.id.iv_fanzhuan:
                tv_chongx.setVisibility(View.GONE);
                releaseCamera();
                if (cameraPosition == 1) {
                    cameraPosition = 0;

                } else {
                    cameraPosition = 1;
                }
                onResume();
                break;
            case R.id.iv_xc:
                PictureSelector.create(this)
                        //                        .openCamera(PictureMimeType.ofImage())
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .maxSelectNum(1)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(false)// 是否显示拍照按钮 true or false
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                        .enableCrop(false)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
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
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    showDialog();
                    mParams.clear();
                    mParams.put("pic", new File(localMedia.get(0).getCompressPath()));
                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.DEFAULT_TYPE);
                    break;
            }
        }
    }

    @Override
    public void success(String bean) {
        hideDialog();
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code == 200) {
            UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
            String data = uploadBean.data;
            mBundle.clear();
            mBundle.putString("pic", data);
            openActivity(FaceActivity.class, mBundle);
            //finish();
        }
    }

    @Override
    public void refresh(String bean) {

    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }

    @Override
    public void finish() {
        releaseCamera();
        super.finish();
    }
}
