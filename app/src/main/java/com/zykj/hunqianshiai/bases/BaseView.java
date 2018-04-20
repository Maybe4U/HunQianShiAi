package com.zykj.hunqianshiai.bases;

/**
 * Created by ${xu} on 2017/10/16.
 */

public interface BaseView<B> {
    void success(B bean);

    void refresh(B bean);

    void loadMore(B bean);

    void failed(B content);
}
