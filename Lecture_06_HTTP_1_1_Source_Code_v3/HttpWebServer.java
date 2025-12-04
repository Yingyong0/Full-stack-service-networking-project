import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * HTTP/1.1 Web Server
 * 对应 Python 版本的 lec-06-prg-01-http-web-server.py
 */
public class HttpWebServer {
    
    public static void main(String[] args) throws IOException {
        String serverName = "localhost";
        int serverPort = 8080;
        
        HttpServer server = HttpServer.create(new InetSocketAddress(serverName, serverPort), 0);
        server.createContext("/", new MyHttpHandler());
        server.setExecutor(null); // 使用默认执行器
        
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
            
            // 打印请求详情
            printHttpRequestDetail(exchange, method, path);
            
            String response = "";
            
            if ("GET".equals(method)) {
                System.out.println("## do_GET() activated.");
                
                if (query != null && query.contains("var1") && query.contains("var2")) {
                    // GET 请求带参数 - 计算功能
                    String[] params = parameterRetrieval(query);
                    int result = simpleCalc(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
                    response = "<html>GET request for calculation => " + 
                               params[0] + " x " + params[1] + " = " + result + "</html>";
                    System.out.println("## GET request for calculation => " + 
                                     params[0] + " x " + params[1] + " = " + result + ".");
                } else {
                    // GET 请求目录
                    response = "<html><p>HTTP Request GET for Path: " + path + "</p></html>";
                    System.out.println("## GET request for directory => " + path + ".");
                }
            } else if ("POST".equals(method)) {
                System.out.println("## do_POST() activated.");
                
                // 读取POST数据
                InputStream requestBody = exchange.getRequestBody();
                String postData = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("## POST request data => " + postData + ".");
                
                String[] params = parameterRetrieval(postData);
                int result = simpleCalc(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
                response = "POST request for calculation => " + 
                          params[0] + " x " + params[1] + " = " + result;
                System.out.println("## POST request for calculation => " + 
                                 params[0] + " x " + params[1] + " = " + result + ".");
            }
            
            // 发送响应
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
    }
}

