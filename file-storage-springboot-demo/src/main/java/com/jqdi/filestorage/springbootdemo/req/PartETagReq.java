package com.jqdi.filestorage.springbootdemo.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class PartETagReq {
    @JsonProperty("PartNumber")
    private Integer partNumber;
    @JsonProperty("ETag")
    private String eTag;
}