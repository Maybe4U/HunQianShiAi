package com.zykj.hunqianshiai.bases;

import com.lzy.okgo.model.HttpParams;

/**
 * Created by ${xu} on 2017/10/16.
 */

public interface BaseModel<T> {

    public static int DEFAULT_TYPE = 0;
    public static int REFRESH_TYPE = 1;
    public static int LOADING_MORE_TYPE = 2;

    interface OnBaseModelListener<T> {
        void success(T bean);

        void refresh(T bean);

        void loadMore(T bean);

        void failed(T content);
    }

    void netData(String url, HttpParams params,OnBaseModelListener<T> listener, int type);
}
