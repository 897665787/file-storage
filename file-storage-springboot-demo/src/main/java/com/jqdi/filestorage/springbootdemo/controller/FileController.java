package com.jqdi.filestorage.springbootdemo.controller;

import com.jqdi.filestorage.core.FileStorage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {
	private Logger log = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	private FileStorage fileStorage;

	@PostMapping("/upload")
	public String upload(@RequestParam("file") MultipartFile file) {
		String name = file.getName();
		String originalFilename = file.getOriginalFilename();
		String contentType = file.getContentType();
		long size = file.getSize();
		log.info("name:{},originalFilename:{},contentType:{},size:{}", name, originalFilename, contentType, size);

		if (size == 0) {
			return "请选择文件";
		}
		
		try (InputStream inputStream = file.getInputStream()) {
			String fileKey = generateFileKey(file.getOriginalFilename());
			String fullFileKey = fullFileKey("img", fileKey);

			fileStorage.upload(inputStream, fullFileKey);
			
			return fileKey;
		} catch (IOException e) {
			log.error("IOException", e);
			return e.getMessage();
		}
	}
	

	private static String fullFileKey(String basePath, String fileKey) {
		if (basePath == null) {
			basePath = "";
		}
		if (basePath.startsWith("/")) {
			basePath = basePath.substring(1, basePath.length());
		}
		if (StringUtils.isNotBlank(basePath) && !basePath.endsWith("/")) {
			basePath = basePath + "/";
		}
		return basePath + fileKey;
	}
	
	private static String generateFileKey(String fileName) {
		// 生成文件名（目录+文件名，例如:basePath/202201/01/00033d9fea0b484eac1509567e87e61a.jpg）
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DAY_OF_MONTH);

		String ext = null;
		String[] fileNameSplit = fileName.split("\\.");
		if (fileNameSplit.length > 1) {
			ext = fileNameSplit[fileNameSplit.length - 1];
		}
		return String.format("%d%02d/%02d/%s.%s", year, month, day, UUID.randomUUID().toString().replace("-", ""), ext);
	}
}
