package com.jqdi.filestorage.core.amazons3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.event.ProgressEventType;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import com.jqdi.filestorage.core.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * 亚马逊AWS S3客户端(STS模式)
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class AmazonS3STSClient {

	private final AmazonS3 client;

	public AmazonS3STSClient(String region, String accessKey, String secretKey, String roleArn, String roleSessionName) {

        AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
				.withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();

		STSAssumeRoleSessionCredentialsProvider credentialsProvider = new STSAssumeRoleSessionCredentialsProvider.Builder(roleArn, roleSessionName)
				.withStsClient(stsClient)
				.build();

		AmazonS3ClientBuilder builder = com.amazonaws.services.s3.AmazonS3Client.builder()
//				.withPathStyleAccessEnabled(true) // 路径样式访问
				.withCredentials(credentialsProvider)
				.withRegion(region);
		client = builder.build();
	}

	public String putObject(String bucketName, String key, InputStream input) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(Utils.guessContentType(key));
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
		URL url = client.generatePresignedUrl(bucketName, key, expiration);// 过期时间最长7天
		String presignedUrl = url.toString();
		log.info("presignedUrl:{}", presignedUrl);
		return presignedUrl;
	}

	public InputStream getObject(String bucketName, String key) {
		S3Object s3Object = client.getObject(bucketName, key);
		log.info("bucketName:{},key:{}", s3Object.getBucketName(), s3Object.getKey());

		InputStream inputStream = s3Object.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}
}