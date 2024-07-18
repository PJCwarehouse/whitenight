//package com.whitenight.blog.AOP;
//
//import com.whitenight.blog.entity.DirectoryDataEntity;
//import com.whitenight.blog.entity.DirectoryEntity;
//import com.whitenight.blog.service.DirectoryService;
//import com.whitenight.blog.service.UserService;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.Model;
//
//import java.util.List;
//
//@Aspect
//@Component
//public class DirectoryAspect {
//
//    private final UserService userService;
//    private final DirectoryService directoryService;
//
//    public DirectoryAspect(UserService userService, DirectoryService directoryService) {
//        this.userService = userService;
//        this.directoryService = directoryService;
//    }
//
////    跳转个人目录列表页前的工作
//    @Before("execution(* com.whitenight.blog.controller.DirectoryController.directoryList(..)) && args(model,..)")
//    public void beforeDirectoryList(JoinPoint joinPoint, Model model) {
//        int userId = userService.getId();
//        List<DirectoryEntity> list = directoryService.getDirectoryListByUserId(userId);
//        model.addAttribute("list", list);
//    }
//
////    跳转到目录对应的文件列表
//    @Before("execution(* com.whitenight.blog.controller.DirectoryController.fileList(..)) && args(directoryId, model)")
//    public void beforeFileList(JoinPoint joinPoint, int directoryId, Model model) {
//        List<DirectoryDataEntity> directoryDatas = directoryService.getDirectoryDataList(directoryId);
//        String directoryName = directoryService.getDirectoryNameById(directoryId);
//        model.addAttribute("directoryDatas", directoryDatas);
//        model.addAttribute("directoryId", directoryId);
//        model.addAttribute("directoryName", directoryName);
//    }
//}
