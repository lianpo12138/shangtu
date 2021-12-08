package com.dubu.turnover.utils;



import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件验证
 * 
 * @author trunks
 *
 */
public class FileUtil {

    public static final String ENCODING = "UTF-8";
    private static Logger logger = Logger.getLogger(FileUtil.class);
    
    /**10MB*/
    public static long MAX_FILE_SIZE = 1024 * 1024 * 10;

    /**上传文件校验 @author trunks*/
    public static boolean validSize(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            return false;
        }
        return true;
    }

    /**获取后缀名(包含.)，如果没有后缀，返回""  @author trunks*/
    public static String getSuffix(String filename) {
        if (StringUtils.isBlank(filename) || filename.indexOf(".") == -1) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".")).trim();
    }

    /**
     * 移动文件到指定target
     * 
     * @param source 被移动文件
     * @param target 目标路径
     * @throws IOException 如果在service中调用，请在catch中 throw ServiceException，确保事务回滚
     * @author trunks
     */
    public static void transferFile(MultipartFile source, String target) throws IOException {
        File targetFile = new File(target);
        targetFile.getParentFile().mkdirs();
        source.transferTo(targetFile);
    }

    /**
     * 遍历制定目录
     * 
     * @param directory
     *            目录
     * @param extensions
     *            允许的扩展名
     * @param recursive
     *            是否递归子目录
     */
    public static Iterator<File> searchFile(File directory, String[] extensions, boolean recursive)
                                                                                                   throws Exception {
        if (!directory.exists()) {
            throw new Exception("目录不存在：" + directory.getAbsolutePath());
        }
        extensions = FileUtil.expandSuffix(extensions);
        return FileUtils.iterateFiles(directory, extensions, recursive);
    }

    /** 将后缀名复制为大写、小写各一份，并去除重复 */
    public static String[] expandSuffix(String[] extensions) {
        if (extensions != null) {
            Set<String> suffix = new HashSet<String>();
            for (String ext : extensions) {
                suffix.add(StringUtils.lowerCase(ext));
                suffix.add(StringUtils.upperCase(ext));
            }
            extensions = suffix.toArray(new String[suffix.size()]);
        }
        return extensions;
    }

    /** 全角转半角 */
    public static String toDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * 补全路径，在路径后增加'/'
     * 
     * @param path
     * @return
     */
    public static String complePath(String path) {
        if (StringUtils.isBlank(path) || path.endsWith("/")) {
            return path;
        }
        return path + "/";
    }

    /**
     * 将文件重命名为全小写(包括后缀名)
     * @param source 源文件
     * @return 重命名后的文件
     * @throws IOException 重命名失败
     */
    public static File renameToLowerCase(File source) throws IOException {
        String lower = source.getName().toLowerCase();
        if (source.getName().equals(lower)) {
            return source;
        }
        File dest = new File(FileUtil.complePath(source.getParent()) + lower);
        if (!source.renameTo(dest)) {
            throw new IOException("图片重命名失败：" + source.getName());
        }
        return dest;
    }

    /**将指定目录下的文件名全部转成小写*/
    public static void renameFolderFile(File directory, String[] extensions, boolean recursive)
                                                                                               throws IOException {
//        Iterator<File> files = FileUtil.searchFile(directory, extensions, recursive);
//        while (files.hasNext()) {
//            FileUtil.renameToLowerCase(files.next());
//        }
    }
    
    /**
     * 读取src目录下的配置文件。
     * 
     * @param source 配置文件名，不带"/"
     * @return 文件内容
     */
    public static String readSourceAsString(String source) {
        try {
            URI uri = FileUtil.class.getResource("/" + source).toURI();
            return FileUtils.readFileToString(new File(uri), ENCODING);
        } catch (Exception e) {
            logger.error("读取文件出错", e);
            return null;
        }
    }

    /**
     * 获取网络文件大小
     * @author Administrator
     * @date   2015年12月24日
     * @param fileUrl
     * @return
     */
    public static Integer getFileSizeByUrl(String fileUrl){
    	try {
			URL url = new URL(fileUrl);
			URLConnection uc = url.openConnection();
			return uc.getContentLength() / 1024;
		} catch (Exception e) {
			return 0;
		}
    	
    }
    
//    public static void main(String[] args) {
//		System.out.println(getFileSizeByUrl("http://zsscache.zhao-cloud.com/2015/12/23/1328086894_9c24cefd385abe1e7edc4224eb62bc57.jpg"));
//	}
}
