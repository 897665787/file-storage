package com.jqdi.filestorage.core.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 本地磁盘客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class LocalClient {
	private String endpoint = null;

	public LocalClient(String endpoint) {
		this.endpoint = endpoint;
	}

	@SneakyThrows
	public String putObject(String bucketName, String key, InputStream input) {
		// bucketName->根目录
		File resultFile = new File(endpoint + File.separator + bucketName + File.separator + key);
		FileUtils.copyToFile(input, resultFile);

		// 文件URL的格式为endpoint/bucketName/key
		String url = resultFile.getAbsolutePath();
		log.info("url:{}", url);
		return url;
	}

	@SneakyThrows
	public InputStream getObject(String bucketName, String key) {
		File file = new File(endpoint + File.separator + bucketName + File.separator + key);
		return new FileInputStream(file);
	}

	@SneakyThrows
	public void deleteObject(String bucketName, String key) {
		File file = new File(endpoint + File.separator + bucketName + File.separator + key);
		file.deleteOnExit();
	}
}