// Note: Gson dependency required for compilation
// Maven: <dependency><groupId>com.google.code.gson</groupId><artifactId>gson</artifactId><version>2.10.1</version></dependency>
// Gradle: implementation 'com.google.code.gson:gson:2.10.1'
// Or download manually: https://mvnrepository.com/artifact/com.google.code.gson/gson
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * REST API Server using HTTP/1.1
 * Corresponds to Python version: lec-06-prg-07-rest-server-v3.py
 * 
 * Note: Gson dependency required for JSON processing
 */
public class RestServer {
    
    private static final Gson gson = new Gson();
    private static final MembershipHandler membershipHandler = new MembershipHandler();
    
    public static void main(String[] args) throws IOException {
        int port = 5000;
        
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        
        // Root path - API information page
        server.createContext("/", new RootHandler());
        
        // REST API endpoint
        server.createContext("/membership_api/", new MembershipApiHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("## REST API server started at http://127.0.0.1:" + port);
        System.out.println("Press Ctrl+C to stop the server...");
    }
    
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Membership REST API</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }\n" +
                "        .container { background: white; padding: 30px; border-radius: 8px; }\n" +
                "        h1 { color: #333; }\n" +
                "        .endpoint { background: #f9f9f9; padding: 15px; margin: 10px 0; border-left: 4px solid #007bff; }\n" +
                "        .method { display: inline-block; padding: 4px 8px; border-radius: 3px; font-weight: bold; margin-right: 10px; }\n" +
                "        .get { background: #28a745; color: white; }\n" +
                "        .post { background: #007bff; color: white; }\n" +
                "        .put { background: #ffc107; color: black; }\n" +
                "        .delete { background: #dc3545; color: white; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Membership REST API</h1>\n" +
                "        <p>Welcome to Membership Management REST API Service</p>\n" +
                "        <h2>API Endpoints</h2>\n" +
                "        <p>Base URL: <code>http://127.0.0.1:5000/membership_api/&lt;member_id&gt;</code></p>\n" +
                "        <div class=\"endpoint\">\n" +
                "            <span class=\"method get\">GET</span>\n" +
                "            <strong>/membership_api/&lt;member_id&gt;</strong>\n" +
                "            <p>Read member information</p>\n" +
                "        </div>\n" +
                "        <div class=\"endpoint\">\n" +
                "            <span class=\"method post\">POST</span>\n" +
                "            <strong>/membership_api/&lt;member_id&gt;</strong>\n" +
                "            <p>Create new member</p>\n" +
                "        </div>\n" +
                "        <div class=\"endpoint\">\n" +
                "            <span class=\"method put\">PUT</span>\n" +
                "            <strong>/membership_api/&lt;member_id&gt;</strong>\n" +
                "            <p>Update member information</p>\n" +
                "        </div>\n" +
                "        <div class=\"endpoint\">\n" +
                "            <span class=\"method delete\">DELETE</span>\n" +
                "            <strong>/membership_api/&lt;member_id&gt;</strong>\n" +
                "            <p>Delete member</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
            
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }
    
    static class MembershipApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            
            // Extract member_id
            String memberId = path.substring("/membership_api/".length());
            
            Map<String, String> response = new HashMap<>();
            
            switch (method) {
                case "GET":
                    response = membershipHandler.read(memberId);
                    break;
                case "POST":
                    String value = getFormValue(exchange, memberId);
                    response = membershipHandler.create(memberId, value);
                    break;
                case "PUT":
                    String updateValue = getFormValue(exchange, memberId);
                    response = membershipHandler.update(memberId, updateValue);
                    break;
                case "DELETE":
                    response = membershipHandler.delete(memberId);
                    break;
                default:
                    response.put("error", "Method not allowed");
                    exchange.sendResponseHeaders(405, 0);
                    exchange.close();
                    return;
            }
            
            // Return JSON response
            String jsonResponse = gson.toJson(response);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, jsonResponse.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
        
        private String getFormValue(HttpExchange exchange, String key) throws IOException {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            // Simple form-data parsing: key=value
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=");
                if (kv.length == 2 && kv[0].equals(key)) {
                    return kv[1];
                }
            }
            return "";
        }
    }
    
    static class MembershipHandler {
        private final Map<String, String> database = new HashMap<>();
        
        public Map<String, String> create(String id, String value) {
            Map<String, String> result = new HashMap<>();
            if (database.containsKey(id)) {
                result.put(id, "None");
            } else {
                database.put(id, value);
                result.put(id, database.get(id));
            }
            return result;
        }
        
        public Map<String, String> read(String id) {
            Map<String, String> result = new HashMap<>();
            if (database.containsKey(id)) {
                result.put(id, database.get(id));
            } else {
                result.put(id, "None");
            }
            return result;
        }
        
        public Map<String, String> update(String id, String value) {
            Map<String, String> result = new HashMap<>();
            if (database.containsKey(id)) {
                database.put(id, value);
                result.put(id, database.get(id));
            } else {
                result.put(id, "None");
            }
            return result;
        }
        
        public Map<String, String> delete(String id) {
            Map<String, String> result = new HashMap<>();
            if (database.containsKey(id)) {
                database.remove(id);
                result.put(id, "Removed");
            } else {
                result.put(id, "None");
            }
            return result;
        }
    }
}

