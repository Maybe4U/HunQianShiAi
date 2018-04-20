package com.zykj.hunqianshiai.home.my;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;
import com.squareup.picasso.Picasso;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.authentication.AuthenticationActivity;
import com.zykj.hunqianshiai.home.my.identifacation_education.IdentificationEducationActivity;
import com.zykj.hunqianshiai.home.my.identifacation_house.IdentificationHouseActivity;
import com.zykj.hunqianshiai.home.my.identification_car.IdentificationCarActivity;
import com.zykj.hunqianshiai.home.my.info.InfoActivity;
import com.zykj.hunqianshiai.home.my.info.PersonageInfoBean;
import com.zykj.hunqianshiai.home.my.info.PopupWindowAddInfo;
import com.zykj.hunqianshiai.home.my.member.MemberActivity;
import com.zykj.hunqianshiai.home.my.my_gift.GiftActivity;
import com.zykj.hunqianshiai.home.my.online.OnlineActivity;
import com.zykj.hunqianshiai.home.my.pic_management.PicManagementActivity;
import com.zykj.hunqianshiai.home.my.set.SetActivity;
import com.zykj.hunqianshiai.home.my.set.ShareActivity;
import com.zykj.hunqianshiai.home.my.wallet.WalletActivity;
import com.zykj.hunqianshiai.look_pic_video.PicsActivity;
import com.zykj.hunqianshiai.look_pic_video.VideoActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;
import com.zykj.hunqianshiai.tools.RxBus;
import com.zykj.hunqianshiai.user_home.UserPageBean;
import com.zykj.hunqianshiai.user_home.personal_assistant.OpenCounselorActivity;
import com.zykj.hunqianshiai.user_home.personal_assistant.PersonalAssistantActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by xu on 2017/12/5.
 */

public class MyFragment extends BaseFragment implements BaseView<String> {

