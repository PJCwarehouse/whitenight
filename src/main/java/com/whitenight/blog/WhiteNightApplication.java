package com.whitenight.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
      /*  (scanBasePackages="controller.com.WhiteNightApplication.blog.UserController")*/
//@MapperScan(basePackages = "mapper.com.WhiteNightApplication.blog.UserMapper")
public class WhiteNightApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhiteNightApplication.class, args);
    }

}
