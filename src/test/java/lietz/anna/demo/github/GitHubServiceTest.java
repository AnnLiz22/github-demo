package lietz.anna.demo.github;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GHRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
 class GitHubServiceTest {

    @Autowired
    private GitHubService gitHubService;
    @Test
     void testGetRepositoryDetails() throws IOException, UserNotFoundException {
        String username = "AnnLiz22";
        List<GHRepository> repos = gitHubService.getRepositoryDetails(username);
        repos.forEach(repo -> System.out.println(repo.getName()));
        Assertions.assertNotNull(repos);
    }
}
