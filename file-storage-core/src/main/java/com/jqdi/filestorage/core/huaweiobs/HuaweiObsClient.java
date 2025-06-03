package com.jqdi.filestorage.core.huaweiobs;

import com.jqdi.filestorage.core.util.Utils;
import com.obs.services.ObsClient;
import com.obs.services.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

/**
 * 华为云OBS客户端
 *
 * @author JQ棣
 *
 */
@Slf4j
public class HuaweiObsClient {

	private ObsClient client = null;

	public HuaweiObsClient(String endpoint, String accessKey, String secretKey) {
		client = new ObsClient(accessKey, secretKey, endpoint);
	}

	public void putObject(String bucketName, String objectKey, InputStream input) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(Utils.guessContentType(objectKey));
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, input);
		putObjectRequest.setMetadata(metadata);

		putObjectRequest.setProgressListener(progressEvent -> {
			long bytes = progressEvent.getTotalBytes();
			int transferPercentage = progressEvent.getTransferPercentage();
			log.info("bytes:{},transferPercentage:{}", bytes, transferPercentage);
		});

		PutObjectResult putObjectResult = client.putObject(putObjectRequest);
		String eTag = putObjectResult.getEtag();
		String versionId = putObjectResult.getVersionId();
		String requestId = putObjectResult.getRequestId();
		String url = putObjectResult.getObjectUrl();

		log.info("eTag:{},versionId:{},requestId:{},url:{}", eTag, versionId, requestId, url);
	}

	public String presignedUrlPut(String bucketName, String key, Date expiryTime) {
		String presignedUrl = client.createSignedUrl(HttpMethodEnum.PUT, bucketName, key, SpecialParamEnum.LOCATION, expiryTime, new HashMap<>(), new HashMap<>());
		log.info("presignedUrl:{}", presignedUrl);
		return presignedUrl;
	}

	public String presignedUrl(String bucketName, String key, Date expiryTime) {
		String presignedUrl = client.createSignedUrl(HttpMethodEnum.GET, bucketName, key, SpecialParamEnum.LOCATION, expiryTime, new HashMap<>(), new HashMap<>());
		log.info("presignedUrl:{}", presignedUrl);
		return presignedUrl;
	}

	public InputStream getObject(String bucketName, String key) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
		ObsObject obsObject = client.getObject(getObjectRequest);
		ObjectMetadata objectMetadata = obsObject.getMetadata();
		String requestId = objectMetadata.getRequestId();

		log.info("bucketName:{},key:{},requestId:{}", obsObject.getBucketName(), obsObject.getObjectKey(), requestId);

		InputStream inputStream = obsObject.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}
}
