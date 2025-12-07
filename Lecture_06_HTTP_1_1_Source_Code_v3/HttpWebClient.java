import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class HttpWebClient {
    
    public static void main(String[] args) {
        System.out.println("## HTTP client started.");
        
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        
        try {
            System.out.println("## GET request for http://localhost:8080/temp/");
            HttpRequest request1 = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/temp/"))
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            
            HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("## GET response [start]");
            System.out.println(response1.body());
            System.out.println("## GET response [end]");
            
            System.out.println("\n## GET request for http://localhost:8080/?var1=9&var2=9");
            HttpRequest request2 = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/?var1=9&var2=9"))
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            
            HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
            System.out.println("## GET response [start]");
            System.out.println(response2.body());
            System.out.println("## GET response [end]");
            
            System.out.println("\n## POST request for http://localhost:8080/ with var1 is 9 and var2 is 9");
            String postData = "var1=9&var2=9";
            HttpRequest request3 = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(postData))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            
            HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
            System.out.println("## POST response [start]");
            System.out.println(response3.body());
            System.out.println("## POST response [end]");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n## HTTP client completed.");
    }
}
