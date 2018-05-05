package com.zykj.hunqianshiai.user_home.dynamic;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.dynamic.DynamicBean;
import com.zykj.hunqianshiai.look_pic_video.PicActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import java.io.Serializable;
import java.util.List;

/**
 * 动态条目
 */

public class UserDynamicAdapter extends BaseQuickAdapter<DynamicBean.DynamicData, BaseViewHolder> {

    private HttpParams mParams;

    public UserDynamicAdapter(@Nullable List<DynamicBean.DynamicData> data) {
        super(R.layout.user_dynamic_item, data);
        mParams = BasesActivity.mParams;
    }

    @Override
    protected void convert(BaseViewHolder helper, final DynamicBean.DynamicData item) {

        Glide.with(mContext)
                .load(item.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_user_heard));

        helper.setText(R.id.tv_username, item.username);
        helper.setText(R.id.tv_time, item.time_befor);
        helper.setText(R.id.tv_content, item.content);
        if(item.address == "" || item.address.equals("不显示位置")){
            helper.setGone(R.id.tv_sit,false);
        }else {
            helper.setVisible(R.id.tv_sit,true);
            helper.setText(R.id.tv_sit, item.address);
        }
        if (null != item.age && !TextUtils.isEmpty(item.age)) {
            helper.setText(R.id.tv_age, item.age + "岁");
        } else {
            helper.setText(R.id.tv_age, "0岁");
        }

        helper.setText(R.id.tv_height, item.height + "cm");
        helper.setText(R.id.tv_work_city, item.arename);

        final CheckBox check_like = helper.getView(R.id.check_like);
        if (item.self_zan.equals("0")) {
            helper.setChecked(R.id.check_like, false);

        } else {
            helper.setChecked(R.id.check_like, true);
        }

        helper.getView(R.id.check_like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("type", 2);
                mParams.put("other_id", item.id);
                mParams.put("rdm", UrlContent.RDM);
                mParams.put("sign", UrlContent.SIGN);
                if (check_like.isChecked()) {
                    mParams.put("type_c", 2);

                } else {
                    mParams.put("type_c", 1);
                }
//                T.showShort(mContext,check_like.isChecked()+"");
                OkGo.<String>post(UrlContent.IS_LIKE_URL)
                        .params(mParams)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                            }
                        });
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        RecyclerView recyclerPic = helper.getView(R.id.recycler_pic);
        recyclerPic.setLayoutManager(gridLayoutManager);

        PicAdapter picAdapter = new PicAdapter(item.img);
        recyclerPic.setAdapter(picAdapter);
        picAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, PicActivity.class);
                intent.putExtra("images", (Serializable) item.img);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });

        helper.addOnClickListener(R.id.iv_more_option).addOnClickListener(R.id.tv_private_comment).addOnClickListener(R.id.iv_user_heard);
    }


    public static class PicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public PicAdapter(@Nullable List<String> data) {
            super(R.layout.dynamic_pic, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            Glide.with(mContext)
                    .load(UrlContent.PIC_URL + item)
                    .apply(BasesActivity.mOptions)
                    .into((ImageView) helper.getView(R.id.iv_dynamic_pic));
        }
    }

}
