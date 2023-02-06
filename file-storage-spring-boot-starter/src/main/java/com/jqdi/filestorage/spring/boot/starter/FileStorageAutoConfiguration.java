package com.jqdi.filestorage.spring.boot.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jqdi.filestorage.core.FileStorage;
import com.jqdi.filestorage.core.alioss.AliossFileStorage;
import com.jqdi.filestorage.core.amazons3.AmazonS3FileStorage;
import com.jqdi.filestorage.core.baidubos.BaiduBosFileStorage;
import com.jqdi.filestorage.core.huaweiobs.HuaweiObsFileStorage;
import com.jqdi.filestorage.core.jingdong.JingdongossFileStorage;
import com.jqdi.filestorage.core.local.LocalFileStorage;
import com.jqdi.filestorage.core.minio.MinioFileStorage;
import com.jqdi.filestorage.core.tencentcos.TencentcosFileStorage;
import com.jqdi.filestorage.core.wangyinos.WangyinosFileStorage;
import com.jqdi.filestorage.spring.boot.starter.properties.AliossProperties;
import com.jqdi.filestorage.spring.boot.starter.properties.AmazonS3Properties;
import com.jqdi.filestorage.spring.boot.starter.properties.BaiduBosProperties;
import com.jqdi.filestorage.spring.boot.starter.properties.HuaweiObsProperties;
import com.jqdi.filestorage.spring.boot.starter.properties.JingdongossProperties;
import com.jqdi.filestorage.spring.boot.starter.properties.LocalProperties;
import com.jqdi.filestorage.spring.boot.starter.properties.MinioProperties;
import com.jqdi.filestorage.spring.boot.starter.properties.TencentcosProperties;
import com.jqdi.filestorage.spring.boot.starter.properties.WangyinosProperties;

@Configuration
@ConditionalOnProperty(prefix = "filestorage", name = "active")
@ConditionalOnMissingBean(FileStorage.class)
@EnableConfigurationProperties({ LocalProperties.class, MinioProperties.class, AliossProperties.class,
		TencentcosProperties.class, AmazonS3Properties.class, BaiduBosProperties.class, HuaweiObsProperties.class,
		WangyinosProperties.class, JingdongossProperties.class })
public class FileStorageAutoConfiguration {
	
	@Bean
	@ConditionalOnProperty(prefix = "filestorage", name = "active", havingValue = "local")
	FileStorage localFileStorage(LocalProperties properties) {
		String endpoint = properties.getEndpoint();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new LocalFileStorage(endpoint, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "filestorage", name = "active", havingValue = "minio")
	FileStorage minioFileStorage(MinioProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new MinioFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "filestorage", name = "active", havingValue = "alioss")
	FileStorage aliossFileStorage(AliossProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new AliossFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "filestorage", name = "active", havingValue = "tencentcos")
	FileStorage tencentcosFileStorage(TencentcosProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new TencentcosFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "filestorage", name = "active", havingValue = "amazons3")
	FileStorage amazonS3FileStorage(AmazonS3Properties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new AmazonS3FileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "filestorage", name = "active", havingValue = "baidubos")
	FileStorage baiduBosFileStorage(BaiduBosProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new BaiduBosFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}

	@Bean
	@ConditionalOnProperty(prefix = "filestorage", name = "active", havingValue = "huaweiobs")
	FileStorage huaweiObsFileStorage(HuaweiObsProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();

		FileStorage fileStorage = new HuaweiObsFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}
	
	@Bean
	@ConditionalOnProperty(prefix = "filestorage", name = "active", havingValue = "wangyinos")
	FileStorage wangyinosFileStorage(HuaweiObsProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();
		
		FileStorage fileStorage = new WangyinosFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}
	
	@Bean
	@ConditionalOnProperty(prefix = "filestorage", name = "active", havingValue = "jingdongoss")
	FileStorage jingdongossFileStorage(HuaweiObsProperties properties) {
		String endpoint = properties.getEndpoint();
		String accessKey = properties.getAccessKey();
		String secretKey = properties.getSecretKey();
		String bucketName = properties.getBucketName();
		String domain = properties.getDomain();
		
		FileStorage fileStorage = new JingdongossFileStorage(endpoint, accessKey, secretKey, bucketName, domain);
		return fileStorage;
	}
}
