package org.example;

public  class Holiday {
    private String HolidayName;
    private String HolidayDay;
    private String HolidayDate;

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
