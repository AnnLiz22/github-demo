package lietz.anna.demo.github;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
        HttpStatusCode statusCode = restTemplate.getForEntity(wireMockServer.baseUrl() + "/api/github/repositories/AnnLiz22", String.class).getStatusCode();
        String body = restTemplate.getForEntity(wireMockServer.baseUrl() + "/api/github/repositories/AnnLiz22", String.class).getBody();

        assertThat(statusCode).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(body).isEqualTo("{\"Workshop-2\": \"AnnLiz22\"}");

    }
}