package lietz.anna.demo.github;

import org.kohsuke.github.GHBranch;
import org.kohsuke.github.GHException;
import org.kohsuke.github.GHRepository;
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

    @Autowired
    private GitHub gitHub;

    public List<GHRepository> getRepositoryDetails(String username) throws IOException, UserNotFoundException {
        logger.info("Fetching repository details for user: {}", username);

        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        try {
            Map<String, GHRepository> repositories = gitHub.getUser(username).getRepositories();
            logger.info("Fetched {} repositories for user: {}", repositories.size(), username);

            return repositories.values().stream()
                    .filter(repository -> !repository.isFork())
                    .collect(Collectors.toList());
        } catch (GHException e) {
            throw new UserNotFoundException("User " + username + " not found.");
        }
    }

    public Map< Map<String, String>, Map<String, String>> getRepositoriesWithBranches(String username) throws IOException, UserNotFoundException {
        List<GHRepository> repositories = getRepositoryDetails(username);
        Map< Map <String,String>, Map<String, String>> repoBranchCommits = new HashMap<>();

        for (GHRepository repository : repositories) {
            Map<String, String> nameAndOwner = new HashMap<>();
            nameAndOwner.put(repository.getName(), repository.getOwnerName());

            Map<String, String> branchCommits = new HashMap<>();

            for (GHBranch branch : repository.getBranches().values()) {
                branchCommits.put(branch.getName(), branch.getSHA1());
            }

            repoBranchCommits.put(nameAndOwner, branchCommits);
        }

        logger.info("Fetched branches and commits for user: {}", username);
        return repoBranchCommits;
    }
}
