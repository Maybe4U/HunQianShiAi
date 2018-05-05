package com.zykj.hunqianshiai.home.my.set;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.tools.SPUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xu on 2018/1/20.
 */

public class BlacklistAdapter extends BaseQuickAdapter<BlacklistBean.BlacklistData, BaseViewHolder> {
    public SparseArray<Boolean> map;// 存放已被选中的CheckBox

    private  Context context;
    private List<BlacklistBean.BlacklistData> data;
    public BlacklistAdapter( Context context, @Nullable List<BlacklistBean.BlacklistData> data) {
        super(R.layout.item_blacklist, data);
        this.context = context;
        this.data = data;
        map = new SparseArray<>();
        for (int i = 0; i < data.size(); i++) {
            map.put(i,false);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, BlacklistBean.BlacklistData item) {

        //helper.addOnClickListener(R.id.iv_yichu);
        Glide.with(mContext)
                .load(item.self.headpic)
                .apply(BasesActivity.mCircleRequestOptions)
                .into((ImageView) helper.getView(R.id.iv_headpic));
        String age = item.self.age;
        if (!TextUtils.isEmpty(age)) {
            helper.setText(R.id.tv_name, item.username + "  " + age + "岁");
        } else {
            helper.setText(R.id.tv_name, item.username);
        }

        helper.setText(R.id.tv_time, "拉黑时间：" + item.addtime);

        boolean edit_mode = (boolean) SPUtils.get(context, "edit_mode", false);
        if(edit_mode){
            map.setValueAt(helper.getPosition(),false);
            helper.setVisible(R.id.iv_yichu,true);
            helper.setChecked(R.id.iv_yichu,false);
        }else {
            map.setValueAt(helper.getPosition(),false);
            helper.setVisible(R.id.iv_yichu,false);
            helper.setChecked(R.id.iv_yichu,false);
        }


        helper.setOnCheckedChangeListener(R.id.iv_yichu, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    map.setValueAt(helper.getPosition(),b);

            }
        });

        if(map!=null&&map.valueAt(helper.getPosition())){
            helper.setChecked(R.id.iv_yichu,true);
        }else {
            helper.setChecked(R.id.iv_yichu,false);
        }


    }
//    public static Map<Integer,Boolean> getMap(Map<Integer,Boolean> mMap){
//        mMap = map;
//        return mMap;
//    }


    public SparseArray<Boolean> getMap() {
        return map;
    }
}
