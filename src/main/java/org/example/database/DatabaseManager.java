package org.example.database;


import org.example.model.Holiday;
import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/k21bp";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "@Sachin3214mysql";

    public void saveHolidayToDatabase(Holiday holiday) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String checksql = "Select count(*) from Holidays where HolidayName=?";
            try (PreparedStatement check = conn.prepareStatement(checksql)) {
                check.setString(1, holiday.getHolidayName());
                try (ResultSet resultSet = check.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt(1);
                    if (count > 0) {
                        System.out.println("This Holiday Already present");
                        return;
                    }
                }
            }
            String sql = "INSERT INTO Holidays (HolidayName, HolidayDay, HolidayDate) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, holiday.getHolidayName());
                stmt.setString(2, holiday.getHolidayDay());
                stmt.setString(3, holiday.getHolidayDate());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Set<Holiday> getHolidaysFromDatabase() {
        Set<Holiday> holidays = new LinkedHashSet<>();
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "SELECT HolidayName, HolidayDay, HolidayDate FROM Holidays";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Holiday holiday = new Holiday(
                            rs.getString("HolidayName"),
                            rs.getString("HolidayDay"),
                            rs.getString("HolidayDate")
                    );
                    holidays.add(holiday);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return holidays;
    }

    public void deleteHolidayFromDatabase(Holiday holiday) {
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            String sql = "DELETE FROM Holidays WHERE HolidayName = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, holiday.getHolidayName());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
