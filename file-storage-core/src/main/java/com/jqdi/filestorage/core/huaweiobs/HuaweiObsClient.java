package com.jqdi.filestorage.core.huaweiobs;

import java.io.InputStream;

import com.obs.services.ObsClient;
import com.obs.services.model.GetObjectRequest;
import com.obs.services.model.ObjectMetadata;
import com.obs.services.model.ObsObject;
import com.obs.services.model.PutObjectRequest;
import com.obs.services.model.PutObjectResult;

import lombok.extern.slf4j.Slf4j;

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

	public String putObject(String bucketName, String objectKey, InputStream input) {
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, input);

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
		return url;
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