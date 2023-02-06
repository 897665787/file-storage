package com.jqdi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.jqdi.filestorage.core.local.LocalClient;

public class LocalClientTest {

	public static void main(String[] args) throws Exception {
		String endpoint = "D:/home/data/oss";
		LocalClient client = new LocalClient(endpoint);

		String bucketName = "buket-test";
		String key = "image/111.jpg";

		// 上传文件
		FileInputStream inputStream = FileUtils.openInputStream(new File("D:/111.jpg"));
		String url = client.putObject(bucketName, key, inputStream);
		System.out.println("url:" + url);

		// 下载文件
		InputStream download = client.getObject(bucketName, key);
		OutputStream out = new FileOutputStream(new File("D:/local-download.jpg"));
		IOUtils.copy(download, out);
		/*
		// 删除文件
		client.deleteObject(bucketName, key);
		*/
	}
}
