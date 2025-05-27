package com.jqdi.core;

import com.jqdi.filestorage.core.amazons3.AmazonS3Client;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;

public class AmazonS3ClientTest {

	public static void main(String[] args) throws Exception {
		String endpoint = "https://s3.cn-north-1.amazonaws.com.cn";
		String region = "cn-north-1";
		String accessKey = "LTAIkcl1bVhsEpGf";
		String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";

		AmazonS3Client ossClient = new AmazonS3Client(endpoint, region, accessKey, secretKey);

		String bucketName = "buket-test";
		String key = "image/111.jpg";

		// 上传文件
		FileInputStream inputStream = FileUtils.openInputStream(new File("D:/111.jpg"));
		ossClient.putObject(bucketName, key, inputStream);

		// 预签名Url
		Date expiration = DateUtils.addSeconds(new Date(), 60);
		String presignedUrl = ossClient.presignedUrl(bucketName, key, expiration);
		System.out.println("presignedUrl:" + presignedUrl);

		// 下载文件
		InputStream download = ossClient.getObject(bucketName, key);
		OutputStream out = Files.newOutputStream(new File("/Users/anker/Downloads/download-" + key).toPath());
		IOUtils.copy(download, out);

		// 删除文件
		ossClient.deleteObject(bucketName, key);
	}
}