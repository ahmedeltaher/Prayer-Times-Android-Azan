package com.azan;

import com.azan.types.BaseTimeAdjustmentType;

/**
 * Created by AhmedEltaher on 10/11/16.
 */

public class TimeAdjustment {
    /**
     * Each time is zero , default adjustment.
     */
    private double fajr = 0;
    private double sunrise = 0;
    private double zuhr = 0;
    private double asr = 0;
    private double maghrib = 0;
    private double isha = 0;

    public TimeAdjustment() {

    }

    public TimeAdjustment(double fajr, double sunrise, double zuhr, double asr,
                          double maghrib, double isha) {
        this.fajr = fajr;
        this.sunrise = sunrise;
        this.zuhr = zuhr;
        this.asr = asr;
        this.maghrib = maghrib;
        this.isha = isha;
    }

    public TimeAdjustment(BaseTimeAdjustmentType baseTimeAdjustmentType) {
        switch (baseTimeAdjustmentType) {
            case TWO_MINUTES_ADJUSTMENT:
                fajr = 2;
                sunrise = 2;
                zuhr = 2;
                asr = 2;
                maghrib = 2;
                isha = 2;
                break;
            case TWO_MINUTES_ZUHR:
                fajr = 0;
                sunrise = 0;
                zuhr = 2;
                asr = 0;
                maghrib = 0;
                isha = 0;
                break;
        }
    }

    public double getFajr() {
        return fajr;
    }

    public double getSunrise() {
        return sunrise;
    }

    public double getZuhr() {
        return zuhr;
    }

    public double getAsr() {
        return asr;
    }

    public double getMaghrib() {
        return maghrib;
    }

    public double getIsha() {
        return isha;
    }

    public void setFajr(double fajr) {
        this.fajr = fajr;
    }

    public void setSunrise(double sunrise) {
        this.sunrise = sunrise;
    }

    public void setZuhr(double zuhr) {
        this.zuhr = zuhr;
    }

    public void setAsr(double asr) {
        this.asr = asr;
    }

    public void setMaghrib(double maghrib) {
        this.maghrib = maghrib;
    }

    public void setIsha(double isha) {
        this.isha = isha;
    }
}
