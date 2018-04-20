package com.zykj.hunqianshiai.login_register.hobby_interest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.home.HomeMainActivity;
import com.zykj.hunqianshiai.net.SPKey;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 兴趣爱好
 */
public class HobbyInterestActivity extends BasesActivity {

    @Bind(R.id.recycler_hobby)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_right)
    TextView right;

    int[] images = {R.mipmap.biaoqian, R.mipmap.dianying, R.mipmap.yinyue, R.mipmap.shuji, R.mipmap.meishi, R.mipmap.yundong};
    String[] hobbies = {"个人标签", "喜欢的电影", "喜欢的音乐", "喜欢的书籍", "喜欢的美食", "喜欢的运动"};
    private HobbyInterestBean mHobbyInterestBean;
    private HobbyInterestAdapter mHobbyInterestAdapter;
    private List<HobbyInterestBean.Label> mLabel;
    private Bundle mBundle;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_hobby_interest;
    }

    @Override
    protected void initView() {
        appBar("兴趣爱好");

        right.setText("确定");
        right.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mHobbyInterestBean = new HobbyInterestBean();
        mHobbyInterestBean.label = new ArrayList<HobbyInterestBean.Label>();
        for (int i = 0; i < images.length; i++) {
            HobbyInterestBean.Label label = new HobbyInterestBean.Label();
            label.image = images[i];
            label.title = hobbies[i];
            label.tag = new ArrayList<String>();
            mHobbyInterestBean.label.add(label);
        }
        mLabel = mHobbyInterestBean.label;
        mHobbyInterestAdapter = new HobbyInterestAdapter(mLabel);
        mRecyclerView.setAdapter(mHobbyInterestAdapter);

        mBundle = new Bundle();
        mHobbyInterestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mLabel.get(position).tag.clear();
                Intent intent = new Intent(HobbyInterestActivity.this, PersonageTagActivity.class);
                intent.putExtra("position",position);
                startActivityForResult(intent, position);
            }
        });
    }

    @OnClick({R.id.tv_right})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_right:
                UrlContent.GOAGO = false;
                destroyApp();
                openActivity(HomeMainActivity.class);
                SPUtils.put(this, SPKey.COMPLETE_LOGIN_KEY,true);

                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            List<String> tag = mHobbyInterestBean.label.get(requestCode).tag;

            String custom_tag = data.getStringExtra("Custom_tag");
            if (!TextUtils.isEmpty(custom_tag)) {
                String replace = custom_tag.replace(",", "，");
                String[] split = replace.split("，");
                tag.addAll(Arrays.asList(split));
            }
            PersonageTagBean mPersonageTagBean = (PersonageTagBean) data.getSerializableExtra("tag");
            if (null!=mPersonageTagBean) {
                List<PersonageTagBean.PersonageTag> tag1 = mPersonageTagBean.data;
                if (null!=tag1&&!tag1.isEmpty()) {
                    for (int i = 0; i < tag1.size(); i++) {
                        if (tag1.get(i).selet == 1) {
                            tag.add(tag1.get(i).interestclassname);
                        }
                    }
                }
            }
            mHobbyInterestAdapter.notifyDataSetChanged();
        }
    }
}
