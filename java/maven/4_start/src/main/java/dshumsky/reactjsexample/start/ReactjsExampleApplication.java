package dshumsky.reactjsexample.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"dshumsky"})
public class ReactjsExampleApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ReactjsExampleApplication.class, args);
    }
}
