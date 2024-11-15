package com.whitenight.blog.breakpointTransfer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileChunk(@ModelAttribute FileChunk fileChunk) {
        try {
            fileUploadService.saveChunk(fileChunk);
            if (fileChunk.getOffset() + fileChunk.getFile().getSize() >= fileChunk.getTotal()) {
                fileUploadService.mergeFiles(fileChunk.getFilename());
                return ResponseEntity.ok("File uploaded successfully");
            }
            return ResponseEntity.ok("Chunk uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during file upload");        }
    }
}