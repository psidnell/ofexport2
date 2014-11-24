package org.psidnell.omnifocus.expr;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    public void testRoundToDay () throws ParseException {
        Date date1 = ExpressionFunctions.roundToDay(DATE_FORMAT.parse("2014-12-03"));
        
        Calendar cal2 = new GregorianCalendar();
        cal2.set(Calendar.YEAR, 2014);
        cal2.set(Calendar.MONTH, Calendar.DECEMBER);
        cal2.set(Calendar.DAY_OF_MONTH, 3);
        ExpressionFunctions.roundToDay(cal2);
        
        assertEquals (date1, cal2.getTime());
    }
    
    @Test
    public void testDays () {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals (cal.getTime(), new ExpressionFunctions() {}.days(-1, new ExpressionFunctions().todayCalendar()));
        
        cal = new GregorianCalendar();
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        assertEquals (cal.getTime(), new ExpressionFunctions() {}.days(1, new ExpressionFunctions().todayCalendar()));
    }
    
    @Test
    public void testDayThisWeek () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-11-17", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("mon", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-18", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("tue", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-19", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("wed", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-20", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("thu", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-21", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("fri", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-22", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("sat", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-23", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("sun", cal).getTime()));
        
        cal.setTime(date);
        assertEquals ("2014-11-17", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("monday", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-18", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("tuesday", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-19", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("wednesday", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-20", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("thursday", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-21", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("friday", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-22", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("saturday", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-23", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("sunday", cal).getTime()));
    }
    
    @Test
    public void testDayNextWeek () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-26"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-12-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+mon", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-02", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+tue", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-03", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+wed", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-04", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+thu", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-05", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+fri", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-06", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+sat", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+sun", cal).getTime()));
    }
    
    @Test
    public void testDayLastWeek () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-26"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-11-17", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-mon", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-18", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-tue", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-19", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-wed", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-20", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-thu", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-21", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-fri", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-22", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-sat", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-23", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-sun", cal).getTime()));
    }
    
    @Test
    public void testMonthThisYear () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-01-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("jan", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-02-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("feb", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-03-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("mar", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-04-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("apr", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-05-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("may", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-06-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("jun", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-07-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("jul", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-08-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("aug", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-09-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("sep", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-10-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("oct", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("nov", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("dec", cal).getTime()));
        
        cal.setTime(date);
        assertEquals ("2014-01-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("january", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-02-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("february", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-03-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("march", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-04-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("april", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-05-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("may", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-06-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("june", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-07-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("july", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-08-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("august", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-09-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("september", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-10-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("october", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("november", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("december", cal).getTime()));
    }
    
    @Test
    public void testMonthNextYear () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2015-01-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+jan", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-02-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+feb", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-03-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+mar", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-04-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+apr", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-05-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+may", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-06-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+jun", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-07-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+jul", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-08-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+aug", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-09-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+sep", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-10-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+oct", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-11-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+nov", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-12-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+dec", cal).getTime()));
    }
    
    @Test
    public void testMonthLastYear () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2013-01-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-jan", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-02-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-feb", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-03-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-mar", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-04-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-apr", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-05-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-may", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-06-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-jun", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-07-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-jul", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-08-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-aug", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-09-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-sep", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-10-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-oct", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-11-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-nov", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-12-01", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-dec", cal).getTime()));
    }
    
    @Test 
    public void testRelativeDay () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-11-20", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1d", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-20", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("1d", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-22", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("3d", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-18", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-1d", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-16", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-3d", cal).getTime()));
        
        cal.setTime(date);
        assertEquals ("2014-11-20", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1day", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-20", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1days", cal).getTime()));
    }
    
    @Test 
    public void testRelativeWeek () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-07");
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-11-14", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1w", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-14", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("1w", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-28", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("3w", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-10-31", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-1w", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-10-17", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-3w", cal).getTime()));
        
        cal.setTime(date);
        assertEquals ("2014-11-14", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1week", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-14", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1weeks", cal).getTime()));
    }
    
    @Test 
    public void testRelativeMonth () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-07");
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-12-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1m", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("1m", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-02-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("3m", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-10-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-1m", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-08-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-3m", cal).getTime()));
        
        cal.setTime(date);
        assertEquals ("2014-12-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1month", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-12-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1months", cal).getTime()));
        
        // When this months date doesn't exist next month
        date = new ExpressionFunctions().date("2014-08-31");
        
        cal.setTime(date);
        assertEquals ("2014-09-30", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1m", cal).getTime()));
        
        // When this months date doesn't exist last month
        date = new ExpressionFunctions().date("2014-04-31");
        
        cal.setTime(date);
        assertEquals ("2014-03-30", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-1m", cal).getTime()));
    }
    
    @Test 
    public void testRelativeYear () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-07");
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2015-11-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1y", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-11-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("1y", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2017-11-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("3y", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2013-11-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-1y", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2011-11-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-3y", cal).getTime()));
        
        cal.setTime(date);
        assertEquals ("2015-11-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1year", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2015-11-07", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1years", cal).getTime()));
        
        // When this months date doesn't exist next year (leap year: 2016)
        date = new ExpressionFunctions().date("2015-02-31");
        
        cal.setTime(date);
        assertEquals ("2016-03-03", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("+1y", cal).getTime()));
        
        // When this months date doesn't exist last month
        date = new ExpressionFunctions().date("2017-02-31");
        
        cal.setTime(date);
        assertEquals ("2016-03-03", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("-1y", cal).getTime()));
    }
    
    @Test
    public void testDayOfMonth () throws ParseException {
      
        Date date = new ExpressionFunctions().date("2014-11-07");
        Calendar cal = new GregorianCalendar();
        
        cal.setTime(date);
        assertEquals ("2014-11-21", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("21st", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-22", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("22nd", cal).getTime()));
        cal.setTime(date);
        assertEquals ("2014-11-13", ExpressionFunctions.YYYYMMDD.format(new ExpressionFunctions().date ("13th", cal).getTime()));
    }
    
    @Test
    public void testWithin () throws ParseException {
        Date date = new ExpressionFunctions().date("2014-11-07");
        
        assertTrue (new ExpressionFunctions().within(date, "2014-11-05", "2014-11-09"));
        assertTrue (new ExpressionFunctions().within(date, "2014-11-06", "2014-11-08"));
        assertTrue (new ExpressionFunctions().within(date, "2014-11-07", "2014-11-07"));
        assertFalse (new ExpressionFunctions().within(date, "2014-11-08", "2014-11-09"));
        assertFalse (new ExpressionFunctions().within(date, "2014-11-05", "2014-11-06"));
    }
}
