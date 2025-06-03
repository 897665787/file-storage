package com.jqdi.filestorage.core.alioss;

import com.jqdi.filestorage.core.FileStorage;
import org.apache.commons.lang3.time.DateUtils;

import java.io.InputStream;
import java.util.Date;

/**
 * 阿里云OSS
 * 
 * @author JQ棣
 *
 */
public class AliossFileStorage implements FileStorage {
	private AliossClient client = null;
	private String bucketName = null;

	public AliossFileStorage(String endpoint, String accessKey, String secretKey, String bucketName) {
		this.client = new AliossClient(endpoint, accessKey, secretKey);
		this.bucketName = bucketName;
	}

	@Override
	public void upload(InputStream inputStream, String fileKey) {
		client.putObject(bucketName, fileKey, inputStream);
	}

	@Override
	public String clientUpload(String fileKey) {
		Date expiration = DateUtils.addSeconds(new Date(), 3600);
		return client.presignedUrlPut(bucketName, fileKey, expiration);
	}

	@Override
	public String presignedUrl(String fileKey) {
		Date expiration = DateUtils.addSeconds(new Date(), 3600);
		return client.presignedUrl(bucketName, fileKey, expiration);
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