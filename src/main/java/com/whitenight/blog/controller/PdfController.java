package com.whitenight.blog.controller;

import com.whitenight.blog.PDF.PdfToTxtConverter;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import java.io.File;
import java.io.IOException;

@Controller
public class PdfController {
    static String pdfFileName;

    //跳转PDF界面
    @RequestMapping(value = "/PDF-txt")
    public String PDF(){
        System.out.println("跳转到PDF界面");
        return "PDF-txt";
    }

    //开启文件管理器
    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "startPage", required = false) Integer startPage,
                                         @RequestParam(value = "endPage", required = false) Integer endPage) throws IOException {
        // 保存上传的文件到本地
        pdfFileName = file.getOriginalFilename();
        String projectRoot = new File("").getAbsolutePath();
        String pdfDirPath = new File(projectRoot, "pdf-txt" + File.separator + "pdf").getCanonicalPath();

        System.out.println("上传pdf文件名为:" + pdfFileName);
        System.out.println("PDF文件存放路径为:" + pdfDirPath);

        // 确保目录存在
        File pdfDir = new File(pdfDirPath);
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }

        File uploadFile = new File(pdfDir, pdfFileName);

        try {
            // 将上传的文件内容写入到本地文件中
            FileCopyUtils.copy(file.getBytes(), uploadFile);

            // 获取PDF文件的页数
            int totalPages = getPdfPageCount(uploadFile);
            System.out.println("PDF总页数: " + totalPages);

            // 设置默认的开始页和结束页
            if (startPage == null || startPage < 0) {
                startPage = 0;
            }
            if (endPage == null || endPage > totalPages) {
                endPage = totalPages;
            }

            System.out.println("开始页数为: " + startPage);
            System.out.println("结束页数为: " + endPage);

            // 调用 PdfToTxtConverter 进行处理
            PdfToTxtConverter.convertPdfToTxt(pdfFileName, startPage, endPage);
            System.out.println("文件成功上传并转换");
            return ResponseEntity.ok("文件上传成功并转换");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件上传失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败");
        }
    }

    /**
     * 获取pdf页数
     * @param file
     * @return
     */
    private int getPdfPageCount(File file) {
        try (PDDocument document = PDDocument.load(file)) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            e.printStackTrace();
            return 0; // 如果出错，返回0页
        }
    }

    // 下载文件
    @GetMapping("/download")
    public ResponseEntity<Resource> download() throws IOException {
        String txtFileName = pdfFileName.replace(".pdf", ".txt");
        // 设置下载文件路径
        String projectRoot = new File("").getAbsolutePath();
        String txtDirPath = new File(projectRoot, "pdf-txt" + File.separator + "txt").getCanonicalPath();
        File txtFile = new File(txtDirPath, txtFileName);

        if (!txtFile.exists()) {
            System.out.println("文件不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Resource resource = new FileSystemResource(txtFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + txtFile.getName());

        System.out.println("txt文件成功传给客户端");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
