package com.jqdi.filestorage.core.local;

import com.jqdi.filestorage.core.FileStorage;

import java.io.InputStream;

/**
 * 本地磁盘
 * 
 * @author JQ棣
 *
 */
public class LocalFileStorage implements FileStorage {
	private LocalClient client = null;
	private String bucketName = null;

	public LocalFileStorage(String endpoint, String bucketName) {
		this.client = new LocalClient(endpoint);
		this.bucketName = bucketName;
	}

	@Override
	public void upload(InputStream inputStream, String fileKey) {
		client.putObject(bucketName, fileKey, inputStream);
	}

	@Override
	public String clientUpload(String fileKey) {
		return client.presignedUrlPut(bucketName, fileKey);
	}

	@Override
	public String presignedUrl(String fileKey) {
		return client.presignedUrl(bucketName, fileKey);
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