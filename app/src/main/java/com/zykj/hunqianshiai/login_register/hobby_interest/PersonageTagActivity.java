package com.zykj.hunqianshiai.login_register.hobby_interest;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseBean;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 个人标签
 */
public class PersonageTagActivity extends BasesActivity implements BaseView<String> {


    @Bind(R.id.recycler_tag)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_right)
    TextView right;
    @Bind(R.id.rl_add_tag)
    RelativeLayout addTag;
    @Bind(R.id.tv_tag)
    TextView tag;

    private BasePresenterImpl mPresenter;
    private int mPosition;
    private PersonageTagBean mPersonageTagBean;
    private String tags = "";

    @Override
    protected int getContentViewX() {
        return R.layout.activity_tag;
    }

    @Override
    protected void initView() {
        appBar("个人标签");
        right.setText("确定");
        right.setVisibility(View.VISIBLE);
        mPosition = getIntent().getIntExtra("position", 0);
        if (mPosition == 0) {
            mPosition = 6;
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("id", mPosition);
        mPresenter.getData(UrlContent.TAG_ONE_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @OnClick({R.id.tv_right, R.id.rl_custom_tag})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_right: //添加标签完成

                mParams.clear();
                mParams.put("userid", UrlContent.USER_ID);
                mParams.put("ids", tags);
                mParams.put("classid",mPosition);
                mPresenter.getData(UrlContent.TAG_TWO_URL, mParams, BaseModel.REFRESH_TYPE);

                break;

            case R.id.rl_custom_tag: //添加自定义标签
                PopupWindowAddTag popupWindowAddTag = new PopupWindowAddTag(this, tag);
                popupWindowAddTag.showAtLocation(tag, Gravity.CENTER, 0, 0);
                popupWindowAddTag.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        String s = tag.getText().toString();
                        if (TextUtils.isEmpty(s)) {
                            tag.setVisibility(View.GONE);
                            addTag.setVisibility(View.VISIBLE);
                        } else {
                            tag.setVisibility(View.VISIBLE);
                            addTag.setVisibility(View.GONE);
                        }

                        mParams.clear();
                        mParams.put("userid", UrlContent.USER_ID);
                        mParams.put("ids", s);
                        mParams.put("classid",mPosition);
                        mPresenter.getData(UrlContent.TAG_THREE_URL, mParams, BaseModel.LOADING_MORE_TYPE);
                    }
                });
                break;
        }
    }

    @Override
    public void success(String bean) {
        mPersonageTagBean = JsonUtils.GsonToBean(bean, PersonageTagBean.class);
        List<PersonageTagBean.PersonageTag> data = mPersonageTagBean.data;


        final PersonageTagAdapter personageTagAdapter = new PersonageTagAdapter(data);
        mRecyclerView.setAdapter(personageTagAdapter);

        personageTagAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<PersonageTagBean.PersonageTag> data = adapter.getData();
                int selet = data.get(position).selet;
                if (selet == 0) {
                    data.get(position).selet = 1;

                } else {
                    data.get(position).selet = 0;
                }
                tags = "";
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).selet == 1) {

                        tags = tags + "," + data.get(i).interestclassid;
                    }

                }
//                toastShow(tags);
                personageTagAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void refresh(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        if (200 == baseBean.code) {
            Intent mIntent = new Intent();
            mIntent.putExtra("tag", mPersonageTagBean);
            mIntent.putExtra("Custom_tag", tag.getText().toString());
            setResult(200, mIntent);
            finish();
        } else {
            toastShow("设置失败");
        }
    }

    @Override
    public void loadMore(String bean) {
        BaseBean baseBean = JsonUtils.GsonToBean(bean, BaseBean.class);
        toastShow(baseBean.message);
    }

    @Override
    public void failed(String content) {

    }
}
