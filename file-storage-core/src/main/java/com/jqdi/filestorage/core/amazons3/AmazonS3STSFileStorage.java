package com.jqdi.filestorage.core.amazons3;

import com.jqdi.filestorage.core.FileStorage;
import org.apache.commons.lang3.time.DateUtils;

import java.io.InputStream;
import java.util.Date;

/**
 * 亚马逊AWS S3(STS模式)
 * 
 * @author JQ棣
 *
 */
public class AmazonS3STSFileStorage implements FileStorage {
	private AmazonS3STSClient client = null;
	private String bucketName = null;

	public AmazonS3STSFileStorage(String region, String accessKey, String secretKey, String roleArn, String roleSessionName, String bucketName) {
		this.client = new AmazonS3STSClient(region, accessKey, secretKey, roleArn, roleSessionName);
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