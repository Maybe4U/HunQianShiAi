package com.zykj.hunqianshiai.home.dynamic.secret_dynamic;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

import butterknife.Bind;

/**
 * 隐私设置
 */
public class SecretDynamicActivity extends BasesActivity implements RadioGroup.OnCheckedChangeListener{

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

    private SecretMeFragment mSecretMeFragment;
    private MySecretFragment mMySecretFragment;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_look_dynamic;
    }

    @Override
    protected void initView() {
        appBar("隐私");
        rg.setOnCheckedChangeListener(this);
        rb_1.performClick();
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case R.id.rb_activities:
                if (null == mSecretMeFragment) {
                    mSecretMeFragment = SecretMeFragment.getInstance();
                    transaction.add(R.id.fl_layout, mSecretMeFragment);
                } else {
                    transaction.show(mSecretMeFragment);
                }
                rl_1.setVisibility(View.VISIBLE);
                rl_2.setVisibility(View.INVISIBLE);

                break;
            case R.id.rb_information:
                if (null == mMySecretFragment) {
                    mMySecretFragment = MySecretFragment.getInstance();
                    transaction.add(R.id.fl_layout, mMySecretFragment);
                } else {
                    transaction.show(mMySecretFragment);
                }
                rl_1.setVisibility(View.INVISIBLE);
                rl_2.setVisibility(View.VISIBLE);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (null != mSecretMeFragment) {
            transaction.hide(mSecretMeFragment);
        }

        if (null != mMySecretFragment) {
            transaction.hide(mMySecretFragment);
        }
    }
}
