package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
      /*  (scanBasePackages="com.example.demo.controller.UserController")*/
@MapperScan(basePackages = "com.example.demo.mapper.UserMapper")
public class Demo1Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo1Application.class, args);
    }

}
