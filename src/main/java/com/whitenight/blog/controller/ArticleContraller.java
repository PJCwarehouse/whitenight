package com.whitenight.blog.controller;

import com.whitenight.blog.entity.ArticleEntity;
import com.whitenight.blog.service.ArticleService;
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
public class ArticleContraller{
    @Resource
    ArticleService articleService;
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
        System.out.println("成功发布文章");

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(Model model){
        List<ArticleEntity> articles = articleService.selectAllArticles();
        model.addAttribute("articles", articles);
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
    public String toArticle(@RequestParam(name = "articleID") int id, Model model){
        //使用 @RequestParam 注解来获取名为 articleID 的参数值，并通过这个值从数据库中获取相应的文章实体。
        ArticleEntity articleEntity = articleService.selectArticlesById(id);
//        model.addAttribute("article", articleEntity);

//        传入后就可以直接使用${article.id}、${article.title} 和 ${article.content}访问文章属性
//        model.addAttribute("article.id", articleEntity.getId());
        model.addAttribute("title", articleEntity.getTitle());
//        model.addAttribute("article.content", articleEntity.getContent());

        // 使用 CommonMark 进行 Markdown to HTML 的转换
        String markdownContent = articleEntity.getContent();
        Parser parser = Parser.builder().build();
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String htmlContent = renderer.render(parser.parse(markdownContent));

        model.addAttribute("content", htmlContent);

        return "/article";
    }

    //删除文章
    @RequestMapping(value = "/deleteArticle")
    public String deleteArticle(@RequestParam(name = "articleID") int articleID){
        articleService.deleteArticle(articleID);
        return "success";
    }

    //进入编辑文章页面
    @RequestMapping(value = "/editArticle")
    public String editArticle(@RequestParam(name = "articleID") int articleID,Model model){
        ArticleEntity articleEntity = articleService.selectArticlesById(articleID);
        model.addAttribute("article",articleEntity);
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


//    @RequestMapping(value = "/deleteArticle")
//    public void deleteArticle(@RequestParam(name = "articleID") int articleID){
//        articleService.deleteArticle(articleID);
//    }


}
