package com.whitenight.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
      /*  (scanBasePackages="controller.com.whitenightApplication.blog.UserController")*/
@MapperScan(basePackages = "mapper.com.whitenightApplication.blog.UserMapper")
public class whitenightApplication {

    public static void main(String[] args) {
        SpringApplication.run(whitenightApplication.class, args);
    }

}
