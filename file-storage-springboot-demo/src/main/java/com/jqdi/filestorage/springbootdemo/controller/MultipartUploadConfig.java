package com.jqdi.filestorage.springbootdemo.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.STSAssumeRoleSessionCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.securitytoken.AWSSecurityTokenService;
import com.amazonaws.services.securitytoken.AWSSecurityTokenServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultipartUploadConfig {

    @Value("${aws.accessKeyId}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.sts.roleArn}")
    private String roleArn;

    @Value("${aws.sts.sessionName}")
    private String sessionName;

    @Bean
    public AWSSecurityTokenService stsClient() {
        return AWSSecurityTokenServiceClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKey, awsSecretKey)))
                .withRegion(awsRegion)
                .build();
    }

    @Bean
    public AmazonS3 amazonS3(AWSSecurityTokenService stsClient) {
        STSAssumeRoleSessionCredentialsProvider credentialsProvider =
                new STSAssumeRoleSessionCredentialsProvider.Builder(roleArn, sessionName)
                        .withStsClient(stsClient)
                        .build();

        return AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(awsRegion)
                .build();
    }
}
