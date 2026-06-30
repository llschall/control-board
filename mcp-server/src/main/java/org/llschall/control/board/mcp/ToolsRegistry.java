package org.llschall.control.board.mcp;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ToolsRegistry {
    private final ControlBoardApiClient apiClient;

    public ToolsRegistry(ControlBoardApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<Tool> getAvailableTools() {
        List<Tool> tools = new ArrayList<>();
        
        Map<String, Object> echoSchema = new LinkedHashMap<>();
        echoSchema.put("type", "object");
        Map<String, Object> properties = new LinkedHashMap<>();
        echoSchema.put("properties", properties);
        echoSchema.put("required", List.of());
        
        tools.add(new Tool(
            "echo",
            "Calls the echo endpoint which returns an incrementing counter. Each call increments the counter by 1.",
            echoSchema
        ));
        
        return tools;
    }

    public Tool getTool(String name) {
        return getAvailableTools().stream()
            .filter(t -> t.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    public Object callTool(String name, Map<String, Object> arguments) {
        switch (name) {
            case "echo":
                return apiClient.callEcho();
            default:
                throw new IllegalArgumentException("Unknown tool: " + name);
        }
    }
}
