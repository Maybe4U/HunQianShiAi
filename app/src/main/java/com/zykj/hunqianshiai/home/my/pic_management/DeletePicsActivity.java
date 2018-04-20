package com.zykj.hunqianshiai.home.my.pic_management;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.look_pic_video.ViewPagerFixed;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.RxBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoView;

/**
 * 图片查看
 */
public class DeletePicsActivity extends BasesActivity {

    @Bind(R.id.view_pager)
    ViewPagerFixed mViewPager;
    @Bind(R.id.iv_delete_pic)
    ImageView iv_delete_pic;

    private int page;
    List<View> imageViews = new ArrayList<>();
    private MyPagerAdapter mAdapter;
    private int mChildCount;
    private ArrayList<String> mPics;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_pics;
    }

    @Override
    protected void initView() {

        iv_delete_pic.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        int position = bundle.getInt("position", 0);
        mPics = bundle.getStringArrayList("pics");
        RequestOptions options = new RequestOptions()
                .error(R.drawable.placeholder);
        for (int i = 0; i < mPics.size(); i++) {
            PhotoView imageView = new PhotoView(this);
            Glide.with(this)
                    .load(UrlContent.PIC_URL + mPics.get(i))
                    .apply(options)
                    .into(imageView);
            imageViews.add(imageView);
        }

        mAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(position);
        page = position;

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeAllViews();
            }
            container.addView(view);
            return imageViews.get(position);
        }

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_delete_pic})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_delete_pic:

                mParams.clear();
                mParams.put("rdm", UrlContent.RDM);
                mParams.put("sign", UrlContent.SIGN);
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("pics", mPics.get(page ));
                OkGo.<String>post(UrlContent.DELETE_PIC_URL)
                        .params(mParams)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                BaseBean baseBean = JsonUtils.GsonToBean(response.body(), BaseBean.class);
                                toastShow(baseBean.message);
                                if (baseBean.code == 200) {
                                    if (imageViews.size() <= 1) {
                                        finish();
                                    } else {
                                        mPics.remove(page);
                                        imageViews.remove(page);
                                        mAdapter.notifyDataSetChanged();
                                        mViewPager.setCurrentItem(page);

                                    }
                                }
                            }
                        });

                break;
        }
    }

    @Override
    public void finish() {
        RxBus.getInstance().post(new BaseBean());
        super.finish();
    }
}
