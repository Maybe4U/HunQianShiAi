package com.zykj.hunqianshiai.net;

/**
 * Created by xu on 2017/12/13.
 */

public class UrlContent {

    private static String BASE_URL = "http://47.104.91.22/api.php?";
    public static final String PIC_URL = "http://47.104.91.22/";
    public static final String KEY = "8934e7d15453e97507ef794cf7b0519d";
    public static final String FIRST_URL = BASE_URL + "c=tool&a=getip";
    public static String RDM = "62d325c5ee717e447f5728d3e4dc5e71";
    public static String SIGN = "656c7b92d3d5309205fa7a563c07c5ca";
    public static String USER_ID = "";
    public static String WX_APP_ID = "wxbbe4f3bd3934bd01";
    public static String USER_NAME = "";//用户昵称
    public static String USER_PIC = "";//用户头像
    public static String TARGET_ID = "";//聊天对象id
    public static boolean IS_MEMBER_KEY = false;
    public static boolean GOAGO = false;
    public static String SEX = "";

    /**
     * 手机号注册
     */
    public static final String REGISTER_PHONE_URL = BASE_URL + "c=common&a=reg";

    /**
     * 手机号登录
     */
    public static final String LOGIN_PHONE_URL = BASE_URL + "c=common&a=login";

    /**
     * 获取城市
     */
    public static final String GET_CITY_URL = BASE_URL + "c=common&a=getSonCityById";

    /**
     * 获取用户状态
     */
    public static final String USER_STATE_URL = BASE_URL + "c=user&a=getUserStatus";

    /**
     * 完善基本资料
     */
    public static final String COMPLETE_INFO_URL = BASE_URL + "c=user&a=setUserBase";

    /**
     * 验证身份，上传个人身份认证图片
     */
    public static final String NAME_AUTHENTICATION_URL = BASE_URL + "c=user&a=uploadUserImg";

    /**
     * 上传图片接口
     */
    public static final String UPLOAD_PIC_URL = BASE_URL + "c=common&a=uploadimg";

    /**
     * 获取所有兴趣爱好标签分类（新）1
     */
    public static final String TAG_ONE_URL = BASE_URL + "c=user&a=getInterestCls";

    /**
     * 设置（自己）用户标签兴趣爱好从公共里面选（新）2
     */
    public static final String TAG_TWO_URL = BASE_URL + "c=user&a=setUserTags";

    /**
     * 设置自定义的标签和兴趣爱好（新）3
     */
    public static final String TAG_THREE_URL = BASE_URL + "c=user&a=setUserTags2";

    /**
     * 忘记密码
     */
    public static final String FORGET_PASSWORD_URL = BASE_URL + "c=common&a=resetPass";

    /**
     * 逛一逛
     */
    public static final String HOME_PAGE_URL = BASE_URL + "c=user&a=pageAll";

    /**
     * 首页同城会员列表
     */
    public static final String HOME_CITY_URL = BASE_URL + "c=user&a=pageIndexCity";

    /**
     * 首页全国搜索列表，分页获取异性、全国 会员列表
     */
    public static final String HOME_COUNTRY_URL = BASE_URL + "c=user&a=pageIndex";

    /**
     * 我的动态
     */
    public static final String MY_DYNAMIC_URL = BASE_URL + "c=Friends&a=me_fridents_f";

    /**
     * 发布朋友圈
     */
    public static final String ISSUE_DYNAMIC_URL = BASE_URL + "c=Friends&a=add_friends_me";

    /**
     * 点赞（取消）
     */
    public static final String IS_LIKE_URL = BASE_URL + "c=Friends&a=dian_zan";

    /**
     * 查询所有符合条件的动态列表
     */
    public static final String ALL_DYNAMIC_URL = BASE_URL + "c=Friends&a=sel_fridends2";

    /**
     * 评论动态
     */
    public static final String COMMENT_DYNAMIC_URL = BASE_URL + "c=Friends&a=comment";

    /**
     * 查询送给我的礼物
     */
    public static final String RECEIVE_GIFT_URL = BASE_URL + "c=Friends&a=sel_giveme_gift";

    /**
     * 举报动态
     */
    public static final String INFORM_URL = BASE_URL + "c=Friends&a=report_friendts";

    /**
     * 举报
     */
    public static final String INFROM_USER_URL = BASE_URL + "c=user&a=submitCpt";

    /**
     * 获取用户的个人详情信息
     */
    public static final String GET_INFO_URL = BASE_URL + "c=user&a=getUserInfo";

    /**
     * 设置用户个人信息
     */
    public static final String SET_INFO_URL = BASE_URL + "c=user&a=setUserInfo";

    /**
     * 获取用户择偶标准
     */
    public static final String SPOUSE_STANDARDS_URL = BASE_URL + "c=user&a=getlinkerInfo";

