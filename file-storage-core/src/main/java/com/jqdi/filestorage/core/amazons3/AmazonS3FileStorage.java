package com.jqdi.filestorage.core.amazons3;

import com.jqdi.filestorage.core.FileStorage;
import org.apache.commons.lang3.time.DateUtils;

import java.io.InputStream;
import java.util.Date;

/**
 * 亚马逊AWS S3
 * 
 * @author JQ棣
 *
 */
public class AmazonS3FileStorage implements FileStorage {
	private AmazonS3Client client = null;
	private String bucketName = null;

	public AmazonS3FileStorage(String endpoint, String region, String accessKey, String secretKey, String bucketName) {
		this.client = new AmazonS3Client(endpoint, region, accessKey, secretKey);
		this.bucketName = bucketName;
	}

	@Override
	public void upload(InputStream inputStream, String fileKey) {
		client.putObject(bucketName, fileKey, inputStream);
	}

	@Override
	public String uploadPresignedUrl(String fileKey) {
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