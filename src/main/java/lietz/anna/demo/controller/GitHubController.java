package lietz.anna.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lietz.anna.demo.exception.UserNotFoundException;
import lietz.anna.demo.model.GitHubUser;
import lietz.anna.demo.service.GitHubService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.kohsuke.github.GHRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor

public class GitHubController {
    private final GitHubService gitHubService;

    @GetMapping(value = "/repositories/{username}", produces = "application/json")
    public ResponseEntity<Object> getRepositoriesWithBranches(@PathVariable String username) {
        try {
            GitHubUser user = gitHubService.getUserRepositories(username);

            return ResponseEntity.ok()
                    .body(user);

        } catch (UserNotFoundException | IOException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                            "User " + username + " not found.")
                    );
        }
    }

    @GetMapping(value = "/repositories/{username}/details", produces = "application/json")
    public ResponseEntity<List<RepositoryResponse>> getRepositoryDetails(@PathVariable String username) {
        try {
            List<GHRepository> repositories = gitHubService.getNonForkedRepositories(username);
            List<RepositoryResponse> responseList = repositories.stream()
                .map(this::toRepositoryResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(responseList);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private RepositoryResponse toRepositoryResponse(GHRepository repository) {
        return new RepositoryResponse(
            repository.getName(),
            repository.getOwnerName(),
            repository.getDescription(),
            repository.getLanguage(),
            repository.getStargazersCount(),
            repository.getForksCount()
        );
    }

    @Getter
    public static class RepositoryResponse {
        private final String name;
        private final String ownerName;
        private final String description;
        private final String language;
        private final int stars;
        private final int forks;

        public RepositoryResponse(String name, String ownerName, String description,
                                  String language, int stars, int forks) {
            this.name = name;
            this.ownerName = ownerName;
            this.description = description;
            this.language = language;
            this.stars = stars;
            this.forks = forks;
        }
    }
    @Setter
    @Getter
    public static class ErrorResponse {
        private final int statusCode;
        private final String message;
        private final String timestamp;


        public ErrorResponse(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
            this.timestamp = LocalDateTime.now().toString();

        }
    }
}
