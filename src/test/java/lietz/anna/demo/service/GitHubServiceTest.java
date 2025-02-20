package lietz.anna.demo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lietz.anna.demo.exception.UserNotFoundException;
import lietz.anna.demo.model.RepositoryInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GitHubServiceTest {

  @Mock
  private GitHub gitHub;

  @Mock
  private GHUser ghUser;

  @Mock
  private GHRepository nonForkedRepo1;

  @Mock
  private GHRepository nonForkedRepo2;

  @Mock
  private GHRepository forkedRepo;

  @Mock
  private GHBranch branch1;

  @Mock
  private GHBranch branch2;

  private GitHubService gitHubService;


  @BeforeEach
  void setUp() {
    gitHubService = new GitHubService(gitHub);
  }

  @Test
  void shouldGetNonForkedRepositories() throws UserNotFoundException, IOException {
    String username = "AnnLiz22";
    Map <String, GHRepository> repositories = new HashMap<>();
    repositories.put("repo-1", nonForkedRepo1);
    repositories.put("repo-2", nonForkedRepo2);
    repositories.put("repo-3", forkedRepo);

    when(gitHub.getUser("AnnLiz22")).thenReturn(ghUser);
    when(ghUser.getRepositories()).thenReturn(repositories);
    when(nonForkedRepo1.isFork()).thenReturn(false);
    when(nonForkedRepo2.isFork()).thenReturn(false);
    when(forkedRepo.isFork()).thenReturn(true);

    List<GHRepository> result = gitHubService.getNonForkedRepositories(username);

    Assertions.assertNotNull(result);

    Assertions.assertEquals(2, result.size());
    assertTrue(result.contains(nonForkedRepo1));
    assertTrue(result.contains(nonForkedRepo2));
    assertFalse(result.contains(forkedRepo));

    verify(gitHub).getUser(username);
    verify(ghUser).getRepositories();
  }

  @Test
  void shouldReturnEmptyListWhenGHUserDoesNotHaveRepositories() throws IOException, UserNotFoundException {
    String username = "emptyUser";

    when(gitHub.getUser(username)).thenReturn(ghUser);
    when(ghUser.getRepositories()).thenReturn(Collections.emptyMap());

    List<GHRepository> result = gitHubService.getNonForkedRepositories(username);

    assertTrue(result.isEmpty());
    verify(gitHub).getUser(username);
    verify(ghUser).getRepositories();
  }

  @Test
  void shouldGetRepositoriesWithBranches() throws UserNotFoundException, IOException {

    String username = "AnnLiz22";
    List<GHRepository> repos = Arrays.asList(nonForkedRepo1, nonForkedRepo2);

    when(nonForkedRepo1.getName()).thenReturn("repo-1");
    when(nonForkedRepo1.getOwnerName()).thenReturn("AnnLiz22");
    Map<String, GHBranch> branchMap1 = new HashMap<>();
    branchMap1.put("main", branch1);
    when(branch1.getName()).thenReturn("main");
    when(branch1.getSHA1()).thenReturn("sha1234");
    when(nonForkedRepo1.getBranches()).thenReturn(branchMap1);

    when(nonForkedRepo2.getName()).thenReturn("repo-2");
    when(nonForkedRepo2.getOwnerName()).thenReturn("AnnLiz22");
    Map<String, GHBranch> branchMap2 = new HashMap<>();
    branchMap2.put("main", branch2);
    when(branch2.getName()).thenReturn("main");
    when(branch2.getSHA1()).thenReturn("sha5678");
    when(nonForkedRepo2.getBranches()).thenReturn(branchMap2);

    GitHubService spyService = spy(gitHubService);
    doReturn(repos).when(spyService).getNonForkedRepositories(username);

    Map<RepositoryInfo, Map<String, String>> result = spyService.getRepositoriesWithBranches(username);

    assertEquals(2, result.size());

    Optional<RepositoryInfo> repo1Info = result.keySet().stream()
        .filter(info -> "repo-1".equals(info.getName()))
        .findFirst();
    assertTrue(repo1Info.isPresent());
    Map<String, String> repo1Branches = result.get(repo1Info.get());
    assertEquals(1, repo1Branches.size());
    assertEquals("sha1234", repo1Branches.get("main"));

    Optional<RepositoryInfo> repo2Info = result.keySet().stream()
        .filter(info -> "repo-2".equals(info.getName()))
        .findFirst();
    assertTrue(repo2Info.isPresent());
    Map<String, String> repo2Branches = result.get(repo2Info.get());
    assertEquals(1, repo2Branches.size());
    assertEquals("sha5678", repo2Branches.get("main"));
  }


  @Test
  void getUserRepositories() {
  }
}