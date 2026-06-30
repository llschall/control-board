package org.llschall.control.board.mcp;

import java.util.Map;

public class Tool {
    private String name;
    private String description;
    private Map<String, Object> inputSchema;

    public Tool(String name, String description, Map<String, Object> inputSchema) {
        this.name = name;
        this.description = description;
        this.inputSchema = inputSchema;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getInputSchema() {
        return inputSchema;
    }
}
