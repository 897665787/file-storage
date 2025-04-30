package com.jqdi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.jqdi.filestorage.core.amazons3.AmazonS3Client;
public class AmazonS3ClientTest {

	public static void main(String[] args) throws Exception {
		String endpoint = "amazons3.com";
		String region = "us-west-2";
		String accessKey = "LTAIkcl1bVhsEpGf";
		String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";

		AmazonS3Client ossClient = new AmazonS3Client(endpoint, region, accessKey, secretKey);

		String bucketName = "buket-test";
		String key = "image/111.jpg";

		// 上传文件
//		File file = new File("/Users/anker/Downloads/20250428-184857.jpeg");
//		FileInputStream inputStream = FileUtils.openInputStream(file);
//		String contentType = Files.probeContentType(file.toPath());
//		String url = ossClient.putObject(bucketName, key, inputStream, contentType);
//		System.out.println("url:" + url);

		String s = ossClient.listObjects(bucketName);


		// 下载文件
//		String presignedUrl = ossClient.presignedUrl(bucketName, key);
//		System.out.println("presignedUrl:" + presignedUrl);


		// 下载文件
//		InputStream download = ossClient.getObject(bucketName, key);
//		OutputStream out = new FileOutputStream(new File("/Users/anker/Downloads/amzs3-download.jpg"));
//		IOUtils.copy(download, out);
/*
		// 删除文件
		ossClient.deleteObject(bucketName, key);
*/
	}
}