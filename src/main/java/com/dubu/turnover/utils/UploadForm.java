package com.dubu.turnover.utils;



/**
 * FTP上传实体类
 * @author hejinguo
 * @version $Id: UploadForm.java, v 0.1 2012-4-6 上午10:07:42
 */
public class UploadForm {
    /**
     * 上传图片序号
     */
    private String fileId;
    /**
     * 上传图片名称                                             
     */
    private String fileName;
    /**
     * 上传图片路径
     */
    private String filePath;
    /**
     * 上传图片服务器路径
     */
    private String fileUrl;
    /**
     * 上传图片大小
     */
    private String fileSize;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 返回提示信息
     */
    private String message;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}
