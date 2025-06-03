package com.jqdi.filestorage.core.util;

public class Utils {
    private Utils() {
    }

    public static String guessContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "log":
            case "txt":
                return "text/plain";
            case "htm":
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "csv":
                return "text/csv";
            case "js":
                return "text/javascript";
            case "json":
                return "application/json";
            case "xml":
                return "application/xml";
            case "pdf":
                return "application/pdf";
            case "doc":
                return "application/msword";
            case "docx":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":
                return "application/vnd.ms-excel";
            case "xlsx":
                return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "zip":
                return "application/zip";
            case "rar":
                return "application/x-rar-compressed";
            case "mp4":
                return "video/mp4";
            default:
                return "application/octet-stream";
        }
    }
}