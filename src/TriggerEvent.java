/*
 * Andrew Darwin
 * www.adarwin.com
 * github.com/adarwin
 * SUNY Oswego
 */

package com.adarwin.edrum;

import java.util.Calendar;

public class TriggerEvent {

    private Drum drum;
    private long time;

    public TriggerEvent(Drum drum) {
        this.drum = drum;
        time = System.currentTimeMillis();
    }


    public long getTime() {
        return time;
    }

    public Drum getDrum() {
        return drum;
    }
}
