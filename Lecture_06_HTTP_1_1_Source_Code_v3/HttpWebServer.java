import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpWebServer {
    
    public static void main(String[] args) throws IOException {
        String serverName = "localhost";
        int serverPort = 8080;
        
        HttpServer server = HttpServer.create(new InetSocketAddress(serverName, serverPort), 0);
        
        server.createContext("/", new MyHttpHandler());
        
        server.setExecutor(null);
        
        System.out.println("## HTTP server started at http://" + serverName + ":" + serverPort);
        
        server.start();
        
        System.out.println("Press Ctrl+C to stop the server...");
    }
    
    static class MyHttpHandler implements HttpHandler {
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String query = exchange.getRequestURI().getQuery();
            
            printHttpRequestDetail(exchange, method, path);
            
            String response = "";
            
            if ("GET".equals(method)) {
                System.out.println("## do_GET() activated.");
                
                if (query != null && query.contains("var1") && query.contains("var2")) {
                    String[] params = parameterRetrieval(query);
                    int result = simpleCalc(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
                    response = createCalculationResultHtml(params[0], params[1], result, "GET");
                    System.out.println("## GET request for calculation => " + 
                                     params[0] + " x " + params[1] + " = " + result + ".");
                } else {
                    response = createFunctionPageHtml();
                    System.out.println("## GET request for directory => " + path + ".");
                }
            } else if ("POST".equals(method)) {
                System.out.println("## do_POST() activated.");
                
                InputStream requestBody = exchange.getRequestBody();
                String postData = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("## POST request data => " + postData + ".");
                
                String[] params = parameterRetrieval(postData);
                int result = simpleCalc(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
                response = createCalculationResultHtml(params[0], params[1], result, "POST");
                System.out.println("## POST request for calculation => " + 
                                 params[0] + " x " + params[1] + " = " + result + ".");
            }
            
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes(StandardCharsets.UTF_8));
            
            os.close();
        }
        
        private void printHttpRequestDetail(HttpExchange exchange, String method, String path) {
            String clientAddress = exchange.getRemoteAddress().getAddress().getHostAddress();
            int clientPort = exchange.getRemoteAddress().getPort();
            String requestLine = method + " " + path + " HTTP/1.1";
            
            System.out.println("::Client address   : " + clientAddress);
            System.out.println("::Client port      : " + clientPort);
            System.out.println("::Request command  : " + method);
            System.out.println("::Request line     : " + requestLine);
            System.out.println("::Request path     : " + path);
            System.out.println("::Request version  : HTTP/1.1");
        }
        
        private int simpleCalc(int para1, int para2) {
            return para1 * para2;
        }
        
        private String[] parameterRetrieval(String msg) {
            String[] fields = msg.split("&");
            String var1 = fields[0].split("=")[1];
            String var2 = fields[1].split("=")[1];
            return new String[]{var1, var2};
        }
        
        private String createFunctionPageHtml() {
            return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>HTTP Web Server - Function Overview</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }\n" +
                "        .container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); max-width: 1000px; margin: 0 auto; }\n" +
                "        h1 { color: #333; border-bottom: 3px solid #007bff; padding-bottom: 10px; }\n" +
                "        h2 { color: #007bff; margin-top: 30px; }\n" +
                "        .feature-box { background: #f9f9f9; padding: 20px; margin: 20px 0; border-left: 4px solid #28a745; border-radius: 4px; }\n" +
                "        .method { display: inline-block; padding: 4px 8px; border-radius: 3px; font-weight: bold; margin-right: 10px; }\n" +
                "        .get { background: #28a745; color: white; }\n" +
                "        .post { background: #007bff; color: white; }\n" +
                "        .example { background: #1e1e1e; color: #d4d4d4; padding: 15px; border-radius: 4px; margin: 10px 0; font-family: 'Courier New', monospace; overflow-x: auto; }\n" +
                "        .example a { color: #4ec9b0; text-decoration: none; }\n" +
                "        .example a:hover { text-decoration: underline; }\n" +
                "        .info-section { background: #e7f3ff; padding: 15px; margin: 15px 0; border-left: 4px solid #007bff; border-radius: 4px; }\n" +
                "        .info-section p { margin: 8px 0; }\n" +
                "        code { background: #f4f4f4; padding: 2px 6px; border-radius: 3px; font-family: 'Courier New', monospace; }\n" +
                "        ul { line-height: 1.8; }\n" +
                "        li { margin: 5px 0; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>🌐 HTTP Web Server - Function Overview</h1>\n" +
                "        <p>This is an HTTP/1.1 web server implemented in Java using <code>com.sun.net.httpserver.HttpServer</code>.</p>\n" +
                "        \n" +
                "        <div class=\"info-section\">\n" +
                "            <h2>📋 Server Information</h2>\n" +
                "            <p><strong>Server Address:</strong> <code>http://localhost:8080</code></p>\n" +
                "            <p><strong>Protocol:</strong> HTTP/1.1</p>\n" +
                "            <p><strong>Supported Methods:</strong> GET, POST</p>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"feature-box\">\n" +
                "            <h2>✨ Core Functions</h2>\n" +
                "            <ul>\n" +
                "                <li><strong>HTTP Request Handling:</strong> Processes GET and POST requests according to HTTP/1.1 protocol</li>\n" +
                "                <li><strong>Parameter Parsing:</strong> Extracts query parameters (GET) and form data (POST)</li>\n" +
                "                <li><strong>Mathematical Calculation:</strong> Performs multiplication operation on two integer parameters</li>\n" +
                "                <li><strong>Request Logging:</strong> Displays detailed request information in server console</li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "        \n" +
                "        <h2>📡 API Endpoints</h2>\n" +
                "        \n" +
                "        <div class=\"feature-box\">\n" +
                "            <span class=\"method get\">GET</span>\n" +
                "            <strong>GET Request with Calculation</strong>\n" +
                "            <p>Send GET request with query parameters to perform multiplication.</p>\n" +
                "            <div class=\"example\">\n" +
                "                <strong>Example URL:</strong><br>\n" +
                "                <a href=\"/?var1=9&var2=9\" target=\"_blank\">http://localhost:8080/?var1=9&var2=9</a><br><br>\n" +
                "                <strong>Parameters:</strong><br>\n" +
                "                - <code>var1</code>: First integer (e.g., 9)<br>\n" +
                "                - <code>var2</code>: Second integer (e.g., 9)<br><br>\n" +
                "                <strong>Response:</strong> HTML page showing calculation result (9 x 9 = 81)\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"feature-box\">\n" +
                "            <span class=\"method post\">POST</span>\n" +
                "            <strong>POST Request with Calculation</strong>\n" +
                "            <p>Send POST request with form data to perform multiplication.</p>\n" +
                "            <div class=\"example\">\n" +
                "                <strong>Example Request:</strong><br>\n" +
                "                URL: <code>http://localhost:8080/</code><br>\n" +
                "                Method: <code>POST</code><br>\n" +
                "                Content-Type: <code>application/x-www-form-urlencoded</code><br>\n" +
                "                Body: <code>var1=9&var2=9</code><br><br>\n" +
                "                <strong>Response:</strong> HTML page showing calculation result (9 x 9 = 81)\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"feature-box\">\n" +
                "            <span class=\"method get\">GET</span>\n" +
                "            <strong>GET Request for Any Path</strong>\n" +
                "            <p>Access any path without calculation parameters to see path information.</p>\n" +
                "            <div class=\"example\">\n" +
                "                <strong>Example URLs:</strong><br>\n" +
                "                <a href=\"/temp/\" target=\"_blank\">http://localhost:8080/temp/</a><br>\n" +
                "                <a href=\"/test\" target=\"_blank\">http://localhost:8080/test</a><br><br>\n" +
                "                <strong>Response:</strong> HTML page showing the requested path\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"info-section\">\n" +
                "            <h2>🔍 Request Details Logged</h2>\n" +
                "            <p>For each request, the server logs the following information to the console:</p>\n" +
                "            <ul>\n" +
                "                <li>Client IP address and port</li>\n" +
                "                <li>HTTP request method (GET/POST)</li>\n" +
                "                <li>Request line (method + path + HTTP version)</li>\n" +
                "                <li>Request path</li>\n" +
                "                <li>HTTP version (HTTP/1.1)</li>\n" +
                "                <li>Request body data (for POST requests)</li>\n" +
                "                <li>Calculation results (if applicable)</li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"info-section\">\n" +
                "            <h2>🧮 Calculation Function</h2>\n" +
                "            <p>The server performs a simple multiplication operation:</p>\n" +
                "            <p><code>result = var1 × var2</code></p>\n" +
                "            <p><strong>Example:</strong> If <code>var1=9</code> and <code>var2=9</code>, the result is <code>81</code>.</p>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"info-section\">\n" +
                "            <h2>💡 Testing the Server</h2>\n" +
                "            <p>You can test this server using:</p>\n" +
                "            <ul>\n" +
                "                <li><strong>Web Browser:</strong> Click the example links above or manually enter URLs</li>\n" +
                "                <li><strong>HttpWebClient.java:</strong> Run the Java client program to send automated requests</li>\n" +
                "                <li><strong>curl command:</strong> Use command-line tools like <code>curl</code> to send requests</li>\n" +
                "            </ul>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        }
        
        private String createCalculationResultHtml(String var1, String var2, int result, String method) {
            return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Calculation Result</title>\n" +
                "    <style>\n" +
                "        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; display: flex; justify-content: center; align-items: center; min-height: 80vh; }\n" +
                "        .container { background: white; padding: 40px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); text-align: center; }\n" +
                "        h1 { color: #333; margin-bottom: 30px; }\n" +
                "        .method-badge { display: inline-block; padding: 6px 12px; border-radius: 4px; font-weight: bold; margin-bottom: 20px; }\n" +
                "        .get { background: #28a745; color: white; }\n" +
                "        .post { background: #007bff; color: white; }\n" +
                "        .calculation { font-size: 24px; color: #007bff; margin: 20px 0; padding: 20px; background: #e7f3ff; border-radius: 4px; }\n" +
                "        .result { font-size: 36px; font-weight: bold; color: #28a745; margin: 20px 0; }\n" +
                "        .back-link { margin-top: 30px; }\n" +
                "        a { color: #007bff; text-decoration: none; font-weight: bold; }\n" +
                "        a:hover { text-decoration: underline; }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <span class=\"method-badge " + method.toLowerCase() + "\">" + method + "</span>\n" +
                "        <h1>Calculation Result</h1>\n" +
                "        <div class=\"calculation\">\n" +
                "            " + var1 + " × " + var2 + " = <span class=\"result\">" + result + "</span>\n" +
                "        </div>\n" +
                "        <div class=\"back-link\">\n" +
                "            <a href=\"/\">← Back to Home</a>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        }
    }
}
