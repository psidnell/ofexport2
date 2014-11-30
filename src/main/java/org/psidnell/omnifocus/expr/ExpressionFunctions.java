/*
 Copyright 2014 Paul Sidnell

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.psidnell.omnifocus.expr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author psidnell
 *
 *         A utility base class that provides methods that simplify using OGNL expressions in
 *         command line options, for example creating Date objects from strings to allow filters to
 *         be expressed more easily.
 */
public class ExpressionFunctions {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionFunctions.class);

    private static final int MONTHS_IN_YEAR = 12;

    private static final int DAYS_IN_WEEK = 7;

    public static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    protected static final long DAY = 1000 * 60 * 60 * 24;

    public Date date(String dateStr) throws ParseException {
        Calendar today = new GregorianCalendar();
        return date(dateStr, today);
    }

    public Date date(String dateStr, Calendar today) throws ParseException {
        LOGGER.debug("date({})", dateStr);
        roundToDay(today);
        dateStr = dateStr.trim().toLowerCase();

        Date result = parseDateByName(dateStr, today);

        if (dateStr.equals("today")) {
            result = days(0, today);
        }

        if (dateStr.equals("yesterday")) {
            result = days(-1, today);
        }

        if (dateStr.equals("tomorrow")) {
            result = days(1, today);
        }

        if (result == null) {
            result = parseDateByByDayOfMonth(dateStr, today);
        }

        if (result == null) {
            result = parseDateByUnit(dateStr, today);
        }

        if (result == null) {
            result = roundToDay(YYYYMMDD.parse(dateStr));
        }

        LOGGER.debug("date({}) = {}", dateStr, result);
        return result;
    }

    public boolean within(Date date, String lower, String upper) throws ParseException {
        LOGGER.debug("within({},{},{})", date, lower, upper);
        Date lowerDate = date(lower);
        Date upperDate = date(upper);
        boolean result = date != null && date.getTime() >= lowerDate.getTime() && date.getTime() <= upperDate.getTime();
        LOGGER.debug("within({},{},{})={}", date, lower, upper, result);
        return result;
    }

    private Date parseDateByByDayOfMonth(String dateStr, Calendar today) {

        StringBuilder numStr = new StringBuilder();
        int i = 0;
        for (i = 0; i < dateStr.length() && Character.isDigit(dateStr.charAt(i)); i++) {
            numStr.append(dateStr.charAt(i));
        }
        if (i == 0) {
            return null;
        }
        int num = Integer.parseInt(numStr.toString());
        dateStr = dateStr.substring(i);

        switch (dateStr) {
            case "st":
            case "th":
            case "nd":
                roundToDay(today);
                today.set(Calendar.DAY_OF_MONTH, num);
                return today.getTime();
            default:
                break;
        }
        return null;
    }

    private Date parseDateByUnit(String dateStr, Calendar today) {
        int direction = 1;
        if (dateStr.startsWith("+")) {
            dateStr = dateStr.replaceFirst("\\+", "").trim();
        } else if (dateStr.startsWith("-")) {
            direction = -1;
            dateStr = dateStr.replaceFirst("-", "").trim();
        }

        StringBuilder numStr = new StringBuilder();
        int i = 0;
        for (i = 0; i < dateStr.length() && Character.isDigit(dateStr.charAt(i)); i++) {
            numStr.append(dateStr.charAt(i));
        }
        if (i == 0) {
            return null;
        }
        int num = Integer.parseInt(numStr.toString());
        dateStr = dateStr.substring(i);

        switch (dateStr) {
            case "d":
            case "day":
            case "days":
                return days(direction * num, today);
            case "w":
            case "week":
            case "weeks":
                return days(direction * DAYS_IN_WEEK * num, today);
            case "m":
            case "month":
            case "months":
                roundToDay(today);
                today.set(Calendar.MONTH, today.get(Calendar.MONTH) + direction * num);
                return today.getTime();
            case "y":
            case "year":
            case "years":
                roundToDay(today);
                today.set(Calendar.YEAR, today.get(Calendar.YEAR) + direction * num);
                return today.getTime();
            default:
                break;
        }
        return null;
    }

    private Date parseDateByName(String dateStr, Calendar today) {
        int direction = 1;
        int offset = 0;
        if (dateStr.startsWith("this")) {
            direction = 1;
            offset = 0;
            dateStr = dateStr.replaceFirst("this", "").trim();
        } else if (dateStr.startsWith("+")) {
            direction = 1;
            offset = 1;
            dateStr = dateStr.replaceFirst("\\+", "").trim();
        } else if (dateStr.startsWith("-")) {
            direction = -1;
            offset = 1;
            dateStr = dateStr.replaceFirst("-", "").trim();
        }

        switch (dateStr) {
            case "mon":
            case "monday":
                return findDay(Calendar.MONDAY, direction, offset, today);
            case "tue":
            case "tuesday":
                return findDay(Calendar.TUESDAY, direction, offset, today);
            case "wed":
            case "wednesday":
                return findDay(Calendar.WEDNESDAY, direction, offset, today);
            case "thu":
            case "thursday":
                return findDay(Calendar.THURSDAY, direction, offset, today);
            case "fri":
            case "friday":
                return findDay(Calendar.FRIDAY, direction, offset, today);
            case "sat":
            case "saturday":
                return findDay(Calendar.SATURDAY, direction, offset, today);
            case "sun":
            case "sunday":
                return findDay(Calendar.SUNDAY, direction, offset, today);
            case "jan":
            case "january":
                return findMonth(Calendar.JANUARY, direction, offset, today);
            case "feb":
            case "february":
                return findMonth(Calendar.FEBRUARY, direction, offset, today);
            case "mar":
            case "march":
                return findMonth(Calendar.MARCH, direction, offset, today);
            case "apr":
            case "april":
                return findMonth(Calendar.APRIL, direction, offset, today);
            case "may":
                return findMonth(Calendar.MAY, direction, offset, today);
            case "jun":
            case "june":
                return findMonth(Calendar.JUNE, direction, offset, today);
            case "jul":
            case "july":
                return findMonth(Calendar.JULY, direction, offset, today);
            case "aug":
            case "august":
                return findMonth(Calendar.AUGUST, direction, offset, today);
            case "sep":
            case "september":
                return findMonth(Calendar.SEPTEMBER, direction, offset, today);
            case "oct":
            case "october":
                return findMonth(Calendar.OCTOBER, direction, offset, today);
            case "nov":
            case "november":
                return findMonth(Calendar.NOVEMBER, direction, offset, today);
            case "dec":
            case "december":
                return findMonth(Calendar.DECEMBER, direction, offset, today);
            default:
                break;
        }
        return null;
    }

    protected Date findDay(int target, int direction, int unitOffset, Calendar cal) {
        int day = adjustStartOfWeek(cal.get(Calendar.DAY_OF_WEEK));
        int diff = adjustStartOfWeek(target) - day;
        int off = direction * unitOffset * DAYS_IN_WEEK + diff;
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + off);
        return cal.getTime();
    }

    protected Date findMonth(int target, int direction, int unitOffset, Calendar cal) {
        int month = cal.get(Calendar.MONTH);
        int diff = target - month;
        int off = direction * unitOffset * MONTHS_IN_YEAR + diff;
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + off);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        roundToDay(cal);
        return cal.getTime();
    }

    private int adjustStartOfWeek(int dow) {
        // DOW starts on Sunday but want Monday
        if (dow == Calendar.SUNDAY) {
            return DAYS_IN_WEEK;
        } else {
            return dow - Calendar.SUNDAY;
        }
    }

    public Date days(int days, Calendar today) {
        today.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH) + days);
        return today.getTime();
    }

    public static void roundToDay(Calendar today) {
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
    }

    public static Date roundToDay(Date d) {
        return d == null ? null : new Date(DAY * (d.getTime() / DAY));
    }
}
