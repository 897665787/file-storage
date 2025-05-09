package com.jqdi.filestorage.springbootdemo.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
public class UploadController {

	@Value("${aws.region}")
	private String region;

	@Value("${aws.bucket.name}")
	private String bucketName;

	@Value("${aws.access.key}")
	private String accessKey;

	@Value("${aws.secret.key}")
	private String secretKey;

	@Value("${aws.sts.role.arn}")
	private String roleArn;

	private static final int TOKEN_DURATION_SECONDS = 3600; // 1小时

	@PostMapping("/init-upload")
	public InitUploadResponse initUpload(@RequestBody FileInfo fileInfo) {
		// 1. 生成唯一文件key
		String fileKey = "uploads/" + UUID.randomUUID() + "-" + fileInfo.getFileName();

		// 2. 初始化分片上传
		AmazonS3 s3Client = getS3Client();
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, fileKey);
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);

		// 3. 获取STS临时凭证
		StsCredentials credentials = getStsCredentials();

		return new InitUploadResponse(
				credentials,
				new UploadInfo(
						fileKey,
						initResponse.getUploadId(),
						fileInfo.getFileName(),
						fileInfo.getFileSize()
				)
		);
	}

	@PostMapping("/refresh-credentials")
	public InitUploadResponse refreshCredentials(@RequestBody FileInfo fileInfo) {
		// 1. 生成唯一文件key
		String fileKey = "uploads/" + UUID.randomUUID() + "-" + fileInfo.getFileName();

		// 2. 初始化分片上传
		AmazonS3 s3Client = getS3Client();
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, fileKey);
		InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);

		// 3. 获取STS临时凭证
		StsCredentials credentials = getStsCredentials();

		return new InitUploadResponse(
				credentials,
				new UploadInfo(
						fileKey,
						initResponse.getUploadId(),
						fileInfo.getFileName(),
						fileInfo.getFileSize()
				)
		);
	}

	@Autowired
	AmazonS3 amazonS3;
	private AmazonS3 getS3Client() {
		return amazonS3;
	}

	private StsCredentials getStsCredentials() {
		AWSSecurityTokenService stsClient = AWSSecurityTokenServiceClientBuilder.standard()
				.withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(
						new BasicAWSCredentials(accessKey, secretKey)))
				.build();

		STSAssumeRoleSessionCredentialsProvider credentialsProvider =
				new STSAssumeRoleSessionCredentialsProvider.Builder(roleArn, "s3-upload-session")
						.withStsClient(stsClient)
						.withRoleSessionDurationSeconds(TOKEN_DURATION_SECONDS)
						.build();

		return new StsCredentials(
				credentialsProvider.getCredentials().getAWSAccessKeyId(),
				credentialsProvider.getCredentials().getAWSSecretKey(),
				credentialsProvider.getCredentials().getSessionToken(),
				new Date(System.currentTimeMillis() + TOKEN_DURATION_SECONDS * 1000)
		);
	}

	// DTO类
	@Data
	static class FileInfo {
		private String fileName;
		private String fileType;
		private long fileSize;
	}

	@Data
	static class InitUploadResponse {
		private final StsCredentials credentials;
		private final UploadInfo uploadInfo;
	}

	@Data
	static class StsCredentials {
		private final String accessKeyId;
		private final String secretAccessKey;
		private final String sessionToken;
		private final Date expiration;
	}

	@Data
	static class UploadInfo {
		private final String fileKey;
		private final String uploadId;
		private final String originalFileName;
		private final long fileSize;
	}
}