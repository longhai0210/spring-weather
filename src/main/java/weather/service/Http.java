package weather.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class Http {

    public HttpResponse<String> get (String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                                                                                                                                                                                                                                                                          .build();
        try{
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
        }
        catch(IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
