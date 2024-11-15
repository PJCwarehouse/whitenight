package com.whitenight.blog.breakpointTransfer;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    private static final String TEMP_DIR = "/path/to/temp"; // 临时目录路径

    public void saveChunk(FileChunk fileChunk) throws IOException {
        String tempFilePath = getTempFilePath(fileChunk.getFilename(), fileChunk.getOffset());
        try (InputStream in = fileChunk.getFile().getInputStream();
             OutputStream out = Files.newOutputStream(Paths.get(tempFilePath))) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        }
    }

    public void mergeFiles(String filename) throws IOException {
        Path targetPath = Paths.get(TEMP_DIR, filename);
        Path tempDir = Paths.get(TEMP_DIR);
        int chunkIndex = 0;
        try (OutputStream targetStream = new FileOutputStream(targetPath.toFile())) {
            while (Files.exists(tempDir.resolve(filename + "." + chunkIndex))) {
                Path chunkPath = tempDir.resolve(filename + "." + chunkIndex);
                try (InputStream chunkStream = Files.newInputStream(chunkPath)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = chunkStream.read(buffer)) != -1) {
                        targetStream.write(buffer, 0, length);
                    }
                }
                Files.delete(chunkPath); // 删除分片文件
                chunkIndex++;
            }
        }
    }

    private String getTempFilePath(String filename, long offset) {
        return TEMP_DIR + "/" + filename + "." + offset;
    }
}