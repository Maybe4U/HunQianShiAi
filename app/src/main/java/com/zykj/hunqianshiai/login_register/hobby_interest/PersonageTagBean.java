package com.zykj.hunqianshiai.login_register.hobby_interest;

import com.zykj.hunqianshiai.bases.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xu on 2017/12/19.
 */

public class PersonageTagBean extends BaseBean {
    public List<PersonageTag> data;

    public static class PersonageTag implements Serializable {
        public String interestclassname;
        public String interestclassid;
        public int selet;
    }
}
