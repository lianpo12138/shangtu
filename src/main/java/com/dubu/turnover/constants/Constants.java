package com.dubu.turnover.constants;

public interface Constants {


    //本地上传
    String FILE = "FILE";
    //从邮册选择
    String IMAGE = "IMAGE";
    
    public  static final String SERVERKEY = "IpsosIMProject";

    /**
     * 第三方登陆获取信息
     *
     * @author 201604261000
     */
    class ThpartyInfo {
        public static final String QQTOKENURL = "https://graph.qq.com/oauth2.0/token";
        public static final String QQMEURL = "https://graph.qq.com/oauth2.0/me";
        public static final String QQUSERINFOURL = "https://graph.qq.com/user/get_user_info";
        public static final String WEIBOTOKENURL = "https://api.weibo.com/oauth2/access_token";
        public static final String WEIBOUSERINFOURL = "https://api.weibo.com/2/users/show.json";
        public static final String WEIXINTOKENURL = "https://api.weixin.qq.com/sns/oauth2/access_token";
        public static final String WEIXINUSERINFOURL = "https://api.weixin.qq.com/sns/userinfo";

        //获取微信access_token
        public static final String WX_GET_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";

        //获取微信js api 的 ticket 的url
        public static final String WX_GETTICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

        //生成二维码
        public static final String WXAPPCODE_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit";

        //生存短域名
        public static final String WX_SHORT_URL = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=";

    }
}
