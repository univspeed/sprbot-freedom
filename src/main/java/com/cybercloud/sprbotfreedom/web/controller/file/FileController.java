package com.cybercloud.sprbotfreedom.web.controller.file;

import com.cybercloud.sprbotfreedom.platform.file.FileService;
import com.cybercloud.sprbotfreedom.platform.util.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author liuyutang
 * @date 2023/8/30
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${upload.dir:F:/video}")
    private String uploadDir;

    @PostMapping("/upload_seed")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("chunk") int chunk,
                             @RequestParam("chunks") int chunks,
                             @RequestParam("fileName") String fileName) throws IOException {

        String tempFileDir = uploadDir + File.separator + fileName.substring(0, fileName.lastIndexOf("."));

        // Create directory if not exists
        File dir = new File(tempFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Save the uploaded file chunk
        Path filePath = Paths.get(tempFileDir, String.valueOf(chunk));
        Files.write(filePath, file.getBytes());

        // If it's the last chunk, merge files
        if (chunk == chunks - 1) {
            mergeFileChunks(tempFileDir, fileName);
            FileUtil.deleteDirectoryRecursively(dir.toPath());
        }

        return "File chunk " + chunk + " uploaded successfully";
    }

    @GetMapping("/status")
    public String getFileStatus(@RequestParam("fileName") String fileName,
                                @RequestParam("chunks") int chunks) {
        String tempFileDir = "/path/to/upload" + File.separator + fileName;

        int uploadedChunks = 0;
        for (int i = 0; i < chunks; i++) {
            File chunkFile = new File(tempFileDir, String.valueOf(i));
            if (chunkFile.exists()) {
                uploadedChunks++;
            }
        }

        return "Uploaded " + uploadedChunks + " out of " + chunks + " chunks";
    }

    private void mergeFileChunks(String tempFileDir, String fileName) throws IOException {
        Path mergedFilePath = Paths.get(uploadDir, fileName);
        Files.createFile(mergedFilePath);

        for (int i = 0; ; i++) {
            Path chunkPath = Paths.get(tempFileDir, String.valueOf(i));
            if (!Files.exists(chunkPath)) {
                break;
            }
            Files.write(mergedFilePath, Files.readAllBytes(chunkPath), StandardOpenOption.APPEND);
        }
    }

}
