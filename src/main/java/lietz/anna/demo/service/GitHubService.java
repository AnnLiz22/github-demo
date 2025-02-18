package lietz.anna.demo.service;

import lietz.anna.demo.exception.UserNotFoundException;
import lietz.anna.demo.model.GitHubUser;
import lietz.anna.demo.model.RepositoryInfo;
import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);
    private final GitHub gitHub;

    @Autowired
    public GitHubService(GitHub gitHub) {
        this.gitHub = gitHub;
    }

    public List<GHRepository> getNonForkedRepositories(String username) throws IOException, UserNotFoundException {
        validateUsername(username);
        logger.info("Fetching repository details for user: {}", username);

        try {
            GHUser user = gitHub.getUser(username);
            Map<String, GHRepository> repositories = user.getRepositories();
            logger.info("Fetched {} repositories for user: {}", repositories.size(), username);

            return repositories.values().stream()
                .filter(repository -> !repository.isFork())
                .collect(Collectors.toList());
        } catch (GHException e) {
            throw new UserNotFoundException("User " + username + " not found.", e);
        }
    }


    public Map<RepositoryInfo, Map<String, String>> getRepositoriesWithBranches(String username)
        throws IOException, UserNotFoundException {
        List<GHRepository> repositories = getNonForkedRepositories(username);
        Map<RepositoryInfo, Map<String, String>> repoBranchCommits = new HashMap<>();

        for (GHRepository repository : repositories) {
            RepositoryInfo repoInfo = new RepositoryInfo(repository.getName(), repository.getOwnerName());
            Map<String, String> branchCommits = getBranchCommits(repository);
            repoBranchCommits.put(repoInfo, branchCommits);
        }

        logger.info("Fetched branches and commits for user: {}", username);
        return repoBranchCommits;
    }

    public GitHubUser getUserRepositories(String username) throws IOException, UserNotFoundException {
        Map<RepositoryInfo, Map<String, String>> repoData = getRepositoriesWithBranches(username);
        return GitHubUser.fromServiceResponse(username, repoData);
    }


    private Map<String, String> getBranchCommits(GHRepository repository) throws IOException {
        Map<String, String> branchCommits = new HashMap<>();
        for (GHBranch branch : repository.getBranches().values()) {
            branchCommits.put(branch.getName(), branch.getSHA1());
        }
        return branchCommits;
    }

    private void validateUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
    }
}
