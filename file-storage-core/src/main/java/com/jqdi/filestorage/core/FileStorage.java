package com.jqdi.filestorage.core;

import java.io.InputStream;

public interface FileStorage {
    /**
     * 上传文件
     *
     * @param inputStream
     * @param fileKey
     */
    void upload(InputStream inputStream, String fileKey);

    /**
     * 预签名链接（上传文件）
     *
     * @param fileKey
     * @return 预签名链接
     */
    String uploadPresignedUrl(String fileKey);

    /**
     * 预签名链接（下载）
     *
     * @param fileKey
     * @return 预签名链接
     */
    String presignedUrl(String fileKey);

    /**
     * 下载文件流
     *
     * @param fileKey
     * @return
     */
    InputStream download(String fileKey);

    /**
     * 删除文件
     *
     * @param fileKey
     * @return
     */
    void remove(String fileKey);
}