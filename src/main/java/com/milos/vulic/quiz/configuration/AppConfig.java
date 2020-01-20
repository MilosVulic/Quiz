package com.milos.vulic.quiz.configuration;

import com.milos.vulic.quiz.models.Role;
import com.milos.vulic.quiz.models.User;
import com.milos.vulic.quiz.services.UserService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class AppConfig {

    @Bean
    public ApplicationRunner setUp(UserService userService) {
        return args -> {

            User admin = new User(1L, Role.ADMIN, "Milos", "Vulic", "neki@email.com",new Date(),"admin","admin");
            User user1 = new User(2L, Role.PARTICIPANT, "Zeljko", "Obradovic", "zeljko@email.com",new Date(),"zeljko","zeljko");
            User user2 = new User(3L, Role.PARTICIPANT, "Test", "test", "aa@email.com",new Date(),"test","test123");

            userService.registerUser(admin);
            userService.registerUser(user1);
            userService.registerUser(user2);
        };
    }
}
