# Control Board MCP Server

A Spring Boot MCP (Model Context Protocol) server that exposes the control-board REST API endpoints as MCP tools.

## Overview

This MCP server implements the JSON-RPC 2.0-based MCP protocol and provides access to the control-board API through standard MCP tool calls.

## Running the Server

### Prerequisites
- The control-board REST API must be running on `http://localhost:8080` (configurable)
- Java 25+

### Start the MCP Server

```bash
cd mcp-server
../gradlew bootRun
```

The server will start on `http://localhost:8081`

### Configuration

Edit `src/main/resources/application.properties` to change settings:
- `server.port=8081` - Port for the MCP server
- `control-board.api.url=http://localhost:8080` - URL of the control-board API

## Available Tools

### echo

Calls the echo endpoint which returns an incrementing counter. Each call increments the counter by 1.

**Request:**
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "params": {
    "name": "echo",
    "arguments": {}
  },
  "id": "1"
}
```

**Response:**
```json
{
  "jsonrpc": "2.0",
  "result": {
    "content": [
      {
        "type": "text",
        "text": "EchoResponse[count=1]"
      }
    ],
    "isError": false
  },
  "id": "1"
}
```

## MCP Protocol Endpoints

### Initialize
- **Method**: `initialize`
- Returns server info and capabilities

### List Tools
- **Method**: `tools/list`
- Returns available tools

### Call Tool
- **Method**: `tools/call`
- Executes a tool with provided arguments

### Health Check
- **Endpoint**: `GET /mcp/health`
- Returns server health status

## Example Usage

```bash
# Initialize
curl -X POST http://localhost:8081/mcp/ \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"initialize","params":{},"id":"1"}'

# List tools
curl -X POST http://localhost:8081/mcp/ \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"tools/list","params":{},"id":"2"}'

# Call echo tool
curl -X POST http://localhost:8081/mcp/ \
  -H "Content-Type: application/json" \
  -d '{"jsonrpc":"2.0","method":"tools/call","params":{"name":"echo","arguments":{}},"id":"3"}'
```

## Architecture

- **McpServerApplication** - Spring Boot entry point
- **McpController** - HTTP endpoint for MCP requests
- **McpRequestHandler** - Routes MCP methods to handlers
- **ToolsRegistry** - Manages available tools
- **ControlBoardApiClient** - HTTP client for control-board API
- **McpRequest/McpResponse** - JSON-RPC 2.0 protocol classes
