package org.example.ui;


import org.example.model.Holiday;
import org.example.logic.HolidayLogic;
import org.example.utils.CountryCode;
import org.example.database.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class HolidaySchedulerUI {
    String selectedCountry;
    static JScrollPane scroll;
    static JScrollPane wishScroll;
    String selectedMonth;
    String selectedDay;
    ArrayList<Holiday> filteredHolidays;
    static Set<Holiday> wishListHolidays;
    static JList<String> ls;
    static JList<String> wl;
    public static JTextField yearField;
    static JComboBox<String> countryComboBox;
    static JComboBox<String> monthComboBox;
    static JComboBox<String> dayComboBox;
    static Map<String, String> dayCodeMap;
    static Map<String, Integer> monthCodeMap;
    static DefaultListModel<String> df;
    static DefaultListModel<String> dw;
    JFrame frame;
    HolidayLogic holidayLogic;
    DatabaseManager databaseManager;
    public HolidaySchedulerUI(){
        holidayLogic=new HolidayLogic();
        databaseManager=new DatabaseManager();
    }

    public void initialize() throws Exception{
        frame = new JFrame("Holiday Scheduler");
        frame.setSize(1400, 940);
        frame.setLayout(null);
        frame.getContentPane().setBackground(new Color(5, 25, 35));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupComponents();
        makeTheWishList();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    private void setupComponents() throws IOException {
        JLabel appName = new JLabel("Holiday Scheduler");
        appName.setFont(new Font("Georgia", Font.ITALIC, 70));
        appName.setForeground(new Color(255, 255, 255));
        appName.setBounds(450, 15, 800, 82);

        JLabel countryName = new JLabel("Country Name: ");
        countryName.setFont(new Font("Georgia", Font.PLAIN, 23));
        countryName.setForeground(new Color(255, 255, 255));
        countryName.setBounds(100, 120, 180, 30);

        Map<String, String> countryCodeMap = CountryCode.getCountryCodeMap();

        countryComboBox = new JComboBox<>(countryCodeMap.keySet().toArray(new String[0]));
        countryComboBox.setFont(new Font("Georgia", Font.ITALIC, 18));
        countryComboBox.addActionListener(e -> selectedCountry = (String) countryComboBox.getSelectedItem());
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


        monthCodeMap = holidayLogic.getMonthMap();
        monthComboBox = new JComboBox<>(monthCodeMap.keySet().toArray(new String[0]));
        monthComboBox.setFont(new Font("Georgia", Font.PLAIN, 20));
        monthComboBox.setBounds(730, 120, 130, 30);
        monthComboBox.addActionListener(e -> selectedMonth = (String) monthComboBox.getSelectedItem());

        JLabel day = new JLabel("Day:");
        day.setFont(new Font("Georgia", Font.PLAIN, 23));
        day.setForeground(new Color(255, 255, 255));
        day.setBounds(870, 120, 100, 30);

        dayCodeMap = holidayLogic.getDayMap();
        dayComboBox = new JComboBox<>(dayCodeMap.keySet().toArray(new String[0]));
        dayComboBox.setFont(new Font("Georgia", Font.ITALIC, 18));
        dayComboBox.setBounds(920, 120, 120, 30);
        dayComboBox.addActionListener(e -> selectedDay = (String) dayComboBox.getSelectedItem());

        JButton showHolidays = new JButton("Show Holidays");
        showHolidays.setFont(new Font("Georgia", Font.PLAIN, 23));
        showHolidays.setBounds(1050, 120, 200, 30);
        showHolidays.addActionListener(e -> {
            try {
                showsHolidays(selectedMonth,selectedCountry,selectedDay,yearField.getText());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

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

        frame.add(appName);
        frame.add(countryName);
        frame.add(countryComboBox);
        frame.add(year);
        frame.add(yearField);
        frame.add(month);
        frame.add(monthComboBox);
        frame.add(day);
        frame.add(dayComboBox);
        frame.add(showHolidays);
        frame.add(allHoliday);
        frame.add(wishedHoliday);
        frame.add(scroll);
        frame.add(wishScroll);
    }

    private void showsHolidays(String selectedMonth, String selectedCountry, String selectedDay, String year) throws IOException {
        filteredHolidays=holidayLogic.fetchHolidays(selectedMonth,selectedCountry,selectedDay,year);
        makeTheList(filteredHolidays);
        makeTheWishList();
    }

    private void makeTheList(ArrayList<Holiday> filteredHolidays) {
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
                        Holiday wishedHoliday = new Holiday(holiday[0], holiday[1], holiday[2]);
                        databaseManager.saveHolidayToDatabase(wishedHoliday);
                        makeTheWishList();
                    }
                }
            });
        } else {
            ls.setModel(df);
        }
    }

    void makeTheWishList() {
        wishListHolidays=databaseManager.getHolidaysFromDatabase();
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
                                databaseManager.deleteHolidayFromDatabase(h);
                                break;
                            }
                        }
                    }
                    makeTheWishList();
                }
            });

        } else {
            wl.setModel(dw);
        }
    }

}