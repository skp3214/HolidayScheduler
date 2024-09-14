package org.example.model;

public  class Holiday {
    private final String HolidayName;
    private final String HolidayDay;
    private final String HolidayDate;

    public Holiday(String holidayName, String holidayDay, String holidayDate) {
        HolidayName = holidayName;
        HolidayDay = holidayDay;
        HolidayDate = holidayDate;
    }

    public String getHolidayName() {
        return HolidayName;
    }

    public String getHolidayDay() {
        return HolidayDay;
    }

    public String getHolidayDate() {
        return HolidayDate;
    }
}
