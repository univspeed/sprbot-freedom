package com.cybercloud.sprbotfreedom.platform.util.file;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Base64编码MultipartFile
 * @author liuyutang
 */
public class BASE64DecodedMultipartFile implements MultipartFile {

    private final byte[] imgContent;
    private final String header;

    private String fileName;

    public BASE64DecodedMultipartFile(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }

    public BASE64DecodedMultipartFile(byte[] imgContent, String header, String fileName) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
        this.fileName = fileName;
    }

    public String getName() {
        return System.currentTimeMillis() + Math.random() + "." + header.split("/")[1];
    }

    public String getOriginalFilename() {
        if (StringUtils.isNotEmpty(fileName)) {
            return fileName + "." + header.split("/")[1];
        }
        return System.currentTimeMillis() + (int)Math.random() * 10000 + "." + header.split("/")[1];
    }

    public String getContentType() {
        return header.split(":")[1];
    }

    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    public long getSize() {
        return imgContent.length;
    }

    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
}
