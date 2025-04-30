package com.jqdi.filestorage.core.amazons3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import com.jqdi.filestorage.core.util.Utils;

import java.io.InputStream;
import java.net.URL;

/**
 * 亚马逊AWS S3客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class AmazonS3Client {

	private AmazonS3 client = null;

	public AmazonS3Client(String endpoint, String region, String accessKey, String secretKey) {
		AmazonS3ClientBuilder builder = com.amazonaws.services.s3.AmazonS3Client.builder()
				.withAccelerateModeEnabled(false)//加速模式
				.withPathStyleAccessEnabled(true) // 禁用路径样式访问
//				.disableChunkedEncoding()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
				.withClientConfiguration(new ClientConfiguration().withMaxConnections(100))
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)));
		client = builder.build();
	}

	public String putObject(String bucketName, String key, InputStream input) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(Utils.guessContentType(key));
		new PutObjectRequest(bucketName, key, input, metadata);
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

	public String listObjects(String bucketName) {
		ObjectListing objectListing = client.listObjects(bucketName);
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			String key = objectSummary.getKey();
			String eTag = objectSummary.getETag();
			long size = objectSummary.getSize();
			log.info("key:{},eTag:{},size:{}", key, eTag, size);

			URL URL = client.getUrl(bucketName, key);
			String url = URL.toString();
			log.info("url:{}", url);
		}
		return null;
	}

	public String presignedUrl(String bucketName, String key) {
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);
		URL URL = client.generatePresignedUrl(generatePresignedUrlRequest);
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