package lietz.anna.demo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GitHubUser {
  private final String username;
  private final List<RepositoryDetails> repositories;

  public GitHubUser(String username, List<RepositoryDetails> repositories) {
    this.username = username;
    this.repositories = repositories;
  }

  public String getUsername() {
    return username;
  }

  public List<RepositoryDetails> getRepositories() {
    return Collections.unmodifiableList(repositories);
  }

  public static GitHubUser fromServiceResponse(String username,
                                               Map<RepositoryInfo, Map<String, String>> serviceResponse) {
    List<RepositoryDetails> repositoryDetailsList = new ArrayList<>();

    for (Map.Entry<RepositoryInfo, Map<String, String>> entry : serviceResponse.entrySet()) {
      RepositoryInfo repoInfo = entry.getKey();
      Map<String, String> branchCommits = entry.getValue();

      List<BranchCommit> branchCommitList = new ArrayList<>();
      for (Map.Entry<String, String> branchEntry : branchCommits.entrySet()) {
        branchCommitList.add(new BranchCommit(branchEntry.getKey(), branchEntry.getValue()));
      }

      repositoryDetailsList.add(new RepositoryDetails(repoInfo, branchCommitList));
    }

    return new GitHubUser(username, repositoryDetailsList);
  }
}