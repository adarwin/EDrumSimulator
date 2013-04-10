/*
 * Andrew Darwin
 * www.adarwin.com
 * github.com/adarwin
 * SUNY Oswego
 */

package com.adarwin.edrum;

public class Rhythm {

    private static long millisPerMinute = 60000;

    private int bpm;

    public Rhythm() {
        bpm = 60;
    }

    public static long millisToBPM(long millis) {
        return millisPerMinute / millis; // Divide # of ms per min by ms
    }


    public void setBPM(int value) {
        if (value > 0) {
            bpm = value;
        } else {
            bpm = 0;
        }
    }


    public void suggestDownbeat(TriggerEvent suggestion, float weight) {
    }


    public void suggestBeatOne(TriggerEvent suggestion, float weight) {
    }


}
