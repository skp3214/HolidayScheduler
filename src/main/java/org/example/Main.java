package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.*;
import java.util.*;


public class Main {

    static ArrayList<Holiday> holidays;
    static String selectedCountry;
    static JScrollPane scroll;
    static JScrollPane wishScroll;
    static String selectedMonth;
    static String selectedDay;
    static ArrayList<Holiday> filteredHolidays;
    static Set<Holiday> wishListHolidays;
    static JList<String> ls;
    static JList<String> wl;
    public static JTextField yearField;
    static JComboBox<String> countryComboBox;
    static JComboBox<String> monthComboBox;
    static JComboBox<String> dayComboBox;
    static Map<String, String> countryCodeMap;
    static Map<String, String> dayCodeMap;
    static Map<String, Integer> monthCodeMap;
    static DefaultListModel<String> df;
    static DefaultListModel<String> dw;


    static class HolidayScheduler {
        JFrame f;

        HolidayScheduler() throws IOException {
            f = new JFrame("Holiday Scheduler");
            wishListHolidays = new LinkedHashSet<>();
            JLabel appName = new JLabel("Holiday Scheduler");
            appName.setFont(new Font("Georgia", Font.ITALIC, 70));
            appName.setForeground(new Color(255, 255, 255));
            appName.setBounds(450, 15, 800, 82);

            JLabel countryName = new JLabel("Country Name: ");
            countryName.setFont(new Font("Georgia", Font.PLAIN, 23));
            countryName.setForeground(new Color(255, 255, 255));
            countryName.setBounds(100, 120, 180, 30);

            countryCodeMap = CountryCode.getCountryCodeMap();

            countryComboBox = new JComboBox<>(countryCodeMap.keySet().toArray(new String[0]));
            countryComboBox.setFont(new Font("Georgia", Font.ITALIC, 18));
            countryComboBox.addActionListener(e -> {
                selectedCountry = (String) countryComboBox.getSelectedItem();
                printHoliday(selectedCountry);
            });
            countryComboBox.setBounds(260, 120, 200, 30);

            JLabel year = new JLabel("Year: ");
            year.setFont(new Font("Georgia", Font.PLAIN, 23));
            year.setForeground(new Color(255, 255, 255));
            year.setBounds(490, 120, 100, 30);

            yearField = new JTextField();
            yearField.setFont(new Font("Georgia", Font.PLAIN, 23));
            yearField.setBounds(550, 120, 80, 30);

            JLabel month = new JLabel("Month:");
            month.setFont(new Font("Georgia", Font.PLAIN, 23));
            month.setForeground(new Color(255, 255, 255));
            month.setBounds(650, 120, 100, 30);


            monthCodeMap = getMonthMap();
            monthComboBox = new JComboBox<>(monthCodeMap.keySet().toArray(new String[0]));
            monthComboBox.setFont(new Font("Georgia", Font.PLAIN, 20));
            monthComboBox.setBounds(730, 120, 130, 30);
            monthComboBox.addActionListener(e -> {
                selectedMonth = (String) monthComboBox.getSelectedItem();
                printHoliday(selectedMonth);
            });

            JLabel day = new JLabel("Day:");
            day.setFont(new Font("Georgia", Font.PLAIN, 23));
            day.setForeground(new Color(255, 255, 255));
            day.setBounds(870, 120, 100, 30);

            dayCodeMap = getDayMap();
            dayComboBox = new JComboBox<>(dayCodeMap.keySet().toArray(new String[0]));
            dayComboBox.setFont(new Font("Georgia", Font.ITALIC, 18));
            dayComboBox.setBounds(920, 120, 120, 30);
            dayComboBox.addActionListener(e -> {
                selectedDay = (String) dayComboBox.getSelectedItem();
                printHoliday(selectedDay);
            });

            JButton showHolidays = new JButton("Show Holidays");
            showHolidays.setFont(new Font("Georgia", Font.PLAIN, 23));
            showHolidays.setBounds(1050, 120, 200, 30);
            showHolidays.addActionListener(e -> fetchHolidays());

            JLabel allHoliday = new JLabel("ALL HOLIDAYS");
            allHoliday.setForeground(Color.white);
            allHoliday.setFont(new Font("Georgia", Font.PLAIN, 40));
            allHoliday.setBounds(230, 195, 400, 50);

            JLabel wishedHoliday = new JLabel("WISHED HOLIDAYS");
            wishedHoliday.setForeground(Color.white);
            wishedHoliday.setFont(new Font("Georgia", Font.PLAIN, 40));
            wishedHoliday.setBounds(830, 195, 400, 50);

            scroll = new JScrollPane(ls);
            scroll.setBounds(80, 250, 600, 630);
            wishScroll = new JScrollPane(wl);
            wishScroll.setBounds(720, 250, 600, 630);

            f.add(appName);
            f.add(countryName);
            f.add(countryComboBox);
            f.add(year);
            f.add(yearField);
            f.add(month);
            f.add(monthComboBox);
            f.add(day);
            f.add(dayComboBox);
            f.add(showHolidays);
            f.add(allHoliday);
            f.add(wishedHoliday);
            f.add(scroll);
            f.add(wishScroll);

            f.setSize(1400, 940);
            f.setLayout(null);
            f.setVisible(true);
            f.setLocationRelativeTo(null);
            f.getContentPane().setBackground(new Color(5, 25, 35));
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }

