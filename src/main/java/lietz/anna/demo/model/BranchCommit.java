package lietz.anna.demo.model;

import lombok.Getter;

@Getter
public class BranchCommit {
  private final String branchName;
  private final String commitSha;

  public BranchCommit(String branchName, String commitSha) {
    this.branchName = branchName;
    this.commitSha = commitSha;
  }
}

