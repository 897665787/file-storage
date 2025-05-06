package com.jqdi.filestorage.core.wangyinos;

import java.io.InputStream;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.jqdi.filestorage.core.FileStorage;
import com.jqdi.filestorage.core.FileUrl;
import org.apache.commons.lang3.time.DateUtils;

/**
 * 网易NOS
 * 
 * @author JQ棣
 *
 */
public class WangyinosFileStorage implements FileStorage {
	private WangyinosClient client = null;
	private String bucketName = null;
	private String domain = null;

	public WangyinosFileStorage(String endpoint, String accessKey, String secretKey, String bucketName, String domain) {
		this.client = new WangyinosClient(endpoint, accessKey, secretKey);
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
		Date expiration = DateUtils.addSeconds(new Date(), 3600);
		return client.presignedUrl(bucketName, fileName, expiration);
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