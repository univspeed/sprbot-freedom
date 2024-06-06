package com.cybercloud.sprbotfreedom.platform.util.file;


import com.cybercloud.sprbotfreedom.platform.util.genid.UUIDUtil;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

/**
 * 文件操作工具类
 *
 * @author liuyutang
 */
@Slf4j
public class FileUtil {

    /**
     * 允许解析的视频格式
     */
    private static final String ALLOW_RESOLVE_FORMAT = "|mp4|wmv|";

    public static long getVideoTime(MultipartFile file) throws IOException, EncoderException, InterruptedException {
        //创建临时文件目录 当前系统用户的home/file_temp下
        File tempDir = new File(String.format("%s%sfile_temp", System.getProperty("user.home"), File.separator));
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        //解析文件扩展名
        String originalFileName = file.getOriginalFilename();
        String extension = null;
        if (originalFileName != null) {
            extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        }
        //如果不是支持的视频格式则不解析
        if (ALLOW_RESOLVE_FORMAT.indexOf("|" + extension.toLowerCase() + "|") < 0) {
            return 0;
        }
        //创建文件,这里生成MD5名，以免文件名称重复导致时长被覆盖
        String fileName = String.format("%s.%s", UUIDUtil.genUUID(), extension);
        File f = new File(String.format("%s%s%s", tempDir.getPath(), File.separator, fileName));
        FileUtils.copyInputStreamToFile(file.getInputStream(), f);
        // 获取视频时长
        Encoder encoder = new Encoder();
        MultimediaInfo m = encoder.getInfo(f);
        // ls 是当前视频总秒数
        long ls = m.getDuration() / 1000;
        int hour = (int) (ls / 3600);
        int minute = (int) (ls % 3600) / 60;
        int second = (int) (ls - hour * 3600 - minute * 60);
        log.debug("视频时长为：{}时{}分{}秒", hour, minute, second);
        //删除解析的文件,此处休眠10毫秒，目的是解决删除文件偶尔失败的问题，猜测是关闭流延迟，导致无法删除。
        TimeUnit.MILLISECONDS.sleep(10);
        log.debug("解析后删除文件:{}", FileUtils.deleteQuietly(f));
        return ls;
    }

    /**
     * 删除文件夹（包括文件夹下有文件的）
     * @param path 文件夹路径
     * @throws IOException
     */
    public static void deleteDirectoryRecursively(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
