package com.jqdi.filestorage.core.minio;

import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.jqdi.filestorage.core.FileStorage;
import com.jqdi.filestorage.core.FileUrl;
import org.apache.commons.lang3.time.DateUtils;

/**
 * minio
 * 
 * @author JQ棣
 *
 */
public class MinioFileStorage implements FileStorage {

	private MinioClient minioClient;
	private String bucketName;
	private String domain;
	
	public MinioFileStorage(String endpoint, String accessKey, String secretKey, String bucketName, String domain) {
		this.minioClient = new MinioClient(endpoint, accessKey, secretKey);
		this.bucketName = bucketName;
		this.domain = domain;
	}

	@Override
	public FileUrl upload(InputStream inputStream, String fileName) {
		String ossUrl = minioClient.putObject(bucketName, inputStream, fileName);
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
		return minioClient.presignedUrl(bucketName, fileName, 3600);
	}

	@Override
	public InputStream download(String fileName) {
		return minioClient.getObject(bucketName, fileName);
	}

	@Override
	public void remove(String fileName) {
		minioClient.removeObject(bucketName, fileName);
	}
}