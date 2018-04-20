package com.zykj.hunqianshiai.home.my;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.RxBus;

import butterknife.Bind;


public class MyInfoActivity extends BasesActivity implements BaseView<String>{

    @Bind(R.id.et_content)
    EditText et_content;
    @Bind(R.id.tv_number)
    TextView tv_number;
    @Bind(R.id.tv_right)
    TextView tv_right;
    private BasePresenterImpl mPresenter;
    private String mKey;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initView() {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("提交");
        tv_right.setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        appBar(title);
        String content = bundle.getString("content", "");
        mKey = bundle.getString("key");
        et_content.setText(content);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());

        //限制输入字数
        et_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = et_content.getText().toString().length();
                tv_number.setText(length + "/300");
                if (length >= 300) {
                    toastShow("超出最大字数");
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_right:
                String trim = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(trim)) {
                    toastShow("不能为空");
                    return;
                }
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put(mKey, trim);
                mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.DEFAULT_TYPE);
                break;
        }
    }

    @Override
    public void success(String bean) {
        RxBus.getInstance().post(new PicsBean());
        finish();
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
}
