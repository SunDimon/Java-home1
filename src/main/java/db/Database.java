package db;
import model.Film;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:films.db";

    public static void init() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS films (" +
                        "id TEXT PRIMARY KEY," +
                        "title TEXT," +
                        "description TEXT," +
                        "director TEXT," +
                        "producer TEXT," +
                        "release_date TEXT," +
                        "rt_score TEXT," +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                try (Statement st = conn.createStatement()) {
                    st.execute(sql);
                }
            }
        } catch (SQLException e) {
            System.err.println("Oshibka inicializacii BD: " + e.getMessage());
        }
    }

    public static void insertFilm(Film film) {
        String sql = "INSERT OR REPLACE INTO films(id, title, description, director, producer, release_date, rt_score) VALUES(?,?,?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, film.getId());
            ps.setString(2, film.getTitle());
            ps.setString(3, film.getDescription());
            ps.setString(4, film.getDirector());
            ps.setString(5, film.getProducer());
            ps.setString(6, film.getRelease_date());
            ps.setString(7, film.getRt_score());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Oshibka pri vstavke v BD: " + e.getMessage());
        }
    }

    public static List<Film> getAllFilms() {
        List<Film> list = new ArrayList<>();
        String sql = "SELECT id, title, description, director, producer, release_date, rt_score, created_at FROM films ORDER BY created_at ASC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Film f = new Film();
                f.setId(rs.getString("id"));
                f.setTitle(rs.getString("title"));
                f.setDescription(rs.getString("description"));
                f.setDirector(rs.getString("director"));
                f.setProducer(rs.getString("producer"));
                f.setRelease_date(rs.getString("release_date"));
                f.setRt_score(rs.getString("rt_score"));
                list.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Oshibka chteniya BD: " + e.getMessage());
        }
        return list;
    }

    public static void printAll() {
        List<Film> films = getAllFilms();
        if (films.isEmpty()) {
            System.out.println("V BD net zapisey");
            return;
        }
        System.out.println("Zapisi v BD:");
        int i = 1;
        for (Film f : films) {
            System.out.println(i++ + ") " + f);
        }
    }
}
