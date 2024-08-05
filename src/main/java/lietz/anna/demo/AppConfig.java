package lietz.anna.demo;

import lietz.anna.demo.github.GitHubService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.kohsuke.github.GitHub;
import java.io.IOException;
@Configuration
public class AppConfig {

    @Bean
    GitHubService gitHubService(){
        return new GitHubService();
    }

    @Bean
    public GitHub gitHub() throws IOException {
        return GitHub.connectAnonymously();
    }
}
