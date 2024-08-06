package com.whitenight.blog.controller;

import com.whitenight.blog.PDF.PdfToTxtConverter;
import com.whitenight.blog.webSocket.WebSocket;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class PdfController {

    static {
        try {
            setPath();  // 在静态代码块中调用希望最先执行的方法
        } catch (IOException e) {
            System.err.println("静态方法执行出错: " + e.getMessage());
            e.printStackTrace();
            // 可以根据实际情况处理异常
        }
    }
    private List<String> uploadedFileNames  = new ArrayList<>();; // 记录上传的文件列表,非final类型
    private static String projectRoot;
    private static String pdfDirPath;
    private static File pdfDir;
    private static String txtDirPath;
    private static File txtDir;
    private static void setPath() throws IOException {
//        projectRoot:当前文件的绝对路径，如果在windows系统中，那就是根目录下面
//        如果是linux系统中，那就是当前jar包的同级目录下面
        projectRoot = new File("").getAbsolutePath();
        pdfDirPath = new File(projectRoot, "pdf-txt" + File.separator + "pdf").getCanonicalPath();
        txtDirPath = new File(projectRoot, "pdf-txt" + File.separator + "txt").getCanonicalPath();
//        File 对象只是一个路径表示，创建 File 对象并不会在文件系统中实际创建文件或目录
        pdfDir = new File(pdfDirPath);
        txtDir = new File(txtDirPath);
    }

    //跳转PDF界面
    @RequestMapping(value = "/PDF-txt")
    public String PDF(){
        System.out.println("跳转到PDF界面");
        return "PDF-txt";
    }

//    上传文件
    @PostMapping("/uploadPdfFiles")
    public ResponseEntity<String> uploadPdfFiles(@RequestParam("files") List<MultipartFile> files,
                                         @RequestParam(value = "startPage", required = false) Integer startPage,
                                         @RequestParam(value = "endPage", required = false) Integer endPage){
        //输入为空，走默认的 startPage<0,endPage>totalPage 的选择，直接转换所有页
        if(startPage == null){
            startPage = -1;
        }

        if(endPage == null){
            endPage = Integer.MAX_VALUE;
        }

        //如果开始页大于结束页，那么交换它们的值
        if(startPage > endPage){
            int temp = startPage;
            startPage = endPage;
            endPage = temp;
        }

//         确保目录存在
        if (!pdfDir.exists()) {
            pdfDir.mkdirs();
        }

        uploadedFileNames.clear(); // 清空之前的记录
        clearDirectory(pdfDir);// 清空pdf文件夹
        clearDirectory(txtDir);// 清空txt文件夹

        for (MultipartFile file : files) {
            String pdfFileName = file.getOriginalFilename();
            File uploadFile = new File(pdfDir, pdfFileName);

            // 检查同名文件并重命名
            int counter = 1;
            while (uploadFile.exists()) {
                String newFileName = pdfFileName.substring(0, pdfFileName.lastIndexOf('.')) + "(" + counter + ")" + pdfFileName.substring(pdfFileName.lastIndexOf('.'));
                uploadFile = new File(pdfDir, newFileName);
                counter++;
            }

            pdfFileName = uploadFile.getName();
            uploadedFileNames.add(uploadFile.getName()); // 记录文件名

            //              通过webSocket发送信息给客户端
            WebSocket.broadcast("正在转换文件" + pdfFileName);

            System.out.println("上传pdf文件名为:" + pdfFileName);
            System.out.println("PDF文件存放路径为:" + pdfDirPath);


            try {
                // 将上传的文件内容写入到本地文件中
                FileCopyUtils.copy(file.getBytes(), uploadFile);

                // 获取PDF文件的页数
                int totalPages = getPdfPageCount(uploadFile);
                System.out.println("PDF总页数: " + totalPages);
                // 设置默认的开始页和结束页

                int FirstPage = startPage;
                int LastPage = endPage;

                if (startPage < 0 || startPage > totalPages) {
                    FirstPage = 0;
                }

                if (endPage < 0 || endPage > totalPages) {
                    LastPage = totalPages;
                }

                System.out.println("开始页数为: " + FirstPage);
                System.out.println("结束页数为: " + LastPage);

                int pageLength = LastPage - FirstPage;
                while(pageLength > 40){
                    PdfToTxtConverter.convertPdfToTxt(pdfFileName, FirstPage, FirstPage + 40);
                    FirstPage += 40;
                    pageLength -= 40;
                    System.out.println("执行起始页数：" + FirstPage);
                }
                PdfToTxtConverter.convertPdfToTxt(pdfFileName, FirstPage, LastPage);//调用 PdfToTxtConverter 进行处理
                System.out.println("文件 " + pdfFileName + " 成功上传并转换\n");

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件 " + pdfFileName + " 上传失败");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件 " + pdfFileName + " 上传失败");
            }
        }
        WebSocket.broadcast("所有文件已成功上传并转换！");
        return ResponseEntity.ok("所有文件上传成功并转换\n");
    }

//    下载文件
    @GetMapping("/download")
    public ResponseEntity<Resource> download() throws IOException {
        String zipDirPath = new File(projectRoot, "pdf-txt" + File.separator + "zip").getCanonicalPath();

        // 创建 zip 文件
        File zipFile = new File(zipDirPath, "converted_files.zip");
        createZipFile(zipFile, txtDirPath, uploadedFileNames);

        if (!zipFile.exists()) {
            System.out.println("文件不存在");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Resource resource = new FileSystemResource(zipFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getName());

        System.out.println("zip文件成功传给客户端");
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

//    新建文件夹
    private void createZipFile(File zipFile, String txtDirPath, List<String> fileNames) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            for (String fileName : fileNames) {
                String txtFileName = fileName.replace(".pdf", ".txt");
                File txtFile = new File(txtDirPath, txtFileName);

                if (txtFile.exists()) {
                    try (FileInputStream fis = new FileInputStream(txtFile)) {
                        ZipEntry zipEntry = new ZipEntry(txtFileName);
                        zos.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, length);
                        }
                        zos.closeEntry();
                    }
                }
            }
        }
    }

//  上传文件之前清空旧文件夹内容
    private static void clearDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        clearDirectory(file); // 递归清空子目录
                    } else {
                        file.delete(); // 删除文件
                    }
                }
            }
        }
    }

//  获取pdf页数
    private int getPdfPageCount(File file) {
        try (PDDocument document = PDDocument.load(file)) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            e.printStackTrace();
            return 0; // 如果出错，返回0页
        }
    }

}