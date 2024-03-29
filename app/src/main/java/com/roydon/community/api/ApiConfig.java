package com.roydon.community.api;

public class ApiConfig {

    public static final int PAGE_SIZE = 10;
    public static final int PAGE_SIZE_20 = 20;

//    public static final String BASE_URl = "http://106.14.105.101:8088";
            public static final String BASE_URl = "http://192.168.26.13:8088";
    //    public static final String BASE_URl = "http://192.168.68.179:8088";
//    public static final String WS_URl = "ws://106.14.105.101:8088";
    public static final String WS_URl = "ws://192.168.26.13:8088";
//    public static final String WS_URl = "ws://192.168.68.179:8088";

    /**
     * app前缀接口
     */
    public static final String LOGIN = "/app/login"; //登录
    public static final String SMS_SEND_CODE = "/sms/sendCode/"; //发送短信验证码
    public static final String SMS_LOGIN = "/app/sms-login"; //短信登录
    public static final String REGISTER = "/app/register"; //注册

    /**
     * 扫描二维码登录接口
     */
    public static final String SCAN = "/scan"; // 扫描二维码，get，参数：uuid
    public static final String CONFIRM = "/confirm"; // 确认登录，get，参数：uuid


    /**
     * user接口
     */
    public static final String USER_INFO = "/app/user/info"; //注册
    public static final String USER_EDIT_INFO = "/app/user/edit"; //注册

    /**
     * user-profile详情接口
     */
    public static final String USER_PROFILE_AVATAR = "/system/user/profile/avatar"; // 修改头像

    /**
     * 健康码接口
     */
    public static final String HEALTH_CODE_GET = "/epidemic/health/code"; // 查询健康码

    /**
     * 疫苗接种审核
     */
    public static final String INOCULATION_AUDIT_REPORT = "/epidemic/inoculation/audit/report"; // 疫苗接种审核提交
    public static final String INOCULATION_AUDIT_UPLOAD_IMAGE = "/epidemic/inoculation/audit/upload-image"; // 疫苗接种审核图片上传

    /**
     * 疫苗接种审核记录
     */
    public static final String INOCULATION_HISTORY_USER = "/epidemic/inoculation/history/user"; // 用户疫苗接种记录

    /**
     * app消息
     */
    public static final String APP_MESSAGE_COUNT = "/app/message/count";
    public static final String APP_MESSAGE_LIST = "/app/message/list";

    /**
     * 新闻模块接口
     */
    public static final String NEWS_CATEGORY_LIST = "/app/news/category"; //新闻分类
    public static final String NEWS_LIST = "/app/news/list"; //新闻list
    public static final String NEWS_DETAIL = "/app/news/detail"; //新闻detail
    public static final String NEWS_HOT = "/app/news/hot"; //新闻detail

    /**
     * 轮播公告
     */
    public static final String BANNER_NOTICE_LIST = "/app/notice/banner"; //新闻分类

    /**
     * mall-goods
     */
    public static final String MALL_GOODS_LIST = "/app/mallGoods/list"; //商品集合
    public static final String MALL_GOODS_DETAIL = "/app/mallGoods"; //商品集合

    /**
     * mall-cart
     */
    public static final String MALL_CART_LIST = "/app/mallUserCart/list"; //购物车集合
    public static final String MALL_ADD_CART = "/app/mallUserCart"; //添加购物车
    public static final String MALL_DEL_CART = "/app/mallUserCart/"; //添加购物车
    public static final String MALL_ALL_CART = "/app/mallUserCart/all"; //添加购物车

    /**
     * mall-address
     */
    public static final String MALL_ADDRESS_LIST = "/app/mallUserAddress/list"; //地址集合
    public static final String MALL_ADD_ADDRESS = "/app/mallUserAddress"; //添加地址
    public static final String MALL_DEFAULT_ADDRESS = "/app/mallUserAddress/default"; //默认收货地址

    /**
     * mall-order
     */
    public static final String MALL_ORDER_CREATE = "/app/mallOrder/create";
    public static final String MALL_USER_ORDER_LIST = "/app/mallOrder/userOrderList";

    /**
     * 疫情防控-出入社区吧报备
     */
    public static final String EPIDEMIC_ACCESS_ADD = "/epidemic/access"; // 新增

    /**
     * 疫情防控-隔离记录
     */
    public static final String EPIDEMIC_ISOLATION_UNFINISHED_RECORD = "/epidemic/isolation/unfinished_record"; // 新增

    /**
     * 紧急热线
     */
    public static final String APP_HOTLINE_ALL = "/app/hotline/all"; // list

    /**
     * 核酸预约
     */
    public static final String NAT_ORDER_QUICK = "/epidemic/nat/order/quick"; // quick
    public static final String NAT_ORDER_MINE_HISTORY = "/epidemic/nat/order/mine_history"; // 我的核酸预约记录

    /**
     * 体温上报
     */
    public static final String TEMPERATURE_REPORT = "/epidemic/temperature/report"; // 上报
    public static final String TEMPERATURE_REPORT_HISTORY_MY = "/epidemic/temperature/report/mine_list"; // 我的上报记录

    /**
     * 功能反馈
     */
    public static final String SYS_FEEDBACK_ADD = "/system/feedback"; // 添加系统反馈

    /**
     * 聊天chat-list
     */
    public static final String CHAT_LIST_NORMAL = "/chat/list/normal";

}