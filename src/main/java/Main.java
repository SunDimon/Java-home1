import db.Database;
import model.Film;
import service.HttpService;
import service.FileService;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Zapusk");
        Database.init();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("\nVvedite komandu (HTTP, FILE, DB, EXIT): ");
            String cmd = sc.nextLine().trim().toUpperCase();

            switch (cmd) {
                case "HTTP":
                    doHttp();
                    break;
                case "FILE":
                    doFile();
                    break;
                case "DB":
                    doDb();
                    break;
                case "EXIT":
                    System.out.println("Zavershenie programmi");
                    return;
                default:
                    System.out.println("Neizvestnaya komanda");
                    break;
            }

        }
    }

    private static void doHttp() {
        try {
            System.out.println("Zapros k API...");
            Film[] films = HttpService.fetchFilms();
            if (films == null || films.length == 0) {
                System.out.println("API vernulo pustoy spisok");
                return;
            }
            System.out.println("Polucheno filmov: " + films.length);
            Arrays.stream(films).limit(3).forEach(f -> System.out.println("- " + f.getTitle() + " (" + f.getRelease_date() + ")"));

            Film first = films[0];
            System.out.println("\nSohranenie v bazu:");
            System.out.println(first);
            Database.insertFilm(first);

            FileService.saveLastFilm(first);
        } catch (Exception e) {
            System.err.println("Oshibka HTTP: " + e.getMessage());
        }
    }

    private static void doFile() {
        Film f = FileService.readLastFilm();
        if (f != null) {
            System.out.println("Dannie iz faila:");
            System.out.println(f);
        }
    }

    private static void doDb() {
        Database.printAll();
    }
}
