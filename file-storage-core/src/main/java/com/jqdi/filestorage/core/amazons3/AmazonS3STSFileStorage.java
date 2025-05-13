package com.jqdi.filestorage.core.amazons3;

import com.jqdi.filestorage.core.FileStorage;
import com.jqdi.filestorage.core.FileUrl;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.InputStream;
import java.util.Date;

/**
 * 亚马逊AWS S3(STS模式)
 * 
 * @author JQ棣
 *
 */
public class AmazonS3STSFileStorage implements FileStorage {
	private AmazonS3STSClient ossClient = null;
	private String bucketName = null;
	private String domain = null;

	public AmazonS3STSFileStorage(String region, String accessKey, String secretKey, String roleArn, String roleSessionName, String bucketName, String domain) {
		this.ossClient = new AmazonS3STSClient(region, accessKey, secretKey, roleArn, roleSessionName);
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
	public String presignedUrl(String fileName) {
		Date expiration = DateUtils.addSeconds(new Date(), 3600);
		return ossClient.presignedUrl(bucketName, fileName, expiration);
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