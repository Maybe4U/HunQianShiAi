package com.zykj.hunqianshiai.bases;

import com.lzy.okgo.model.HttpParams;

/**
 * Created by ${xu} on 2017/10/16.
 */

public interface BasePresenter {

    void getData(String url, HttpParams params, int type);

    void onDestroy();
}
