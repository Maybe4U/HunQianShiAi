package com.zykj.hunqianshiai.home.dynamic.my_dynamic;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zykj.hunqianshiai.look_pic_video.PicActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2017/12/26.
 */

public class MyDynamicAdapter extends BaseQuickAdapter<MyDynamicBean.MyDynamicData, BaseViewHolder> {

    private HttpParams mParams;

    public MyDynamicAdapter(@Nullable List<MyDynamicBean.MyDynamicData> data) {
        super(R.layout.my_dynamic_item, data);
        mParams = BasesActivity.mParams;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MyDynamicBean.MyDynamicData item) {
        helper.addOnClickListener(R.id.tv_private_comment).addOnClickListener(R.id.iv_more_option);
        String addtime3 = item.addtime3;
        String month = addtime3.substring(0, 3);
        String day = addtime3.substring(3, 5);
        helper.setText(R.id.tv_day, day);
        helper.setText(R.id.tv_month, month);
        helper.setText(R.id.tv_username, item.username);
        helper.setText(R.id.tv_content, item.content);
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
                    item.self_zan = "1";
                } else {
                    mParams.put("type_c", 1);
                    item.self_zan = "0";
                }
                OkGo.<String>post(UrlContent.IS_LIKE_URL)
                        .params(mParams)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                            }
                        });
            }
        });
//        helper.setOnCheckedChangeListener(R.id.check_like, new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//
//            }
//        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        RecyclerView recyclerPic = helper.getView(R.id.recycler_pic);
        recyclerPic.setLayoutManager(gridLayoutManager);

        List<String> img = item.img;
        PicAdapter picAdapter = new PicAdapter(img);
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

    }

    class PicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
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
