package com.jqdi.core;

import com.jqdi.filestorage.core.amazons3.AmazonS3STSClient;
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

public class AmazonS3STSClientTest {

    public static void main(String[] args) throws Exception {
        String region = "cn-north-1";
        String accessKey = "LTAIkcl1bVhsEpGf";
        String secretKey = "D9hwgRigRI1Q6rIE4PBU1lmKsqTpUm";
        String roleArn = "arn:aws-cn:iam::111111111111111:role/s3-cn-role";
        String roleSessionName = "test-session";
        AmazonS3STSClient ossClient = new AmazonS3STSClient(region, accessKey, secretKey, roleArn,
                roleSessionName);

        String bucketName = "buket-test";
        String key = "image/111.jpg";

        // 上传文件
        File file = new File("/Users/anker/Downloads/" + key);
        FileInputStream inputStream = FileUtils.openInputStream(file);
        String url = ossClient.putObject(bucketName, key, inputStream);
        System.out.println("url:" + url);

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