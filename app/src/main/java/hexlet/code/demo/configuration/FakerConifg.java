package hexlet.code.demo.configuration;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FakerConifg {

    @Bean
    public Faker faker() {
        return new Faker();
    }

}
