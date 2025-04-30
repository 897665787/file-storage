package com.jqdi.filestorage.core.amazons3;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.jqdi.filestorage.core.FileStorage;
import com.jqdi.filestorage.core.FileUrl;

/**
 * 亚马逊AWS S3
 * 
 * @author JQ棣
 *
 */
public class AmazonS3FileStorage implements FileStorage {
	private AmazonS3Client ossClient = null;
	private String bucketName = null;
	private String domain = null;

	public AmazonS3FileStorage(String endpoint, String region, String accessKey, String secretKey, String bucketName, String domain) {
		this.ossClient = new AmazonS3Client(endpoint, region, accessKey, secretKey);
		this.bucketName = bucketName;
		this.domain = domain;
	}

	@Override
	public FileUrl upload(InputStream inputStream, String fileName) {
		String ossUrl = ossClient.putObject(bucketName, fileName, inputStream);
		FileUrl fileUrl = new FileUrl();
		fileUrl.setOssUrl(ossUrl);
		if (StringUtils.isNotBlank(domain)) {
			String domainUrl = String.format("%s/%s", domain, fileName);
			fileUrl.setDomainUrl(domainUrl);
		} else {
			fileUrl.setDomainUrl(ossUrl);
		}
		return fileUrl;
	}

	@Override
	public InputStream download(String fileName) {
		return ossClient.getObject(bucketName, fileName);
	}

	@Override
	public void remove(String fileName) {
		ossClient.deleteObject(bucketName, fileName);
	}
}