        public void printHoliday(String selectedColor) {
            System.out.println(selectedColor);
        }

        public static Map<String, Integer> getMonthMap() {
            Map<String, Integer> monthMap = new LinkedHashMap<>();
            monthMap.put("Select", null);
            monthMap.put("January", 1);
            monthMap.put("February", 2);
            monthMap.put("March", 3);
            monthMap.put("April", 4);
            monthMap.put("May", 5);
            monthMap.put("June", 6);
            monthMap.put("July", 7);
            monthMap.put("August", 8);
            monthMap.put("September", 9);
            monthMap.put("October", 10);
            monthMap.put("November", 11);
            monthMap.put("December", 12);
            return monthMap;
        }

        public static Map<String, String> getDayMap() {
            Map<String, String> dayMap = new LinkedHashMap<>();
            dayMap.put("Select", null);
            dayMap.put("Sunday", "sunday");
            dayMap.put("Monday", "monday");
            dayMap.put("Tuesday", "tuesday");
            dayMap.put("Wednesday", "wednesday");
            dayMap.put("Thursday", "thursday");
            dayMap.put("Friday", "friday");
            dayMap.put("Saturday", "saturday");
            return dayMap;
        }

        void fetchHolidays() {
            String apiUrl;
            if (!Objects.equals(monthCodeMap.get(selectedMonth), null)) {
                apiUrl = "https://calendarific.com/api/v2/holidays?&api_key=2r6BTSv9nICNsYfObPwYeSRjiuj5V0Z0&country=" + countryCodeMap.get(selectedCountry) + "&year=" + yearField.getText() + "&month=" + monthCodeMap.get(selectedMonth);
                System.out.println("With Month");
            } else {
                apiUrl = "https://calendarific.com/api/v2/holidays?&api_key=2r6BTSv9nICNsYfObPwYeSRjiuj5V0Z0&country=" + countryCodeMap.get(selectedCountry) + "&year=" + yearField.getText();
                System.out.println("Without Month");
            }
            AllHolidays allHolidays = new AllHolidays(apiUrl);
            holidays = (ArrayList<Holiday>) allHolidays.fetchAllHolidays();
            selectedDay = dayCodeMap.get(selectedDay);
            if (!Objects.equals(selectedDay, null)) {
                filteredHolidays = filterHolidaysByDay(holidays, selectedDay);
            } else {
                filteredHolidays = holidays;
            }
            System.out.println(filteredHolidays.get(0).getHolidayDay());
            makeTheList(filteredHolidays);
        }

        void makeTheList(ArrayList<Holiday> filteredHolidays) {
            if (df == null) {
                df = new DefaultListModel<>();
            } else {
                df.clear();
            }
            for (Holiday filteredHoliday : filteredHolidays) {
                String formattedOutput = String.format(" %-33s | %-33s | %-33s ", filteredHoliday.getHolidayName(), filteredHoliday.getHolidayDay(), filteredHoliday.getHolidayDate());
                df.addElement(formattedOutput);
            }
            if (ls == null) {
                ls = new JList<>(df);
                scroll.setViewportView(ls);
                ls.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                ls.setCellRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        c.setFont(c.getFont().deriveFont(Font.PLAIN, 15));
                        if (index >= 0 && index % 2 == 0) {
                            c.setBackground(new Color(230, 230, 230));
                            c.setForeground(new Color(5, 25, 35));
                        } else {
                            c.setBackground(new Color(5, 25, 35));
                            c.setForeground(new Color(230, 230, 230));
                        }
                        c.setPreferredSize(new Dimension(c.getPreferredSize().width, 35));
                        return c;
                    }
                });

