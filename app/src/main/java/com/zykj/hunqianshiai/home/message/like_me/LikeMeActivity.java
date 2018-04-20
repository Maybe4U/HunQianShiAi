package com.zykj.hunqianshiai.home.message.like_me;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

import butterknife.Bind;

/**
 * 心动
 */
public class LikeMeActivity extends BasesActivity implements RadioGroup.OnCheckedChangeListener{

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

    private LikeMeFragment mLikeMeFragment;
    private MyLikeFragment mMyLikeFragment;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_like_me;
    }

    @Override
    protected void initView() {
        appBar("心动");
        rg.setOnCheckedChangeListener(this);
        rb_1.performClick();
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case R.id.rb_activities:
                if (null == mLikeMeFragment) {
                    mLikeMeFragment = LikeMeFragment.getInstance();
                    transaction.add(R.id.fl_layout, mLikeMeFragment);
                } else {
                    transaction.show(mLikeMeFragment);
                }
                rl_1.setVisibility(View.VISIBLE);
                rl_2.setVisibility(View.INVISIBLE);

                break;
            case R.id.rb_information:
                if (null == mMyLikeFragment) {
                    mMyLikeFragment = MyLikeFragment.getInstance();
                    transaction.add(R.id.fl_layout, mMyLikeFragment);
                } else {
                    transaction.show(mMyLikeFragment);
                }
                rl_1.setVisibility(View.INVISIBLE);
                rl_2.setVisibility(View.VISIBLE);
                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (null != mLikeMeFragment) {
            transaction.hide(mLikeMeFragment);
        }

        if (null != mMyLikeFragment) {
            transaction.hide(mMyLikeFragment);
        }
    }
}
