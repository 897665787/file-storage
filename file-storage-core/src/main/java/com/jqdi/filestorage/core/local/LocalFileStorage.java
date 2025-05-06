package com.jqdi.filestorage.core.local;

import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.jqdi.filestorage.core.FileStorage;
import com.jqdi.filestorage.core.FileUrl;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 本地磁盘
 * 
 * @author JQ棣
 *
 */
public class LocalFileStorage implements FileStorage {
	private LocalClient client = null;
	private String bucketName = null;
	private String domain = null;

	public LocalFileStorage(String endpoint, String bucketName, String domain) {
		this.client = new LocalClient(endpoint);
		this.bucketName = bucketName;
		this.domain = domain;
	}

	@Override
	public FileUrl upload(InputStream inputStream, String fileName) {
		String ossUrl = client.putObject(bucketName, fileName, inputStream);
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
		return client.presignedUrl(bucketName, fileName);
	}

	@Override
	public InputStream download(String fileName) {
		return client.getObject(bucketName, fileName);
	}

	@Override
	public void remove(String fileName) {
		client.deleteObject(bucketName, fileName);
	}
}