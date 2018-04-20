package com.zykj.hunqianshiai.home.my.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.login_register.hobby_interest.HobbyInterestAdapter;
import com.zykj.hunqianshiai.login_register.hobby_interest.HobbyInterestBean;
import com.zykj.hunqianshiai.login_register.hobby_interest.PersonageTagActivity;
import com.zykj.hunqianshiai.login_register.hobby_interest.PersonageTagBean;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * 兴趣爱好
 * Created by xu on 2018/1/5.
 */

public class HobbiesFragment extends BaseFragment implements BaseView<String> {

    @Bind(R.id.recycler_hobby)
    RecyclerView mRecyclerView;

    private BasePresenterImpl mPresenter;
    String[] hobbies = {"个人标签", "喜欢的电影", "喜欢的音乐", "喜欢的书籍", "喜欢的美食", "喜欢的运动"};
    int[] images = {R.mipmap.biaoqian, R.mipmap.dianying, R.mipmap.yinyue, R.mipmap.shuji, R.mipmap.meishi, R.mipmap.yundong};
    private HobbyInterestBean mHobbyInterestBean;
    private List<HobbyInterestBean.Label> mLabel;
    private HobbyInterestAdapter mHobbyInterestAdapter;
    private Bundle mBundle;

    public HobbiesFragment() {
    }

    public static HobbiesFragment getInstance() {
        return HobbiesFragment.Instance.mHobbiesFragment;
    }


    private static class Instance {
        static HobbiesFragment mHobbiesFragment = new HobbiesFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_hobbies;
    }

    @Override
    public void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams.clear();
        mParams.put("userid", UrlContent.USER_ID);
        mPresenter.getData(UrlContent.ALL_TAG_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {
        HobbiesBean hobbiesBean = JsonUtils.GsonToBean(bean, HobbiesBean.class);
        HobbiesBean.HobbiesData data = hobbiesBean.data;

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

        List<HobbiesBean.Biaoqian> biaoqian = data.biaoqian;
        if (null != biaoqian && !biaoqian.isEmpty()) {
            List<String> tag1 = mLabel.get(0).tag;
            for (int i = 0; i < biaoqian.size(); i++) {
                if (!TextUtils.isEmpty(biaoqian.get(i).interestclassname)) {
                    tag1.add(biaoqian.get(i).interestclassname);
                }
            }
        }

        List<HobbiesBean.Biaoqian> video = data.video;
        if (null != video && !video.isEmpty()) {
            List<String> tag2 = mLabel.get(1).tag;
            for (int i = 0; i < video.size(); i++) {
                if (!TextUtils.isEmpty(video.get(i).interestclassname)) {
                    tag2.add(video.get(i).interestclassname);
                }
            }
        }

        List<HobbiesBean.Biaoqian> music = data.music;
        if (null != music && !music.isEmpty()) {
            List<String> tag3 = mLabel.get(2).tag;
            for (int i = 0; i < music.size(); i++) {
                if (!TextUtils.isEmpty(music.get(i).interestclassname)) {
                    tag3.add(music.get(i).interestclassname);
                }
            }
        }

        List<HobbiesBean.Biaoqian> book = data.book;
        if (null != book && !book.isEmpty()) {
            List<String> tag4 = mLabel.get(3).tag;
            for (int i = 0; i < book.size(); i++) {
                if (!TextUtils.isEmpty(book.get(i).interestclassname)) {
                    tag4.add(book.get(i).interestclassname);
                }
            }
        }
        List<HobbiesBean.Biaoqian> food = data.food;
        if (null != food && !food.isEmpty()) {
            List<String> tag5 = mLabel.get(4).tag;
            for (int i = 0; i < food.size(); i++) {
                if (!TextUtils.isEmpty(food.get(i).interestclassname)) {
                    tag5.add(food.get(i).interestclassname);
                }

            }
        }

        List<HobbiesBean.Biaoqian> yundong = data.yundong;
        if (null != yundong && !yundong.isEmpty()) {
            List<String> tag6 = mLabel.get(5).tag;
            for (int i = 0; i < yundong.size(); i++) {
                if (!TextUtils.isEmpty(yundong.get(i).interestclassname)) {
                    tag6.add(yundong.get(i).interestclassname);
                }

            }
        }

        mHobbyInterestAdapter = new HobbyInterestAdapter(mLabel);
        mRecyclerView.setAdapter(mHobbyInterestAdapter);


        mHobbyInterestAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mLabel.get(position).tag.clear();
                Intent intent = new Intent(mContext, PersonageTagActivity.class);
                intent.putExtra("position", position);
                startActivityForResult(intent, position);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            if (null != mPersonageTagBean) {
                List<PersonageTagBean.PersonageTag> tag1 = mPersonageTagBean.data;
                if (null != tag1 && !tag1.isEmpty()) {
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
