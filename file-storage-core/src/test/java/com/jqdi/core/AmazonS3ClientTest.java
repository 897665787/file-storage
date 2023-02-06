package com.jqdi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.jqdi.filestorage.core.amazons3.AmazonS3Client;
public class AmazonS3ClientTest {

	public static void main(String[] args) throws Exception {
		String endpoint = "amazons3.com";
		String accessKey = "LTAIkcl1bVhsEpGf";
		String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";

		AmazonS3Client ossClient = new AmazonS3Client(endpoint, accessKey, secretKey);

		String bucketName = "buket-test";
		String key = "image/111.jpg";

		// 上传文件
		FileInputStream inputStream = FileUtils.openInputStream(new File("D:/111.jpg"));
		String url = ossClient.putObject(bucketName, key, inputStream);
		System.out.println("url:" + url);

		// 下载文件
		InputStream download = ossClient.getObject(bucketName, key);
		OutputStream out = new FileOutputStream(new File("D:/amzs3-download.jpg"));
		IOUtils.copy(download, out);
/*
		// 删除文件
		ossClient.deleteObject(bucketName, key);
*/
	}
}