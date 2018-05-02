package com.zykj.hunqianshiai.select_city;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzy.okgo.model.HttpParams;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.bases.BaseModel;
import com.zykj.hunqianshiai.bases.BaseModelImpl;
import com.zykj.hunqianshiai.bases.BasePresenterImpl;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import java.util.List;

import butterknife.Bind;

/**
 * 选择城市
 */
public class SelectCityActivity extends BasesActivity implements BaseView<String> {

    @Bind(R.id.recycler_province)
    RecyclerView mRecyclerView1;
    @Bind(R.id.recycler_city)
    RecyclerView mRecyclerView2;
    @Bind(R.id.recycler_counties)
    RecyclerView mRecyclerView3;
    @Bind(R.id.tv_province)
    TextView province;
    @Bind(R.id.tv_city)
    TextView city;
    @Bind(R.id.tv_counties)
    TextView counties;
    @Bind(R.id.tv_lines)
    TextView lines;
    @Bind(R.id.tv_right)
    TextView right;

    private View mHeadView;
    private HttpParams mParams;
    private BasePresenterImpl mPresenter;
    private SelectCityAdapter mSelectCityAdapter;
    private SelectCityAdapter mSelectCityAdapter1;
    private String CITY_ID;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_select_city;
    }

    @Override
    protected void initView() {
        appBar("选择城市");
        right.setText("确定");
        right.setVisibility(View.VISIBLE);
        right.setOnClickListener(this);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        layoutManager3.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView1.setLayoutManager(layoutManager1);
        mRecyclerView2.setLayoutManager(layoutManager2);
        mRecyclerView3.setLayoutManager(layoutManager3);
        mPresenter = new BasePresenterImpl(this, new BaseModelImpl());
        mParams = new HttpParams();
        mParams.put("areaid", 0);
        mParams.put("flag", 0);
        mPresenter.getData(UrlContent.GET_CITY_URL, mParams, BaseModel.DEFAULT_TYPE);
    }

    @Override
    public void success(String bean) {

        mHeadView = LayoutInflater.from(SelectCityActivity.this).inflate(R.layout.select_city_item, null);
        TextView tv_city = mHeadView.findViewById(R.id.tv_city);
        tv_city.setText("不限");
        //province.setText("不限");


        SelectCityBean selectCityBean = JsonUtils.GsonToBean(bean, SelectCityBean.class);
        List<SelectCityBean.SelectCityData> data = selectCityBean.data;
        final SelectCityAdapter selectCityAdapter = new SelectCityAdapter(data);
        mRecyclerView1.setAdapter(selectCityAdapter);
        selectCityAdapter.setHeaderView(mHeadView);
        selectCityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<SelectCityBean.SelectCityData> data = adapter.getData();
                SelectCityBean.SelectCityData selectCityData = data.get(position);
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).select = 0;
                }
                selectCityData.select = 1;
                CITY_ID = selectCityData.areaid;
                tv_city.setTextColor(0xff000000);
                province.setText(selectCityData.areaname);
                city.setText("");
                counties.setText("");
                mRecyclerView3.setVisibility(View.INVISIBLE);
                lines.setVisibility(View.INVISIBLE);
                mParams.clear();
                mParams.put("areaid", selectCityData.areaid);
                mParams.put("flag", 1);
                mPresenter.getData(UrlContent.GET_CITY_URL, mParams, BaseModel.REFRESH_TYPE);
                selectCityAdapter.notifyDataSetChanged();
            }
        });
        mHeadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                province.setText("不限");
                tv_city.setTextColor(0xffedbd5a);
                city.setText("");
                counties.setText("");
                //selectCityAdapter.
                CITY_ID = "";
                //selectCityAdapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.putExtra("city_id", CITY_ID);
                intent.putExtra("city", province.getText().toString() + city.getText().toString() + counties.getText().toString());
                setResult(102, intent);
                finish();

            }
        });
    }

    @Override
    public void refresh(String bean) {
        SelectCityBean selectCityBean = JsonUtils.GsonToBean(bean, SelectCityBean.class);
        List<SelectCityBean.SelectCityData> data = selectCityBean.data;
        if (null == mSelectCityAdapter) {
            mSelectCityAdapter = new SelectCityAdapter(data);
            mRecyclerView2.setAdapter(mSelectCityAdapter);

            mSelectCityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    List<SelectCityBean.SelectCityData> data = adapter.getData();

                    SelectCityBean.SelectCityData selectCityData = data.get(position);
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).select = 0;
                    }
                    CITY_ID = selectCityData.areaid;
                    selectCityData.select = 1;
                    mSelectCityAdapter.notifyDataSetChanged();
                    city.setText(data.get(position).areaname);
                    mRecyclerView3.setVisibility(View.VISIBLE);
                    lines.setVisibility(View.VISIBLE);
                    counties.setText("");
//                    mParams.clear();
//                    mParams.put("areaid", data.get(position).areaid);
//                    mParams.put("flag", 2);
//                    mPresenter.getData(UrlContent.GET_CITY_URL, mParams, BaseModel.LOADING_MORE_TYPE);
                }
            });
        } else {
            mSelectCityAdapter.setNewData(data);
        }
    }

    @Override
    public void loadMore(String bean) {
        SelectCityBean selectCityBean = JsonUtils.GsonToBean(bean, SelectCityBean.class);
        List<SelectCityBean.SelectCityData> data = selectCityBean.data;
        if (null == mSelectCityAdapter1) {
            mSelectCityAdapter1 = new SelectCityAdapter(data);
            mRecyclerView3.setAdapter(mSelectCityAdapter1);
            mSelectCityAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    List<SelectCityBean.SelectCityData> data = adapter.getData();
                    counties.setText(data.get(position).areaname);
                    SelectCityBean.SelectCityData selectCityData = data.get(position);
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).select = 0;
                    }
                    selectCityData.select = 1;
                    CITY_ID = selectCityData.areaid;
                    mSelectCityAdapter1.notifyDataSetChanged();
                }
            });
        } else {
            mSelectCityAdapter1.setNewData(data);
        }
    }

    @Override
    public void failed(String content) {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_right:

                if (TextUtils.isEmpty(province.getText().toString().trim())) {
                    toastShow("请选择地区");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("city_id", CITY_ID);
                intent.putExtra("city", province.getText().toString() + city.getText().toString() + counties.getText().toString());
                setResult(102, intent);
                finish();

                break;
        }
    }
}
