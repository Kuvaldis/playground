package kuvaldis.play.greenmail.spring;

import com.icegreen.greenmail.spring.GreenMailBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Application {

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplicationBuilder()
                .main(Application.class)
                .sources(Application.class)
                .showBanner(false)
                .build();
        app.run(args);
    }

    @Bean
    public GreenMailBean greenMail() {
        return new GreenMailBean();
    }
}
