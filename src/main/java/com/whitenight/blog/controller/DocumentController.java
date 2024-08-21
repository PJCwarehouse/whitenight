package com.whitenight.blog.controller;

import com.whitenight.blog.entity.DirectoryTreeNode;
import com.whitenight.blog.entity.DocumentEntity;
import com.whitenight.blog.entity.PathSegment;
import com.whitenight.blog.service.DocumentService;
import com.whitenight.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class DocumentController {
    static {
        try {
            getPath();  // 在静态代码块中调用希望最先执行的方法
        } catch (IOException e) {
            System.err.println("静态方法执行出错: " + e.getMessage());
            e.printStackTrace();
            // 可以根据实际情况处理异常
        }
    }

    @Autowired
    DocumentService documentService;

    @Autowired
    UserService userService;
    private static String loadPath;
    private static String zipPath;
    //        获取当前文件的绝对路径，如果在windows系统中，那就是根目录下面
//        如果是linux系统中，那就是当前jar包的同级目录下面
    public static void getPath() throws IOException {
        String projectRoot = new File("").getAbsolutePath();
        loadPath = new File(projectRoot, "fileManagement" + File.separator + "loadFiles").getCanonicalPath();
        zipPath = new File(projectRoot,"fileManagement" + File.separator + "file-directory-zip").getCanonicalPath();
    }

    //    文件管理界面
    @GetMapping("/FileManagement")
    public String FileManagement(@RequestParam("parentId") int parentId, Model model){
        DirectoryTreeNode root = new DirectoryTreeNode(0, "root", "directory");
        moduleAssignment(parentId, model, documentService.getDirectoryList(parentId));
        documentService.getChildrenTrees(root);
        model.addAttribute("root",root);
        return "Directory/FileManagement";
    }

    //搜索功能跳转界面
    @GetMapping("/searchResult")
    public String searchResult(@RequestParam("searchString") String searchString, Model model){
        List<DocumentEntity> documentEntities = documentService.searchResult(searchString);
        if(documentEntities.isEmpty()){
            model.addAttribute("empty","没有搜索到任何文件或者目录");
            return "Directory/searchResult :: contentFragment";
        }
        model.addAttribute("returnsId",0);
        for(DocumentEntity documentEntity : documentEntities){
            String username = userService.selectUserNameById(documentEntity.getUserId()).getUsername();
            documentEntity.setUrl(username);
        }
        model.addAttribute("directoryList", documentEntities);
//        当前登录用户id
        model.addAttribute("currentUserId", userService.getId());

        return "Directory/searchResult :: contentFragment";
    }

//    文件管理内容部分跳转
    @GetMapping("/FileManagementContent")
    public String getDirectoryContent(@RequestParam("parentId") int parentId, Model model) {
        System.out.println("跳转目录，parentId: " + parentId);
        moduleAssignment(parentId, model, documentService.getDirectoryList(parentId));
        return "Directory/FileManagement :: contentFragment"; // 返回只包含内容部分的Thymeleaf片段
    }

//    文件管理界面刷新树形目录
    @GetMapping("/FileManagementFolder")
    public String getDirectoryFolder(Model model){
        DirectoryTreeNode root = new DirectoryTreeNode(0, "root", "directory");
        documentService.getChildrenTrees(root);
        model.addAttribute("root",root);
        return "Directory/FileManagement :: folderFragment";
    }

    //    文件管理返回函数
    @GetMapping("/returnParentTable")
    public String returnParentTable(@RequestParam("parentId") int parentId){
        DocumentEntity documentEntity = documentService.select(parentId);
        int newParentId = documentEntity.getParentId();
        return "redirect:/FileManagementContent?parentId=" + newParentId;
    }

    //    创建目录
    @PostMapping("/createDirectory")
    public ResponseEntity<Map<String, String>> createDirectory(@RequestParam("parentId") int parentId, @RequestParam("directoryName") String directoryName) {
        String type = "directory";
        int userId = userService.getId();
        Date time = new Date();//获取创建日期
        int visibleType = documentService.select(parentId).getVisibleType();
        DocumentEntity documentEntity = new DocumentEntity(type, userId, parentId, directoryName, time, 0, "B", visibleType);
        documentService.insert(documentEntity);

        Map<String, String> response = new HashMap<>();
        response.put("message", "success");
        response.put("redirectUrl", "/FileManagement?parentId=" + parentId);
        return ResponseEntity.ok(response);
    }

    //    文件上传
    @PostMapping("/fileUpload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file,
                                                          @RequestParam("parentId") int parentId) throws IOException {
        System.out.println("文件上传");
        Map<String, String> response = new HashMap<>();
        if (file.isEmpty()) {
            response.put("message", "请选择一个非空文件");
            System.out.println("空文件上传失败");
            return ResponseEntity.badRequest().body(response);
        }
        File uploadPath = new File(loadPath);
        //         确保目录存在
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        try {
            String type = "file";//设置文件类型
            int userId = userService.getId();//获取用户id
            String fileName = file.getOriginalFilename();//获取文件名
            Date time = new Date();//获取上传日期
            long fileSize = file.getSize(); // 获取文件大小
            double[] size = documentService.fileSize(fileSize);//size第一位保留fileSize的大小，第二位保留单位数组units的位次大小
            final String[] units = {"B", "KB", "MB", "GB", "TB"};//设置文件大小单位
            DocumentEntity documentEntity = new DocumentEntity(type, userId, parentId, fileName, time, size[0], units[ (int)size[1] ], 1);

            documentService.insert(documentEntity);

            System.out.println("文件" + fileName + "已上传");
            String id = String.valueOf(documentEntity.getId());
            // 构建文件下载URL,由于构建url时需要文件的id作为鉴别，所以先存文件，而后再存url
            String fileDownloadUrl = MvcUriComponentsBuilder.fromMethodName(DocumentController.class,
                    "downloadFile", id).build().toString();
            documentService.updateUrl(documentEntity.getId(),fileDownloadUrl);
            // 保存文件到本地
            File downloadFile = new File(uploadPath, id);
            FileCopyUtils.copy(file.getBytes(), downloadFile);
            System.out.println("上传文件:" + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.put("message", "success");
        response.put("redirectUrl", "/FileManagement?parentId=" + parentId);
        return ResponseEntity.ok(response);
    }

    //    上传文件url单个下载
    @GetMapping("/download/{fileId:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable int fileId) {
        try {
            DocumentEntity documentEntity = documentService.select(fileId);
            String filename = documentEntity.getName();
            Path file = Paths.get(loadPath).resolve(String.valueOf(fileId));
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                System.out.println("文件读取成功：" + file.toString());
                // URL编码文件名，确保特殊字符正确显示
                String encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename + "\"")
                        .body(resource);
            } else {
                System.err.println("无法读取文件：" + file.toString());
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            System.err.println("URL格式错误：" + e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("运行时异常：" + e.getMessage());
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    //    删除选中文件和目录
    @PostMapping("/perform-delete")
    public ResponseEntity<String> deleteFilesAndDirectories(@RequestBody Map<String, List<Integer>> request) {
        System.out.println("删除选中文件和目录");
        List<Integer> fileIds = request.get("files");
        List<Integer> directoryIds = request.get("directories");
        int i = 0;
        //遍历每个目录下的文件并添加到fileList，每个目录下的目录添加到directoryList
        while(i != directoryIds.size()){
            List<DocumentEntity> fileLists = documentService.getFileList(directoryIds.get(i));
            if(!fileLists.isEmpty()){
                for(DocumentEntity documentEntity : fileLists){
                    fileIds.add(documentEntity.getId());
                }
            }
            List<DocumentEntity> documentLists = documentService.getDirectoryList(directoryIds.get(i));
            if(!documentLists.isEmpty()){
                for(DocumentEntity documentEntity : documentLists){
                    directoryIds.add(documentEntity.getId());
                }
            }
            i++;
        }
        fileIds.addAll(directoryIds);
        // 处理删除文件和目录的逻辑
        for(int fileId : fileIds){
            documentService.deleteFile(fileId);
            // 删除位于 loadPath/fileId 位置上的文件
            File file = new File(loadPath, String.valueOf(fileId));
            try {
                Files.deleteIfExists(Paths.get(file.getCanonicalPath()));
                System.out.println("删除文件id：" + fileId);
            } catch (IOException e) {
                System.err.println("无法删除文件 id：" + fileId + "，错误：" + e.getMessage());
            }
        }
        return ResponseEntity.ok("Delete successful");
    }

    // 批量下载选中文件和目录
    @PostMapping("/perform-download")
    public ResponseEntity<Resource> downloadFilesAndDirectories(@RequestBody Map<String, List<Integer>> request) throws IOException {
        List<Integer> fileIds = request.get("files");
        List<Integer> directoryIds = request.get("directories");
        System.out.println("批量下载文件\n");

        // 创建临时 ZIP 文件
        Path temporaryZip = Paths.get(zipPath, "download.zip");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(temporaryZip.toFile()))) {
            // 添加文件到 ZIP
            for (Integer fileId : fileIds) {
                DocumentEntity fileData = documentService.select(fileId);
                // 通过文件id获取文件数据，再获取父目录id
                int parentDirectoryId = fileData.getParentId();
                System.out.println("下载文件：" + fileId);
                Path filePath = Paths.get(loadPath, String.valueOf(fileId));
                if(parentDirectoryId != 0){
                    // 通过父目录id获取父目录的name,将父目录添加在路径前面。
                    String parentName = documentService.select(parentDirectoryId).getName();
                    addToZip(filePath,parentName + "/" + fileData.getName(), zipOut);
                }else {
                    // 如果父目录为0的就不用在下载的目录前面添一个目录了
                    addToZip(filePath, fileData.getName(), zipOut);
                }
            }
            // 添加目录及其内容到 ZIP
            for (Integer directoryId : directoryIds) {
                DocumentEntity directoryData = documentService.select(directoryId);
                int parentDirectoryId = directoryData.getParentId();
                if(parentDirectoryId != 0){
                    String parentName = documentService.select(parentDirectoryId).getName();
                    addDirectoryToZip(directoryId,parentName + "/" + directoryData.getName(), zipOut);
                }else {
                    addDirectoryToZip(directoryId,directoryData.getName(), zipOut);
                }
            }
        }
        System.out.println("下载完成\n");
        // 创建资源并返回响应
        Resource resource = new UrlResource(temporaryZip.toUri());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"download.zip\"")
                .body(resource);
    }

    // 将文件添加到 ZIP 包
    private void addToZip(Path filePath, String fileName, ZipOutputStream zipOut) throws IOException {
        //逻辑：创建一个新的 ZipEntry 并将其添加到 ZipOutputStream。
        // 以fileName作为压缩文件的名字，将filePath对应路径下的文件拷贝到压缩文件中
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        Files.copy(filePath, zipOut);
        zipOut.closeEntry();
    }

    // 递归地将目录及其内容添加到 ZIP 包
    private void addDirectoryToZip(Integer directoryId, String parentPath, ZipOutputStream zipOut) throws IOException {
        // 创建目录，即使它是空的
        parentPath = parentPath + "/";
        zipOut.putNextEntry(new ZipEntry(parentPath));
        zipOut.closeEntry();
        System.out.println("下载目录：" + directoryId);

        List<DocumentEntity> fileLists = documentService.getFileList(directoryId);
        for (DocumentEntity fileData : fileLists) {
            System.out.println("下载文件：" + fileData.getId());
            Path filePath = Paths.get(loadPath, String.valueOf(fileData.getId()));
            addToZip(filePath, parentPath + fileData.getName(), zipOut);
        }

        List<DocumentEntity> directoryLists = documentService.getDirectoryList(directoryId);
        for (DocumentEntity directory : directoryLists) {
            if(directory.getVisibleType() == 0 && directory.getUserId() != userService.getId()) continue;
            addDirectoryToZip(directory.getId(), parentPath + directory.getName(), zipOut);
        }
    }

    //    给相应的model赋值
    private void moduleAssignment(@RequestParam("parentId") int parentId, Model model, List<DocumentEntity> directoryList) {
//        这里增加一个判断只是因为目前没有id为0的目录，如果新增一个id为0的目录，且父目录为-1，则无需这个判断条件
        if(parentId == 0){
            model.addAttribute("returnsId",-1);
            //        当前进入目录的用户id,parentId为0，进入的是root目录，用户id设置为-1，即root目录下不可新建文件夹及文件上传，且root目录下的文件不可被删除
            model.addAttribute("directoryUserId", -1);
        }else {
            model.addAttribute("returnsId", documentService.select(parentId).getParentId());
            model.addAttribute("directoryUserId", documentService.select(parentId).getUserId());
        }
        List<DocumentEntity> documentEntities = documentService.getFileList(parentId);
        model.addAttribute("parentId", parentId);
        model.addAttribute("directoryList", directoryList);
        model.addAttribute("fileList", documentEntities);
//        当前登录用户id
        model.addAttribute("currentUserId", userService.getId());

        List<PathSegment> pathSegments = documentService.getPathSegment(parentId);
        model.addAttribute("pathSegments", pathSegments);
    }

    @GetMapping("/turnOpen")
    public ResponseEntity<Map<String, Object>> turnOpen(@RequestParam("directoryId") int directoryId){
        int parentId = documentService.select(directoryId).getParentId();
        while(parentId != 0){
            DocumentEntity documentEntity = documentService.select(parentId);
            if(documentEntity.getVisibleType() == 0){
                documentService.updateOpenness(parentId, 1);
            }
            parentId = documentEntity.getParentId();
        }
        List<Integer> directoryIds = new ArrayList<>();
        directoryIds.add(directoryId);
        int i = 0;
        //遍历每个目录下的文件并添加到fileList，每个目录下的目录添加到directoryList
        while(i != directoryIds.size()){
            List<DocumentEntity> documentLists = documentService.getDirectoryList(directoryIds.get(i));
            documentService.updateOpenness(directoryIds.get(i), 1);
            if(!documentLists.isEmpty()){
                for(DocumentEntity documentEntity : documentLists){
                    directoryIds.add(documentEntity.getId());
                }
            }
            i++;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/turnClose")
    public ResponseEntity<Map<String, Object>> turnClose(@RequestParam("directoryId") int directoryId){
        List<Integer> directoryIds = new ArrayList<>();
        directoryIds.add(directoryId);
        int i = 0;
        //遍历每个目录下的文件并添加到fileList，每个目录下的目录添加到directoryList
        while(i != directoryIds.size()){
            List<DocumentEntity> documentLists = documentService.getDirectoryList(directoryIds.get(i));
            documentService.updateOpenness(directoryIds.get(i), 0);
            if(!documentLists.isEmpty()){
                for(DocumentEntity documentEntity : documentLists){
                    directoryIds.add(documentEntity.getId());
                }
            }
            i++;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    //    总目录
    @GetMapping("/directorySummary")
    public String directoryController(Model model){
        Map<String, List<DocumentEntity>> map = new HashMap<>();
        //获取以0为父目录的用户根目录列表
        List<DocumentEntity> lists = documentService.getDirectoryList(0);
        for(DocumentEntity list : lists){
            // 获取目录列表并过滤掉 Openness 值为 0 的条目
            List<DocumentEntity> directoryList = documentService.getDirectoryList(list.getId())
                    .stream()
                    .filter(directory -> directory.getVisibleType() != 0)
                    .collect(Collectors.toList());
            if(directoryList.isEmpty()) continue;
            map.put(list.getName(), directoryList);
        }
        model.addAttribute("map",map);
        return "Directory/directorySummary";
    }

    //  目录管理界面
    @GetMapping("/publicDirectory")
    public String publicDirectory(@RequestParam("parentId") int parentId, Model model){
        if(parentId == 0){
            return "redirect:/directorySummary";
        }
        List<DocumentEntity> directoryList = documentService.getDirectoryList(parentId)
                .stream()
                .filter(directory -> directory.getVisibleType() != 0)
                .collect(Collectors.toList());
        moduleAssignment(parentId, model, directoryList);
        return "Directory/publicDirectory";
    }

    //    目录管理返回函数
    @GetMapping("/returnParentDisplay")
    public String returnParentDisplay(@RequestParam("directoryId") int directoryId){
        int parentId = documentService.select(directoryId).getParentId();
        if(documentService.select(parentId).getParentId() == 0){
            return "redirect:/directorySummary";
        }
        return "redirect:/publicDirectory?parentId=" + parentId;
    }
}