package com.zykj.hunqianshiai.login_register.goago;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.dynamic.DynamicFragment;
import com.zykj.hunqianshiai.home.dynamic.my_dynamic.IssueDynamicActivity;
import com.zykj.hunqianshiai.home.home.HomeFragment;
import com.zykj.hunqianshiai.home.message.MessageFragment;
import com.zykj.hunqianshiai.home.my.MyFragment;
import com.zykj.hunqianshiai.home.region.RegionsFragment;
import com.zykj.hunqianshiai.login_register.login.LoginActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.TabButton;

import butterknife.Bind;
import butterknife.OnClick;

public class HomeGoagoActivity extends BasesActivity implements RadioGroup.OnCheckedChangeListener, BaseView<String> {

    @Bind(R.id.tb_home)
    TabButton home;
    @Bind(R.id.tb_region)
    TabButton region;
    @Bind(R.id.tb_dynamic)
    TabButton dynamic;
    @Bind(R.id.tb_message)
    TabButton message;
    @Bind(R.id.tb_my)
    TabButton tb_my;
    @Bind(R.id.rg_tab)
    RadioGroup tab;
    @Bind(R.id.iv_message_dot)
    ImageView iv_message_dot;
    @Bind(R.id.msg_num)
    TextView msg_num;

    private int click = 0;
    private HomeFragment mHomeFragment;
    private RegionsFragment mRegionFragment;
    private DynamicFragment mDynamicFragment;
    private MessageFragment mMessageFragment;
    private MyFragment mMyFragment;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {

        region.setClickable(false);
        dynamic.setClickable(false);
        message.setClickable(false);
        tb_my.setClickable(false);

        tab.setOnCheckedChangeListener(this);
        home.performClick();

        //
        iv_message_dot.setVisibility(View.GONE);
        msg_num.setVisibility(View.GONE);

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (null != mHomeFragment) {
            transaction.hide(mHomeFragment);
        }
        if (null != mRegionFragment) {
            transaction.hide(mRegionFragment);
        }
        if (null != mDynamicFragment) {
            transaction.hide(mDynamicFragment);
        }
        if (null != mMessageFragment) {
            transaction.hide(mMessageFragment);
        }
        if (null != mMyFragment) {
            transaction.hide(mMyFragment);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        click = 0;
        switch (i) {
            case R.id.tb_home:
                if (null == mHomeFragment) {
                    mHomeFragment = HomeFragment.getInstance();
                    transaction.add(R.id.fl_layout, mHomeFragment);
                } else {
                    transaction.show(mHomeFragment);
                }

                break;
            case R.id.tb_region:
                if (null == mRegionFragment) {
                    mRegionFragment = RegionsFragment.getInstance();
                    transaction.add(R.id.fl_layout, mRegionFragment);
                } else {
                    transaction.show(mRegionFragment);
                }
                break;
            case R.id.tb_dynamic:
                if (null == mDynamicFragment) {
                    mDynamicFragment = DynamicFragment.getInstance();
                    transaction.add(R.id.fl_layout, mDynamicFragment);
                } else {
                    transaction.show(mDynamicFragment);
                }
                break;
            case R.id.tb_message:
                if (null == mMessageFragment) {
                    mMessageFragment = MessageFragment.getInstance();
                    transaction.add(R.id.fl_layout, mMessageFragment);
                } else {
                    transaction.show(mMessageFragment);
                }
                break;
            case R.id.tb_my:
                if (null == mMyFragment) {
                    mMyFragment = MyFragment.getInstance();
                    transaction.add(R.id.fl_layout, mMyFragment);
                } else {
                    transaction.show(mMyFragment);
                }
                break;
        }
        transaction.commit();
    }

    @OnClick({R.id.tb_dynamic,R.id.rg_tab})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tb_dynamic:
                click++;
                if (click > 1) {
//                    toastShow(dynamic.isChecked() + "");
                    openActivity(IssueDynamicActivity.class);
                }

                break;
            case R.id.rg_tab:
                if (UrlContent.GOAGO) {
                    openActivity(LoginActivity.class);
                    return;
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
