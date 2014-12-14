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

import org.psidnell.omnifocus.ConfigParams;

/**
 * @author psidnell
 *
 *         A wrapper for dates that makes the OGNL expressions more convenient.
 */
public class Date extends ExpressionFunctions implements Comparable<Date> {
    // TODO Merge with Expression Functions

    private java.util.Date date;
    private java.util.Date roundedDate;

    public Date(java.util.Date date, ConfigParams config) {
        this.date = date;
        this.roundedDate = roundToDay(date);
        setConfigParams(config);
    }

    public boolean isSet() {
        return date != null;
    }

    public boolean is(String dateStr) throws ParseException {
        return date(dateStr).equals(roundedDate);
    }

    public boolean isSoon() throws ParseException {
        return roundedDate != null && roundedDate.getTime() < date(config.getDueSoon()).getTime();
    }

    public boolean after(String dateStr) throws ParseException {
        return roundedDate != null && roundedDate.getTime() > date(dateStr).getTime();
    }

    public boolean onOrAfter(String dateStr) throws ParseException {
        return roundedDate != null && roundedDate.getTime() >= date(dateStr).getTime();
    }

    public boolean before(String dateStr) throws ParseException {
        return roundedDate != null && roundedDate.getTime() < date(dateStr).getTime();
    }

    public boolean onOrBefore(String dateStr) throws ParseException {
        return roundedDate != null && roundedDate.getTime() <= date(dateStr).getTime();
    }

    public boolean between(String fromStr, String toStr) throws ParseException {
        java.util.Date from = date(fromStr);
        java.util.Date to = date(toStr);
        return roundedDate != null && roundedDate.getTime() >= from.getTime() && roundedDate.getTime() <= to.getTime();
    }

    @Override
    public String toString() {
        return "" + date;
    }

    public java.util.Date getDate() {
        return date;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Date other = (Date) obj;
        if (date == null) {
            if (other.date != null) {
                return false;
            }
        } else if (!date.equals(other.date)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(Date o) {
        // We should never encounter a null wrapper - that's the point
        if (date == null && o.date == null) {
            return 0;
        }
        if (date == null && o.date != null) {
            return 1;
        }
        if (date != null && o.date == null) {
            return -1;
        }
        return date.compareTo(o.date);
    }
}
