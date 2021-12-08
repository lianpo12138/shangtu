package com.dubu.turnover.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.dubu.turnover.component.config.SysConfig;
import com.dubu.turnover.component.redis.RedisClient;
import com.dubu.turnover.configure.Configurer;
import com.dubu.turnover.controller.common.UploadController;
import com.dubu.turnover.domain.rest.OSInfo;
import com.dubu.turnover.service.UserAuthService;
import com.dubu.turnover.utils.Hmac;
import com.dubu.turnover.utils.JsonUtils;
import com.dubu.turnover.utils.RequestUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 此拦截器主要是验证客户端是否合法
 * <p>
 * Date: 2017/11/1
 * Time: 20:21
 */
@Component
public class AuthUtil {


    private static final Logger LOG = LoggerFactory.getLogger(AuthUtil.class);


    @Autowired
    UserAuthService userAuthService;

    @Autowired
    RedisClient redisUtils;

    private Map<String, String> secretMap = new HashMap<>();

    @PostConstruct
    public void init() {

    }

    /**
     * 检查ip 在second 秒内访问次数是否超过limitCount
     *
     * @param request
     * @param second
     * @param limitCount
     * @return
     */
    public boolean isIpRequestLimitValid(HttpServletRequest request, long second, int limitCount) {
        String uri = request.getRequestURI();
        String ip = RequestUtils.getIP();
        String method = request.getMethod();
        LOG.info("访问IP:{},url:{}", ip, uri);
        if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(uri)) {
            uri = uri.substring(uri.indexOf("/") + 1);
            String key = Configurer.IP_INTERFACE_LIMIT + method + "_" + uri + ":" + ip;
            Integer times = redisUtils.increment(key);
            if (times != null) {
                if (times.intValue() == 1) {
                    redisUtils.expire(key, 2L, TimeUnit.SECONDS);
                } else {
                    if (times > limitCount) {
                        LOG.error("IP:{}在{}秒内访问接口:{} {}次，超过最大限制：{}", ip, second, method + "_" + uri, times, limitCount);
                        //记录次数
                        Integer invalidIpCount = redisUtils.increment(Configurer.IP_INVALID_COUNT + ip);
                        if (invalidIpCount != null) {
                            if (invalidIpCount.intValue() == 1) {
                                redisUtils.expire(Configurer.IP_INVALID_COUNT + ip, 60L, TimeUnit.SECONDS);
                            }
                            if (invalidIpCount.intValue() > 100) {
                                LOG.error("IP:{}在{}s内访问超限{}次", ip, 60, invalidIpCount);
                                if (redisUtils.putHash("ip_blacklist")) {
                                    redisUtils.delete(Configurer.IP_BLACKLIST);
                                    List<String> list = new ArrayList<>();
                                    list.add(ip);
                                    redisUtils.put(Configurer.IP_BLACKLIST, list, 24 * 60 * 60L);
                                    LOG.error("{}访问太过频繁被加入黑名单", ip);
                                } else {
                                    List<String> blackList = redisUtils.get(Configurer.IP_BLACKLIST);
                                    if (!CollectionUtils.isEmpty(blackList) && !blackList.contains(ip)) {
                                        redisUtils.put(Configurer.IP_BLACKLIST, ip);
                                        LOG.error("{}访问太过频繁被加入黑名单,当前黑名单数量:{}", ip, blackList.size() + 1);
                                    }
                                }
                            }
                            return false;
                        }
                    }
                }
            }

        }
        return true;
    }

    /**
     * UUID限制，相同的url，10秒内只能请求2次
     *
     * @return
     */
    public boolean isUUidValid() {
        String uri = RequestUtils.getRequest().getRequestURI();
        String ip = RequestUtils.getIP();
        OSInfo osInfo = RequestUtils.getOSInfo();
        OSInfo.Platform os = osInfo.getOs();
        //appVersion
        String appVersion = osInfo.getAppVersion();
        if (os != OSInfo.Platform.WX_XCX && (StringUtils.isEmpty(appVersion) || getAppVersion(appVersion) > 2.3d)) {
            //授权码格式：
            String authorization = osInfo.getAuthorization();
            if (authorization == null) {
                return false;
            }
            if (authorization.split(" ").length != 3) {
                return false;
            }
            //uuid
            String uuid = authorization.substring(authorization.lastIndexOf(" ") + 1);
            String key = Configurer.UUID_COUNT + uuid;
            int limitCount = 1;
            int uuidCount = redisUtils.increment(key);
            if (uuidCount == 1) {
                redisUtils.expire(key, 5L, TimeUnit.SECONDS);
            }
            if (uuidCount > limitCount) {
                LOG.error("ip:{}, uri:{},uuid:{} 在5秒内请求超过{}次，禁止访问", ip, uri, uuid, limitCount);
                return false;
            }
        }
        return true;
    }

    /**
     * @param appVersion
     * @return
     */
    public static double getAppVersion(String appVersion) {
        double version = 0;
        if (!StringUtils.isEmpty(appVersion)) {
            if (appVersion.contains(".") && appVersion.split("\\.").length > 2) {
                String pre = appVersion.substring(0, appVersion.indexOf("."));
                String suff = appVersion.substring(appVersion.indexOf(".") + 1);
                suff = suff.replace(".", "");
                appVersion = pre + "." + suff;
            }
            version = Double.parseDouble(appVersion);
        }
        return version;
    }

    /**
     * 黑名单检查
     *
     * @return
     */
    public boolean isblackListValid() {
        String ip = RequestUtils.getIP();
        List<String> blackList = redisUtils.get(Configurer.IP_BLACKLIST);
        if (!CollectionUtils.isEmpty(blackList) && blackList.contains(ip)) {
            LOG.error("ip:{}在黑名单内,拒绝访问", ip);
            return false;
        }
        return true;
    }


    /**
     * 验证码接口次数限制
     *
     * @return
     */
    public boolean sendCaptchaCountLimit() {
        int limitCount = 20;
        String ip = RequestUtils.getIP();
        String key = Configurer.IP_SENDCAPTCHACOUNT + ip;
        Integer times = redisUtils.increment(key);
        if (times != null && times.intValue() == 1) {
            redisUtils.expire(key, 24 * 60 * 60L, TimeUnit.SECONDS);
        }
        LOG.info("ip:{},当天请求发送验证码接口次数：{}", ip, times);
        if (times != null && times > limitCount) {
            LOG.error("发送验证码次数：{}超过限制{},请明天重试", times, limitCount);
            return false;
        }
        return true;
    }

}
