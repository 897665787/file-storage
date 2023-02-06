package com.jqdi.filestorage.spring.boot.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "filestorage.local")
public class LocalProperties {
	private String endpoint;
	private String bucketName;
	private String domain;
}
