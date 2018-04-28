package com.zykj.hunqianshiai.home.my.set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.inform.InformPicAdapter;
import com.zykj.hunqianshiai.inform.PopupWindowInform;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.tv_right)
    TextView right;
    @Bind(R.id.et_content)
    EditText content;
    @Bind(R.id.tv_words_number)
    TextView wordsNumber;
    @Bind(R.id.recycler_inform)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_inform_type)
    TextView informType;
    @Bind(R.id.ll_layout)
    LinearLayout mLinearLayout;
    @Bind(R.id.et_phone)
    EditText et_phone;

    private List<LocalMedia> selectList = new ArrayList<>();
    private InformPicAdapter mInformPicAdapter;
    private BasePresenterImpl mPresenter;
    private String mContents;
    private String mPhone;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {
        appBar("意见反馈");
        right.setVisibility(View.VISIBLE);
        right.setText("提交");
        right.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mInformPicAdapter = new InformPicAdapter(selectList);
        mRecyclerView.setAdapter(mInformPicAdapter);

        //限制输入字数
        content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
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
                wordsNumber.setText(length + "/500");
                if (length >= 500) {
                    toastShow("超出最大字数");
                }
            }
        });

        mInformPicAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_delete_pic:
//                        selectList.remove(position);
                        adapter.remove(position);
                        break;
                    default:
                        break;
                }
            }
        });

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
    }

    @OnClick({R.id.rl_inform_type, R.id.iv_app_pic})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rl_inform_type://选取举报类型

                PopupWindowInform popupWindowInform = new PopupWindowInform(this);
                popupWindowInform.showAtLocation(wordsNumber, Gravity.BOTTOM, 0, 0);
                popupWindowInform.setClickListener(new BasePopupWindow.ClickListener() {
                    @Override
                    public void onClickListener(Object object) {
                        mLinearLayout.setVisibility(View.GONE);
                        informType.setVisibility(View.VISIBLE);
                        informType.setText(object.toString());
                    }
                });
                break;
            case R.id.iv_app_pic://添加图片

//                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .maxSelectNum(8)// 最大图片选择数量 int
                        .minSelectNum(1)// 最小选择数量 int
                        .imageSpanCount(4)// 每行显示个数 int
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                        .previewImage(true)// 是否可预览图片 true or false
                        .isCamera(false)// 是否显示拍照按钮 true or false
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                        .enableCrop(false)// 是否裁剪 true or false
                        .compress(true)// 是否压缩 true or false
                        .selectionMedia(selectList)// 是否传入已选图片 List<LocalMedia> list
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.tv_right:

                mContents = content.getText().toString().trim();
                if (TextUtils.isEmpty(mContents)) {
                    toastShow("请描述您反馈的问题");
                    return;
                }
                mPhone = et_phone.getText().toString().trim();
                if (TextUtils.isEmpty(mPhone)) {
                    toastShow("请输入联系方式");
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
                    mPhone = et_phone.getText().toString().trim();
                    if (TextUtils.isEmpty(mPhone)) {
                        toastShow("请输入手机号");
                        return;
                    }
                    mParams.clear();
                    mParams.put("userid", UrlContent.USER_ID);
                    mParams.put("content", mContents);
                    mParams.put("concact", mPhone);

                    mPresenter.getData(UrlContent.FEEDBACK_URL, mParams, BaseModel.REFRESH_TYPE);
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
            }
        }
    }

    @Override
    public void success(String bean) {
        UploadBean uploadBean = JsonUtils.GsonToBean(bean, UploadBean.class);
        if (200 == uploadBean.code) {
            mPhone = et_phone.getText().toString().trim();

            mParams.clear();
            mParams.put("userid", UrlContent.USER_ID);

            mParams.put("content", mContents);
            mParams.put("concact", mPhone);
            mParams.put("pic", uploadBean.data);

            mPresenter.getData(UrlContent.FEEDBACK_URL, mParams, BaseModel.REFRESH_TYPE);
        }
    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        hideDialog();
        toastShow(baseBean.message);
        if (baseBean.code == 200) {
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
