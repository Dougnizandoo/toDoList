package services;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Note;

public class NoteRepository {
    private static final String DB_URL = "jdbc:sqlite:notesdb.db";
    private final String resp = "OK";
    private Connection connectDB() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // construtor
    public NoteRepository(){
        createTable();
    }

    public void createTable() {
        String sql_query = """
                CREATE TABLE IF NOT EXISTS notes(
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    note TEXT NOT NULL,
                    is_done BOOLEAN NOT NULL DEFAULT 0
                );
                """;
        try (Connection conn = connectDB();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql_query);
        } catch (SQLException e) {
            System.out.println("Error trying create table: " + e.getMessage());
        }
    }

    public String createData(Note newNote) {
        String sql_query = """
                    INSERT INTO notes (note) values (?);
                """;
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql_query)) {
            ps.setString(1, newNote.getNote());
            ps.executeUpdate();
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
        return resp;
    }

    public List<Note> readAllData() {
        List<Note> notes = new ArrayList<>();
        String sql_query = """
            SELECT id, note, is_done FROM notes;
            """;
        try (Connection conn = connectDB();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql_query)) {

            while (rs.next()) {
                notes.add(new Note(
                        rs.getInt("id"),
                        rs.getString("note"),
                        rs.getBoolean("is_done")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return notes;
    }

    public Note readDataByID(int ID) {
        String sql_query = """
                        SELECT id, note, is_done FROM notes WHERE id = ?;
                    """;
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql_query)) {
            ps.setInt(1, ID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Note(
                            rs.getInt("id"),
                            rs.getString("note"),
                            rs.getBoolean("is_done")
                    );
                }else {
                    return null;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public List<Note> readDataByStatus(boolean status){
        List<Note> filteredList = new ArrayList<>();
        String sql_query = """
                        SELECT id, note FROM notes WHERE is_done = ?;
                    """;
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql_query)) {
            ps.setBoolean(1, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    filteredList.add(new Note(
                            rs.getInt("id"),
                            rs.getString("note"),
                            status
                    ));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return filteredList;
    }

    public String updateData(Note newNote) {
        String sql_query = """
                    UPDATE notes SET note = ?, is_done = ? WHERE id = ?;
                """;
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql_query)) {
            ps.setString(1, newNote.getNote());
            ps.setBoolean(2, newNote.isDone());
            ps.setInt(3, newNote.ID());
            ps.executeUpdate();
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
        return resp;
    }

    public String deleteData(int ID) {
        String sql_query = """
                    DELETE FROM notes where id = ?;
                """;
        try (Connection conn = connectDB();
             PreparedStatement ps = conn.prepareStatement(sql_query)) {
            ps.setInt(1, ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            return "Error: " + e.getMessage();
        }
        return resp;
    }
}
