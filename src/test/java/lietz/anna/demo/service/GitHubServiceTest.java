package lietz.anna.demo.service;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import lietz.anna.demo.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GHRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GitHubServiceTest {

  @Autowired
  private GitHubService gitHubService;

  @BeforeEach
  void setUp() {
  }

  @Test
  void shouldGetNonForkedRepositories() throws UserNotFoundException, IOException {
    String username = "AnnLiz22";
    List<GHRepository> repositories = gitHubService.getNonForkedRepositories(username);

    repositories.forEach(ghRepository -> System.out.println("Repository name: " + ghRepository.getName() + ", owner name: "
        + ghRepository.getOwnerName()));
    Assertions.assertNotNull(repositories);
  }

  @Test
  void getRepositoriesWithBranches() {
  }

  @Test
  void getUserRepositories() {
  }
}