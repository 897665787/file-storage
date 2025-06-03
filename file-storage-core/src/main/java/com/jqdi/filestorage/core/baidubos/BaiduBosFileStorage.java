package com.jqdi.filestorage.core.baidubos;

import com.jqdi.filestorage.core.FileStorage;

import java.io.InputStream;

/**
 * 百度BOS
 * 
 * @author JQ棣
 *
 */
public class BaiduBosFileStorage implements FileStorage {
	private BaiduBosClient client = null;
	private String bucketName = null;
	
	public BaiduBosFileStorage(String endpoint, String accessKey, String secretKey, String bucketName) {
		this.client = new BaiduBosClient(endpoint, accessKey, secretKey);
		this.bucketName = bucketName;
	}

	@Override
	public void upload(InputStream inputStream, String fileKey) {
		client.putObject(bucketName, fileKey, inputStream);
	}

	@Override
	public String clientUpload(String fileKey) {
		return client.presignedUrlPut(bucketName, fileKey, 3600);
	}

	@Override
	public String presignedUrl(String fileKey) {
		return client.presignedUrl(bucketName, fileKey, 3600);
	}

	@Override
	public InputStream download(String fileKey) {
		return client.getObject(bucketName, fileKey);
	}

	@Override
	public void remove(String fileKey) {
		client.deleteObject(bucketName, fileKey);
	}
}