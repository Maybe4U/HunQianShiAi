package com.zykj.hunqianshiai.activities;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.activities.activity.ActivitiesFragment;
import com.zykj.hunqianshiai.activities.information.InformationFragment;
import com.zykj.hunqianshiai.bases.BasesActivity;

import butterknife.Bind;

public class ActivitiesActivity extends BasesActivity implements RadioGroup.OnCheckedChangeListener {

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

    private ActivitiesFragment mActivitiesFragment;
    private InformationFragment mInformationFragment;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_activities;
    }

    @Override
    protected void initView() {
        appBar("活动");
        rg.setOnCheckedChangeListener(this);
        rb_1.performClick();
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case R.id.rb_activities:
                if (null == mActivitiesFragment) {
                    mActivitiesFragment = ActivitiesFragment.getInstance();
                    transaction.add(R.id.fl_layout, mActivitiesFragment);
                } else {
                    transaction.show(mActivitiesFragment);
                }
                rl_1.setVisibility(View.VISIBLE);
                rl_2.setVisibility(View.INVISIBLE);

                break;
            case R.id.rb_information:
                if (null == mInformationFragment) {
                    mInformationFragment = InformationFragment.getInstance();
                    transaction.add(R.id.fl_layout, mInformationFragment);
                } else {
                    transaction.show(mInformationFragment);
                }
                rl_1.setVisibility(View.INVISIBLE);
                rl_2.setVisibility(View.VISIBLE);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (null != mActivitiesFragment) {
            transaction.hide(mActivitiesFragment);
        }

        if (null != mInformationFragment) {
            transaction.hide(mInformationFragment);
        }
    }
}
