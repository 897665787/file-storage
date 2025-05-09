package com.jqdi.filestorage.springbootdemo.req;

import lombok.Data;

import java.util.Date;

@Data
public class StsToken {
    private String accessKeyId;
    private String secretAccessKey;
    private String sessionToken;
    private Date expiration;
}