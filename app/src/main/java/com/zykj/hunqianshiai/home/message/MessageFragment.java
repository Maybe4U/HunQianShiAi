package com.zykj.hunqianshiai.home.message;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.home.message.like_me.LikeMeActivity;
import com.zykj.hunqianshiai.home.message.look.LookActivity;
import com.zykj.hunqianshiai.home.message.people_nearby.PeopleNearbyActivity;
import com.zykj.hunqianshiai.home.message.system_message.SystemMessageActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by xu on 2017/12/5.
 */

public class MessageFragment extends BaseFragment {

    public MessageFragment() {
    }

    public static MessageFragment getInstance() {
        return Instance.mMessageFragment;
    }

    private static class Instance {
        static MessageFragment mMessageFragment = new MessageFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView() {

//        View headView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_message_header, null);
        LinearLayout layout1 = mView.findViewById(R.id.ll_layout1);
        LinearLayout layout2 = mView.findViewById(R.id.ll_layout2);
        LinearLayout layout3 = mView.findViewById(R.id.ll_layout3);
        LinearLayout layout4 = mView.findViewById(R.id.ll_layout4);
        layout1.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);


        ConversationListFragment fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + mContext.getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话，该会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话，该会话非聚合显示
                .build();
        fragment.setUri(uri);  //设置 ConverssationListFragment 的显示属性

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.conversationlist, fragment);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_layout1://心动
                openActivity(LikeMeActivity.class);
                break;
            case R.id.ll_layout2://最近来访
                openActivity(LookActivity.class);
                break;
            case R.id.ll_layout3://附近
                openActivity(PeopleNearbyActivity.class);
                break;
            case R.id.ll_layout4://系统
                Bundle bundle = new Bundle();
                bundle.putString("userid", UrlContent.USER_ID);
                openActivity(SystemMessageActivity.class, bundle);
                break;
            default:
                break;
        }
    }
}
