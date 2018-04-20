package com.zykj.hunqianshiai.user_home.personal_assistant;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.net.UrlContent;

import butterknife.Bind;
import butterknife.OnCheckedChanged;

/**
 * 开通婚恋管家
 */
public class OpenStewardActivity extends BasesActivity implements CheckBox.OnCheckedChangeListener, BaseView<String> {

    @Bind(R.id.cb_three_month)
    CheckBox threeMonth;
    @Bind(R.id.cb_six_month)
    CheckBox sixMonth;
    @Bind(R.id.cb_year)
    CheckBox year;
    @Bind(R.id.cb_zfb)
    CheckBox zfb;
    @Bind(R.id.cb_wx)
    CheckBox wx;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_open_steward;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        String type = bundle.getString("type");
        appBar(title);
        zfb.performClick();
        threeMonth.performClick();

        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("type", type);
        presenter.getData(UrlContent.GET_PRICE_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @OnCheckedChanged({R.id.cb_three_month, R.id.cb_six_month, R.id.cb_year, R.id.cb_zfb, R.id.cb_wx})
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.cb_three_month:
                if (b) {
                    sixMonth.setChecked(false);
                    year.setChecked(false);
                } else {
                    if (!year.isChecked() && !sixMonth.isChecked()) {
                        threeMonth.setChecked(true);
                    }
                }
                break;
            case R.id.cb_six_month:
                if (b) {
                    threeMonth.setChecked(false);
                    year.setChecked(false);
                } else {
                    if (!threeMonth.isChecked() && !year.isChecked()) {
                        sixMonth.setChecked(true);
                    }
                }
                break;
            case R.id.cb_year:
                if (b) {
                    threeMonth.setChecked(false);
                    sixMonth.setChecked(false);
                } else {
                    if (!threeMonth.isChecked() && !sixMonth.isChecked()) {
                        year.setChecked(true);
                    }
                }
                break;
            case R.id.cb_zfb:
                if (b) {
                    wx.setChecked(false);
                } else {
                    if (!wx.isChecked()) {
                        zfb.setChecked(true);
                    }
                }
                break;
            case R.id.cb_wx:
                if (b) {
                    zfb.setChecked(false);
                } else {
                    if (!zfb.isChecked()) {
                        wx.setChecked(true);
                    }
                }
                break;
        }
    }

    @Override
    public void success(String bean) {

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
