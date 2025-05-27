package com.jqdi.filestorage.spring.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "filestorage.baidubos")
public class BaiduBosProperties {
	private String endpoint;
	private String accessKey;
	private String secretKey;
	private String bucketName;
}
