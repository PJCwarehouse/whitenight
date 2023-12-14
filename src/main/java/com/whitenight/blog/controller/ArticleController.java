package com.whitenight.blog.controller;

import com.whitenight.blog.entity.ArticleEntity;
import com.whitenight.blog.entity.CommentsEntity;
import com.whitenight.blog.entity.UserEntity;
import com.whitenight.blog.service.ArticleService;
import com.whitenight.blog.service.CommentsService;
import com.whitenight.blog.service.UserService;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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

    @RequestMapping(value = "/deposit",method = RequestMethod.POST)
    @ResponseBody // 添加这个注解以将返回值作为响应的主体
    public Map<String, String> deposit(String title, String content){
        articleService.Insert(title, content);
        System.out.println("成功发布文章:" + title);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@RequestParam(name = "SuccessSubmitted", required = false) String SuccessSubmitted, Model model) {
        List<ArticleEntity> articles = articleService.selectAllArticles();
        model.addAttribute("articles", articles);

        int userid = userService.getId();
        model.addAttribute("userid", userid);

        // 使用 paramName，这里可以根据参数进行逻辑处理
        if (SuccessSubmitted != null) {
            model.addAttribute("SuccessSubmitted", SuccessSubmitted);
            System.out.println("成功提交文章");
        }

        System.out.println("进入首页");
        return "index";
    }



    //进入文章管理界面
    @RequestMapping("/articles management")
    public String management(Model model){
        List<ArticleEntity> articles = articleService.selectAllArticles();
        model.addAttribute("articles", articles);

        System.out.println("跳转到文章管理界面");
        return "articles management";
    }

    //查看文章
    @RequestMapping(value = "/toArticle",method = RequestMethod.GET)
    //只需要从服务器获取数据而不做任何修改时，使用 GET 是合适的。例如，获取文章列表、用户信息等。GET 请求通常被缓存，这有助于提高性能。
    public String toArticle(@RequestParam(name = "articleId") int articleId, Model model){
        //使用 @RequestParam 注解来获取名为 articleID 的参数值，并通过这个值从数据库中获取相应的文章实体。
        ArticleEntity articleEntity = articleService.selectArticlesById(articleId);
//        model.addAttribute("article", articleEntity);
//        传入后就可以直接使用${article.id}、${article.title} 和 ${article.content}访问文章属性

        model.addAttribute("articleId", articleEntity.getId());
        model.addAttribute("title", articleEntity.getTitle());//获得当前文章的标题

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
//    @RequestMapping(value = "/deleteArticle")返回值需要是个网页
//    public void deleteArticle(@RequestParam(name = "articleID") int articleID){
//        articleService.deleteArticle(articleID);
//    }

    //进入编辑文章页面
//    @RequestMapping(value = "/editArticle")
//    public String editArticle(@RequestParam(name = "articleID") int articleID,Model model){
//        ArticleEntity articleEntity = articleService.selectArticlesById(articleID);
//        model.addAttribute("article",articleEntity);
//        return "editArticle";
//    }
    @RequestMapping(value = "/editArticle")
    public String editArticle(@RequestParam(name = "articleId") int articleID, Model model) {
        ArticleEntity articleEntity = articleService.selectArticlesById(articleID);
        model.addAttribute("article", articleEntity);
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
