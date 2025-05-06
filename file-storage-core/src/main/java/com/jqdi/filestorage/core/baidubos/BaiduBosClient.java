package com.jqdi.filestorage.core.baidubos;

import com.baidubce.Protocol;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.baidubce.services.bos.BosObjectInputStream;
import com.baidubce.services.bos.model.*;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;

/**
 * 百度BOS客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class BaiduBosClient {

	private BosClient client = null;
	private String endpoint = null;
	
	public BaiduBosClient(String endpoint, String accessKey, String secretKey) {
		BosClientConfiguration config = new BosClientConfiguration();
		config.setCredentials(new DefaultBceCredentials(accessKey, secretKey));
		config.setEndpoint(endpoint);
		config.setProtocol(Protocol.HTTPS);
		client = new BosClient(config);
		
		this.endpoint = endpoint;
	}

	public String putObject(String bucketName, String key, InputStream input) {
		ObjectMetadata metadata = new ObjectMetadata();
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, input, metadata);

		PutObjectResponse putObjectResponse = client.putObject(putObjectRequest);
		String eTag = putObjectResponse.getETag();
		log.info("eTag:{}", eTag);
		
		// 文件URL的格式为https://BucketName.Endpoint/ObjectName
		String url = String.format("https://%s.%s/%s", bucketName, endpoint, key);
		log.info("url:{}", url);
		
		return url;
	}

	public String presignedUrl(String bucketName, String key, int expirationInSeconds) {
		URL url = client.generatePresignedUrl(bucketName, key, expirationInSeconds);
		String presignedUrl = url.toString();
		log.info("presignedUrl:{}", presignedUrl);
		return presignedUrl;
	}

	public InputStream getObject(String bucketName, String key) {
		GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
		BosObject bosObject = client.getObject(getObjectRequest);

		log.info("bucketName:{},key:{}", bosObject.getBucketName(), bosObject.getKey());

		BosObjectInputStream inputStream = bosObject.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}
}