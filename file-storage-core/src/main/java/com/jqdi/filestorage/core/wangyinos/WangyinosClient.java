package com.jqdi.filestorage.core.wangyinos;

import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.netease.cloud.services.nos.model.NOSObject;
import com.netease.cloud.services.nos.model.NOSObjectInputStream;
import com.netease.cloud.services.nos.model.PutObjectRequest;
import com.netease.cloud.services.nos.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * 网易NOS客户端
 * 
 * @author JQ棣
 *
 */
@Slf4j
public class WangyinosClient {

	private NosClient client = null;
	private String endpoint = null;

	public WangyinosClient(String endpoint, String accessKey, String secretKey) {
		Credentials credentials = new BasicCredentials(accessKey, secretKey);
		this.client = new NosClient(credentials);
		this.endpoint = endpoint;
	}

	public String putObject(String bucketName, String key, InputStream input) {
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, input, null);

		putObjectRequest.setProgressListener(progressEvent -> {
			int bytes = progressEvent.getBytesTransfered();
			int eventCode = progressEvent.getEventCode();
			log.info("bytes:{},eventCode:{}", bytes, eventCode);
		});
		
		PutObjectResult putObjectResult = client.putObject(putObjectRequest);
		String eTag = putObjectResult.getETag();
		String versionId = putObjectResult.getVersionId();
		log.info("eTag:{},versionId:{}", eTag, versionId);

		// 文件URL的格式为https://BucketName.Endpoint/ObjectName
		String url = String.format("https://%s.%s/%s", bucketName, endpoint, key);
		log.info("url:{}", url);
		return url;
	}

	public String presignedUrl(String bucketName, String key, Date expiration) {
		URL url = client.generatePresignedUrl(bucketName, key, expiration);
		String presignedUrl = url.toString();
		log.info("presignedUrl:{}", presignedUrl);
		return presignedUrl;
	}

	public InputStream getObject(String bucketName, String key) {
		NOSObject nosObject = client.getObject(bucketName, key);
		NOSObjectInputStream inputStream = nosObject.getObjectContent();
		return inputStream;
	}

	public void deleteObject(String bucketName, String key) {
		client.deleteObject(bucketName, key);
	}
}