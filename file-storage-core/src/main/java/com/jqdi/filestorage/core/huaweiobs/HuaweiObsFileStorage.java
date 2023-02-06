package com.jqdi.filestorage.core.huaweiobs;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

import com.jqdi.filestorage.core.FileStorage;
import com.jqdi.filestorage.core.FileUrl;

/**
 * 华为云OBS
 * 
 * @author JQ棣
 *
 */
public class HuaweiObsFileStorage implements FileStorage {
	private HuaweiObsClient ossClient = null;
	private String bucketName = null;
	private String domain = null;

	public HuaweiObsFileStorage(String endpoint, String accessKey, String secretKey, String bucketName, String domain) {
		this.ossClient = new HuaweiObsClient(endpoint, accessKey, secretKey);
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