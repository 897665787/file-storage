package com.jqdi.filestorage.springbootdemo.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.model.AssumeRoleRequest;
import com.amazonaws.services.securitytoken.model.AssumeRoleResult;
import com.amazonaws.services.securitytoken.model.Credentials;
import com.jqdi.filestorage.springbootdemo.req.PartETagReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/upload")
public class UploadController2 {

	@Autowired
	private AWSSecurityTokenService stsClient;

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${aws.s3.bucket}")
	private String bucketName;

	@Value("${aws.sts.duration:900}")// 最少900秒
	private int stsDuration;

	@Value("${aws.sts.roleArn}")
	private String roleArn;

	@Value("${aws.sts.sessionName}")
	private String sessionName;

	@Value("${aws.region}")
	private String awsRegion;

	// 获取STS临时凭证
	@GetMapping("/getTempCredentials")
	public ResponseEntity<Map<String, String>> getTempCredentials() {
		AssumeRoleRequest assumeRoleRequest = new AssumeRoleRequest()
				.withRoleArn(roleArn)
				.withRoleSessionName(sessionName)
				.withDurationSeconds(stsDuration);

		AssumeRoleResult assumeRoleResult = stsClient.assumeRole(assumeRoleRequest);
		Credentials credentials = assumeRoleResult.getCredentials();

		Map<String, String> response = new HashMap<>();
		response.put("region", awsRegion);
		response.put("bucketName", bucketName);
		response.put("accessKeyId", credentials.getAccessKeyId());
		response.put("secretAccessKey", credentials.getSecretAccessKey());
		response.put("sessionToken", credentials.getSessionToken());
		response.put("expiration", credentials.getExpiration().toString());

		return ResponseEntity.ok(response);
	}

	// 初始化分片上传
	@PostMapping("/initMultipartUpload")
	public ResponseEntity<Map<String, String>> initMultipartUpload(@RequestParam String fileName) {
		String objectKey = "uploads/" + UUID.randomUUID() + "/" + fileName;

		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, objectKey);
		InitiateMultipartUploadResult initResponse = amazonS3.initiateMultipartUpload(initRequest);

		Map<String, String> response = new HashMap<>();
		response.put("uploadId", initResponse.getUploadId());
		response.put("objectKey", objectKey);

//		response.put("uploadId", "LCwj8FbPu1as.n7JdI1ZKfKNkzYRYFxKizfth2Gx6OTb9.P4wfSu4Qz9FXzPDKQDNPH_cssvzjSnutvB8R6Fg--");
//		response.put("objectKey", "uploads/2a47b93e-cd18-4a5b-b6a6-abdf5398a3a3/p2.mp4");


		return ResponseEntity.ok(response);
	}

	// 获取已上传的分片列表
	@GetMapping("/listParts")
	public ResponseEntity<List<PartSummary>> listParts(
			@RequestParam String uploadId,
			@RequestParam String objectKey) {

		ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, objectKey, uploadId);
		PartListing partListing = amazonS3.listParts(listPartsRequest);

		return ResponseEntity.ok(partListing.getParts());
	}

	// 完成分片上传
	@PostMapping("/completeMultipartUpload")
	public ResponseEntity<String> completeMultipartUpload(
			@RequestParam String uploadId,
			@RequestParam String objectKey,
			@RequestBody List<PartETagReq> partETagReqs) {

		List<PartETag> partETags = partETagReqs.stream().map(v -> new PartETag(v.getPartNumber(), v.getETag())).collect(Collectors.toList());

		CompleteMultipartUploadRequest completeRequest = new CompleteMultipartUploadRequest(
				bucketName, objectKey, uploadId, partETags);

		amazonS3.completeMultipartUpload(completeRequest);
		return ResponseEntity.ok("Upload completed successfully");
	}
}