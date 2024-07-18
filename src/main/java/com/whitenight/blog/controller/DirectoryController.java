package com.whitenight.blog.controller;

import com.whitenight.blog.entity.DirectoryDataEntity;
import com.whitenight.blog.entity.DirectoryEntity;
import com.whitenight.blog.entity.PathSegment;
import com.whitenight.blog.service.DirectoryService;
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class DirectoryController {
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
    DirectoryService directoryService;

    @Autowired
    UserService userService;
    private static String loadPath;
//        获取当前文件的绝对路径，如果在windows系统中，那就是根目录下面
//        如果是linux系统中，那就是当前jar包的同级目录下面
    public static void getPath() throws IOException {
        String projectRoot = new File("").getAbsolutePath();
        loadPath = new File(projectRoot, "loadFiles").getCanonicalPath();
//        System.out.println("loadPath路径为" + loadPath);
    }

    //    总目录
    @GetMapping("/directorySummary")
    public String directoryController(Model model){
        Map<String, List<DirectoryEntity>> map = new HashMap<>();
        List<DirectoryEntity> lists = directoryService.getDirectoryList(0);
        for(DirectoryEntity list : lists){
            // 获取目录列表并过滤掉 Openness 值为 0 的条目
            List<DirectoryEntity> directoryList = directoryService.getDirectoryList(list.getId())
                    .stream()
                    .filter(directory -> directory.getOpenness() != 0)
                    .collect(Collectors.toList());
            if(directoryList.isEmpty()) continue;
            map.put(list.getDirectoryName(), directoryList);
        }
        model.addAttribute("map",map);
        return "Directory/directorySummary";
    }


//    跳转目录
    @GetMapping("/list")
    public String list(@RequestParam("parent_tableId") int parent_tableId, Model model){
        if(parent_tableId == 0){
            parent_tableId = directoryService.getDirectoryIdByUserId(userService.getId());
        }
        List<DirectoryEntity> directoryList = directoryService.getDirectoryList(parent_tableId);
        List<DirectoryDataEntity> directoryDataEntities = directoryService.getDirectoryDataList(parent_tableId);
        model.addAttribute("parent_tableId", parent_tableId);
        model.addAttribute("directoryName", directoryService.getDirectoryById(parent_tableId).getDirectoryName());
        model.addAttribute("directoryList", directoryList);
        model.addAttribute("fileList", directoryDataEntities);
        String path = "";

        List<PathSegment> pathSegments = new ArrayList<>();//创建一个类包含目录名和id
        while (parent_tableId != 0) {
            String parent_tableName = directoryService.getDirectoryById(parent_tableId).getDirectoryName();
            pathSegments.add(new PathSegment(parent_tableName, parent_tableId));
            parent_tableId = directoryService.getParent_tableIdById(parent_tableId);
        }
        Collections.reverse(pathSegments);//反转pathSegments以保持正确的顺序

        model.addAttribute("pathSegments", pathSegments);
        return "Directory/list";
    }

//    获取父目录
    @GetMapping("/returnParentTable")
    public String getParentTable(@RequestParam("directoryId") int directoryId){
        int parent_tableId = directoryService.getParent_tableIdById(directoryId);
        int parent_tableId2 = directoryService.getDirectoryById(directoryId).getParent_tableId();
        if(parent_tableId == 0){
            return "redirect:/page/1";
        }
        System.out.println("parent_tableId2: " + parent_tableId2 + ", parent_tableId: " + parent_tableId );
        return "redirect:/list?parent_tableId=" + parent_tableId;
    }

//    bug:parent_tableId一直返回0,他妈的肯定是entity哪里出问题了，沟槽的直接select都能正确返回
//    @GetMapping("/getParentTable")
//    public String getParentTable(@RequestParam("directoryId") int directoryId){
//        int parent_tableId = directoryService.getDirectoryById(directoryId).getParent_tableId();
//        System.out.println("directoryId: " + directoryId + ", parent_tableId: " + parent_tableId );
//        return "redirect:/list?parent_tableId=" + parent_tableId;
//
//    }

//    创建目录
    @PostMapping("/createDirectory")
    public String createDirectory(@RequestParam("parent_tableId") int parent_tableId,@RequestParam("directoryName") String directoryName){
        int userId = userService.getId();
        directoryService.createDirectory(userId, parent_tableId, directoryName);
        return "redirect:/list?parent_tableId=" + parent_tableId;
    }

//    文件上传
    @PostMapping("/fileUpload")
    public String uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("directoryId") int directoryId, Model model) throws IOException {
        if (file.isEmpty()) {
            model.addAttribute("message", "请选择一个非空文件");
            System.out.println("空文件上传失败");
            return "redirect:/fileList?directoryId=" + directoryId;
        }
        File uploadPath = new File(loadPath);
        //         确保目录存在
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        try {
            // 保存文件到本地
            String fileName = file.getOriginalFilename();
//            Path path = Paths.get(uploadPath + File.separator + fileName);
            File downloadFile = new File(uploadPath, fileName);
            FileCopyUtils.copy(file.getBytes(), downloadFile);

            // 构建文件下载URL
            String fileDownloadUrl = MvcUriComponentsBuilder.fromMethodName(DirectoryController.class,
                    "downloadFile", fileName).build().toString();

            directoryService.insert(directoryId,fileName,fileDownloadUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/list?parent_tableId=" + directoryId;
    }

//    上传文件下载
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            if (filename == null || filename.isEmpty()) {
                System.out.println("文件名为空");
                throw new IllegalArgumentException("File path must not be null or empty");
            }
            Path file = Paths.get(loadPath).resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

    }

    @GetMapping("/turnOpen")
    public String turnOpen(@RequestParam("directoryId") int directoryId){
        directoryService.updateOpenness(directoryId, 1);
        return "redirect:/directoryList";
    }


    @GetMapping("/turnClose")
    public String turnClose(@RequestParam("directoryId") int directoryId){
        directoryService.updateOpenness(directoryId, 0);
        return "redirect:/directoryList";
    }
}