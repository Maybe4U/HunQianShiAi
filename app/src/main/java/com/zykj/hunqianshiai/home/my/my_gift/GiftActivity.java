package com.zykj.hunqianshiai.home.my.my_gift;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.my.wallet.WalletActivity;

import butterknife.Bind;

public class GiftActivity extends BasesActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.rb_activities)
    RadioButton rb_1;
    @Bind(R.id.rb_information)
    RadioButton rb_2;
    @Bind(R.id.rg_activities)
    RadioGroup rg;
    @Bind(R.id.rl_1)
    RelativeLayout rl_1;
    @Bind(R.id.rl_2)
    RelativeLayout rl_2;
    @Bind(R.id.iv_right_share)
    ImageView right;
    private ReceiveGiftFragment mReceiveGiftFragment;
    private SendGiftFragment mSendGiftFragment;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_gift;
    }

    @Override
    protected void initView() {
        appBar("我的礼物");
        right.setVisibility(View.VISIBLE);
        right.setImageResource(R.mipmap.liwu_tixian);
        right.setOnClickListener(this);
        rg.setOnCheckedChangeListener(this);
        rb_1.performClick();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_right_share:
                openActivity(WalletActivity.class);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case R.id.rb_activities:
                if (null == mReceiveGiftFragment) {
                    mReceiveGiftFragment = ReceiveGiftFragment.getInstance();
                    transaction.add(R.id.fl_layout, mReceiveGiftFragment);
                } else {
                    transaction.show(mReceiveGiftFragment);
                }
                rl_1.setVisibility(View.VISIBLE);
                rl_2.setVisibility(View.INVISIBLE);

                break;
            case R.id.rb_information:
                if (null == mSendGiftFragment) {
                    mSendGiftFragment = SendGiftFragment.getInstance();
                    transaction.add(R.id.fl_layout, mSendGiftFragment);
                } else {
                    transaction.show(mSendGiftFragment);
                }
                rl_1.setVisibility(View.INVISIBLE);
                rl_2.setVisibility(View.VISIBLE);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (null != mReceiveGiftFragment) {
            transaction.hide(mReceiveGiftFragment);
        }

        if (null != mSendGiftFragment) {
            transaction.hide(mSendGiftFragment);
        }
    }
}
