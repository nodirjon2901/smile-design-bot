package uz.result.smiledesignbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmileDesignBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmileDesignBotApplication.class, args);
    }

}
