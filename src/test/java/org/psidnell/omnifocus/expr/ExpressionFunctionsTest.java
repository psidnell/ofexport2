package org.psidnell.omnifocus.expr;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

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

public class ExpressionFunctionsTest {
    
    @Test
    public void testToday () {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals (cal.getTime(), new ExpressionFunctions() {}.today());
    }
    
    @Test
    public void testDays () {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals (cal.getTime(), new ExpressionFunctions() {}.days(-1));
        
        cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals (cal.getTime(), new ExpressionFunctions() {}.days(1));
    }
    
    @Test
    public void testThisWeek () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-11-17", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.MONDAY, 1, 0, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-18", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.TUESDAY, 1, 0, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-19", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.WEDNESDAY, 1, 0, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-20", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.THURSDAY, 1, 0, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-21", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.FRIDAY, 1, 0, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-22", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.SATURDAY, 1, 0, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-23", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.SUNDAY, 1, 0, cal).getTime()));
    }
    
    @Test
    public void testNextWeek () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-26"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-12-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.MONDAY, 1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-02", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.TUESDAY, 1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-03", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.WEDNESDAY, 1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-04", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.THURSDAY, 1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-05", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.FRIDAY, 1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-06", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.SATURDAY, 1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.SUNDAY, 1, 1, cal).getTime()));
    }
    
    @Test
    public void testLastWeek () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-26"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-11-17", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.MONDAY, -1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-18", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.TUESDAY, -1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-19", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.WEDNESDAY, -1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-20", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.THURSDAY, -1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-21", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.FRIDAY, -1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-22", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.SATURDAY, -1, 1, cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-23", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().day (Calendar.SUNDAY, -1, 1, cal).getTime()));
    }
}
