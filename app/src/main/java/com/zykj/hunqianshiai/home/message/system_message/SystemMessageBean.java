package com.zykj.hunqianshiai.home.message.system_message;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2018/1/24.
 */

public class SystemMessageBean extends BaseBean {
    public List<SystemMessageData> data;

    public static class SystemMessageData implements Serializable {
        public String catogry;
        public String content;
        public String id;
        public String other_id_p;
        public String remark;
        public String time;
        public String title;
    }
}
