package service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import model.Film;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class HttpService {
    private static final String API_URL = "https://ghibliapi.vercel.app/films";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static Film[] fetchFilms() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 200) {
            throw new IOException("Neozhidanniy kod otveta: " + resp.statusCode());
        }

        try {
            Film[] films = gson.fromJson(resp.body(), Film[].class);
            return films;
        } catch (JsonSyntaxException e) {
            throw new IOException("Oshibka parsinga JSON: " + e.getMessage(), e);
        }
    }
}
