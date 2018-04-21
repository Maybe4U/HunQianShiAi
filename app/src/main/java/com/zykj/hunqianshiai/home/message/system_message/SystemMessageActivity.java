package com.zykj.hunqianshiai.home.message.system_message;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhihu.matisse.internal.utils.UIUtils;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 系统通知
 */
public class SystemMessageActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.recycler_system)
    RecyclerView mRecyclerView;
    private SystemMessageAdapter mSystemMessageAdapter;

    private int mMeasuredHeight;
    private boolean isOpen = true;
    private List<SystemMessageBean.SystemMessageData> mData;
    private TextView mContent;
    private ImageView mArrow;
    private String mContentText;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_system_message;
    }

    @Override
    protected void initView() {
        appBar("系统通知");
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        View view = View.inflate(this,R.layout.system_message_imte,null);
        mContent = (TextView)view.findViewById(R.id.tv_content);
        mArrow = view.findViewById(R.id.iv_arrow);

        Bundle bundle = getIntent().getExtras();
        String userid = bundle.getString("userid");
        BasePresenterImpl presenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", userid);
        presenter.getData(UrlContent.SYSTEM_MESSAGE_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(mContent != null){
//            mContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//                    mMeasuredHeight = mContent.getMeasuredHeight();
//                    Log.e("测量的高度",mMeasuredHeight+"");
//                    //默认折叠
//                    //toggle(false);
//                    mContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }
//            });
//        }else {
//            Log.e("mContent","为空");
//        }
//    }

    @Override
    public void success(String bean) {
        SystemMessageBean systemMessageBean = JsonUtils.GsonToBean(bean, SystemMessageBean.class);
        mData = systemMessageBean.data;

        mSystemMessageAdapter = new SystemMessageAdapter(mData);
        mRecyclerView.setAdapter(mSystemMessageAdapter);

        mSystemMessageAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toastShow("点击");
                toggle(true);
            }
        });

        //布局完成的监听事件
        mContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mMeasuredHeight = mContent.getMeasuredHeight();
                Log.e("测量的高度",mMeasuredHeight+"");
                //默认折叠
                toggle(false);
                mContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
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



    private void toggle(boolean isAnimation) {
        int end = 0;
        int start = 0;
        mContentText = mContent.getText().toString();
        if(isOpen){
            /**
             mTvDes高度发生改变
             应有的高度-->7行的高度
             */
            start = mMeasuredHeight;
            end = getShortHeight(1,mContentText);
            if(isAnimation){
                doAnimation(start,end);
            }else {
                mContent.setHeight(end);
            }


        }else {

            start = getShortHeight(1,mContentText);
            end = mMeasuredHeight;
            if(isAnimation){
                doAnimation(start,end);
            }else {
                mContent.setHeight(end);
            }


        }
        if(isAnimation){
            if(isOpen){
                ObjectAnimator.ofFloat(mArrow,"rotation",180,0).start();
            }else {
                ObjectAnimator.ofFloat(mArrow,"rotation",0,180).start();
            }
        }
        isOpen = !isOpen;
    }
    private void doAnimation(int start, int end) {
        ObjectAnimator animator = ObjectAnimator.ofInt(mContent,"height",start,end);
        animator.start();

        animator.addListener(new Animator.AnimatorListener() {
            //动画结束
            //去找mTvDes的父类，递归向上查找，直到父类为空或者父类为ScrollView
            @Override
            public void onAnimationEnd(Animator animation) {
                ViewParent parent = mContent.getParent();
                while(true){
                    parent = parent.getParent();
                    if(parent == null){
                        break;
                    }
                    if(parent instanceof ScrollView){
                        ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);
                        break;
                    }
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
    private int getShortHeight(int line, String contents) {
        TextView tv = new TextView(getApplicationContext());
        tv.setLines(line);
        tv.setText(contents);

        tv.measure(0,0);
        int tvMeasuredHeight = tv.getMeasuredHeight();
        return tvMeasuredHeight;
    }
}
