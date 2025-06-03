package com.jqdi.filestorage.spring.boot.starter.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "filestorage.amazons3sts")
public class AmazonS3STSProperties {
	private String region;
	private String accessKey;
	private String secretKey;
	private String roleArn;
	private String roleSessionName;
	private String bucketName;
}
