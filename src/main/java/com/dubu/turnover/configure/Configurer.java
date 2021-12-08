package com.dubu.turnover.configure;

public class Configurer {
    /***********************************************系统设置*************************************************************************/
    public static final String APPLICATION = "PRIORITY";// 项目名称，

    private static final String BASE_PACKAGE = "com.dubu.turnover";// 项目基础包名称，根据自己公司的项目修改
    public static final String MODEL_PACKAGE = BASE_PACKAGE + ".entity";// Model所在包
    public static final String MAPPER_PACKAGE = BASE_PACKAGE + ".mapper";// Mapper所在包
    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".core.Mapper";// Mapper插件基础接口的完全限定名

    


    public static final long SMS_EXPIRE_TIME = 15 * 60;//验证码失效时间
    public static final Integer MIN_FREQ = 1;//一分钟之内只一条
    public static final Integer HOUR_FREQ = 10;//一小时之内只十条
    public static final Integer DAY_FREQ = 20;//一天之内只十条
    

    public static final String UUID_COUNT="PRIORITY_UUID_COUNT";//请求头随机数验证
    public static final String APP_DEVICEID = "PRIORITY:VAR:SERVICE:DEVICEID:KEY:";//设备id
    public static final String HASH_KEY="PRIORITY_HASH_KEY";//设备id

    /***********************************************系统认证登录常亮*************************************************************************/
    public static final String HEADER_TOKEN_KEY = "%s-token";//ERP验证TOKEN
    public static final String CACHE_TOKEN_KEY = "token-%s-%s";//TOKEN前缀格式
    public static final String USER_HEADER_TOKEN_KEY = "%s-usertoken";//ERP验证TOKEN

    
    /***********************************************缓存LOCK*************************************************************************/
    
    public static final String PAY_LOCK_KEY 				= "PRIORITY:LOCK:SERVICE:PAY";//支付锁
    

    /***********************************************缓存常亮*************************************************************************/
    
    public static final String PRI_ACC_SMS_MIN_KEY 			= "PRIORITY:VAR:SERVICE:MIN:KEY:";
    
    public static final String PRI_ACC_SMS_HOUR_KEY 		= "PRIORITY:VAR:SERVICE:HOUR:KEY:";
    
    public static final String PRI_ACC_SMS_DAY_KEY 			= "PRIORITY:VAR:SERVICE:DAY:KEY:";

    
    public static final String HEADER_TOEKN_USERINFO 		= "PRIORITY:VAR:SERVICE:CACHE:app-login-cache-%s";//APP登录后缓存
    
    public static final String ERP_PERMISSION_CACHE			="PRIORITY:VAR:SERVICE:PERMISSION:erp-permission-cache-%s";//ERP权限认证缓存前缀
    
    public static final String ERP_LOGIN_CACHE				="PRIORITY:VAR:SERVICE:CACHE:erp-login-cache-%s";//ERP登录缓存前缀

    public static final String IP_INTERFACE_LIMIT			="PRIORITY:VAR:SERVICE:IP:IP_INTERFACE_LIMIT";//按IP地址和请求Url控制访问次数
    
    public static final String IP_INVALID_COUNT				="PRIORITY:VAR:SERVICE:IP:IP_INVALID_COUNT";//访问频繁的IP 出现次数
    
    public static final String IP_SENDCAPTCHACOUNT			="PRIORITY:VAR:SERVICE:IP:IP_SENDCAPTCHACOUNT";//ip地址发送短信次数限制
    
    public static final String IP_BLACKLIST					="PRIORITY:VAR:SERVICE:IP:IP_BLACKLIST";//ip 黑名单
    /* 发送验证码常量 */
    public static final String PRI_ACC_VCODE_SMS_KEY 		="PRIORITY:VAR:SERVICE:ACCOUNTS:VALIDATECODE_SMS_KEY:";//钱包密码验证码

    public static final String PRI_USER_PUSH_DAY_KEY       ="PRIORITY:VAR:SERVICE:PUSH:DAY:KEY";//用户推送
    
    
    public static final String PRI_COUNTRY_KEY       ="PRIORITY:VAR:SERVICE:COUNTRY:ALL";
    
    public static final String PRI_COUNTRY_PROVINCE_KEY       ="PRIORITY:VAR:SERVICE:PROVINCE:";
    
    public static final String PRI_COUNTRY_CITY_KEY       ="PRIORITY:VAR:SERVICE:CITY:";
    
    public static final String PRI_COUNTRY_BANK_KEY       ="PRIORITY:VAR:SERVICE:BANK:ALL";
    
    public static final String PRI_COUNTRY_FUND_KEY       ="PRIORITY:VAR:SERVICE:FUND:ALL";

    public static final String PRI_USER_WX_TOKEN           ="PRIORITY:VAR:SERVICE:WX:TOKEN";
    
    
    public static final String PRI_STORE_COUNT          ="PRIORITY:VAR:SERVICE:STORE:COUNT";

    /**********************************************第三方登陆获取信息*************************************************************************/
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
