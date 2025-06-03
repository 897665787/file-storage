package com.jqdi.filestorage.core;

import java.io.InputStream;

public interface FileStorage {
    /**
     * 上传文件
     *
     * @param inputStream 文件流
     * @param fileKey
     */
    void upload(InputStream inputStream, String fileKey);

    /**
     * 上传文件（客户端）
     *
     * @param fileKey
     * @return 预签名链接
     */
    String clientUpload(String fileKey);

    /**
     * 预签名链接（下载）
     *
     * @param fileKey
     * @return 预签名链接
     */
    String presignedUrl(String fileKey);

    /**
     * 下载文件
     *
     * @param fileKey
     * @return 文件流
     */
    InputStream download(String fileKey);

    /**
     * 删除文件
     *
     * @param fileKey
     */
    void remove(String fileKey);
}