    @Bind(R.id.tv_username)
    TextView tv_username;
    @Bind(R.id.tv_id)
    TextView tv_id;
    @Bind(R.id.tv_like_number)
    TextView tv_like_number;
    @Bind(R.id.tv_marry)
    TextView tv_marry;
    @Bind(R.id.tv_meinfo)
    TextView tv_meinfo;
    @Bind(R.id.tv_work_plan)
    TextView tv_work_plan;
    @Bind(R.id.tv_feeling)
    TextView tv_feeling;
    @Bind(R.id.tv_education)
    TextView tv_education;
    @Bind(R.id.tv_car)
    TextView tv_car;
    @Bind(R.id.tv_house)
    TextView tv_house;
    @Bind(R.id.tv_family)
    TextView tv_family;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.tv_pic_management)
    TextView tv_pic_management;
    @Bind(R.id.tv_remind)
    TextView tv_remind;
    @Bind(R.id.tv_sfrz)
    TextView tv_sfrz;
    @Bind(R.id.tv_rz_name)
    TextView tv_rz_name;
    @Bind(R.id.tv_rz_number)
    TextView tv_rz_number;
    @Bind(R.id.tv_rz_time)
    TextView tv_rz_time;
    @Bind(R.id.tv_ma)
    TextView tv_ma;
    @Bind(R.id.tv_city)
    TextView tv_city;
    @Bind(R.id.tv_set_info)
    TextView tv_set_info;
    @Bind(R.id.iv_recommend)
    ImageView iv_recommend;

    private ArrayList<String> pics = new ArrayList<>();
    List<View> imageViews = new ArrayList<>();

    private BasePresenterImpl mPresenter;
    private PopupWindowAddInfo mPopupWindowAddInfo;
    private View mView;
    private Bundle mBundle;
    private String mVideo;
    private String mHeadpic;
    private MyPagerAdapter mAdapter;
    private int page;
    private int mCode;
    private boolean head;
    private Subscription mSubscription;
    private String mMa;
    private String mMeinfo;
    private String mFamilyinfo;
    private String mWorkplan;
    private String mFeeling;

    public MyFragment() {
    }

    public static MyFragment getInstance() {
        return Instance.mMyFragment;
    }

    private static class Instance {
        static MyFragment mMyFragment = new MyFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_my;
    }

    @Override
    public void initView() {
        mBundle = new Bundle();
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.GET_INFO_URL, mParams, BaseModel.DEFAULT_TYPE);

        mSubscription = RxBus.getInstance().toObserverable(PicsBean.class)
                .subscribe(new Action1<PicsBean>() {
                    @Override
                    public void call(PicsBean picsBean) {
                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mPresenter.getData(UrlContent.GET_INFO_URL, mParams, BaseModel.DEFAULT_TYPE);
                    }
                });
    }

    @Override
    public void success(String bean) {
        PersonageInfoBean personageInfoBean = JsonUtils.GsonToBean(bean, PersonageInfoBean.class);
        PersonageInfoBean.PersonageInfoData data = personageInfoBean.data;
        tv_username.setText(data.username);
        tv_id.setText("ID : " + data.userid);
        tv_like_number.setText(data.like_num + "");

        String areaname = data.areaname;
        areaname = areaname.replace("省", "");
        areaname = areaname.replace("市", "");
        tv_city.setText(areaname);
        if (!TextUtils.isEmpty(data.marrytime)) {
            tv_marry.setText(" | 期望" + data.marrytime + "结婚");
        }

        if (data.is_vip == 1) {
            iv_recommend.setVisibility(View.VISIBLE);

        } else {
            iv_recommend.setVisibility(View.GONE);
        }

        mMeinfo = data.meinfo;
        tv_meinfo.setText(mMeinfo);
        mWorkplan = data.workplan;
        tv_work_plan.setText(mWorkplan);
        mFeeling = data.feeling;
        tv_feeling.setText(mFeeling);
        tv_education.setText(data.renzheng.xueli_auth);
        tv_car.setText(data.renzheng.car_auth);
        tv_house.setText(data.renzheng.huose_auth);
        mFamilyinfo = data.familyinfo;
        tv_family.setText(mFamilyinfo);
        tv_set_info.setText("资料完善度" + data.Integrity);

        mMa = data.ma;
        tv_ma.setText("推广码： " + mMa);

        UserPageBean.Auth auth = data.auth;
        mCode = auth.code;
        if (mCode == 0) {
            tv_remind.setText("未开通");
        } else if (auth.code == 2) {
            tv_remind.setText("已经到期");
        } else {
            tv_remind.setText("已开通");
        }
        imageViews.clear();
        pics.clear();

        mVideo = data.video;
        if (!TextUtils.isEmpty(mVideo)) {
            mView = View.inflate(mContext, R.layout.video_pic, null);
            ImageView video = mView.findViewById(R.id.iv_video);
            Glide.with(mContext)
                    .load(UrlContent.PIC_URL + data.video)
                    .apply(BasesActivity.mOptions)
                    .into(video);
            imageViews.add(mView);
            if (null != mAdapter) {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            mView = null;
        }

        mHeadpic = data.headpic;
        if (null != mHeadpic && !TextUtils.isEmpty(mHeadpic)) {
            pics.add(mHeadpic);
            head = true;
        } else {
            head = false;
        }

        if (null != mAdapter) {
            mAdapter.notifyDataSetChanged();
        }

        PersonageInfoBean.RenZheng renzheng = data.renzheng;

        tv_sfrz.setText(renzheng.shenfen_auth);

        String realname = renzheng.realname;
        if (!TextUtils.isEmpty(realname)) {
            String substring = realname.substring(0, 1);
            substring = substring + "**";
            tv_rz_name.setText(substring);
        }

        String card_number = renzheng.card_number;
        if (!TextUtils.isEmpty(card_number)) {
            String substring = card_number.substring(0, 10);
            substring = substring + "*******";
            tv_rz_number.setText(substring);
        }

        if (!TextUtils.isEmpty(renzheng.verifytime)) {
            tv_rz_time.setText("认证通过时间：" + renzheng.verifytime);
        }

        RongIM.getInstance().setCurrentUserInfo(new UserInfo(data.userid, data.username, Uri.parse(data.headpic)));

        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.GET_PIC_URL, mParams, BaseModel.REFRESH_TYPE);
    }

    @Override
    public void refresh(String bean) {
        PicsBean picsBean = JsonUtils.GsonToBean(bean, PicsBean.class);
        if (picsBean.code == 200) {
            pics.addAll(picsBean.data);
        }

        for (int i = 0; i < pics.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            if (i == 0 && head) {

                Glide.with(mContext)
                        .load(pics.get(i))
                        .apply(BasesActivity.mOptions)
                        .into(imageView);
                imageViews.add(imageView);
            } else {
                Glide.with(mContext)
                        .load(UrlContent.PIC_URL + pics.get(i))
                        .apply(BasesActivity.mOptions)
                        .into(imageView);
                imageViews.add(imageView);
            }

//            if (null != mAdapter) {
//                mAdapter.notifyDataSetChanged();
//            }
        }
        if (null == mAdapter) {
            mAdapter = new MyPagerAdapter();
            mViewPager.setAdapter(mAdapter);
        } else {
            mAdapter = null;
            mAdapter = new MyPagerAdapter();
            mViewPager.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
        }

        final int size = imageViews.size();

        tv_pic_management.setText("管理照片 " + 1 + "/" + size);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position + 1;
                tv_pic_management.setText("管理照片 " + position + "/" + size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(page);
    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }

    @OnClick({R.id.iv_identification_car, R.id.iv_identification_house, R.id.tv_pic_management, R.id.iv_identification_education, R.id.ll_member,
            R.id.rl_gift, R.id.tv_set_info, R.id.ll_layout_1, R.id.ll_layout_2, R.id.ll_layout_3, R.id.ll_layout_4, R.id.ll_counselor, R.id.ll_steward,
            R.id.rl_remind, R.id.iv_set, R.id.ll_wallet, R.id.tv_share, R.id.ll_sfz})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_sfz:
                openActivity(AuthenticationActivity.class);
                break;
            case R.id.tv_share:
                mBundle.clear();
                mBundle.putString("title", "分享");
                mBundle.putString("url", "http://47.104.91.22/api.php?c=Fell&a=fenxiang&comeuserid=" + mMa + "&userid=" + UrlContent.USER_ID);
                openActivity(ShareActivity.class, mBundle);
                break;
            case R.id.iv_identification_car://车辆认证
                openActivity(IdentificationCarActivity.class);
                break;
            case R.id.iv_identification_house://房屋认证
                openActivity(IdentificationHouseActivity.class);
                break;
            case R.id.tv_pic_management://照片管理
                page = mViewPager.getCurrentItem();
                mBundle.clear();
                mBundle.putString("headpic", mHeadpic);
                if (!TextUtils.isEmpty(mVideo)) {
                    mBundle.putString("video", mVideo);
                }
                openActivity(PicManagementActivity.class, mBundle);
                break;
            case R.id.iv_identification_education://学历认证
                openActivity(IdentificationEducationActivity.class);
                break;
            case R.id.ll_member://会员
                openActivity(MemberActivity.class);
                break;
            case R.id.rl_gift://我的礼物
                openActivity(GiftActivity.class);
                break;
            case R.id.tv_set_info://编辑资料
                page = mViewPager.getCurrentItem();
                openActivity(InfoActivity.class);
                break;
            case R.id.ll_layout_1://自我介绍
                mBundle.clear();
                mBundle.putString("content", mMeinfo);
                mBundle.putString("title", "自我介绍");
                mBundle.putString("key", "meinfo");
                openActivity(MyInfoActivity.class, mBundle);
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_meinfo, "");
//                mPopupWindowAddInfo.showAtLocation(tv_meinfo, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("meinfo", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });
                break;
            case R.id.ll_layout_2://家庭状况
                mBundle.clear();
                mBundle.putString("content", mFamilyinfo);
                mBundle.putString("title", "家庭状况");
                mBundle.putString("key", "familyinfo");
                openActivity(MyInfoActivity.class, mBundle);
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_family, "");
//                mPopupWindowAddInfo.showAtLocation(tv_meinfo, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("familyinfo", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });
                break;
            case R.id.ll_layout_3://职业规划
                mBundle.clear();
                mBundle.putString("content", mWorkplan);
                mBundle.putString("title", "职业规划");
                mBundle.putString("key", "workplan");
                openActivity(MyInfoActivity.class, mBundle);
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_work_plan, "");
//                mPopupWindowAddInfo.showAtLocation(tv_meinfo, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("workplan", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });
                break;
            case R.id.ll_layout_4://感情经历
                mBundle.clear();
                mBundle.putString("content", mFeeling);
                mBundle.putString("title", "感情经历");
                mBundle.putString("key", "feeling");
                openActivity(MyInfoActivity.class, mBundle);
