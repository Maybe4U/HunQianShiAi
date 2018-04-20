package com.zykj.hunqianshiai.home.message.look;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

import butterknife.Bind;

public class LookActivity extends BasesActivity implements RadioGroup.OnCheckedChangeListener {


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

    private LookMeFragment mLookMeFragment;
    private MyLookFragment mMyLookFragment;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_look;
    }

    @Override
    protected void initView() {
        appBar("最近来访");
        rg.setOnCheckedChangeListener(this);
        rb_1.performClick();
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case R.id.rb_activities:
                if (null == mLookMeFragment) {
                    mLookMeFragment = LookMeFragment.getInstance();
                    transaction.add(R.id.fl_layout, mLookMeFragment);
                } else {
                    transaction.show(mLookMeFragment);
                }
                rl_1.setVisibility(View.VISIBLE);
                rl_2.setVisibility(View.INVISIBLE);

                break;
            case R.id.rb_information:
                if (null == mMyLookFragment) {
                    mMyLookFragment = MyLookFragment.getInstance();
                    transaction.add(R.id.fl_layout, mMyLookFragment);
                } else {
                    transaction.show(mMyLookFragment);
                }
                rl_1.setVisibility(View.INVISIBLE);
                rl_2.setVisibility(View.VISIBLE);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (null != mLookMeFragment) {
            transaction.hide(mLookMeFragment);
        }

        if (null != mMyLookFragment) {
            transaction.hide(mMyLookFragment);
        }
    }
}
