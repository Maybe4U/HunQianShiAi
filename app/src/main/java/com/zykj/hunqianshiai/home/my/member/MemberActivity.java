package com.zykj.hunqianshiai.home.my.member;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.login_register.UploadBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.pay.MemberPayActivity;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MemberActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.recycler_member)
    RecyclerView mRecyclerView;
    @Bind(R.id.recycler_vip)
    RecyclerView mRecyclerVip;
    @Bind(R.id.tv_date)
    TextView tv_date;
    @Bind(R.id.tv_zhe)
    TextView tv_zhe;

    int[] mImageViews = {R.mipmap.huiyuan_01, R.mipmap.huiyuan_02, R.mipmap.huiyuan_03, R.mipmap.huiyuan_04, R.mipmap.huiyuan_05, R.mipmap.hui_tixian, R.mipmap.huiyuan_07, R.mipmap.huiyuan_08};
    String[] mStrings = {"会员标识", "交换微信", "优先展示", "在线状态", "屏蔽功能", "现金提现", "无限畅聊", "优先推荐"};
    List<MemberBean> mList = new ArrayList<>();
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_member;
    }

    @Override
    protected void initView() {
        appBar("会员中心");

        mBundle = new Bundle();
        for (int i = 0; i < mImageViews.length; i++) {
            MemberBean memberBean = new MemberBean();
            memberBean.icon = mImageViews[i];
            memberBean.iconName = mStrings[i];
            mList.add(memberBean);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(layoutManager);
        MemberAdapter memberAdapter = new MemberAdapter(mList);
        mRecyclerView.setAdapter(memberAdapter);

        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mParams.put("type", 1);
        presenter.getData(UrlContent.GET_PRICE_URL, mParams, BaseModel.DEFAULT_TYPE);
        presenter.getData(UrlContent.IS_VIP_URL, mParams, BaseModel.REFRESH_TYPE);
    }

    @Override
    public void success(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {
            return;
        }
        MembersBean membersBean = JsonUtils.GsonToBean(bean, MembersBean.class);
        List<MembersBean.VipList> list = membersBean.data.list;
        String zhe = membersBean.data.zhe;

        String str = "用户突破百万，特推出<font color='#edbd5a'>" + zhe + "折</font>会员秒杀服务";
        tv_zhe.setText(Html.fromHtml(str));

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerVip.setLayoutManager(layoutManager);
        MembersAdapter membersAdapter = new MembersAdapter(zhe, list);
        mRecyclerVip.setAdapter(membersAdapter);
        membersAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mBundle.clear();
                List<MembersBean.VipList> data = adapter.getData();
                double zhe_price = data.get(position).zhe_price;
                mBundle.putString("days", data.get(position).days);
                if (zhe_price > 0) {
                    mBundle.putString("price", zhe_price + "");
                } else {
                    mBundle.putString("price", data.get(position).price);
                }
                mBundle.putString("id", data.get(position).id);
                openActivity(MemberPayActivity.class, mBundle);
            }
        });
    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (baseBean.code != 200) {
            tv_date.setVisibility(View.GONE);
            return;
        }
        VipBean vipBean = JsonUtils.GsonToBean(bean, VipBean.class);
        if (vipBean.data.state.equals("1")) {
            tv_date.setText("限时会员  " + "到期时间：" + vipBean.data.endtime);
        } else {
            tv_date.setText("到期时间：终身会员");
        }

    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }

    class MemberBean implements Serializable {
        public int icon;
        public String iconName;
    }

    class MemberAdapter extends BaseQuickAdapter<MemberBean, BaseViewHolder> {
        public MemberAdapter(@Nullable List<MemberBean> data) {
            super(R.layout.member_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MemberBean item) {
            helper.setImageResource(R.id.iv_icon, item.icon);
            helper.setText(R.id.tv_icon, item.iconName);
        }
    }
}