    /**
     * 设置择偶标准
     */
    public static final String SET_SPOUSE_URL = BASE_URL + "c=user&a=setlinkerInfo";

    /**
     * 获取自己多个分类的信息
     */
    public static final String ALL_TAG_URL = BASE_URL + "c=user&a=getUserTags2";

    /**
     * 上传视频接口
     */
    public static final String UPLOAD_VIDEO_URL = BASE_URL + "c=common&a=uploadvideo";

    /**
     * 添加修改形象视频
     */
    public static final String ADD_VIDEO_URL = BASE_URL + "c=user&a=add_video";

    /**
     * 获取相册图片
     */
    public static final String GET_PIC_URL = BASE_URL + "c=user&a=getAlbum";

    /**
     * 添加用户相册
     */
    public static final String ADD_PIC_URL = BASE_URL + "c=user&a=setAlbum";

    /**
     * 删除相册图片
     */
    public static final String DELETE_PIC_URL = BASE_URL + "c=user&a=delAlbum";

    /**
     * 设置认证信息（房产和汽车和学历）
     */
    public static final String SET_IDENTIFICATION_URL = BASE_URL + "c=user&a=setUserauth";

    /**
     * 用户基本资料全（一个）
     */
    public static final String USERINFO_URL = BASE_URL + "c=user&a=userinfo_p";

    /**
     * 添加心动用户
     */
    public static final String ADD_LIKE_URL = BASE_URL + "c=user&a=addLinker";

    /**
     * 取消心动用户
     */
    public static final String DELETE_LIKE_URL = BASE_URL + "c=user&a=cancelLinker";

    /**
     * 获取用户心动记录
     */
    public static final String MY_LIKE_URL = BASE_URL + "c=user&a=getMyLinker";

    /**
     * 获取他人对用户心动的记录
     */
    public static final String LIKE_ME_URL = BASE_URL + "c=user&a=getToLinker";

    /**
     * 附近的用户
     */
    public static final String PEOPLE_NEARBY_URL = BASE_URL + "c=user&a=near_user";

    /**
     * 活动列表
     */
    public static final String ACTIVITIES_LIST_URL = BASE_URL + "c=user&a=getActivityPage";

    /**
     * 资讯列表
     */
    public static final String INFORMATION_LIST_URL = BASE_URL + "c=user&a=getActivityPage2";

    /**
     * 根据id搜索会员
     */
    public static final String SEARCH_ID_URL = BASE_URL + "c=user&a=idSelect";

    /**
     * vip高级搜索
     */
    public static final String SEARCH_VIP_URL = BASE_URL + "c=user&a=vipSelect";

    /**
     * 添加用户来访记录
     */
    public static final String ADD_LOOK_URL = BASE_URL + "c=user&a=setUsercomer";

    /**
     * 获取认证费
     */
    public static final String REN_ZHENG_URL = BASE_URL + "c=user&a=sysprice";

    /**
     * 修改推广期不交认证费的认证状态
     */
    public static final String SET_STATE_URL = BASE_URL + "c=user&a=change_type";

    /**
     * 支付（认证礼物会员等支付宝微信）
     * 1认证费；2加入或延期会员；3购买礼物；4充值，5加入或延期婚恋助理，6加入或延期贵宾婚恋助理， 7参加活动，8上线提醒
     */
    public static final String PAY_URL = BASE_URL + "c=user&a=order";

    /**
     * 改变用户状态（在线离线）
     */
    public static final String SET_ONLINE_URL = BASE_URL + "c=user&a=changeState";

    /**
     * 获取用户来访记录
     */
    public static final String GET_COMER_URL = BASE_URL + "c=user&a=getUsercomer";

    /**
     * 获取VIP，私人助理，婚恋管家价格、上线提醒时间价格
     * 值1、2、3，表示：1会员 2私人助理 3贵宾助理；4上线提醒
     */
    public static final String GET_PRICE_URL = BASE_URL + "c=user&a=getvipprice";

    /**
     * 获取融云token
     */
    public static final String GET_TOKEN_URL = BASE_URL + "c=Common&a=getToken";

    /**
     * 刷新融云token
     */
    public static final String REFRESH_TOKEN_URL = BASE_URL + "c=Common&a=refreshToken";

    /**
     * 判断用户是否为VIP
     */
    public static final String IS_VIP_URL = BASE_URL + "c=user&a=isvip";

    /**
     * 是否是开通上线用户
     * type 1私人助理；2贵宾助理；3上线提醒；4vip
     */
    public static final String GET_STATE_URL = BASE_URL + "c=user&a=islogin_xian";

    /**
     * 加入黑名单
     */
    public static final String ADD_BLACKLIST_URL = BASE_URL + "c=user&a=setBlacklist";

    /**
     * 获取用户黑名单
     */
    public static final String GET_BLACKLIST_URL = BASE_URL + "c=user&a=getBlacklist";