                ls.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (ls.getSelectedIndex() != -1) {
                            String[] holiday = ls.getSelectedValue().split("\\|");
                            printHoliday(holiday[0]);
                            Holiday wishedHoliday = new Holiday(holiday[0], holiday[1], holiday[2]);
                            saveInDatabase(wishedHoliday);

                        }
                    }
                });
            } else {
                ls.setModel(df);
            }
        }


        void makeTheWishList(Set<Holiday> wishListHolidays) {
            if (dw == null) {
                dw = new DefaultListModel<>();
            } else {
                dw.clear();
            }
            for (Holiday filteredHoliday : wishListHolidays) {
                String formattedOutput = String.format(" %-33s | %-33s | %-33s ", filteredHoliday.getHolidayName(), filteredHoliday.getHolidayDay(), filteredHoliday.getHolidayDate());
                dw.addElement(formattedOutput);
            }
            if (wl == null) {
                wl = new JList<>(dw);
                wishScroll.setViewportView(wl);
                wl.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                wl.setCellRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        c.setFont(c.getFont().deriveFont(Font.PLAIN, 15));
                        if (index >= 0 && index % 2 == 0) {
                            c.setBackground(new Color(230, 230, 230));
                            c.setForeground(new Color(5, 25, 35));
                        } else {
                            c.setBackground(new Color(5, 25, 35));
                            c.setForeground(new Color(230, 230, 230));
                        }
                        c.setPreferredSize(new Dimension(c.getPreferredSize().width, 35));
                        return c;
                    }
                });
                wl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (wl.getSelectedIndex() != -1) {
                            String[] holiday = wl.getSelectedValue().split("\\|");
                            String selectedHolidayName = holiday[0].trim();
                            System.out.println("Selected Holiday Name: " + selectedHolidayName);

                            for (Holiday h : wishListHolidays) {
                                if (h.getHolidayName().trim().equals(selectedHolidayName)) {
                                    deleteFromDatabase(h);
                                    break;
                                }
                            }
                            for (Holiday h : wishListHolidays) {
                                System.out.println(h.getHolidayName());
                            }
                        }
                    }
                });
            } else {
                wl.setModel(dw);
            }
        }


        private void saveInDatabase(Holiday wishedHoliday) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://127.0.0.1:3306/k21bp";
                String dbUsername = "root";
                String dbPassword = "@Sachin3214mysql";
                try (Connection con = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    String checksql = "Select count(*) from Holidays where HolidayName=?";
                    try (PreparedStatement check = con.prepareStatement(checksql)) {
                        check.setString(1, wishedHoliday.getHolidayName());
                        try (ResultSet resultSet = check.executeQuery()) {
                            resultSet.next();
                            int count = resultSet.getInt(1);
                            if (count > 0) {
                                System.out.println("This Holiday Already present");
                                return;
                            }
                        }
                    }


                    String sql = "Insert Into Holidays (HolidayName,HolidayDay,HolidayDate) Values (?,?,?)";
                    try (PreparedStatement smt = con.prepareStatement(sql)) {
                        smt.setString(1, wishedHoliday.getHolidayName());
                        smt.setString(2, wishedHoliday.getHolidayDay());
                        smt.setString(3, wishedHoliday.getHolidayDate());
                        int rowsInserted = smt.executeUpdate();
                        if (rowsInserted > 0) {
                            System.out.println("Added Successfully");
                            getHolidayFromDatabase(false);
                        } else {
                            System.out.println("Not added");
                        }
                    }

                }
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private void getHolidayFromDatabase(boolean first) {
            if (!first) {
                wishListHolidays = new LinkedHashSet<>();
            }
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://127.0.0.1:3306/k21bp";
                String dbUsername = "root";
                String dbPassword = "@Sachin3214mysql";
                try (Connection con = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    String sql = "SELECT HolidayName, HolidayDay, HolidayDate FROM Holidays";
                    try (PreparedStatement smt = con.prepareStatement(sql)) {
                        try (ResultSet rs = smt.executeQuery()) {
                            while (rs.next()) {
                                String holidayName = rs.getString("HolidayName");
                                String holidayDay = rs.getString("HolidayDay");
                                String holidayDate = rs.getString("HolidayDate");
                                Holiday holiday = new Holiday(holidayName, holidayDay, holidayDate);
                                wishListHolidays.add(holiday);
                            }
                        }
                    }
                }
                makeTheWishList(wishListHolidays);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }


        private void deleteFromDatabase(Holiday wishedHoliday) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String url = "jdbc:mysql://127.0.0.1:3306/k21bp";
                String dbUsername = "root";
                String dbPassword = "@Sachin3214mysql";
                try (Connection con = DriverManager.getConnection(url, dbUsername, dbPassword)) {
                    String sql = "DELETE FROM Holidays WHERE HolidayName=?";
                    try (PreparedStatement smt = con.prepareStatement(sql)) {
                        smt.setString(1, wishedHoliday.getHolidayName());
                        int rowsDeleted = smt.executeUpdate();
                        if (rowsDeleted > 0) {
                            System.out.println("Deleted successfully.");
                            getHolidayFromDatabase(false);
                        } else {
                            System.out.println("Failed to delete holiday.");
                        }
                    }
                }
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }


        private ArrayList<Holiday> filterHolidaysByDay(ArrayList<Holiday> holidays, String dayOfWeek) {
            ArrayList<Holiday> filteredHolidays = new ArrayList<>();
            for (Holiday holiday : holidays) {
                String holidayDay = holiday.getHolidayDay().toLowerCase();
                if (holidayDay.contains(dayOfWeek.toLowerCase())) {
                    filteredHolidays.add(holiday);
                }
            }
            return filteredHolidays;
        }


    }

    public static void main(String[] args) throws IOException {
        HolidayScheduler obj = new HolidayScheduler();
        obj.getHolidayFromDatabase(true);
    }
}