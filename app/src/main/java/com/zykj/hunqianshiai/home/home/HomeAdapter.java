package com.zykj.hunqianshiai.home.home;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.login_register.login.LoginActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import java.util.List;

/**
 * Created by xu on 2017/12/13.
 */

public class HomeAdapter extends BaseQuickAdapter<HomeBean.HomeData, BaseViewHolder> {

    private HttpParams mParams;

    public HomeAdapter(@Nullable List<HomeBean.HomeData> data) {
        super(R.layout.home_item, data);
        mParams = BasesActivity.mParams;
    }

    @Override
    protected void convert(BaseViewHolder helper, final HomeBean.HomeData item) {
        helper.addOnClickListener(R.id.iv_recommend).addOnClickListener(R.id.ll_chat).addOnClickListener(R.id.ll_dynamic).addOnClickListener(R.id.ll_heartbeat);
        helper.setText(R.id.tv_identification, item.userauth);
        Glide.with(mContext)
                .load(item.headpic)
                .apply(BasesActivity.mOptions)
                .into((ImageView) helper.getView(R.id.iv_user_pic));
        helper.setChecked(R.id.check_heartbeat, item.isliker);
        helper.setText(R.id.tv_username, item.username);
        helper.setText(R.id.tv_user_wish, item.age + "岁");
        helper.setText(R.id.tv_user_sit, item.areaname);
        if (!TextUtils.isEmpty(item.age)) {
            helper.setVisible(R.id.tv_age, true);
            helper.setText(R.id.tv_age, item.age + "岁");
        } else {
            helper.setGone(R.id.tv_age, false);
        }

        if (!TextUtils.isEmpty(item.height)) {
            helper.setVisible(R.id.tv_stature, true);
            helper.setText(R.id.tv_stature, item.height + "cm");
        } else {
            helper.setGone(R.id.tv_stature, false);
        }

        if (!TextUtils.isEmpty(item.constellation)) {
            helper.setVisible(R.id.tv_constellation, true);
            helper.setText(R.id.tv_constellation, item.constellation);
        } else {
            helper.setGone(R.id.tv_constellation, false);
        }

        if (!TextUtils.isEmpty(item.yearmoney)) {
            helper.setVisible(R.id.tv_salary, true);

            helper.setText(R.id.tv_salary, item.yearmoney);
        } else {
            helper.setGone(R.id.tv_salary, false);
        }

        helper.setText(R.id.tv_user_description, item.meinfo);
        helper.setText(R.id.tv_on_line, item.isonline);

        final CheckBox check_heartbeat = helper.getView(R.id.check_heartbeat);
        helper.setVisible(R.id.iv_recommend, item.isvip);

        helper.setChecked(R.id.check_heartbeat, item.isliker);

        helper.getView(R.id.ll_heartbeat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UrlContent.GOAGO) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
//                    openActivity(LoginActivity.class);
                    return;
                }
//                mParams.clear();
//                mParams.put("userid", UrlContent.USER_ID);
//                mParams.put("uid", item.userid);
//                mParams.put("rdm", UrlContent.RDM);
//                mParams.put("sign", UrlContent.SIGN);
//                OkGo.<String>post(UrlContent.IS_LIKER_URL)
//                        .params(mParams)
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onSuccess(Response<String> response) {
//                                if(data)
//                                check_heartbeat.setChecked(true);
//                            }
//                        });

                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("uid", item.userid);
                mParams.put("rdm", UrlContent.RDM);
                mParams.put("sign", UrlContent.SIGN);
                if (!check_heartbeat.isChecked()) {

                    OkGo.<String>post(UrlContent.ADD_LIKE_URL)
                            .params(mParams)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    Toast.makeText(mContext,"心动成功",Toast.LENGTH_SHORT).show();
                                    check_heartbeat.setChecked(true);
                                }

                                @Override
                                public void onError(Response<String> response) {
                                    Toast.makeText(mContext,response.message(),Toast.LENGTH_SHORT).show();
                                    super.onError(response);
                                }
                            });
                } else {

                    OkGo.<String>post(UrlContent.DELETE_LIKE_URL)
                            .params(mParams)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {
                                    Toast.makeText(mContext,"取消心动",Toast.LENGTH_SHORT).show();
                                    check_heartbeat.setChecked(false);
                                }
                            });
                }
            }
        });
//        helper.setOnCheckedChangeListener(R.id.check_heartbeat, new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                mParams.clear();
//                mParams.put("userid", UrlContent.USER_ID);
//                mParams.put("uid", item.userid);
//                mParams.put("rdm", UrlContent.RDM);
//                mParams.put("sign", UrlContent.SIGN);
//                if (b) {
//
//                    OkGo.<String>post(UrlContent.ADD_LIKE_URL)
//                            .params(mParams)
//                            .execute(new StringCallback() {
//                                @Override
//                                public void onSuccess(Response<String> response) {
//
//                                }
//                            });
//                } else {
//                    OkGo.<String>post(UrlContent.DELETE_LIKE_URL)
//                            .params(mParams)
//                            .execute(new StringCallback() {
//                                @Override
//                                public void onSuccess(Response<String> response) {
//
//                                }
//                            });
//                }
//            }
//        });
    }


    //通过position获取当前view
    @Nullable
    @Override
    public View getViewByPosition(int position, int viewId) {
        return super.getViewByPosition(position, viewId);
    }
}
