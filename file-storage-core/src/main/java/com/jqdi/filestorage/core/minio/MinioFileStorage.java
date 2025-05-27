package com.jqdi.filestorage.core.minio;

import com.jqdi.filestorage.core.FileStorage;

import java.io.InputStream;

/**
 * minio
 * 
 * @author JQ棣
 *
 */
public class MinioFileStorage implements FileStorage {

	private MinioClient client;
	private String bucketName;
	
	public MinioFileStorage(String endpoint, String accessKey, String secretKey, String bucketName) {
		this.client = new MinioClient(endpoint, accessKey, secretKey);
		this.bucketName = bucketName;
	}

	@Override
	public void upload(InputStream inputStream, String fileKey) {
		client.putObject(bucketName, inputStream, fileKey);
	}

	@Override
	public String uploadPresignedUrl(String fileKey) {
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
		client.removeObject(bucketName, fileKey);
	}
}