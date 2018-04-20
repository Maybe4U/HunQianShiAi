package com.zykj.hunqianshiai.activities.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 活动详情
 */
public class ActivitiesParticularsActivity extends BasesActivity {

    @Bind(R.id.iv_right_share)
    ImageView rightShare;
    @Bind(R.id.recycler_apply)
    RecyclerView mRecyclerView;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_activities_particulars;
    }

    @Override
    protected void initView() {
        appBar("活动详情");
        rightShare.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @OnClick({R.id.tv_apply})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_apply:
                openActivity(ActivitiesApplyActivity.class);
                break;
        }
    }
}
