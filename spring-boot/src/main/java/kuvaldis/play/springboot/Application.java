package kuvaldis.play.springboot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;

@Slf4j
@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableConfigurationProperties(ImportantConfig.class)
//@EnableGlobalMethodSecurity // enables security for controller methods with default password on start.
public class Application {

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplicationBuilder()
                .main(Application.class)
                .sources(Application.class)
                .showBanner(false)
                .listeners(event -> log.info("Application event is thrown {}", event))
                .build();
        final Environment environment = app.run(args).getEnvironment();
        final String serverPort = environment.getProperty("server.port", "8080");
        log.info("The application is started on localhost:{}. Access url is http://localhost:{}", serverPort, serverPort);
    }

    // binds to url in case of not found error
    @Bean
    public EmbeddedServletContainerCustomizer error404Customize() {
        return container -> container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
    }
}
