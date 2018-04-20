package com.zykj.hunqianshiai.home.my.pic_management;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.camera.AppConstant;
import com.zykj.hunqianshiai.camera.CameraActivity;
import com.zykj.hunqianshiai.home.my.PicsBean;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.RxBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import rx.Subscription;
import rx.functions.Action1;

/**
 * 我的照片
 */
public class MyPicActivity extends BasesActivity implements BaseView<String>, View.OnLongClickListener {

    ImageView pic1;
    ImageView pic2;
    ImageView pic3;
    ImageView pic4;
    ImageView pic5;
    ImageView pic6;
    @Bind(R.id.iv_right_share)
    ImageView right;
    @Bind(R.id.recycler_pics)
    RecyclerView mRecyclerView;

    private List<LocalMedia> mLocalMedia1;
    private List<ImageView> mImageViews;
    private BasePresenterImpl mPresenter;
    private MyPicAdapter mMyPicAdapter;
    private MyPicAdapter mMyPicAdapter1;
    private View mHeadView;
    private ArrayList<String> mData;
    private List<String> newData = new ArrayList<>();
    private Bundle mBundle;
    private String mImgPath;
    private String mHeaderView;
    private Subscription mSubscribe;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_my_pic;
    }

    @Override
    protected void initView() {
        appBar("我的照片");
        right.setVisibility(View.VISIBLE);
        right.setImageResource(R.mipmap.wode_paishexiangji);
        right.setOnClickListener(this);

        mBundle = getIntent().getExtras();
        mImgPath = mBundle.getString("headpic");


        mHeadView = LayoutInflater.from(this).inflate(R.layout.my_pic_header, null);
        pic1 = mHeadView.findViewById(R.id.iv_pic1);
        pic2 = mHeadView.findViewById(R.id.iv_pic2);
        pic3 = mHeadView.findViewById(R.id.iv_pic3);
        pic4 = mHeadView.findViewById(R.id.iv_pic4);
        pic5 = mHeadView.findViewById(R.id.iv_pic5);
        pic6 = mHeadView.findViewById(R.id.iv_pic6);
        pic1.setOnClickListener(this);
        pic2.setOnClickListener(this);
        pic3.setOnClickListener(this);
        pic4.setOnClickListener(this);
        pic5.setOnClickListener(this);
        pic6.setOnClickListener(this);

        glide(mImgPath, pic1, mOptions);

        ImageView[] imageViews = {pic2, pic3, pic4, pic5, pic6};
        mImageViews = Arrays.asList(imageViews);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.GET_PIC_URL, mParams, BaseModel.DEFAULT_TYPE);

        mSubscribe = RxBus.getInstance().toObserverable(BaseBean.class)
                .subscribe(new Action1<BaseBean>() {
                    @Override
                    public void call(BaseBean picsBean) {
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mPresenter.getData(UrlContent.GET_PIC_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                });

//        pic1.setOnLongClickListener(this);
//        pic2.setOnLongClickListener(this);
//        pic3.setOnLongClickListener(this);
//        pic4.setOnLongClickListener(this);
//        pic5.setOnLongClickListener(this);
//        pic6.setOnLongClickListener(this);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_pic1:
//                Intent intent = new Intent(this, CameraActivity.class);
//                startActivityForResult(intent, AppConstant.REQUEST_CODE.CAMERA);
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
//                        .selectionMedia(mLocalMedia1)// 是否传入已选图片 List<LocalMedia> list
                        .forResult(222);//结果回调onActivityResult code
                break;
            case R.id.iv_pic2:
                if (mData.size() > 0) {
                    lookPics(0);
                } else {
                    addPics();
                }
                break;
            case R.id.iv_pic3:
                if (mData.size() > 1) {
                    lookPics(1);
                } else {
                    addPics();
                }
                break;
            case R.id.iv_pic4:
                if (mData.size() > 2) {
                    lookPics(2);
                } else {
                    addPics();
                }
                break;
            case R.id.iv_pic5:
                if (mData.size() > 3) {
                    lookPics(3);
                } else {
                    addPics();
                }
                break;
            case R.id.iv_pic6:
                if (mData.size() > 4) {
                    lookPics(4);
                } else {
                    addPics();
                }
                break;
            case R.id.iv_right_share:
                addPics();
                break;
            default:
                break;
        }
    }

    private void lookPics(int i) {
        mBundle.clear();
        mBundle.putInt("position", i);
        mBundle.putStringArrayList("pics", mData);
        openActivity(DeletePicsActivity.class, mBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 222:
//                    mImgPath = data.getStringExtra(AppConstant.KEY.IMG_PATH);
//                    mParams.clear();
//                    mParams.put("pic", new File(mImgPath));
//                    glide(mImgPath, pic1, mOptions);
//                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.LOADING_MORE_TYPE);
                    List<LocalMedia> localMedia = PictureSelector.obtainMultipleResult(data);
                    String compressPath = localMedia.get(0).getCompressPath();
                    mParams.clear();
                    mParams.put("pic", new File(compressPath));
                    glide(compressPath, pic1, mOptions);
                    mPresenter.getData(UrlContent.UPLOAD_PIC_URL, mParams, BaseModel.LOADING_MORE_TYPE);
                    break;
                case 199:
                    // 图片选择结果回调
                    mLocalMedia1 = PictureSelector.obtainMultipleResult(data);
                    newData.clear();
                    for (int i = 0; i < mLocalMedia1.size(); i++) {
                        String path = mLocalMedia1.get(i).getPath();
                        newData.add(path);
                    }
                    mParams.clear();
                    for (int i = 0; i < mLocalMedia1.size(); i++) {
                        mParams.put("pic" + i, new File(mLocalMedia1.get(i).getCompressPath()));
                    }
                    mParams.put("rdm", UrlContent.RDM);
                    mParams.put("sign", UrlContent.SIGN);

                    OkGo.<String>post(UrlContent.UPLOAD_PIC_URL)
                            .params(mParams)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    String bean = response.body();
                                    BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
                                    if (baseBean.code == 200) {
                                        UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
                                        String data = uploadBean.data;
                                        addPic(data);
                                    }
                                }
                            });
                    break;
            }
        }
    }

    //添加图片
    private void addPic(String data) {
        mParams.clear();
        mParams.put("rdm", UrlContent.RDM);
        mParams.put("sign", UrlContent.SIGN);
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("pics", data);
        OkGo.<String>post(UrlContent.ADD_PIC_URL)
                .params(mParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mPresenter.getData(UrlContent.GET_PIC_URL, mParams, BaseModel.REFRESH_TYPE);
                    }
                });
    }

    @Override
    public void success(String bean) {
        PicsBean picsBean = JsonUtils.GsonToBean(bean, PicsBean.class);

        mData = picsBean.data;
        if (mData.size() > 5) {
            for (int i = 0; i < 5; i++) {
                glide(UrlContent.PIC_URL + mData.get(i), mImageViews.get(i), mOptions);
            }
            List<String> strings = mData.subList(5, mData.size());
            mMyPicAdapter = new MyPicAdapter(strings);
        } else {
            for (int i = 0; i < mData.size(); i++) {
                glide(UrlContent.PIC_URL + mData.get(i), mImageViews.get(i), mOptions);
            }
            mMyPicAdapter = new MyPicAdapter(null);
        }


        mRecyclerView.setAdapter(mMyPicAdapter);
        mMyPicAdapter.addHeaderView(mHeadView);

//        mMyPicAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//                popupWindow(view, position + 5);
//                return true;
//            }
//        });

        mMyPicAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                lookPics(position + 5);
            }
        });
    }

    @Override
    public void refresh(String bean) {
        PicsBean picsBean = JsonUtils.GsonToBean(bean, PicsBean.class);
        mData = picsBean.data;
        if (mData.size() > 5) {
            for (int i = 0; i < 5; i++) {
                glide(UrlContent.PIC_URL + mData.get(i), mImageViews.get(i), mOptions);
            }
            List<String> strings = mData.subList(5, mData.size());
            mMyPicAdapter.setNewData(strings);
        } else {
            mMyPicAdapter.setNewData(null);
            for (int i = 0; i < 5; i++) {
                mImageViews.get(i).setImageResource(R.mipmap.wode_tianjiazhaopian);
            }
            for (int i = 0; i < mData.size(); i++) {
                glide(UrlContent.PIC_URL + mData.get(i), mImageViews.get(i), mOptions);
            }
        }
    }

    @Override
    public void loadMore(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        toastShow(baseBean.message);
        if (baseBean.code == 200) {
            UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
            mHeaderView = uploadBean.data;
            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);
            mParams.put("headpic", mHeaderView);
            mParams.put("rdm", UrlContent.RDM);
            mParams.put("sign", UrlContent.SIGN);
            OkGo.<String>post(UrlContent.SET_INFO_URL)
                    .params(mParams)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                        }
                    });
        }
    }

    @Override
    public void failed(String content) {

    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {

            case R.id.iv_pic2:
                if (mData.size() > 0) {
                    popupWindow(pic2, 0);
                }

                break;
            case R.id.iv_pic3:
                if (mData.size() > 1) {
                    popupWindow(pic3, 1);
                }

                break;
            case R.id.iv_pic4:
                if (mData.size() > 2) {
                    popupWindow(pic4, 2);
                }

                break;
            case R.id.iv_pic5:
                if (mData.size() > 3) {
                    popupWindow(pic5, 3);
                }

                break;
            case R.id.iv_pic6:
                if (mData.size() > 4) {
                    popupWindow(pic6, 4);
                }

                break;
        }
        return true;
    }

    private void popupWindow(View view, final int i) {
        PopupWindowDeletePic popupWindowDeletePic = new PopupWindowDeletePic(this);
        popupWindowDeletePic.showAtLocation(mRecyclerView, Gravity.CENTER, 0, 0);
        popupWindowDeletePic.setClickListener(new BasePopupWindow.ClickListener() {
            @Override
            public void onClickListener(Object object) {
                if (object.equals("add")) {
                    addPics();

                } else {

                    mParams.clear();
                    mParams.put("rdm", UrlContent.RDM);
                    mParams.put("sign", UrlContent.SIGN);
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("pics", mData.get(i));
                    OkGo.<String>post(UrlContent.DELETE_PIC_URL)
                            .params(mParams)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                                    toastShow(baseBean.message);
                                    if (baseBean.code == 200) {
                                        mData.remove(i);
                                        refreshPage();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void addPics() {
        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .maxSelectNum(5)// 最大图片选择数量 int
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
//                        .selectionMedia(mLocalMedia1)// 是否传入已选图片 List<LocalMedia> list
                .forResult(199);//结果回调onActivityResult code
    }

    private void refreshPage() {
        if (mData.size() > 5) {
            for (int i = 0; i < 5; i++) {
                glide(UrlContent.PIC_URL + mData.get(i), mImageViews.get(i), mOptions);
            }
            List<String> strings = mData.subList(5, mData.size());
            mMyPicAdapter.setNewData(strings);
        } else {
            mMyPicAdapter.setNewData(null);
            for (int i = 0; i < 5; i++) {
                mImageViews.get(i).setImageResource(R.mipmap.wode_tianjiazhaopian);
            }
            for (int i = 0; i < mData.size(); i++) {
                glide(UrlContent.PIC_URL + mData.get(i), mImageViews.get(i), mOptions);
            }
        }
    }

    @Override
    public void finish() {
        if (null != mHeaderView && !TextUtils.isEmpty(mHeaderView)) {
            Intent intent = new Intent();
            intent.putExtra("mImgPath", mHeaderView);
            setResult(RESULT_OK, intent);
        }

        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (!mSubscribe.isUnsubscribed()) {
            mSubscribe.unsubscribe();
            super.onDestroy();
        }
    }
}
