package com.dubu.turnover.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.springframework.util.StringUtils;

import com.dubu.turnover.service.UserAccountsService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片处理工具
 * <p>
 * Date: 2018/1/16
 * Time: 10:00
 */
public class ImgUtil {

    private static final Log LOG = LogFactory.getLog(ImgUtil.class);

    /**
     * 图片转字节数组
     *
     * @param imgUrl
     * @return
     */
    public static byte[] getImgByte(String imgUrl) {
        InputStream inputStream = readInStream(imgUrl);
        if (inputStream != null) {
            return toByteArray(inputStream, getImgType(imgUrl));
        }
        return null;
    }

    /**
     * 读取远程图片地址
     *
     * @param imgUrl 图片url
     * @return
     */
    public static InputStream readInStream(String imgUrl) {
        try {
            URL url = new URL(imgUrl);
            //打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置请求方式为"GET"
            conn.setRequestMethod("GET");
            //超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //通过输入流获取图片数据
            InputStream inStream = conn.getInputStream();
            return inStream;
        } catch (Exception e) {
            LOG.error("获取远程图片流失败", e);
        }
        return null;
    }

    /**
     * 获取图片文件格式，只支持jpg,jpeg,png，其他格式默认用jpg
     *
     * @param imgUrl
     * @return
     */
    public static String getImgType(String imgUrl) {
        String fileType = "jpg";
        if (!StringUtils.isEmpty(imgUrl) && imgUrl.indexOf(".") != -1) {
            String tempType = imgUrl.substring(imgUrl.lastIndexOf(".") + 1).trim();
            if ("jpg".equalsIgnoreCase(tempType) || "jpeg".equalsIgnoreCase(tempType) || "png".equalsIgnoreCase(tempType)) {
                fileType = tempType;
            }
        }
        return fileType;
    }

    /**
     * @param inputStream
     * @param format      图片格式，只支持“jpg”，“jpeg”，“png”
     * @return
     * @throws Exception
     */
    public static byte[] toByteArray(InputStream inputStream, String format) {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        try {
            BufferedImage img = ImageIO.read(inputStream);
            ImageIO.write(img, format, buf);
        } catch (Exception e) {
            LOG.error("inputStream 转成ByteArray 异常", e);
            return null;
        }
        return buf.toByteArray();
    }
}
