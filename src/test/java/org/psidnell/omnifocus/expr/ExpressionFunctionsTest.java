/*
 * Copyright 2015 Paul Sidnell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.psidnell.omnifocus.expr;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.psidnell.omnifocus.ApplicationContextFactory;
import org.psidnell.omnifocus.ConfigParams;
import org.springframework.context.ApplicationContext;

public class ExpressionFunctionsTest {


    private SimpleDateFormat dateFormat;
    private ConfigParams config;
    private ExpressionFunctions fn;

    @Before
    public void setUp () {
        ApplicationContext appContext = ApplicationContextFactory.getContext();
        config = appContext.getBean("configparams", ConfigParams.class);
        dateFormat = new SimpleDateFormat(config.getExpressionDateFormat());
        fn = new ExpressionFunctions();
        fn.setConfigParams(config);
    }

    @Test
    public void testDateRoundToDay () throws ParseException {
        Date date1 = ExpressionFunctions.roundToDay(new Date());

        Calendar cal2 = new GregorianCalendar();

        assertNotEquals(date1, cal2.getTime());

        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        assertEquals(date1, cal2.getTime());
    }

    @Test
    public void testCalendarRoundToDay () throws ParseException {
        Date date1 = ExpressionFunctions.roundToDay(new Date());

        Calendar cal2 = new GregorianCalendar();

        assertNotEquals(date1, cal2.getTime());

        ExpressionFunctions.roundToDay(cal2);

        assertEquals(date1, cal2.getTime());
    }

    @Test
    public void testDays () {

        Calendar today = new GregorianCalendar();
        ExpressionFunctions.roundToDay(today);

        Calendar yesterday = new GregorianCalendar();
        ExpressionFunctions.roundToDay(yesterday);
        yesterday.set(Calendar.DAY_OF_MONTH, yesterday.get(Calendar.DAY_OF_MONTH) - 1);

        Calendar tomorrow = new GregorianCalendar();
        ExpressionFunctions.roundToDay(tomorrow);
        tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) + 1);

        assertEquals (today.getTime(), fn.days(0, clone(today)));
        assertEquals (yesterday.getTime(), fn.days(-1, clone(today)));
        assertEquals (tomorrow.getTime(), fn.days(1, clone(today)));

    }

    @Test
    public void testDayThisWeek () throws ParseException {

        Date date = fn.date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2014-11-17", date("mon", cal));
        assertEquals ("2014-11-18", date("tue", cal));
        assertEquals ("2014-11-19", date("wed", cal));
        assertEquals ("2014-11-20", date("thu", cal));
        assertEquals ("2014-11-21", date("fri", cal));
        assertEquals ("2014-11-22", date("sat", cal));
        assertEquals ("2014-11-23", date("sun", cal));

        assertEquals ("2014-11-17", date("monday", cal));
        assertEquals ("2014-11-18", date("tuesday", cal));
        assertEquals ("2014-11-19", date("wednesday", cal));
        assertEquals ("2014-11-20", date("thursday", cal));
        assertEquals ("2014-11-21", date("friday", cal));
        assertEquals ("2014-11-22", date("saturday", cal));
        assertEquals ("2014-11-23", date("sunday", cal));
    }

    @Test
    public void testDayNextWeek () throws ParseException {

        Date date = fn.date("2014-11-26"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2014-12-01", date("+mon", cal));
        assertEquals ("2014-12-02", date("+tue", cal));
        assertEquals ("2014-12-03", date("+wed", cal));
        assertEquals ("2014-12-04", date("+thu", cal));
        assertEquals ("2014-12-05", date("+fri", cal));
        assertEquals ("2014-12-06", date("+sat", cal));
        assertEquals ("2014-12-07", date("+sun", cal));
    }

    @Test
    public void testDayLastWeek () throws ParseException {

        Date date = fn.date("2014-11-26"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2014-11-17", date("-mon", cal));
        assertEquals ("2014-11-18", date("-tue", cal));
        assertEquals ("2014-11-19", date("-wed", cal));
        assertEquals ("2014-11-20", date("-thu", cal));
        assertEquals ("2014-11-21", date("-fri", cal));
        assertEquals ("2014-11-22", date("-sat", cal));
        assertEquals ("2014-11-23", date("-sun", cal));
    }

    @Test
    public void testMonthThisYear () throws ParseException {

        Date date = fn.date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2014-01-01", date("jan", cal));
        assertEquals ("2014-02-01", date("feb", cal));
        assertEquals ("2014-03-01", date("mar", cal));
        assertEquals ("2014-04-01", date("apr", cal));
        assertEquals ("2014-05-01", date("may", cal));
        assertEquals ("2014-06-01", date("jun", cal));
        assertEquals ("2014-07-01", date("jul", cal));
        assertEquals ("2014-08-01", date("aug", cal));
        assertEquals ("2014-09-01", date("sep", cal));
        assertEquals ("2014-10-01", date("oct", cal));
        assertEquals ("2014-11-01", date("nov", cal));
        assertEquals ("2014-12-01", date("dec", cal));

        assertEquals ("2014-01-01", date("january", cal));
        assertEquals ("2014-02-01", date("february", cal));
        assertEquals ("2014-03-01", date("march", cal));
        assertEquals ("2014-04-01", date("april", cal));
        assertEquals ("2014-05-01", date("may", cal));
        assertEquals ("2014-06-01", date("june", cal));
        assertEquals ("2014-07-01", date("july", cal));
        assertEquals ("2014-08-01", date("august", cal));
        assertEquals ("2014-09-01", date("september", cal));
        assertEquals ("2014-10-01", date("october", cal));
        assertEquals ("2014-11-01", date("november", cal));
        assertEquals ("2014-12-01", date("december", cal));
    }

    @Test
    public void testMonthNextYear () throws ParseException {

        Date date = fn.date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2015-01-01", date("+jan", cal));
        assertEquals ("2015-02-01", date("+feb", cal));
        assertEquals ("2015-03-01", date("+mar", cal));
        assertEquals ("2015-04-01", date("+apr", cal));
        assertEquals ("2015-05-01", date("+may", cal));
        assertEquals ("2015-06-01", date("+jun", cal));
        assertEquals ("2015-07-01", date("+jul", cal));
        assertEquals ("2015-08-01", date("+aug", cal));
        assertEquals ("2015-09-01", date("+sep", cal));
        assertEquals ("2015-10-01", date("+oct", cal));
        assertEquals ("2015-11-01", date("+nov", cal));
        assertEquals ("2015-12-01", date("+dec", cal));
    }

    @Test
    public void testMonthLastYear () throws ParseException {

        Date date = fn.date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2013-01-01", date("-jan", cal));
        assertEquals ("2013-02-01", date("-feb", cal));
        assertEquals ("2013-03-01", date("-mar", cal));
        assertEquals ("2013-04-01", date("-apr", cal));
        assertEquals ("2013-05-01", date("-may", cal));
        assertEquals ("2013-06-01", date("-jun", cal));
        assertEquals ("2013-07-01", date("-jul", cal));
        assertEquals ("2013-08-01", date("-aug", cal));
        assertEquals ("2013-09-01", date("-sep", cal));
        assertEquals ("2013-10-01", date("-oct", cal));
        assertEquals ("2013-11-01", date("-nov", cal));
        assertEquals ("2013-12-01", date("-dec", cal));
    }

    @Test
    public void testRelativeDay () throws ParseException {

        Date date = fn.date("2014-11-19"); // A Wednesday
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2014-11-20", date("+1d", cal));
        assertEquals ("2014-11-20", date("1d", cal));
        assertEquals ("2014-11-22", date("3d", cal));
        assertEquals ("2014-11-18", date("-1d", cal));
        assertEquals ("2014-11-16", date("-3d", cal));

        assertEquals ("2014-11-20", date("+1day", cal));
        assertEquals ("2014-11-20", date("+1days", cal));
    }

    @Test
    public void testRelativeWeek () throws ParseException {

        Date date = fn.date("2014-11-07");
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2014-11-14", date("+1w", cal));
        assertEquals ("2014-11-14", date("1w", cal));
        assertEquals ("2014-11-28", date("3w", cal));
        assertEquals ("2014-10-31", date("-1w", cal));
        assertEquals ("2014-10-17", date("-3w", cal));

        assertEquals ("2014-11-14", date("+1week", cal));
        assertEquals ("2014-11-14", date("+1weeks", cal));
    }

    @Test
    public void testRelativeMonth () throws ParseException {
        Date date = fn.date("2014-11-07");
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2014-12-07", date("+1m", cal));
        assertEquals ("2014-12-07", date("1m", cal));
        assertEquals ("2015-02-07", date("3m", cal));
        assertEquals ("2014-10-07", date("-1m", cal));
        assertEquals ("2014-08-07", date("-3m", cal));

        assertEquals ("2014-12-07", date("+1month", cal));
        assertEquals ("2014-12-07", date("+1months", cal));

        // When this months date doesn't exist next month
        date = fn.date("2014-08-31");
        cal.setTime(date);
        assertEquals ("2014-10-01", date("+1m", cal));

        // When this months date doesn't exist last month
        date = fn.date("2014-04-31");
        cal.setTime(date);
        assertEquals ("2014-04-01", date("-1m", cal));
    }

    @Test
    public void testRelativeYear () throws ParseException {

        Date date = fn.date("2014-11-07");
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2015-11-07", date("+1y", cal));
        assertEquals ("2015-11-07", date("1y", cal));
        assertEquals ("2017-11-07", date("3y", cal));
        assertEquals ("2013-11-07", date("-1y", cal));
        assertEquals ("2011-11-07", date("-3y", cal));

        assertEquals ("2015-11-07", date("+1year", cal));
        assertEquals ("2015-11-07", date("+1years", cal));

        // When this months date doesn't exist next year (leap year: 2016)
        date = fn.date("2015-02-31");
        cal.setTime(date);
        assertEquals ("2016-03-03", date("+1y", cal));

        // When this months date doesn't exist last month
        date = fn.date("2017-02-31");
        cal.setTime(date);
        assertEquals ("2016-03-03", date("-1y", cal));
    }

    @Test
    public void testDayOfMonth () throws ParseException {

        Date date = fn.date("2014-11-07");
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);

        assertEquals ("2014-11-21", date("21st", cal));
        assertEquals ("2014-11-22", date("22nd", cal));
        assertEquals ("2014-11-13", date("13th", cal));
    }

    @Test
    public void testWithin () throws ParseException {
        Date date = fn.date("2014-11-07");

        assertTrue (fn.within(date, "2014-11-05", "2014-11-09"));
        assertTrue (fn.within(date, "2014-11-06", "2014-11-08"));
        assertTrue (fn.within(date, "2014-11-07", "2014-11-07"));
        assertFalse (fn.within(date, "2014-11-08", "2014-11-09"));
        assertFalse (fn.within(date, "2014-11-05", "2014-11-06"));

        assertTrue (fn.within(new Date(), "-1y", "7d"));
    }

    private Calendar clone(Calendar cal) {
        return (Calendar) cal.clone();
    }

    private String date(String x, Calendar cal) throws ParseException {
        Calendar calCopy = clone(cal);
        Date date = fn.date (x, calCopy);
        return dateFormat.format (date);
    }
}
