package com.zykj.hunqianshiai.home.message.look;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.message.MyLikeBean;

import java.util.List;

/**
 * Created by xu on 2017/12/26.
 */

public class MyLookAdapter extends BaseQuickAdapter<MyLikeBean.MyLikeData, BaseViewHolder> {
    public MyLookAdapter(@Nullable List<MyLikeBean.MyLikeData> data) {
        super(R.layout.my_look_item, data);
        //        for(int i=0;i<data.size();i++){
        //            String comeid = data.get(i).comeuserid;
        //            if(comeid.equals(UrlContent.USER_ID)){
        //                data.remove(i);
        //            }
        //        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MyLikeBean.MyLikeData item) {
        MyLikeBean.Info info = item.info;
        Glide.with(mContext)
                .load(info.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_headpic));

        String isonline = info.isonline;
        helper.setText(R.id.tv_isonline, isonline);
        if (isonline.equals("在线")) {
            helper.setTextColor(R.id.tv_isonline, mContext.getResources().getColor(R.color.default_color));
        } else {
            helper.setTextColor(R.id.tv_isonline, mContext.getResources().getColor(R.color.gray));
        }

        if(TextUtils.isEmpty(info.age))
        {
            helper.setText(R.id.tv_head_username, info.nickname);
        }else {
            helper.setText(R.id.tv_head_username, info.nickname + "  " + info.age + "岁");
        }

        helper.setText(R.id.tv_areaname, info.areaname);
        helper.setText(R.id.tv_yearmoney, info.yearmoney);
        helper.setText(R.id.tv_time, item.time);
        if(item.info.isauth.equals("已认证身份")){
            helper.setText(R.id.tv_comeuser,"您查看了"+item.info.username +"的个人资料");
        }else {
            helper.setText(R.id.tv_comeuser,"您查看了"+item.info.nickname +"的个人资料");
        }
    }
}
