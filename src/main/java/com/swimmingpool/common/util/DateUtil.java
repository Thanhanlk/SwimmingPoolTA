package com.swimmingpool.common.util;

import lombok.experimental.UtilityClass;

import java.sql.Time;
import java.util.Calendar;

@UtilityClass
public class DateUtil {

    public Time strToTime(String text) {
        try {
            String[] split = text.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(split[0]));
            calendar.set(Calendar.MINUTE, Integer.valueOf(split[1]));
            calendar.set(Calendar.SECOND, 0);
            return new Time(calendar.getTimeInMillis());
        } catch (Exception ex) { return null; }
    }
}
