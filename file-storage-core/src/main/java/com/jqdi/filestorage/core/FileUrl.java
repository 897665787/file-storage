package com.jqdi.filestorage.core;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUrl {

	/**
	 * 域名访问地址(推荐使用域名)
	 */
	String domainUrl;

	/**
	 * oss访问地址
	 */
	String ossUrl;
}
