package rest.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by amardeep2551 on 8/29/2017.
 */
@SpringBootApplication
@ComponentScan(basePackages = "rest.api")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
