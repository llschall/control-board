package org.llschall.control.board.mcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ControlBoardApiClient {
    private static final Logger logger = LoggerFactory.getLogger(ControlBoardApiClient.class);

    @Value("${control-board.api.url:http://localhost:8080}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public EchoResponse callEcho() {
        try {
            String url = apiUrl + "/api/echo";
            logger.debug("Calling echo endpoint: {}", url);
            return restTemplate.getForObject(url, EchoResponse.class);
        } catch (Exception e) {
            logger.error("Error calling echo endpoint", e);
            throw new RuntimeException("Failed to call echo endpoint: " + e.getMessage(), e);
        }
    }

    public int getEchoCount() {
        try {
            String url = apiUrl + "/api/echo-count";
            logger.debug("Calling echo-count endpoint: {}", url);
            EchoCountResponse response = restTemplate.getForObject(url, EchoCountResponse.class);
            return response != null ? response.count() : 0;
        } catch (Exception e) {
            logger.error("Error calling echo-count endpoint", e);
            throw new RuntimeException("Failed to call echo-count endpoint: " + e.getMessage(), e);
        }
    }

    public record EchoResponse(int count) {}
    public record EchoCountResponse(int count) {}
}
