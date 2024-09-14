package org.example.logic;

import org.example.model.Holiday;
import org.example.utils.AllHolidays;
import org.example.utils.CountryCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class HolidayLogic {
    public Map<String, Integer> getMonthMap() {
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

    public Map<String, String> getDayMap() {
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

    public ArrayList<Holiday> fetchHolidays(String selectedMonth, String selectedCountry, String selectedDay, String year) throws IOException {
        String apiUrl;
        ArrayList<Holiday> filteredHolidays;
        ArrayList<Holiday> holidays;
        if (!Objects.equals(getMonthMap().get(selectedMonth), null)) {
            apiUrl = "https://calendarific.com/api/v2/holidays?&api_key=2r6BTSv9nICNsYfObPwYeSRjiuj5V0Z0&country=" + CountryCode.getCountryCodeMap().get(selectedCountry) + "&year=" + year + "&month=" + getMonthMap().get(selectedMonth);
            System.out.println("With Month");
        } else {
            apiUrl = "https://calendarific.com/api/v2/holidays?&api_key=2r6BTSv9nICNsYfObPwYeSRjiuj5V0Z0&country=" + CountryCode.getCountryCodeMap().get(selectedCountry) + "&year=" + year;
            System.out.println("Without Month");
        }
        AllHolidays allHolidays=new AllHolidays(apiUrl);
        holidays=(ArrayList<Holiday>) allHolidays.fetchAllHolidays();
        String realSelectedDay=getDayMap().get(selectedDay);
        if(!Objects.equals(realSelectedDay,null)){
            filteredHolidays=filterHolidaysByDay(holidays,realSelectedDay);
        }
        else{
            filteredHolidays=holidays;
        }
        return filteredHolidays;
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
