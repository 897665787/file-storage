package com.jqdi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.jqdi.filestorage.core.minio.MinioClient;
public class MinioClientTest {

	public static void main(String[] args) throws Exception {
		String endpoint = "http://127.0.0.1:9000";
		String accessKey = "pRYo5fCm9ZyVNKLA";
		String secretKey = "Lv5yFUrBOXrNsZJVZ93XxSVl7JiNA99J";

		MinioClient ossClient = new MinioClient(endpoint, accessKey, secretKey);
		
		String bucketName = "buket-test";
		String objectName = "image/333.jpg";
		
		// 上传文件
		FileInputStream inputStream = FileUtils.openInputStream(new File("D:/111.jpg"));
		ossClient.putObject(bucketName, inputStream, objectName);

		// 下载文件
		InputStream download = ossClient.getObject(bucketName, objectName);
		OutputStream out = new FileOutputStream(new File("D:/minio-download.jpg"));
		IOUtils.copy(download, out);
		
		/*
		// 删除文件
		ossClient.removeObject(bucketName, objectName);
		*/
	}
}