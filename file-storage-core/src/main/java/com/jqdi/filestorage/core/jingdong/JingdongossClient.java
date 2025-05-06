package com.jqdi.filestorage.core.jingdong;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 京东云客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class JingdongossClient {

	private AmazonS3 client = null;

	public JingdongossClient(String endpoint, String accessKey, String secretKey) {
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)));
		builder.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, "CHINA-HK"));
		client = builder.build();
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
		
		log.info("eTag:{},versionId:{}", eTag, versionId);
		
		URL URL = client.getUrl(bucketName, key);
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
		S3Object s3Object = client.getObject(getObjectRequest);
		log.info("bucketName:{},key:{}", s3Object.getBucketName(), s3Object.getKey());

		InputStream inputStream = s3Object.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}
}