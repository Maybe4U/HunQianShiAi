package com.zykj.hunqianshiai.look_pic_video;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

/**
 * 图片查看
 */
public class PicsActivity extends BasesActivity {

    @Bind(R.id.view_pager)
    ViewPagerFixed mViewPager;
    List<View> imageViews = new ArrayList<>();

    @Override
    protected int getContentViewX() {
        return R.layout.activity_pics;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position", 0);
        boolean head = bundle.getBoolean("head");
        ArrayList<String> pics = bundle.getStringArrayList("pics");
        RequestOptions options = new RequestOptions()
                .error(R.drawable.placeholder);
        for (int i = 0; i < pics.size(); i++) {
            PhotoView imageView = new PhotoView(this);
            if (i == 0 && head) {
                Glide.with(this)
                        .load(pics.get(i))
                        .apply(options)
                        .into(imageView);
            } else {
                Glide.with(this)
                        .load(UrlContent.PIC_URL + pics.get(i))
                        .apply(options)
                        .into(imageView);

            }
            imageViews.add(imageView);
        }
        mViewPager.setAdapter(new MyPagerAdapter());
        mViewPager.setCurrentItem(position);
    }

    public class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View view = imageViews.get(position);

            container.addView(view);
            return imageViews.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View view) {
        super.onClick(view);
    }
}
