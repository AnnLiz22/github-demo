package lietz.anna.demo.service;

import lietz.anna.demo.service.GitHubService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GitHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private GitHubService gitHubService;

    @Test
    void shouldGetRepositoriesWithBranches() throws Exception{
        mockMvc.perform(get("/api/github/repositories/{username}", "AnnLiz22"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}