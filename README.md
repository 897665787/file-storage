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

## 开源共建

### 开源协议

file-storage 开源软件遵循 [Apache 2.0 协议](https://www.apache.org/licenses/LICENSE-2.0.html)。
允许商业使用，但务必保留类作者、Copyright 信息。

### 其他说明

1. 联系作者 <a href="mailto:897665787@qq.com">897665787@qq.com</a>
