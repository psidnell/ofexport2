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
package org.psidnell.omnifocus;

/**
 * @author psidnell
 *
 *         General Shared Configuration Parameters
 *
 */
public class ConfigParams {

    private String dueSoon;
    private String flattenedRootName;
    private String expressionDateFormat;
    private int icsAlarmMinutes;
    private boolean icsAlarm;
    private String icsStart;
    private String icsEnd;
    private boolean icsAllDay;

    public void setDueSoon(String dueSoon) {
        this.dueSoon = dueSoon;
    }

    public String getDueSoon() {
        return dueSoon;
    }

    public void setFlattenedRootName(String name) {
        this.flattenedRootName = name;
    }

    public String getFlattenedRootName() {
        return flattenedRootName;
    }

    public void setExpressionDateFormat(String format) {
        this.expressionDateFormat = format;
    }

    public String getExpressionDateFormat() {
        return expressionDateFormat;
    }

    public int getIcsAlarmMinutes() {
        return icsAlarmMinutes;
    }

    public void setIcsAlarmMinutes(int icsAlarmMinutes) {
        this.icsAlarmMinutes = icsAlarmMinutes;
    }

    public boolean getIcsAlarm() {
        return icsAlarm;
    }

    public void setIcsAlarm(boolean icsAlarm) {
        this.icsAlarm = icsAlarm;
    }

    public String getIcsStart() {
        return icsStart;
    }

    public void setIcsStart(String icsStart) {
        this.icsStart = icsStart;
    }

    public String getIcsEnd() {
        return icsEnd;
    }

    public void setIcsEnd(String icsEnd) {
        this.icsEnd = icsEnd;
    }

    public boolean getIcsAllDay() {
        return icsAllDay;
    }

    public void setIcsAllDay(boolean icsAllDay) {
        this.icsAllDay = icsAllDay;
    }

}
