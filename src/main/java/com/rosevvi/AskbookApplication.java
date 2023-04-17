package com.rosevvi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class AskbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(AskbookApplication.class, args);
    }

}
