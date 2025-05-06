package com.jqdi.filestorage.core;

import java.io.InputStream;

public interface FileStorage {
	/**
	 * 上传文件
	 * 
	 * @param inputStream
	 * @param fileName
	 * @return
	 */
	FileUrl upload(InputStream inputStream, String fileName);

	/**
	 * 预览文件链接
	 *
	 * @param fileName
	 * @return
	 */
	String presignedUrl(String fileName);

	/**
	 * 下载文件流
	 * 
	 * @param fileName
	 * @return
	 */
	InputStream download(String fileName);

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	void remove(String fileName);
}