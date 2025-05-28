package com.jqdi.filestorage.core.huaweiobs;

import com.jqdi.filestorage.core.FileStorage;
import org.apache.commons.lang3.time.DateUtils;

import java.io.InputStream;
import java.util.Date;

/**
 * 华为云OBS
 * 
 * @author JQ棣
 *
 */
public class HuaweiObsFileStorage implements FileStorage {
	private HuaweiObsClient client = null;
	private String bucketName = null;

	public HuaweiObsFileStorage(String endpoint, String accessKey, String secretKey, String bucketName) {
		this.client = new HuaweiObsClient(endpoint, accessKey, secretKey);
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