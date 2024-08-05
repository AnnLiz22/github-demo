package lietz.anna.demo.github;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    @Autowired
    private GitHubService gitHubService;

    @GetMapping(value = "/repositories/{username}", produces = "application/json")
    public ResponseEntity<?> getRepositoriesWithBranches(@PathVariable String username) {
        try {
            Map<Map<String, String>, Map<String, String>> repoBranchCommits = gitHubService.getRepositoriesWithBranches(username);

            return ResponseEntity.ok()
                    .body(repoBranchCommits);

        } catch (UserNotFoundException | IOException e) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
                            "User " + username + " not found.")
                    );
        }
    }

    @Setter
    @Getter
    public static class ErrorResponse {
        private int statusCode;
        private String message;

        public ErrorResponse(int statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;

        }
    }
}
