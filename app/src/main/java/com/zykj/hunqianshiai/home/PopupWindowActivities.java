package com.zykj.hunqianshiai.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.activities.activity.ActivitiesBean;
import com.zykj.hunqianshiai.activities.activity.ActivitiesDetailActivity;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 海报
 * Created by xu on 2018/2/3.
 */

public class PopupWindowActivities extends BasePopupWindow {

    ArrayList<ImageView> imageViews = new ArrayList<>();
    private Bundle mBundle;

    protected PopupWindowActivities(List<ActivitiesBean.ActivitiesData> data1, Activity activity) {
        super(activity, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dismissPop();
        mBundle = new Bundle();
        for (int i = 0; i < data1.size(); i++) {
            ImageView imageView = new ImageView(mActivity);

            final ActivitiesBean.ActivitiesData activitiesData = data1.get(i);
            Glide.with(mActivity)
                    .load(UrlContent.PIC_URL + activitiesData.thumb2)
                    .apply(BasesActivity.mOptionsX)
                    .into(imageView);
            imageViews.add(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBundle.clear();
                    mBundle.putString("title", "活动详情");
                    mBundle.putString("actid", activitiesData.actid);
                    mBundle.putString("cost", activitiesData.cost);
                    mBundle.putString("state", activitiesData.state);
                    mBundle.putString("url", UrlContent.PIC_URL + "api.php?c=Fell&a=huodong&actid=" + activitiesData.actid + "&userid=" + UrlContent.USER_ID);
                    Intent intent = new Intent(mActivity, ActivitiesDetailActivity.class);
                    intent.putExtras(mBundle);
                    mActivity.startActivity(intent);
//                    openActivity(ActivitiesDetailActivity.class, mBundle);
                }
            });
        }
        ViewPager view_pager = view.findViewById(R.id.view_pager);
        view_pager.setAdapter(new PosterAdapter());
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_activities;
    }

    class PosterAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            // Now just add PhotoView to ViewPager and return it
            container.addView(imageViews.get(position), ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }
}
