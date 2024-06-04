package com.whitenight.blog.controller;

import com.github.pagehelper.PageInfo;
import com.whitenight.blog.entity.ArticleEntity;
import com.whitenight.blog.entity.CommentsEntity;
import com.whitenight.blog.service.ArticleService;
import com.whitenight.blog.service.CommentsService;
import com.whitenight.blog.service.UserService;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class ArticleController {
    @Resource
    ArticleService articleService;
    @Resource
    CommentsService commentsService;
    @Resource
    UserService userService;

//    UserEntity userEntity;
    //进入发布文章界面
    @RequestMapping("/articles publish")
    public String art(){
        System.out.println("跳转到发布文章界面");
        return "articles publish";
    }

    //发布文章
    @RequestMapping(value = "/deposit",method = RequestMethod.POST)
    @ResponseBody // 添加这个注解以将返回值作为响应的主体
    public Map<String, String> deposit(String title, String content){
        Date time = new Date();
        String author = userService.getUsername();
        int userId = userService.getId();
        articleService.Insert(title,content,time,author,userId);
        System.out.println("成功发布文章:" + title);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }

    //发布文章后返回首页

    @GetMapping(value = "/page/{p}")
    public String index(@RequestParam(name = "SuccessSubmitted", required = false) String SuccessSubmitted,
                        @RequestParam(name = "NoPermission", required = false) String NoPermission,
                        @PathVariable("p") int page, @RequestParam(value = "count", defaultValue = "5") int count, Model model) {
        PageInfo<ArticleEntity> articles = articleService.selectArticleWithPage(page, count);
        model.addAttribute("articles", articles);

        int userid = userService.getId();
        String username = userService.getUsername();
        model.addAttribute("userid", userid);
        model.addAttribute("username", username);
        // 使用 paramName，这里可以根据参数进行逻辑处理
        if (SuccessSubmitted != null) {
            model.addAttribute("SuccessSubmitted", SuccessSubmitted);
            System.out.println("成功提交文章");
        }

        if (NoPermission != null) {
            model.addAttribute("NoPermission", NoPermission);
            System.out.println("用户：" + userid + "，权限不足，被拒绝访问管理界面" );
        }
        System.out.println("分页获取文章信息: 页码 "+page+",条数 "+count);
        return "index";
    }


    //进入文章管理界面，这里在security里面设置了权限，非admin权限用户进入会被限制
    @RequestMapping("/articles management")
    public String management(Model model){
        int userId = userService.getId();
        if(userId == 1){
            List<ArticleEntity> articles = articleService.selectAllArticles();
            model.addAttribute("articles", articles);
        }else {
            List<ArticleEntity> articles = articleService.selectArticlesByUserId(userId);
            model.addAttribute("articles", articles);
        }

        System.out.println("跳转到文章管理界面");
        return "articles management";
    }
    //进入用户文章管理界面
    @RequestMapping("/homepage")
    public String homepage(Model model){
        int userId = userService.getId();
        List<ArticleEntity> articles = articleService.selectArticlesByUserId(userId);
        model.addAttribute("articles", articles);

        System.out.println("跳转到个人文章管理界面");
        return "homepage";
    }


    //查看文章
    @RequestMapping(value = "/toArticle",method = RequestMethod.GET)
    //只需要从服务器获取数据而不做任何修改时，使用 GET 是合适的。例如，获取文章列表、用户信息等。GET 请求通常被缓存，这有助于提高性能。
    public String toArticle(@RequestParam(name = "articleId") int articleId, Model model){
        //使用 @RequestParam 注解来获取名为 articleID 的参数值，并通过这个值从数据库中获取相应的文章实体。
        ArticleEntity articleEntity = articleService.selectArticlesByArticleId(articleId);
        model.addAttribute("article", articleEntity);

        // 使用 CommonMark 进行 Markdown to HTML 的转换
        String markdownContent = articleEntity.getContent();
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlContent = renderer.render(parser.parse(markdownContent));

        model.addAttribute("articleContent", htmlContent);

        List<CommentsEntity> commentsEntity = commentsService.selectAllCommentsByArticleId(articleId);

        model.addAttribute("comments", commentsEntity);
//        if (commentsEntity == null) {
//            // 如果 commentsEntity 为空，创建一个空的评论列表
//            commentsEntity = Collections.emptyList();
//        }
//        model.addAttribute("comments", commentsEntity);
        int userid = userService.getId();
        String username = userService.getUsername();
        model.addAttribute("userid",userid);
        model.addAttribute("username",username);



        return "article";
    }

    //删除文章
    @RequestMapping(value = "/deleteArticle")
    public String deleteArticle(@RequestParam(name = "articleId") int articleID){
        articleService.deleteArticle(articleID);
        return "success";
    }


    //进入编辑文章页面
    @RequestMapping(value = "/editArticle")
    public String editArticle(@RequestParam(name = "articleId") int articleID, @RequestParam(name = "type") int type, Model model) {
        ArticleEntity articleEntity = articleService.selectArticlesByArticleId(articleID);
        model.addAttribute("article", articleEntity);
        model.addAttribute("type", type);
        return "editArticle";
    }



    //更新文章
    @RequestMapping(value = "/updateArticle")
    @ResponseBody // 添加这个注解以将返回值作为响应的主体
    public Map<String, String> updateArticle(int id,String title, String content){
        articleService.updateArticle(id,title,content);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }


}
