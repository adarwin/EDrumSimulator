/*
 * Andrew Darwin
 * www.adarwin.com
 * github.com/adarwin
 * SUNY Oswego
 */

package com.adarwin.edrum;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class EDrumSimulator {

    private static JFrame frame;
    private static boolean quit = false;
    private static Drum hihat, snare, kick, openHat;
    private static LinkedList<Drum> playedDrums = new LinkedList<Drum>();
    private static ArrayBlockingQueue<TriggerEvent> triggerEvents;

    private static ArrayList<Integer> hihats = new ArrayList<Integer>(
        Arrays.asList(71, 72, 74) // G, H, J
    );
    private static ArrayList<Integer> openHats = new ArrayList<Integer>(
        Arrays.asList(76) // L
    );
    private static ArrayList<Integer> snares = new ArrayList<Integer>(
        Arrays.asList(68, 75, 69, 82, 83) // D, K, E, R, S
    );
    private static ArrayList<Integer> kicks = new ArrayList<Integer>(
        Arrays.asList(70, 73, 85, 89) // F, U, I, Y
    );




    public static void main(String[] args) {
        triggerEvents = new ArrayBlockingQueue<TriggerEvent>(5);
                                            // The event queue doesn't need to
                                            // be very large since drums are
                                            // played in real time.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

        System.out.println("Now entering loop");
        TriggerEvent triggerEvent = null;
        long lastHiHatTime = 0;
        long lastSnareTime = 0;
        long lastKickTime = 0;
        long triggerTime = 0;
        long currentTime = 0;
        long nextBeatMark = 0;
        LinkedList<Long> tapDurations = new LinkedList<Long>();
        int maxTapDurationsSize = 2;

        while (!quit) {
            currentTime = System.currentTimeMillis();
            if (currentTime > nextBeatMark - 30 &&
                    currentTime < nextBeatMark + 30) {
                playDrum(new Snare());
                //Figure out next beat mark
                if (tapDurations.size() > 0) {
                    nextBeatMark = currentTime + averageDuration(tapDurations);
                }
            }
            triggerEvent = triggerEvents.poll();
            if (triggerEvent != null) {
                System.out.println(getBPM(tapDurations));
                triggerTime = triggerEvent.getTime();
                // Do something with the trigger event
                Drum drum = triggerEvent.getDrum();
                // Figure out which drum was hit
                if (drum instanceof HiHat) {
                    long duration = triggerTime - lastHiHatTime;
                    tapDurations.add(duration);
                    if (tapDurations.size() > maxTapDurationsSize) {
                        tapDurations.poll();
                    }
                    lastHiHatTime = triggerTime;
                } else if (drum instanceof Snare) {
                    lastSnareTime = triggerTime;
                } else if (drum instanceof Kick) {
                    lastKickTime = triggerTime;
                } else {
                }
                if (drum != null) {
                    System.out.println("Processing " + drum);
                    playDrum(drum);
                }
            }
        }
        System.out.println("Quit Loop");
    }


    private static long averageDuration(List<Long> durationList) {
        long sum = 0;
        for (long duration : durationList) {
            sum += duration;
        }
        return sum/durationList.size();
    }

    private static long getBPM(List<Long> durationList) {
        long bpm = 0;
        long average = 0;
        if (durationList != null && durationList.size() > 0) {
            average = averageDuration(durationList);
            bpm = 60000 / average;
        }
        // Divide number of miliseconds per minute by average duration
        return bpm;
    }

    private static void createAndShowGUI() {
        //hihat = new Drum("HiHat", "audio/jazz/hihat - closed side - 1.wav");
        //snare = new Drum("Snare", "audio/jazz/snare - snares on - 1.wav");
        //kick = new Drum("Kick", "audio/pearl_master/kick 4 outside - 22in pearl master custom.wav");
        openHat = new Drum("Open HiHat", "audio/jazz/hihat - opened 1 - 1.wav");

        frame = new JFrame("EDrumSimulator");
        frame.setSize(new Dimension(800, 600));
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == 81) {
                    quit = true;
                } else if (hihats.contains(keyCode)) {
                    // Fire Trigger Event
                    TriggerEvent newEvent = new TriggerEvent(new HiHat());
                    triggerEvents.add(newEvent);
                    //if (triggerEvents.contains(newEvent)) {
                        //System.out.println("Added new HiHat trigger event");
                    //}
                    //playDrum(hihat);
                    //try {
                        //Thread.sleep(10);
                    //} catch (InterruptedException ex) {
                        //System.out.println(ex);
                    //}
                } else if (snares.contains(keyCode)) {
                    System.out.println("snare");
                    TriggerEvent newEvent = new TriggerEvent(new Snare());
                    triggerEvents.add(newEvent);
                    //playDrum(snare);
                    //try {
                        //Thread.sleep(10);
                    //} catch (InterruptedException ex) {
                        //System.out.println(ex);
                    //}
                    muteDrum(kick);
                } else if (kicks.contains(keyCode)) {
                    System.out.println("kick");
                    TriggerEvent newEvent = new TriggerEvent(new Kick());
                    triggerEvents.add(newEvent);
                    //playDrum(kick);
                    //try {
                        //Thread.sleep(10);
                    //} catch (InterruptedException ex) {
                        //System.out.println(ex);
                    //}
                    //muteDrum(snare);
                } else if (openHats.contains(keyCode)) {
                    System.out.println("OpenHat");
                    triggerEvents.add(new TriggerEvent(openHat));
                    //playDrum(openHat);
                } else {
                    System.out.println(e.getKeyCode());
                }
            }
            public void keyReleased(KeyEvent e) {
            }
            public void keyTyped(KeyEvent e) {
            }
        });
        frame.setVisible(true);
    }


    private static void fireTriggerEvent(Drum drum) {
    }
    
    private static void playDrum(Drum drum) {
        //playedDrums.add(drum);
        drum.play();
        //if (playedDrums.size() > 9) {
            //muteDrum(playedDrums.poll());
        //}
    }

    private static void muteDrum(Drum drum) {
        drum.mute();
    }

}
