package lietz.anna.demo.model;

import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RepositoryDetails {
  private final RepositoryInfo info;
  private final List<BranchCommit> branchCommits;

  public RepositoryInfo getInfo() {
    return info;
  }

  public List<BranchCommit> getBranchCommits() {
    return Collections.unmodifiableList(branchCommits);
  }
}