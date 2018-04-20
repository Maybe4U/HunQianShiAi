package com.zykj.hunqianshiai.login_register.login;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;

/**
 * Created by xu on 2017/12/30.
 */

public class UserStateBean extends BaseBean {
    public UserStateData data;

    public static class UserStateData implements Serializable {
        public String isauth;//是否身份认证通过（0等待验证、1通过、2不通过、3未交认证费4 交了认证费）',
        public String iscomplete;//是否完善资料（1是、0否）',
        public String isonline;//是否在线（1是、0否）',
        public String isable;//是否封号（被举报后可封号,1是、0否）',
        public String is_ren_lian;//是否上传人脸自拍（1是、0否）',
        public String is_shenfeizheng;//是否身份上传身份证（1是、0否）',
        public String is_xingqu;//是否填写兴趣（1是、0否）',
    }
}
