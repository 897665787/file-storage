package com.jqdi.filestorage.core.alioss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.VoidResult;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 阿里云OSS客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class AliossClient {

	private OSS client = null;
	private String endpoint = null;

	public AliossClient(String endpoint, String accessKey, String secretKey) {
		CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKey, secretKey);
		this.client = OSSClientBuilder.create().endpoint(endpoint).credentialsProvider(credentialsProvider).build();
		this.endpoint = endpoint;
	}

	public String putObject(String bucketName, String key, InputStream input) {

		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, input);
		
		putObjectRequest.setProgressListener(progressEvent -> {
			long bytes = progressEvent.getBytes();
			ProgressEventType eventType = progressEvent.getEventType();
			log.info("bytes:{},eventType:{}", bytes, eventType);
		});
		
		PutObjectResult putObjectResult = client.putObject(putObjectRequest);
		String eTag = putObjectResult.getETag();
		String versionId = putObjectResult.getVersionId();
		String requestId = putObjectResult.getRequestId();
		Long clientCRC = putObjectResult.getClientCRC();
		Long serverCRC = putObjectResult.getServerCRC();
		
		String uri = Optional.ofNullable(putObjectResult.getResponse()).map(ResponseMessage::getUri).orElse(null);
		Map<String, String> headers = Optional.ofNullable(putObjectResult.getResponse())
				.map(ResponseMessage::getHeaders).orElse(null);
		log.info("eTag:{},versionId:{},requestId:{},clientCRC:{},serverCRC:{},uri:{},headers:{}", eTag, versionId,
				requestId, clientCRC, serverCRC, uri, headers);
		
		// 文件URL的格式为https://BucketName.Endpoint/ObjectName
		String url = String.format("https://%s.%s/%s", bucketName, endpoint, key);
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
		OSSObject ossObject = client.getObject(bucketName, key);

		String requestId = ossObject.getRequestId();
		Long clientCRC = ossObject.getClientCRC();
		Long serverCRC = ossObject.getServerCRC();

		ResponseMessage response = ossObject.getResponse();
		String uri = response.getUri();
		Map<String, String> headers = response.getHeaders();
		log.info("bucketName:{},key:{},requestId:{},clientCRC:{},serverCRC:{},uri:{},headers:{}",
				ossObject.getBucketName(), ossObject.getKey(), requestId, clientCRC, serverCRC, uri, headers);

		InputStream inputStream = ossObject.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		VoidResult voidResult = client.deleteObject(bucketName, key);
		
		String requestId = voidResult.getRequestId();
		Long clientCRC = voidResult.getClientCRC();
		Long serverCRC = voidResult.getServerCRC();

		ResponseMessage response = voidResult.getResponse();
		String uri = response.getUri();
		Map<String, String> headers = response.getHeaders();
		log.info("requestId:{},clientCRC:{},serverCRC:{},uri:{},headers:{}", requestId, clientCRC, serverCRC, uri,
				headers);
	}
}