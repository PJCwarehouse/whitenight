package com.whitenight.blog;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600 * 24)
@SpringBootApplication
      /*  (scanBasePackages="controller.com.WhiteNightApplication.blog.UserController")*/
//@MapperScan(basePackages = "mapper.com.WhiteNightApplication.blog.UserMapper")
public class WhiteNightApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhiteNightApplication.class, args);
    }

}
