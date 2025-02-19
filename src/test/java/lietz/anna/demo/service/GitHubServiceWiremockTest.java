package lietz.anna.demo.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

class GitHubServiceWiremockTest {
    private static final WireMockServer wireMockServer = new WireMockServer(8080);
    @BeforeAll
    public static void setUp() {
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void shouldTest() {
        wireMockServer.stubFor(
                get(
                        urlMatching("/api/github/repositories/.*")
                )
                        .willReturn(
                                aResponse()
                                        .withStatus(HttpStatus.OK.value())
                                        .withBody("{\"Workshop-2\": \"AnnLiz22\"}")
                        ));
        RestTemplate restTemplate = new RestTemplate();
        String url = wireMockServer.baseUrl() + "/api/github/repositories/AnnLiz22";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("{\"Workshop-2\": \"AnnLiz22\"}");
    }
}