    /**
     * 移除黑名单
     */
    public static final String REMOVE_BLACKLIST_URL = BASE_URL + "c=user&a=del_Blacklist";

    /**
     * 不让显示不让看
     */
    public static final String SEE_URL = BASE_URL + "c=friends&a=limit_see";

    /**
     * 查询推送给自己的信息
     */
    public static final String PUSH_MESSAGE_URL = BASE_URL + "c=Friends&a=user_push";

    /**
     * 查询用户开通在线提醒的列表
     */
    public static final String ONLINE_LIST_URL = BASE_URL + "c=friends&a=open_user_xianList";

    /**
     * 开通某一位用户上线提醒
     */
    public static final String ONLINE_REMIND_URL = BASE_URL + "c=friends&a=open_user_xian";

    /**
     * 取消上线提醒
     */
    public static final String REMOVE_REMIND_URL = BASE_URL + "c=friends&a=del_user_xian";

    /**
     * 获取用户余额
     */
    public static final String GET_BALANCE_URL = BASE_URL + "c=user&a=get_blance";

    /**
     * 充值消费记录
     */
    public static final String WALLET_RECORD_URL = BASE_URL + "c=user&a=blance_waiter";

    /**
     * 清空自己的推送
     */
    public static final String DELETE_MESSAGE_URL = BASE_URL + "c=Friends&a=del_push_user";

    /**
     * 朋友圈详情
     */
    public static final String DYNAMIC_DETAILS_URL = BASE_URL + "c=friends&a=friends_details";

    /**
     * 查询点赞列表
     */
    public static final String LIKE_LIST_URL = BASE_URL + "c=Friends&a=zan_list_user";

    /**
     * 动态作者回复评论人
     */
    public static final String REPLY_URL = BASE_URL + "c=Friends&a=comment2";

    /**
     * 删除评论
     */
    public static final String DELETE_COMMENT_URL = BASE_URL + "c=Friends&a=del_comment";

    /**
     * 查询推送给自己的系统消息
     */
    public static final String SYSTEM_MESSAGE_URL = BASE_URL + "c=Friends&a=system_push";

    /**
     * 脸谱
     */
    public static final String FACE_ALIKE_URL = "http://47.104.91.22/api.php?c=Fell&a=fuqixiang&actid=13";

    /**
     * 资讯详情
     */
    public static final String INFORMATION_PARTICULARS_URL = BASE_URL + "c=user&a=getActivity2";

    /**
     * 评论资讯
     */
    public static final String COMMENT_INFORMATION_URL = BASE_URL + "c=fell&a=comment";

    /**
     * 删除动态
     */
    public static final String DELETE_DYNAMIC_URL = BASE_URL + "c=Friends&a=del_user_friends";

    /**
     * 获取礼物价格
     */
    public static final String GIFT_LIST_URL = BASE_URL + "c=Friends&a=gift_price";

    /**
     * 查询我送出的礼物
     */
    public static final String SEND_GIFT_URL = BASE_URL + "c=Friends&a=sel_me_gift";

    /**
     * 申请提现
     */
    public static final String WITHDRAW_DEPOSIT_URL = BASE_URL + "c=user&a=applycashP";

    /**
     * 评论资讯
     */
    public static final String REPLY_COMMENT_INFORMATION_URL = BASE_URL + "c=Fell&a=comment2";

    /**
     * 意见反馈
     */
    public static final String FEEDBACK_URL = BASE_URL + "c=user&a=setUserMsg";

    /**
     * 用户参加活动
     */
    public static final String JOIN_ACTIVITY_URL = BASE_URL + "c=user&a=joinActivity";

    /**
     * 第三方登录调用（注册）
     */
    public static final String THREE_LOGIN_URL = BASE_URL + "c=common&a=openid_login";

    /**
     * 帮我约她
     */
    public static final String HELP_ME_URL = BASE_URL + "c=friends&a=yue_ta";

    /**
     * 注销账号
     */
    public static final String WRITE_OFF_URL = BASE_URL + "c=user&a=applyDeleteUser";

    /**
     * 添加聊天纪律
     */
    public static final String CHAT_COUNT_URL = BASE_URL + "c=user&a=chat_logs";

    /**
     * 删除视频
     */
    public static final String DELETE_VIDEO_URL = BASE_URL + "c=user&a=del_video";

    /**
     * 分销商列表
     */
    public static final String FEN_XIAO_URL = BASE_URL + "c=user&a=fen_shop";

    /**
     * 首页活动海报
     */
    public static final String ACTIVITIES_POSTER_URL = BASE_URL + "c=user&a=ling_haibao";

    /**
     * 参加活动免缴费
     */
    public static final String ACTIVITIES_NOPAY_URL = BASE_URL + "c=user&a=change_activity";
}
