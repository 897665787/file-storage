package com.jqdi.core;

import com.jqdi.filestorage.core.amazons3.AmazonS3STSClient;
import com.jqdi.filestorage.core.util.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Date;

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
        FileInputStream inputStream = FileUtils.openInputStream(new File("D:/111.jpg"));
        ossClient.putObject(bucketName, key, inputStream);

        // 客户端上传文件
        Date expirationPut = DateUtils.addSeconds(new Date(), 600);
        String presignedUrlPut = ossClient.presignedUrlPut(bucketName, key, expirationPut);
        System.out.println("presignedUrlPut:" + presignedUrlPut);
        clientUpload(presignedUrlPut, key);

        // 预签名Url
        Date expiration = DateUtils.addSeconds(new Date(), 60);
        String presignedUrl = ossClient.presignedUrl(bucketName, key, expiration);
        System.out.println("presignedUrl:" + presignedUrl);

        // 下载文件
        InputStream download = ossClient.getObject(bucketName, key);
        OutputStream out = Files.newOutputStream(new File("D:/download-" + key).toPath());
        IOUtils.copy(download, out);

        // 删除文件
        ossClient.deleteObject(bucketName, key);
    }

    private static void clientUpload(String presignedUrlPut, String key) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            HttpPut put = new HttpPut(presignedUrlPut);
            HttpEntity entity = new FileEntity(new File("D:/111.jpg"));
            put.setEntity(entity);
            put.addHeader("Content-Type", Utils.guessContentType(key)); // 根据实际文件类型设置
            // 如果生成预签名URL时设置了header参数，例如用户元数据，存储类型等，则调用预签名URL上传文件时，也需要将这些参数发送至服务端。如果签名和发送至服务端的不一致，会报签名错误。
//            for(Map.Entry header: headers.entrySet()){
//                put.addHeader(header.getKey().toString(),header.getValue().toString());
//            }
//            for(Map.Entry meta: userMetadata.entrySet()){
//                // 如果使用userMeta，sdk内部会为userMeta拼接"x-oss-meta-"前缀。当您使用其他方式生成预签名URL进行上传时，userMeta也需要拼接"x-oss-meta-"前缀。
//                put.addHeader("x-oss-meta-"+meta.getKey().toString(), meta.getValue().toString());
//            }

            httpClient = HttpClients.createDefault();

            response = httpClient.execute(put);

            System.out.println("返回上传状态码："+response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode() == 200){
                System.out.println("使用网络库上传成功");
            }
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
    }
}