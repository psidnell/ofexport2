package org.psidnell.omnifocus.expr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

public class ExpressionFunctions {

    protected static final SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");

    protected static final long DAY = 1000 * 60 * 60 * 24;

    
    public Date date(String dateStr) throws ParseException {
        dateStr = dateStr.trim().toLowerCase();
        int direction = 1;
        int offset = 0;
        if (dateStr.startsWith("this")) {
            direction = 1;
            offset = 0;
            dateStr.replaceFirst("this", "").trim();
        }
        else if (dateStr.startsWith("+")) {
            direction = 1;
            offset = 1;
            dateStr.replaceFirst("+", "").trim();
        }
        else if (dateStr.startsWith("-")) {
            direction = -1;
            offset = 1;
            dateStr.replaceFirst("-", "").trim();
        }
        
        switch (dateStr) {
            case ("mon"):
            case ("monday"):
                return day(Calendar.MONDAY, direction, offset);
            case ("tue"):
            case ("tuesday"):
                return day(Calendar.TUESDAY, direction, offset);
            case ("wed"):
            case ("wednesday"):
                return day(Calendar.WEDNESDAY, direction, offset);
            case ("thu"):
            case ("thursday"):
                return day(Calendar.THURSDAY, direction, offset);
            case ("fri"):
            case ("friday"):
                return day(Calendar.FRIDAY, direction, offset);
            case ("sat"):
            case ("saturday"):
                return day(Calendar.SATURDAY, direction, offset);
            case ("sun"):
            case ("sunday"):
                return day(Calendar.SUNDAY, direction, offset);
        }

        return YYYYMMDD.parse(dateStr);
    }

    protected Date day(int target, int direction, int offset) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(today());
        return day(target, direction, offset, cal);
    }

    protected Date day(int target, int direction, int unitOffset, Calendar cal) {
        int day = adjustStartOfWeek(cal.get(Calendar.DAY_OF_WEEK));
        int diff = adjustStartOfWeek(target) - day;
        int off = direction * unitOffset * 7 + diff;
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)  + off);
        return cal.getTime();
    }

    private int adjustStartOfWeek(int dow) {
        // DOW starts on Sunday but want Monday
        if (dow == Calendar.SUNDAY) {
            return 7;
        }
        else {
            return dow - Calendar.SUNDAY;
        }
    }

    public Date today() {
        return new java.util.Date(DAY * (System.currentTimeMillis() / DAY));
    }

    public Date days(int days) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(today());
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + days);
        return cal.getTime();
    }

    public Date yesterday() {
        return days(-1);
    }

    public Date tomorrow() {
        return days(1);
    }
}
