package com.zykj.hunqianshiai.home.my.info;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.luck.picture.lib.entity.LocalMedia;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.my.PicsBean;
import com.zykj.hunqianshiai.home.my.set.ShareActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.RxBus;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class InfoActivity extends BasesActivity {

    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.magic_indicator)
    MagicIndicator mMagicIndicator;
    @Bind(R.id.tv_title)
    TextView title;
    @Bind(R.id.tv_right)
    TextView tv_right;

    private PersonageInfoFragment mPersonageInfoFragment;
    private SpouseStandardsFragment mSpouseStandardsFragment;
    private HobbiesFragment mHobbiesFragment;
    private ArrayList<String> titleList;
    private List<LocalMedia> selectList;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_info;
    }

    @Override
    protected void initView() {
        appBar("个人资料");
        tv_right.setText("邀请好友");
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        titleList = new ArrayList<String>();// 每个页面的Title数据
        titleList.add("个人资料");
        titleList.add("择偶标准");
        titleList.add("兴趣爱好");

        mBundle = new Bundle();

        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.BLACK);
                colorTransitionPagerTitleView.setSelectedColor(getResources().getColor(R.color.default_color));
                colorTransitionPagerTitleView.setTextSize(15);
                colorTransitionPagerTitleView.setText(titleList.get(i));
                colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mViewPager.setCurrentItem(i);
                    }
                });
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.default_color));
                indicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
                return indicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return 1.0f;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    if (null == mPersonageInfoFragment) {
                        mPersonageInfoFragment = PersonageInfoFragment.getInstance();
                    }

                    return mPersonageInfoFragment;
                } else if (position == 1) {
                    if (null == mSpouseStandardsFragment) {
                        mSpouseStandardsFragment = SpouseStandardsFragment.getInstance();
                    }

                    return mSpouseStandardsFragment;
                } else {
                    if (null == mHobbiesFragment) {
                        mHobbiesFragment = HobbiesFragment.getInstance();
                    }

                    return mHobbiesFragment;
                }

            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        title.setText("个人资料");
                        break;
                    case 1:
                        title.setText("择偶标准");
                        break;
                    case 2:
                        title.setText("兴趣爱好");
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_right:
                mBundle.clear();
                mBundle.putString("title", "分享");
                mBundle.putString("url", "http://47.104.91.22/api.php?c=Fell&a=fenxiang&comeuserid=" + UrlContent.USER_ID + "&userid=" + UrlContent.USER_ID);
                openActivity(ShareActivity.class, mBundle);
                break;
        }
    }

    @Override
    public void finish() {
        RxBus.getInstance().post(new PicsBean());
        super.finish();
    }
}
