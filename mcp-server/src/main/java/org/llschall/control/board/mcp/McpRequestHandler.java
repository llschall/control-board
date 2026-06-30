package org.llschall.control.board.mcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class McpRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(McpRequestHandler.class);

    private final ToolsRegistry toolsRegistry;

    public McpRequestHandler(ToolsRegistry toolsRegistry) {
        this.toolsRegistry = toolsRegistry;
    }

    public McpResponse handle(McpRequest request) {
        try {
            logger.debug("Handling MCP request: method={}, id={}", request.getMethod(), request.getId());
            
            switch (request.getMethod()) {
                case "initialize":
                    return handleInitialize(request);
                case "resources/list":
                    return handleResourcesList(request);
                case "tools/list":
                    return handleToolsList(request);
                case "tools/call":
                    return handleToolCall(request);
                default:
                    return error(-32601, "Method not found", request.getId());
            }
        } catch (Exception e) {
            logger.error("Error handling MCP request", e);
            return error(-32603, "Internal error: " + e.getMessage(), request.getId());
        }
    }

    private McpResponse handleInitialize(McpRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("protocolVersion", "2024-11-05");
        result.put("capabilities", new LinkedHashMap<>());
        result.put("serverInfo", Map.of(
            "name", "control-board-mcp-server",
            "version", "1.0.0"
        ));
        return new McpResponse(result, request.getId());
    }

    private McpResponse handleResourcesList(McpRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("resources", List.of());
        return new McpResponse(result, request.getId());
    }

    private McpResponse handleToolsList(McpRequest request) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> tools = new ArrayList<>();
        
        for (Tool tool : toolsRegistry.getAvailableTools()) {
            Map<String, Object> toolMap = new LinkedHashMap<>();
            toolMap.put("name", tool.getName());
            toolMap.put("description", tool.getDescription());
            toolMap.put("inputSchema", tool.getInputSchema());
            tools.add(toolMap);
        }
        
        result.put("tools", tools);
        return new McpResponse(result, request.getId());
    }

    private McpResponse handleToolCall(McpRequest request) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> params = (Map<String, Object>) request.getParams();
            String toolName = (String) params.get("name");
            @SuppressWarnings("unchecked")
            Map<String, Object> arguments = (Map<String, Object>) params.getOrDefault("arguments", new HashMap<>());
            
            Object toolResult = toolsRegistry.callTool(toolName, arguments);
            
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", List.of(Map.of(
                "type", "text",
                "text", toolResult.toString()
            )));
            result.put("isError", false);
            
            return new McpResponse(result, request.getId());
        } catch (Exception e) {
            logger.error("Error calling tool", e);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("content", List.of(Map.of(
                "type", "text",
                "text", "Error: " + e.getMessage()
            )));
            result.put("isError", true);
            return new McpResponse(result, request.getId());
        }
    }

    private McpResponse error(int code, String message, String id) {
        return new McpResponse(new McpResponse.McpError(code, message), id);
    }
}
