package com.azan;

import android.os.Parcel;
import android.os.Parcelable;

import static com.azan.Constants.MINUTE_IN_HOUR;
import static com.azan.Constants.SECONDS_IN_HOUR;
import static com.azan.Constants.SECOND_IN_MINUTE;
import static java.lang.Integer.compare;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.String.format;

/**
 * Created by AhmedEltaher on 10/11/16.
 */
public class Time implements Parcelable, Comparable<Time> {
    private final String TIME_FORMAT = "%02d:%02d:%02d";
    private int hour;
    private int minute;
    private int second;

    public Time(double hours) {
        double seconds = ceil(hours * SECONDS_IN_HOUR);
        this.hour = (int) floor(seconds / SECONDS_IN_HOUR);
        this.minute = (int) floor(this.hour / MINUTE_IN_HOUR);
        this.second = (int) floorMod(seconds, SECOND_IN_MINUTE);
    }

    private double floorMod(double dividend, double divisor) {
        double Modulus = dividend % divisor;
        return Modulus < 0 ? Modulus + divisor : Modulus;
    }

    @Override
    public String toString() {
        return format(TIME_FORMAT, hour, minute, second);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Time(Parcel in) {
        hour = in.readInt();
        minute = in.readInt();
        second = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(hour);
        parcel.writeInt(minute);
        parcel.writeInt(second);
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    @Override
    public int compareTo(Time time) {
        int equale = compare(hour, time.hour);
        if (equale != 0)
            return equale;
        equale = compare(minute, time.minute);
        if (equale != 0)
            return equale;
        return compare(second, time.second);

    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }
}
