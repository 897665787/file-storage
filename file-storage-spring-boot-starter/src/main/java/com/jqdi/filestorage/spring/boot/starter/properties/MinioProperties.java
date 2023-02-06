package com.jqdi.filestorage.spring.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "filestorage.minio")
public class MinioProperties {
	private String endpoint;
	private String accessKey;
	private String secretKey;
	private String bucketName;
	private String domain;
}