package lietz.anna.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.kohsuke.github.GitHub;
import java.io.IOException;
@Configuration
public class AppConfig {

    @Bean
    public GitHub gitHub() throws IOException {
        return GitHub.connectAnonymously();
    }
}
