package com.jqdi.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.jqdi.filestorage.core.alioss.AliossClient;

public class AliossClientTest {
	
	public static void main(String[] args) throws Exception {
		String endpoint = "oss-cn-shenzhen.aliyuncs.com";
		String accessKey = "LTAIkcl1bVhsEpGf";
		String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";
		
		AliossClient ossClient = new AliossClient(endpoint, accessKey, secretKey);
		
		String bucketName = "buket-test";
		String key = "image/111.jpg";
		
		// 上传文件
		FileInputStream inputStream = FileUtils.openInputStream(new File("D:/111.jpg"));
		ossClient.putObject(bucketName, key, inputStream);
//		System.out.println("略缩url:" + (url + "?x-oss-process=image/resize,m_lfit,h_60,w_60"));
		
		// 下载文件
		InputStream download = ossClient.getObject(bucketName, key);
		OutputStream out = new FileOutputStream(new File("D:/alioss-download.jpg"));
		IOUtils.copy(download, out);
		
		/*
		// 删除文件
		ossClient.deleteObject(bucketName, key);
		*/
	}
}