package io.rong.callkit;

import android.content.Context;

import javax.sql.ConnectionEventListener;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by Administrator on 2018-5-1.
 */

public class RongCloudEvent implements RongIMClient.OnReceiveMessageListener, RongIMClient.ConnectionStatusListener, RongIM.OnSendMessageListener {
    private static RongCloudEvent mRongCloudInstance;
    private final Context mContext;
    @Override
    public boolean onReceived(Message message, int i) {
        //这里要返回默认的false  会走融云的逻辑
        return false;
    }

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {
        if (mRongCloudInstance == null) {
            synchronized (RongCloudEvent.class) {
                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        initDefaultListener();
    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {
        RongIM.getInstance().setOnReceiveMessageListener(this);//设置消息接收监听器
        RongIM.getInstance().setSendMessageListener(this);
        RongIM.setConnectionStatusListener(this);
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

    }

    @Override
    public Message onSend(Message message) {

        return message;
    }

    @Override
    public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {

        return false;
    }
}
