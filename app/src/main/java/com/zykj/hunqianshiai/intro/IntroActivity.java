package com.zykj.hunqianshiai.intro;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.SPKey;
import com.zykj.hunqianshiai.tools.SPUtils;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 引导页
 */
public class IntroActivity extends BasesActivity {

    @Bind(R.id.vp_viewPager)
    ViewPager mViewPager;

    private int[] images = {R.mipmap.intro_1, R.mipmap.intro_2, R.mipmap.intro_3};
    private MyPagerAdapter mMyPagerAdapter;
    private ArrayList<ImageView> mImageViews;
    private boolean isLastPage = false;
    private boolean isDragPage = false;
    private boolean canJumpPage = true;
    private int currentItem;


    @Override
    protected int getContentViewX() {
        return R.layout.activity_intro;
    }


    @Override
    protected void initView() {

        mImageViews = new ArrayList<>();

        for (int i = 0; i < images.length; i++) {
            ImageView imageView = new ImageView(IntroActivity.this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(images[i]);
            mImageViews.add(imageView);
        }
        mMyPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mMyPagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;//获取位置，即第几页
                Log.i("Guide", "监听改变" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {
                isDragPage = arg0 == 1;
            }

            @Override
            public void onPageSelected(int arg0) {
                isLastPage = arg0 == mImageViews.size() - 1;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (isLastPage && isDragPage && arg2 == 0) {   //当前页是最后一页，并且是拖动状态，并且像素偏移量为0
                    if (canJumpPage) {
                        canJumpPage = false;
                        SPUtils.put(IntroActivity.this, SPKey.FIRST_LOGIN_KEY, false);
                        openActivity(ChooseLoginActivity.class);

                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);//这部分代码是切换Activity时的动画，看起来就不会很生硬
                        finish();
                    }
                }
            }
        });
    }

    class MyPagerAdapter extends android.support.v4.view.PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            container.addView(mImageViews.get(position));
            return mImageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews.get(position));
        }
    }

}
