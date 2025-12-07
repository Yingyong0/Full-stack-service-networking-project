import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonWebServer {
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public static void main(String[] args) throws IOException {
        int port = 8081;
        
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        
        server.createContext("/", new RootHandler());
        
        server.createContext("/parsed/", new ParsedDataHandler());
        
        server.createContext("/json/", new JsonFileHandler());
        
        server.createContext("/api/", new JsonApiHandler());
        
        server.setExecutor(null);
        server.start();
        
        System.out.println("## JSON Web Server started at http://localhost:" + port);
        System.out.println("## Access JSON examples in browser:");
        System.out.println("##   - http://localhost:" + port + "/  (View all JsonExample.java examples)");
        System.out.println("##   - http://localhost:" + port + "/parsed/03  (View parsed structured data)");
        System.out.println("##   - http://localhost:" + port + "/json/03  (View JSON file 03)");
        System.out.println("##   - http://localhost:" + port + "/json/04  (View JSON file 04)");
        System.out.println("##   - http://localhost:" + port + "/api/data  (View dynamically generated JSON)");
        System.out.println("Press Ctrl+C to stop the server...");
    }
    
    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String example1Result = executeExample1();
            String example2Result = executeExample2();
            String example3Result = executeExample3();
            String example4Result = executeExample4();
            
            String html = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>JSON Examples - JsonExample.java Demo</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }\n" +
                "        .container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 1200px; margin: 0 auto; }\n" +
                "        h1 { color: #333; border-bottom: 3px solid #007bff; padding-bottom: 10px; }\n" +
                "        h2 { color: #007bff; margin-top: 30px; }\n" +
                "        .example-box { background: #f9f9f9; padding: 20px; margin: 20px 0; border-left: 4px solid #28a745; border-radius: 4px; }\n" +
                "        .example-title { font-weight: bold; color: #28a745; font-size: 18px; margin-bottom: 10px; }\n" +
                "        .json-container { background: #1e1e1e; padding: 15px; border-radius: 4px; margin: 10px 0; overflow-x: auto; }\n" +
                "        .json-container pre { color: #d4d4d4; font-family: 'Courier New', monospace; margin: 0; white-space: pre-wrap; word-wrap: break-word; }\n" +
                "        .result-box { background: #e7f3ff; padding: 15px; border-left: 4px solid #007bff; margin: 10px 0; border-radius: 4px; }\n" +
                "        .result-box pre { margin: 0; font-family: 'Courier New', monospace; }\n" +
                "        .link-section { margin-top: 30px; padding-top: 20px; border-top: 2px solid #ddd; }\n" +
                "        .link-box { background: #f9f9f9; padding: 15px; margin: 10px 0; border-left: 4px solid #007bff; }\n" +
                "        a { color: #007bff; text-decoration: none; font-weight: bold; }\n" +
                "        a:hover { text-decoration: underline; }\n" +
                "        .description { color: #666; margin-top: 5px; font-size: 14px; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>📄 JSON Examples Demo - JsonExample.java</h1>\n" +
                "        <p>This page displays all four examples from JsonExample.java:</p>\n" +
                "        \n" +
                "        <div class=\"example-box\">\n" +
                "            <div class=\"example-title\">Example 1: Read from JSON file</div>\n" +
                "            <p><strong>Function:</strong> Read lec-06-prg-03-json-example.json file and parse as Java object</p>\n" +
                "            <div class=\"result-box\">\n" +
                "                <strong>Read Result:</strong>\n" +
                "                <pre>" + example1Result + "</pre>\n" +
                "            </div>\n" +
                "            <div class=\"json-container\">\n" +
                "                <strong style=\"color: #d4d4d4;\">Original JSON File Content:</strong>\n" +
                "                <pre>" + getJsonFileContent("lec-06-prg-03-json-example.json") + "</pre>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"example-box\">\n" +
                "            <div class=\"example-title\">Example 2: Create JSON object and write to file</div>\n" +
                "            <p><strong>Function:</strong> Create Java object in memory, convert to JSON and write to file</p>\n" +
                "            <div class=\"result-box\">\n" +
                "                <strong>Execution Result:</strong>\n" +
                "                <pre>" + example2Result + "</pre>\n" +
                "            </div>\n" +
                "            <div class=\"json-container\">\n" +
                "                <strong style=\"color: #d4d4d4;\">Generated JSON Content:</strong>\n" +
                "                <pre>" + getJsonFileContent("lec-06-prg-04-json-example.json") + "</pre>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"example-box\">\n" +
                "            <div class=\"example-title\">Example 3: Object to JSON string</div>\n" +
                "            <p><strong>Function:</strong> Convert Java object to formatted JSON string</p>\n" +
                "            <div class=\"json-container\">\n" +
                "                <strong style=\"color: #d4d4d4;\">Converted JSON String:</strong>\n" +
                "                <pre>" + escapeHtml(example3Result) + "</pre>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"example-box\">\n" +
                "            <div class=\"example-title\">Example 4: JSON string to object</div>\n" +
                "            <p><strong>Function:</strong> Parse JSON string to Java object and access properties</p>\n" +
                "            <div class=\"result-box\">\n" +
                "                <strong>Parse Result:</strong>\n" +
                "                <pre>" + example4Result + "</pre>\n" +
                "            </div>\n" +
                "            <div class=\"json-container\">\n" +
                "                <strong style=\"color: #d4d4d4;\">Original JSON String:</strong>\n" +
                "                <pre>" + escapeHtml(example3Result) + "</pre>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"link-section\">\n" +
                "            <h2>🔗 Other Viewing Options</h2>\n" +
                "            <div class=\"link-box\">\n" +
                "                <a href=\"/json/03\">View JSON File 03</a>\n" +
                "                <div class=\"description\">lec-06-prg-03-json-example.json</div>\n" +
                "            </div>\n" +
                "            <div class=\"link-box\">\n" +
                "                <a href=\"/json/04\">View JSON File 04</a>\n" +
                "                <div class=\"description\">lec-06-prg-04-json-example.json</div>\n" +
                "            </div>\n" +
                "            <div class=\"link-box\">\n" +
                "                <a href=\"/api/data\">View Dynamically Generated JSON (Pure JSON)</a>\n" +
                "                <div class=\"description\">Pure JSON format for API calls</div>\n" +
                "            </div>\n" +
                "            <div class=\"link-box\">\n" +
                "                <a href=\"/parsed/03\">View Parsed Data (Structured Display)</a>\n" +
                "                <div class=\"description\">Display parsed squad name, hometown, member information, etc. in a friendly way</div>\n" +
                "            </div>\n" +
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
    
    static class ParsedDataHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String fileName = "";
            
            if (path.endsWith("/03") || path.contains("/parsed/03")) {
                fileName = "lec-06-prg-03-json-example.json";
            } else if (path.endsWith("/04") || path.contains("/parsed/04")) {
                fileName = "lec-06-prg-04-json-example.json";
            } else {
                String html = createParsedDataHtml(createSuperHeroData(), "Dynamically Generated Superhero Data");
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, html.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(html.getBytes(StandardCharsets.UTF_8));
                os.close();
                return;
            }
            
            try {
                FileReader reader = new FileReader(fileName);
                SuperHeroData data = gson.fromJson(reader, SuperHeroData.class);
                reader.close();
                
                String html = createParsedDataHtml(data, "Parsed from: " + fileName);
                
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, html.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(html.getBytes(StandardCharsets.UTF_8));
                os.close();
            } catch (Exception e) {
                String errorHtml = "<!DOCTYPE html><html><head><title>Error</title></head><body>" +
                    "<h1>Parse Error</h1><p>" + e.getMessage() + "</p>" +
                    "<p><a href=\"/\">Back to Home</a></p></body></html>";
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(500, errorHtml.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(errorHtml.getBytes(StandardCharsets.UTF_8));
                os.close();
            }
        }
    }
    
    static class JsonFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String fileName = "";
            
            if (path.endsWith("/03") || path.contains("/json/03")) {
                fileName = "lec-06-prg-03-json-example.json";
            } else if (path.endsWith("/04") || path.contains("/json/04")) {
                fileName = "lec-06-prg-04-json-example.json";
            } else {
                fileName = "lec-06-prg-03-json-example.json";
            }
            
            try {
                String jsonContent = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
                
                String html = createJsonViewerHtml(fileName, jsonContent);
                
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, html.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(html.getBytes(StandardCharsets.UTF_8));
                os.close();
            } catch (IOException e) {
                String errorHtml = "<!DOCTYPE html><html><head><title>Error</title></head><body>" +
                    "<h1>File Not Found</h1><p>Cannot find file: " + fileName + "</p>" +
                    "<p><a href=\"/\">Back to Home</a></p></body></html>";
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(404, errorHtml.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(errorHtml.getBytes(StandardCharsets.UTF_8));
                os.close();
            }
        }
    }
    
    static class JsonApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            
            if (path.contains("/api/data")) {
                SuperHeroData data = createSuperHeroData();
                String jsonResponse = gson.toJson(data);
                
                String html = createJsonViewerHtml("Dynamically Generated JSON Data", jsonResponse);
                
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, html.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(html.getBytes(StandardCharsets.UTF_8));
                os.close();
            } else {
                SuperHeroData data = createSuperHeroData();
                String jsonResponse = gson.toJson(data);
                
                exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(200, jsonResponse.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
                os.close();
            }
        }
    }
    
    private static String createJsonViewerHtml(String title, String jsonContent) {
        String escapedJson = jsonContent
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
        
        return "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <title>" + title + "</title>\n" +
            "    <style>\n" +
            "        body { font-family: 'Courier New', monospace; margin: 20px; background: #1e1e1e; color: #d4d4d4; }\n" +
            "        .container { background: #252526; padding: 20px; border-radius: 8px; }\n" +
            "        h1 { color: #4ec9b0; margin-bottom: 20px; }\n" +
            "        .json-container { background: #1e1e1e; padding: 20px; border: 1px solid #3e3e42; border-radius: 4px; overflow-x: auto; }\n" +
            "        pre { margin: 0; white-space: pre-wrap; word-wrap: break-word; }\n" +
            "        .back-link { margin-top: 20px; }\n" +
            "        a { color: #4ec9b0; text-decoration: none; }\n" +
            "        a:hover { text-decoration: underline; }\n" +
            "        .info { color: #858585; font-size: 12px; margin-bottom: 10px; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <h1>📄 " + title + "</h1>\n" +
            "        <div class=\"info\">File Size: " + jsonContent.getBytes(StandardCharsets.UTF_8).length + " bytes</div>\n" +
            "        <div class=\"json-container\">\n" +
            "            <pre>" + escapedJson + "</pre>\n" +
            "        </div>\n" +
            "        <div class=\"back-link\">\n" +
            "            <a href=\"/\">← Back to Home</a>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";
    }
    
    private static String executeExample1() {
        try {
            FileReader reader = new FileReader("lec-06-prg-03-json-example.json");
            SuperHeroData superHeroes = gson.fromJson(reader, SuperHeroData.class);
            reader.close();
            
            return "Home Town: " + superHeroes.homeTown + "\n" +
                   "Active: " + superHeroes.active + "\n" +
                   "Power: " + superHeroes.members[1].powers[2];
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    private static String executeExample2() {
        try {
            SuperHeroData superHeroes = createSuperHeroData();
            FileWriter writer = new FileWriter("lec-06-prg-04-json-example.json");
            gson.toJson(superHeroes, writer);
            writer.close();
            return "JSON data written to file successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    private static String executeExample3() {
        SuperHeroData superHeroes = createSuperHeroData();
        return gson.toJson(superHeroes);
    }
    
    private static String executeExample4() {
        String jsonString = executeExample3();
        SuperHeroData parsed = gson.fromJson(jsonString, SuperHeroData.class);
        return "Home Town: " + parsed.homeTown;
    }
    
    private static String getJsonFileContent(String fileName) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
            return escapeHtml(content);
        } catch (Exception e) {
            return "File not found: " + fileName;
        }
    }
    
    private static String escapeHtml(String text) {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
    
    private static String createParsedDataHtml(SuperHeroData data, String title) {
        StringBuilder membersHtml = new StringBuilder();
        
        if (data.members != null) {
            for (int i = 0; i < data.members.length; i++) {
                Member member = data.members[i];
                StringBuilder powersHtml = new StringBuilder();
                if (member.powers != null) {
                    for (String power : member.powers) {
                        powersHtml.append("<li>").append(escapeHtml(power)).append("</li>");
                    }
                }
                
                membersHtml.append("<div class=\"member-card\">")
                    .append("<h3>").append(escapeHtml(member.name != null ? member.name : "Unknown")).append("</h3>")
                    .append("<div class=\"member-info\">")
                    .append("<p><strong>Age:</strong> ").append(member.age).append(" years old</p>")
                    .append("<p><strong>Secret Identity:</strong> ").append(escapeHtml(member.secretIdentity != null ? member.secretIdentity : "Unknown")).append("</p>")
                    .append("<p><strong>Powers:</strong></p>")
                    .append("<ul class=\"powers-list\">").append(powersHtml.toString()).append("</ul>")
                    .append("</div>")
                    .append("</div>");
            }
        }
        
        String html = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <title>Parsed Data - " + escapeHtml(title) + "</title>\n" +
            "    <style>\n" +
            "        body { font-family: Arial, sans-serif; margin: 20px; background: #f5f5f5; }\n" +
            "        .container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 1200px; margin: 0 auto; }\n" +
            "        h1 { color: #333; border-bottom: 3px solid #007bff; padding-bottom: 10px; }\n" +
            "        h2 { color: #007bff; margin-top: 30px; margin-bottom: 15px; }\n" +
            "        .info-section { background: #f9f9f9; padding: 20px; margin: 20px 0; border-left: 4px solid #28a745; border-radius: 4px; }\n" +
            "        .info-row { display: flex; margin: 10px 0; padding: 8px 0; border-bottom: 1px solid #e0e0e0; }\n" +
            "        .info-row:last-child { border-bottom: none; }\n" +
            "        .info-label { font-weight: bold; color: #555; width: 150px; }\n" +
            "        .info-value { color: #333; flex: 1; }\n" +
            "        .members-section { margin-top: 30px; }\n" +
            "        .member-card { background: #fff; border: 2px solid #007bff; border-radius: 8px; padding: 20px; margin: 15px 0; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }\n" +
            "        .member-card h3 { color: #007bff; margin-top: 0; border-bottom: 2px solid #007bff; padding-bottom: 10px; }\n" +
            "        .member-info p { margin: 8px 0; }\n" +
            "        .powers-list { list-style-type: none; padding-left: 0; margin: 10px 0; }\n" +
            "        .powers-list li { background: #e7f3ff; padding: 8px 12px; margin: 5px 0; border-radius: 4px; border-left: 3px solid #007bff; }\n" +
            "        .back-link { margin-top: 30px; padding-top: 20px; border-top: 2px solid #ddd; }\n" +
            "        a { color: #007bff; text-decoration: none; font-weight: bold; }\n" +
            "        a:hover { text-decoration: underline; }\n" +
            "        .status-active { color: #28a745; font-weight: bold; }\n" +
            "        .status-inactive { color: #dc3545; font-weight: bold; }\n" +
            "        code { background: #f4f4f4; padding: 2px 6px; border-radius: 3px; font-family: 'Courier New', monospace; }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <h1>📊 " + escapeHtml(title) + "</h1>\n" +
            "        <p>The following is all data parsed from JsonExample.java:</p>\n" +
            "        \n" +
            "        <div class=\"info-section\">\n" +
            "            <h2>🏠 Squad Basic Information</h2>\n" +
            "            <div class=\"info-row\">\n" +
            "                <span class=\"info-label\">Squad Name:</span>\n" +
            "                <span class=\"info-value\">" + escapeHtml(data.squadName != null ? data.squadName : "Unknown") + "</span>\n" +
            "            </div>\n" +
            "            <div class=\"info-row\">\n" +
            "                <span class=\"info-label\">Home Town:</span>\n" +
            "                <span class=\"info-value\">" + escapeHtml(data.homeTown != null ? data.homeTown : "Unknown") + "</span>\n" +
            "            </div>\n" +
            "            <div class=\"info-row\">\n" +
            "                <span class=\"info-label\">Formed Year:</span>\n" +
            "                <span class=\"info-value\">" + data.formed + "</span>\n" +
            "            </div>\n" +
            "            <div class=\"info-row\">\n" +
            "                <span class=\"info-label\">Secret Base:</span>\n" +
            "                <span class=\"info-value\">" + escapeHtml(data.secretBase != null ? data.secretBase : "Unknown") + "</span>\n" +
            "            </div>\n" +
            "            <div class=\"info-row\">\n" +
            "                <span class=\"info-label\">Status:</span>\n" +
            "                <span class=\"info-value\">" + 
            (data.active ? "<span class=\"status-active\">✓ Active</span>" : "<span class=\"status-inactive\">✗ Inactive</span>") +
            "            </div>\n" +
            "        </div>\n" +
            "        \n" +
            "        <div class=\"members-section\">\n" +
            "            <h2>👥 Member List (" + (data.members != null ? data.members.length : 0) + " members)</h2>\n" +
            membersHtml.toString() +
            "        </div>\n" +
            "        \n" +
            "        <div class=\"info-section\">\n" +
            "            <h2>📝 Code Access Examples</h2>\n" +
            "            <div class=\"info-row\">\n" +
            "                <span class=\"info-label\">Access Squad Name:</span>\n" +
            "                <span class=\"info-value\"><code>superHeroes.squadName</code> = \"" + escapeHtml(data.squadName != null ? data.squadName : "") + "\"</span>\n" +
            "            </div>\n" +
            "            <div class=\"info-row\">\n" +
            "                <span class=\"info-label\">Access Home Town:</span>\n" +
            "                <span class=\"info-value\"><code>superHeroes.homeTown</code> = \"" + escapeHtml(data.homeTown != null ? data.homeTown : "") + "\"</span>\n" +
            "            </div>\n" +
            "            <div class=\"info-row\">\n" +
            "                <span class=\"info-label\">Access Status:</span>\n" +
            "                <span class=\"info-value\"><code>superHeroes.active</code> = " + data.active + "</span>\n" +
            "            </div>\n";
        
        StringBuilder htmlBuilder = new StringBuilder(html);
        
        if (data.members != null && data.members.length > 1) {
            String power = data.members[1].powers != null && data.members[1].powers.length > 2 
                ? data.members[1].powers[2] : "Unknown";
            htmlBuilder.append("            <div class=\"info-row\">\n")
                .append("                <span class=\"info-label\">Access Member Power:</span>\n")
                .append("                <span class=\"info-value\"><code>superHeroes.members[1].powers[2]</code> = \"").append(escapeHtml(power)).append("\"</span>\n")
                .append("            </div>\n");
        }
        
        htmlBuilder.append("        </div>\n")
            .append("        \n")
            .append("        <div class=\"back-link\">\n")
            .append("            <a href=\"/\">← Back to Home</a> | \n")
            .append("            <a href=\"/json/03\">View Original JSON</a>\n")
            .append("        </div>\n")
            .append("    </div>\n")
            .append("</body>\n")
            .append("</html>");
        
        return htmlBuilder.toString();
    }
    
    static class SuperHeroData {
        String squadName;
        String homeTown;
        int formed;
        String secretBase;
        boolean active;
        Member[] members;
    }
    
    static class Member {
        String name;
        int age;
        String secretIdentity;
        String[] powers;
    }
    
    private static SuperHeroData createSuperHeroData() {
        SuperHeroData data = new SuperHeroData();
        data.squadName = "Super hero squad";
        data.homeTown = "Metro City";
        data.formed = 2016;
        data.secretBase = "Super tower";
        data.active = true;
        
        Member member1 = new Member();
        member1.name = "Molecule Man";
        member1.age = 29;
        member1.secretIdentity = "Dan Jukes";
        member1.powers = new String[]{"Radiation resistance", "Turning tiny", "Radiation blast"};
        
        Member member2 = new Member();
        member2.name = "Madame Uppercut";
        member2.age = 39;
        member2.secretIdentity = "Jane Wilson";
        member2.powers = new String[]{"Million tonne punch", "Damage resistance", "Superhuman reflexes"};
        
        Member member3 = new Member();
        member3.name = "Eternal Flame";
        member3.age = 1000000;
        member3.secretIdentity = "Unknown";
        member3.powers = new String[]{"Immortality", "Heat Immunity", "Inferno", "Teleportation", "Interdimensional travel"};
        
        data.members = new Member[]{member1, member2, member3};
        
        return data;
    }
}

