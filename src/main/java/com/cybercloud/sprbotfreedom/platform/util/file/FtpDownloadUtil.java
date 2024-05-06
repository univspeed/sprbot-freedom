package com.cybercloud.sprbotfreedom.platform.util.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * FTP下载工具
 * @author liuyutang
 * @date 2023/9/15
 */
@Slf4j
@Component
public class FtpDownloadUtil {

    /**
     * 下载FTP文件到指定目录
     * @param ftpFileUrl
     * @param localFilePath
     * @throws IOException
     */
    public static void downloadFtpFile(String ftpFileUrl, String localFilePath) throws IOException {
        FTPClient ftpClient = new FTPClient();
        FileOutputStream outputStream = null;
        // 解析FTP文件的URL
        URL url = new URL(ftpFileUrl);
        String username = url.getUserInfo().split(":")[0];
        String password = url.getUserInfo().split(":")[1];
        String serverAddress = url.getHost();
        int port = url.getPort(); // 获取端口号，默认是21
        String remoteFilePath = new String(url.getPath().getBytes("UTF-8"), "ISO-8859-1");;
        try {
            ftpClient.connect(serverAddress, port);
            ftpClient.login(username, password);
            ftpClient.enterLocalPassiveMode();
            // 设置文件类型为二进制
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setControlEncoding("UTF-8");
            outputStream = new FileOutputStream(localFilePath);
            // 下载文件
            boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);
            outputStream.flush();
            if (success) {
                log.info("[{}]文件下载成功",ftpFileUrl);
            } else {
                log.info("[{}]文件下载失败",ftpFileUrl);
            }
        } finally {
            IOUtils.closeQuietly(outputStream);
            if (ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
    }

    /**
     * 下载文件到指定目录
     * @param ftpFileUrl
     * @param localPath
     * @return
     */
    public static boolean downloadFile(String ftpFileUrl, String localPath) {
        URL url;
        URLConnection conn;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            url = new URL(ftpFileUrl);
            conn = url.openConnection();
            bis = new BufferedInputStream(conn.getInputStream());
            bos = new BufferedOutputStream(new FileOutputStream(localPath));
            byte[] buffer = new byte[2048];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.info("[{}]文件下载失败",ftpFileUrl);
            return false;
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("[{}]文件下载成功",ftpFileUrl);
        return true;
    }

    /**
     * 获取FTP文件列表
     * @param ftpServerIp
     * @param ftpServerPort
     * @param ftpUser
     * @param ftpPsw
     * @param requedstFolder
     * @param fileRegex
     * @return
     * @throws IOException
     */
    public static List<String> dirFileList(String ftpServerIp, Integer ftpServerPort, String ftpUser,
                                           String ftpPsw, String requedstFolder, String fileRegex) {
        List<String> strs = new ArrayList<>();
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpServerIp, ftpServerPort);
            ftpClient.login(ftpUser, ftpPsw);
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.changeWorkingDirectory(requedstFolder);
            FTPFile[] files = ftpClient.listFiles();
            Pattern fileNameFormate = Pattern.compile(fileRegex);

            for (FTPFile file : files) {
                String fileName = file.getName();
                if (fileNameFormate.matcher(fileName).matches()) {
                    strs.add(fileName);
                }
            }
        }
        catch (Exception ex){
            log.error("{}",ex);
        }
        finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.logout();
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return strs;
    }
}
