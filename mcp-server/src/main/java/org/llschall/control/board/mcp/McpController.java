package org.llschall.control.board.mcp;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mcp")
public class McpController {
    private final McpRequestHandler requestHandler;

    public McpController(McpRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

    @PostMapping("/")
    public ResponseEntity<?> handleRequest(@RequestBody McpRequest request) {
        McpResponse response = requestHandler.handle(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "healthy"));
    }

    private static class Map {
        public static java.util.Map<String, String> of(String key, String value) {
            java.util.Map<String, String> map = new java.util.HashMap<>();
            map.put(key, value);
            return map;
        }
    }
}
