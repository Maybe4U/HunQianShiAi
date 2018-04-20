package com.zykj.hunqianshiai.look_pic_video;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

/**
 * 图片查看
 */
public class PicActivity extends BasesActivity {

    @Bind(R.id.view_pager)
    ViewPagerFixed mViewPager;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_pic;
    }

    @Override
    protected void initView() {
        List<String> images = (List<String>) getIntent().getSerializableExtra("images");
        int position = getIntent().getIntExtra("position",0);
        try {
            mViewPager.setAdapter(new SamplePagerAdapter(images));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mViewPager.setCurrentItem(position);
    }

    @OnClick({R.id.iv_back})
    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    private class SamplePagerAdapter extends PagerAdapter {

        private List<String> images;

        public SamplePagerAdapter(List<String> images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            Glide.with(PicActivity.this)
                    .load(UrlContent.PIC_URL + images.get(position))
                    .apply(mOptionsX)
                    .into(photoView);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

            return photoView;
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
