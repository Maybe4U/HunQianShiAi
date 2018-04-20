package com.zykj.hunqianshiai.user_home;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.model.HttpParams;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePopupWindow;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.List;

/**
 * 礼物弹窗
 * Created by xu on 2017/12/22.
 */

public class PopupWindowGift extends BasePopupWindow implements BaseView<String> {


    private RecyclerView mRecycler_gift;
    private List<GiftBean.GiftData> mData;

    public PopupWindowGift(Activity activity, String balance) {
        super(activity);
        setAnimationStyle(R.style.popwin_anim_style);
        TextView cancel = view.findViewById(R.id.tv_cancel);
        TextView affirm = view.findViewById(R.id.tv_affirm);
//        mRose = view.findViewById(R.id.ll_rose);
//        mCake = view.findViewById(R.id.ll_cake);
//        mRing = view.findViewById(R.id.ll_ring);
//        mCar = view.findViewById(R.id.ll_car);
//        mBoat = view.findViewById(R.id.ll_boat);
//        mHose = view.findViewById(R.id.ll_hose);
        TextView tv_money = view.findViewById(R.id.tv_money);
        mRecycler_gift = view.findViewById(R.id.recycler_gift);
        tv_money.setText("余额：" + balance);
        cancel.setOnClickListener(this);
        affirm.setOnClickListener(this);
//        mRose.setOnClickListener(this);
//        mCake.setOnClickListener(this);
//        mRing.setOnClickListener(this);
//        mCar.setOnClickListener(this);
//        mBoat.setOnClickListener(this);
//        mHose.setOnClickListener(this);

        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        presenter.getData(UrlContent.GIFT_LIST_URL, new HttpParams(), BaseModel.DEFAULT_TYPE);

    }


    @Override
    public int getViewId() {
        return R.layout.popupwindow_gift;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_affirm:
                mClickListener.onClickListener(mData);
                break;
//            case R.id.ll_rose:
//                mRose.setBackgroundResource(R.color.default_color);
//                mCake.setBackgroundResource(R.color.white);
//                mRing.setBackgroundResource(R.color.white);
//                mCar.setBackgroundResource(R.color.white);
//                mBoat.setBackgroundResource(R.color.white);
//                mHose.setBackgroundResource(R.color.white);
//                break;
//            case R.id.ll_cake:
//                mRose.setBackgroundResource(R.color.white);
//                mCake.setBackgroundResource(R.color.default_color);
//                mRing.setBackgroundResource(R.color.white);
//                mCar.setBackgroundResource(R.color.white);
//                mBoat.setBackgroundResource(R.color.white);
//                mHose.setBackgroundResource(R.color.white);
//                break;
//            case R.id.ll_ring:
//                mRose.setBackgroundResource(R.color.white);
//                mCake.setBackgroundResource(R.color.white);
//                mRing.setBackgroundResource(R.color.default_color);
//                mCar.setBackgroundResource(R.color.white);
//                mBoat.setBackgroundResource(R.color.white);
//                mHose.setBackgroundResource(R.color.white);
//                break;
//            case R.id.ll_car:
//                mRose.setBackgroundResource(R.color.white);
//                mCake.setBackgroundResource(R.color.white);
//                mRing.setBackgroundResource(R.color.white);
//                mCar.setBackgroundResource(R.color.default_color);
//                mBoat.setBackgroundResource(R.color.white);
//                mHose.setBackgroundResource(R.color.white);
//                break;
//            case R.id.ll_boat:
//                mRose.setBackgroundResource(R.color.white);
//                mCake.setBackgroundResource(R.color.white);
//                mRing.setBackgroundResource(R.color.white);
//                mCar.setBackgroundResource(R.color.white);
//                mBoat.setBackgroundResource(R.color.default_color);
//                mHose.setBackgroundResource(R.color.white);
//                break;
//            case R.id.ll_hose:
//                mRose.setBackgroundResource(R.color.white);
//                mCake.setBackgroundResource(R.color.white);
//                mRing.setBackgroundResource(R.color.white);
//                mCar.setBackgroundResource(R.color.white);
//                mBoat.setBackgroundResource(R.color.white);
//                mHose.setBackgroundResource(R.color.default_color);
//                break;
        }
    }

    @Override
    public void success(String bean) {
        GiftBean giftBean = JsonUtils.GsonToBean(bean, GiftBean.class);
        mData = giftBean.data;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 3);
        mRecycler_gift.setLayoutManager(gridLayoutManager);
        final GiftAdapter giftAdapter = new GiftAdapter(mData);
        mRecycler_gift.setAdapter(giftAdapter);
        giftAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<GiftBean.GiftData> data1 = adapter.getData();
                for (int i = 0; i < data1.size(); i++) {

                    if (i == position) {
                        data1.get(i).select = 1;
                    } else {
                        data1.get(i).select = 0;
                    }
                }
                giftAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void refresh(String bean) {

    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }

    class GiftAdapter extends BaseQuickAdapter<GiftBean.GiftData, BaseViewHolder> {
        public GiftAdapter(@Nullable List<GiftBean.GiftData> data) {
            super(R.layout.item_gift, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, GiftBean.GiftData item) {
            if (item.select == 0) {
                helper.setBackgroundRes(R.id.ll_rose, R.color.white);

            } else {
                helper.setBackgroundRes(R.id.ll_rose, R.color.default_color);
            }

            Glide.with(mContext)
                    .load(UrlContent.PIC_URL + item.url)
                    .apply(BasesActivity.mOptions)
                    .into((ImageView) helper.getView(R.id.iv_gift));
            helper.setText(R.id.tv_name, item.name);
            helper.setText(R.id.tv_price, item.price + "元");
        }
    }

}
