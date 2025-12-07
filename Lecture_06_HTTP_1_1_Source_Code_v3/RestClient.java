import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RestClient {
    
    private static final Gson gson = new Gson();
    
    public static void main(String[] args) {
        System.out.println("## REST API client started.");
        System.out.println("## Make sure the REST server is running on http://127.0.0.1:5000");
        System.out.println();
        
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        
        try {
            testGet(client, "0001", "#1");
            
            testPost(client, "0001", "apple", "#2");
            
            testGet(client, "0001", "#3");
            
            testPost(client, "0001", "xpple", "#4");
            
            testPut(client, "0002", "xrange", "#5");
            
            testPost(client, "0002", "xrange", "#6 (create)");
            testPut(client, "0002", "orange", "#6 (update)");
            
            testDelete(client, "0001", "#7");
            
            testDelete(client, "0001", "#8");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println();
        System.out.println("## REST API client completed.");
    }
    
    private static void testGet(HttpClient client, String memberId, String testNum) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:5000/membership_api/" + memberId))
                    .GET()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            
            System.out.println(testNum + " Code: " + response.statusCode() + 
                             " >> JSON: " + response.body() + 
                             " >> JSON Result: " + json.get(memberId).getAsString());
            
        } catch (Exception e) {
            System.out.println(testNum + " ERROR: " + e.getMessage());
        }
    }
    
    private static void testPost(HttpClient client, String memberId, String value, String testNum) {
        try {
            String postData = memberId + "=" + value;
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:5000/membership_api/" + memberId))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(postData))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            
            System.out.println(testNum + " Code: " + response.statusCode() + 
                             " >> JSON: " + response.body() + 
                             " >> JSON Result: " + json.get(memberId).getAsString());
        } catch (Exception e) {
            System.out.println(testNum + " ERROR: " + e.getMessage());
        }
    }
    
    private static void testPut(HttpClient client, String memberId, String value, String testNum) {
        try {
            String putData = memberId + "=" + value;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:5000/membership_api/" + memberId))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .PUT(HttpRequest.BodyPublishers.ofString(putData))
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            
            System.out.println(testNum + " Code: " + response.statusCode() + 
                             " >> JSON: " + response.body() + 
                             " >> JSON Result: " + json.get(memberId).getAsString());
        } catch (Exception e) {
            System.out.println(testNum + " ERROR: " + e.getMessage());
        }
    }
    
    private static void testDelete(HttpClient client, String memberId, String testNum) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:5000/membership_api/" + memberId))
                    .DELETE()
                    .version(HttpClient.Version.HTTP_1_1)
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            
            System.out.println(testNum + " Code: " + response.statusCode() + 
                             " >> JSON: " + response.body() + 
                             " >> JSON Result: " + json.get(memberId).getAsString());
        } catch (Exception e) {
            System.out.println(testNum + " ERROR: " + e.getMessage());
        }
    }
}
