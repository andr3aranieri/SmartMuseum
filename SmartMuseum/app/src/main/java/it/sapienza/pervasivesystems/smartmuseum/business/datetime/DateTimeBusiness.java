package it.sapienza.pervasivesystems.smartmuseum.business.datetime;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by andrearanieri on 04/05/16.
 */
public class DateTimeBusiness {

    private Calendar calendar;

    public DateTimeBusiness(Date d) {
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(d);
    }

    public int getYear() {
        return this.calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return this.calendar.get(Calendar.MONTH) + 1;
    }

    public int getDayOfMonth() {
        return this.calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getHour() {
        return this.calendar.get(Calendar.HOUR);
    }

    public int getMinute() {
        return this.calendar.get(Calendar.MINUTE);
    }

    public int getSecond() {
        return this.calendar.get(Calendar.SECOND);
    }

    public long getMillis() {
        return this.calendar.getTime().getTime();
    }

    static public Date getDateFromMillis(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        return cal.getTime();
    }

    static public int howManySecondsFromDate(Date fromDate) {
        long millis = new Date().getTime() - fromDate.getTime();
        return (int) (millis / 1000);
    }
}
