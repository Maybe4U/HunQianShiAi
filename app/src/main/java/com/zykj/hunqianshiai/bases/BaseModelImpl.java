package com.zykj.hunqianshiai.bases;

import android.util.Log;
import android.widget.Toast;

import com.huawei.hms.support.log.LogLevel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.zykj.hunqianshiai.net.UrlContent;

/**
 * Created by ${xu} on 2017/10/30.
 */

public class BaseModelImpl implements BaseModel<String> {

    @Override
    public void netData(String url, HttpParams params, final OnBaseModelListener<String> listener, final int type) {
        params.put("rdm", UrlContent.RDM);
        params.put("sign", UrlContent.SIGN);
        OkGo.<String>post(url)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        //Log.e("netData-----",response.message());
                        //Log.e("type-----",type + "");
                        switch (type) {
                            case BaseModel.DEFAULT_TYPE:
                                listener.success(response.body());
                                break;
                            case BaseModel.REFRESH_TYPE:
                                listener.refresh(response.body());
                                break;
                            case BaseModel.LOADING_MORE_TYPE:
                                listener.loadMore(response.body());
                                break;

                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        listener.failed(response.body());
                        super.onError(response);
                    }
                });
    }
}
