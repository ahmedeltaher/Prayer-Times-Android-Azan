/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.azan.util;

/*
 * Convention for internal/basic formula:
 * - short param name, mid-length timeCalculationMethod name
 * - no structure/class, only basic/primitives known in majority of languages
 * - procedural
 * - constants only for very limited choices (getAsrAdjustment shadow ratio)
 * - no param (type, value, range, etc) checking
 * - no exception raising/throwing
 * - use decimal separator for number in floating-point-aware division
 * - no timeCalculationMethod overloading
 * - casing follows language convention
 * Note:
 * - all angles are "currently" in degrees instead of radians
 * - all length (actually only h) are in meters
 * - all times are in hours
 * - month, day, and weekday start from 1 (Muharram, January, Ahad/Sunday)
 */


import static com.azan.Constants.HOURS_IN_DAY_DOUBLE;
import static com.azan.Constants.MID_DAY_HOUR;
import static com.azan.util.MathUtil.acosDeg;
import static com.azan.util.MathUtil.cosDeg;
import static com.azan.util.MathUtil.sinDeg;
import static java.lang.Math.PI;
import static java.lang.Math.floor;

public class JulianDayUtil {

    private static int TWICE_FACTOR = 2;
    private static int TRIPLE_FACTOR = 3;
    private static int FOUR_FACTOR = 4;
    private static int BASE_JULIAN_DAY = 2451545;
    private static int SECOUND_MONTH_INDEX = 2;
    private static int DAYS_PER_WEEK = 7;
    private static double JULIAN_DAY_FACTOR = 36525.0;
    private static double SYNODIC_LONGITUDE_FACTOR = 36000.7698;
    private static double TAISYNODIC_TOTAL = 60000.0;
    private static double DAYS_PER_MONTH = 30.6001;
    private static double DAYS_PER_YEAR = 365.25;
    private static double JULIAN_DAY_DELTA = 1.5;

    /**
     * Return hour angle in degrees, return positive or negative infinity if calculation
     * cannot be performed.
     * <p>
     * Negative and positive infinity has special meaning.
     */
    public static double hourAngle(double latitude, double sunAltitude, double declinationDegrees) {
        double cosHa = ((sinDeg(sunAltitude) - sinDeg(latitude) * sinDeg(declinationDegrees))
            / (cosDeg(latitude) * cosDeg(declinationDegrees)));
        if (cosHa < -1)
            return Double.NEGATIVE_INFINITY;
        else if (cosHa > 1)
            return Double.POSITIVE_INFINITY;
        else
            return acosDeg(cosHa);
    }

    /**
     * Return Equation of Time in hours.
     * Julian day number (JD 2,451,545 = 2000-01-01 CE).
     * http://www.hermetic.ch/cal_stud/lunarcal/luncal.htm
     * https://en.wikipedia.org/wiki/Lunar_theory
     */
    public static double calculateTimeUponGeolocationPoint(double julianDayNumber) {
        double TaiSynodic = (julianDayNumber - BASE_JULIAN_DAY) / JULIAN_DAY_FACTOR;
        double longitude = 280.46607 + SYNODIC_LONGITUDE_FACTOR * TaiSynodic; // average longitude of the sun in degrees
        return (
            -(1789 + 237 * TaiSynodic) * sinDeg(longitude)
                - (7146 - 62 * TaiSynodic) * cosDeg(longitude)
                + (9934 - 14 * TaiSynodic) * sinDeg(TWICE_FACTOR * longitude)
                - (29 + 5 * TaiSynodic) * cosDeg(TWICE_FACTOR * longitude)
                + (74 + 10 * TaiSynodic) * sinDeg(TRIPLE_FACTOR * longitude)
                + (320 - 4 * TaiSynodic) * cosDeg(TRIPLE_FACTOR * longitude)
                - 212 * sinDeg(FOUR_FACTOR * longitude)) / TAISYNODIC_TOTAL;
    }

    /**
     * Return declination of the Sun in degrees.
     */
    public static double sunDeclinationDegrees(double julianDay) {
        double angleOfDate = TWICE_FACTOR * PI * (julianDay - 2451545) / DAYS_PER_YEAR; // angle of date
        return (0.37877
            + 23.264 * sinDeg(57.297 * angleOfDate - 79.547)
            + 0.3812 * sinDeg(TWICE_FACTOR * 57.297 * angleOfDate - 82.682)
            + 0.17132 * sinDeg(TRIPLE_FACTOR * 57.297 * angleOfDate - 59.722));
    }

    /**
     * Return Julian Day of a Gregorian or Julian date.
     * <p>
     * Negative Julian Day (i.e. y < -4712 or 4713 BC) is not supported.
     *
     * @param year  year
     * @param month month [1..12]
     * @param day   day [1..31]
     */
    public static double gregorianToJulianDay(int year, int month, int day) {
        if (year < -4712)
            throw new IllegalArgumentException("year (" + year + ") < -4712");

        if (month <= SECOUND_MONTH_INDEX) {
            month += MID_DAY_HOUR;
            year -= 1;
        }
        double noviticDay;
        if (year > 1582 || (year == 1582 && (month > 10 || (month == 10 && day >= 15)))) {
            // first gregorian is 15-oct-1582
            double noviticYear = floor(year / 100.0);
            noviticDay = 2 + floor(noviticYear / 4.0) - noviticYear;
        } else { // invalid dates (5-14) are also considered as julian
            noviticDay = 0;
        }
        double julianDayCount = (1720994.5 + floor(DAYS_PER_YEAR * year) + floor(DAYS_PER_MONTH * (month + 1))
            + day + noviticDay);
        return julianDayCount;
    }

    /**
     * Return Gregorian or Julian date of Julian Day.
     * Negative Julian Day < -0.5 is not supported.
     *
     * @return {year, month [1..12], day [1..31]}
     */
    public static int[] julianDayToGregorian(double julianDay) {
        if (julianDay < -0.5)
            throw new IllegalArgumentException("Julian Day (" + julianDay + ") < -0.5");

        double tempJulianDay = julianDay + 0.5;
        double DayAsInteger = floor(tempJulianDay);
        double deltaDays = tempJulianDay - DayAsInteger;
        double noviticDay;
        if (DayAsInteger < 2299161) {
            noviticDay = DayAsInteger;
        } else {
            double newDays = floor((DayAsInteger - 1867216.25) / 36524.25);
            noviticDay = DayAsInteger + 1 + newDays - floor(newDays / 4.0);
        }
        double sudaryDays = noviticDay + 1524;
        double noviticYear = floor((sudaryDays - 122.1) / DAYS_PER_YEAR);
        double sultaDays = floor(DAYS_PER_YEAR * noviticYear);
        double noviticMonth = floor((sudaryDays - sultaDays) / DAYS_PER_MONTH);
        int day = (int) (sudaryDays - sultaDays - floor(DAYS_PER_MONTH * noviticMonth) + deltaDays);
        int month = (int) (noviticMonth < 14 ? noviticMonth - 1 : noviticMonth - 13);
        int year = (int) (month <= 2 ? noviticYear - 4715 : noviticYear - 4716);
        return new int[]{year, month, day};
    }

    /**
     * Return weekday of Julian Day
     *
     * @return weekday [1..7] where 1 is Ahad/Sunday
     */
    public static int julianDayToWeekday(double julianDay) {
        return (int) floor(julianDay + JULIAN_DAY_DELTA) % DAYS_PER_WEEK + 1;
    }

    /**
     * Return Julian Day with added hours.
     */
    public static double adjustJdHour(double julianDay, double hours) {
        return julianDay + hours / HOURS_IN_DAY_DOUBLE;
    }
}
