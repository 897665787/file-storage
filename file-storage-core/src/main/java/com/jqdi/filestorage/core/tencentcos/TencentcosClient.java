package com.jqdi.filestorage.core.tencentcos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.endpoint.UserSpecifiedEndpointBuilder;
import com.qcloud.cos.event.ProgressEventType;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 腾讯云COS客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class TencentcosClient {

	private COSClient client = null;

	public TencentcosClient(String endpoint, String accessKey, String secretKey) {
		COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);

		ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));

		UserSpecifiedEndpointBuilder endpointBuilder = new UserSpecifiedEndpointBuilder(endpoint, endpoint);
		clientConfig.setEndpointBuilder(endpointBuilder);

		client = new COSClient(cred, clientConfig);
	}

	public String putObject(String bucketName, String key, InputStream input) {
		ObjectMetadata metadata = new ObjectMetadata();
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, input, metadata);

		putObjectRequest.setGeneralProgressListener(progressEvent -> {
			long bytes = progressEvent.getBytes();
			ProgressEventType eventType = progressEvent.getEventType();
			log.info("bytes:{},eventType:{}", bytes, eventType);
		});

		PutObjectResult putObjectResult = client.putObject(putObjectRequest);
		String eTag = putObjectResult.getETag();
		String versionId = putObjectResult.getVersionId();
		String requestId = putObjectResult.getRequestId();

		log.info("eTag:{},versionId:{},requestId:{}", eTag, versionId, requestId);

		URL URL = client.getObjectUrl(bucketName, key);
		String url = URL.toString();
		log.info("url:{}", url);
		return url;
	}

	public String presignedUrl(String bucketName, String key, Date expiration) {
		URL URL = client.generatePresignedUrl(bucketName, key, expiration);
		String presignedUrl = URL.toString();
		log.info("presignedUrl:{}", presignedUrl);
		return presignedUrl;
	}

	public InputStream getObject(String bucketName, String key) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
		COSObject cosObject = client.getObject(getObjectRequest);

		ObjectMetadata objectMetadata = cosObject.getObjectMetadata();
		String requestId = objectMetadata.getRequestId();

		log.info("bucketName:{},key:{},requestId:{}", cosObject.getBucketName(), cosObject.getKey(), requestId);

		COSObjectInputStream inputStream = cosObject.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}
}