//                mPopupWindowAddInfo = new PopupWindowAddInfo(mActivity, tv_feeling, "");
//                mPopupWindowAddInfo.showAtLocation(tv_meinfo, Gravity.CENTER, 0, 0);
//                mPopupWindowAddInfo.setClickListener(new BasePopupWindow.ClickListener() {
//                    @Override
//                    public void onClickListener(Object object) {
//                        mParams.clear();
//                        mParams.put("userid", UrlContent.USER_ID);
//                        mParams.put("feeling", object.toString());
//                        mPresenter.getData(UrlContent.SET_INFO_URL, mParams, BaseModel.REFRESH_TYPE);
//                    }
//                });
                break;
            case R.id.ll_counselor://私人助理
                openActivity(ZhuLiActivity.class);
//                mBundle.clear();
//                mBundle.putString("title", "开通婚恋助理");
//                mBundle.putString("type1", "2");
//                mBundle.putString("type2", "1");
//                mBundle.putString("type3", "5");
//                openActivity(OpenCounselorActivity.class, mBundle);
                break;
            case R.id.ll_steward://婚恋管家
                openActivity(GuanjiaActivity.class);
//                mBundle.clear();
//                mBundle.putString("title", "开通婚恋管家");
//                mBundle.putString("type1", "3");
//                mBundle.putString("type2", "2");
//                mBundle.putString("type3", "6");
//                openActivity(OpenCounselorActivity.class, mBundle);
                break;
            case R.id.rl_remind://上线提醒
                if (mCode == 0) {
                    PopupWindowRemind popupWindowRemind = new PopupWindowRemind(mActivity);
                    popupWindowRemind.showAtLocation(mViewPager, Gravity.CENTER, 0, 0);
                } else {
                    openActivity(OnlineActivity.class);
                }

                break;
            case R.id.iv_set://设置
                mBundle.clear();
                mBundle.putString("ma", mMa);
                openActivity(SetActivity.class, mBundle);
                break;
            case R.id.ll_wallet:
                openActivity(WalletActivity.class);
                break;
            default:
                break;
        }
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
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBundle.clear();
                    mBundle.putStringArrayList("pics", pics);
                    mBundle.putBoolean("head", head);
                    if (null == mView) {
                        mBundle.putInt("position", position);

                    } else {
                        mBundle.putInt("position", position - 1);
                        if (position == 0) {
                            mBundle.putString("video", mVideo);
                            openActivity(VideoActivity.class, mBundle);
                            return;
                        }
                    }
                    openActivity(PicsActivity.class, mBundle);
                }
            });

            container.addView(view);
            return imageViews.get(position);
        }

        private int mChildCount = 0;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mSubscription && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
