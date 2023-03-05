一个封装了文件存储的框架，支持本地磁盘、阿里云OSS、MinIO、亚马逊AWS、百度BOS、华为云OBS、京东云OOS、腾讯云COS、网易NOS等

### 平台支持

| 平台                 | 支持   |
| -------------------- |--------|
| 阿里云OSS            | √      |
| 亚马逊AWS            | √      |
| 百度BOS              | √      |
| 华为云OBS            | √      |
| 京东云OOS            | √      |
| 本地磁盘             | √      |
| MinIO                | √      |
| 腾讯云COS            | √      |
| 网易NOS              | √      |

### 模块说明

```lua
file-storage
├── file-storage-core -- 核心代码
	 └── alioss -- 阿里云OSS
	 └── amazons3 -- 亚马逊AWS
	 └── baidubos -- 百度BOS
	 └── huaweiobs -- 华为云OBS
	 └── jingdongoss -- 京东云OOS
	 └── local -- 本地磁盘
	 └── minio -- MinIO
	 └── tencentcos -- 腾讯云COS
	 └── wangyinos -- 网易NOS
└── file-storage-boot-starter -- 整合springboot代码
```

### 使用说明

#### 1：编译源码
mvn install，使用maven将源码编译成jar包并且安装到本地仓库，如有私服也可以部署到私服

#### 2：jar包引用（如使用阿里云OSS），其他可参考file-storage-core的pom配置

```
<dependency>
    <groupId>com.jqdi</groupId>
    <artifactId>file-storage-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>3.15.1</version>
</dependency>
```
#### 3：springboot yml 配置（如使用阿里云OSS），其他可参考file-storage-springboot-demo的pom配置
```
filestorage:
  active: alioss
  alioss:
    endpoint: oss-cn-shenzhen.aliyuncs.com
    accessKey: LTAIkcl2bVhsEpGf
    secretKey: D9hwgRig2IKQ6rIE4PBUglmKsqTpUm
    bucketName: buket-template
    domain: https://image.domain.com
```
## 
开源共建

### 开源协议

file-storage 开源软件遵循 [Apache 2.0 协议](https://www.apache.org/licenses/LICENSE-2.0.html)。
允许商业使用，但务必保留类作者、Copyright 信息。

### 其他说明

1. 联系作者 <a href="mailto:897665787@qq.com">897665787@qq.com</a>
