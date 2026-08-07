package com.example.mainpofol;

import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication
public class MainpofolApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainpofolApplication.class, args);
    }

}
