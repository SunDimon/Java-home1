package service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Film;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {
    private static final Path LAST_FILE = Path.of("last_film.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveLastFilm(Film film) {
        try {
            String json = gson.toJson(film);
            Files.writeString(LAST_FILE, json);
            try {
                new ProcessBuilder("notepad.exe", LAST_FILE.toString()).start();
            } catch (Exception ex) {
                System.out.println("Ne udalos otkrit bloknot. Fail sohranen v: " + LAST_FILE.toAbsolutePath());
            }
            System.out.println("Fail " + LAST_FILE + " sohranen");
        } catch (IOException e) {
            System.err.println("Oshibka zapisi faila: " + e.getMessage());
        }
    }

    public static Film readLastFilm() {
        try {
            if (!Files.exists(LAST_FILE)) {
                System.out.println("Fail " + LAST_FILE + " ne naiden");
                return null;
            }
            String json = Files.readString(LAST_FILE);
            Film film = gson.fromJson(json, Film.class);
            return film;
        } catch (IOException e) {
            System.err.println("Oshibka chteniya faila: " + e.getMessage());
            return null;
        }
    }
}
