package com.china.lhf.app.http;

/**
 *
 */
public class Contants {

    public static final String CAMPAIGN_ID="campaign_id";
    public static final String WARE="ware";
    public static final String USER_JSON="user_json";
    public static final String TOKEN="token";

    public static final int REQUEST_CODE=0;


    public static class API{
        public static final String BASE_URL="http://101.200.167.75:8080/phoenixshop/";
        public static final String BANNER_URL=BASE_URL+"banner/query";
        public static final String CAMPAIGN_HOME=BASE_URL+"campaign/recommend";
        public static final String WARES_HOT=BASE_URL+"wares/hot";
        public static final String CATEGORY_LIST=BASE_URL+"category/list";
        public static final String WARES_LIST=BASE_URL+"wares/list";
        public static final String WARES_DETAIL=BASE_URL+"wares/detail.html";

        public static final String CAMPAIGN_LIST=BASE_URL+"campaign/list";
        public static final String LOGIN=BASE_URL+"auth/login";
        public static final String USER_DETAIL=BASE_URL+"user/get?id=1";

        public static final String REG=BASE_URL+"auth/reg";
        public static final String ORDER_CREATE = BASE_URL + "user/ordercreate";
        public static final String ORDER_COMPLETE = BASE_URL + "user/ordercomplete";
        //获取用户订单
        public static final String ORDER_LIST = BASE_URL + "user/orderlist";

        //获取地址列表
        public static final String ADDR_LIST = BASE_URL + "user/addrlist";
        //更新地址
        public static final String ADDR_UPDATE = BASE_URL + "user/addrupdate";
        //删除地址
        public static final String ADDR_DEL = BASE_URL + "user/addrdel";
        //  添加地址
        public static final String ADDR_CREATE = BASE_URL + "user/addrcreate";

        //添加到收藏夹
        public static final String FAVORITE_CREATE = BASE_URL + "user/favoritecreate";
        //收藏列表
        public static final String FAVORITE_LIST = BASE_URL + "user/favoritelist";
        //移除收藏商品
        public static final String FAVORITE_DELETE = BASE_URL + "user/favoritedelete";

    }
}
