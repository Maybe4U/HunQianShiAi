package com.zykj.hunqianshiai.bases;

import com.lzy.okgo.model.HttpParams;

/**
 * Created by ${xu} on 2017/10/30.
 */

public class BasePresenterImpl implements BasePresenter, BaseModel.OnBaseModelListener<String> {

    private BaseView view;
    private BaseModel model;

    public BasePresenterImpl(BaseView view, BaseModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void getData(String url, HttpParams params, int type) {
        model.netData(url, params, this, type);
    }

    @Override
    public void onDestroy() {
        view = null;
    }

    @Override
    public void success(String bean) {
        if (null != view) {
            view.success(bean);
        }
    }

    @Override
    public void refresh(String bean) {
        if (null != view) {
            view.refresh(bean);
        }
    }

    @Override
    public void loadMore(String bean) {
        if (null != view) {
            view.loadMore(bean);
        }
    }

    @Override
    public void failed(String content) {
        if (null!=view) {
            view.failed(content);
        }
    }
}
