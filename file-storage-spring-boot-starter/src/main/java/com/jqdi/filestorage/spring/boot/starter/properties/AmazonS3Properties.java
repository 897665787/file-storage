package com.jqdi.filestorage.spring.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "filestorage.amazons3")
public class AmazonS3Properties {
	private String endpoint;
	private String region;
	private String accessKey;
	private String secretKey;
	private String bucketName;
	private String domain;
}
