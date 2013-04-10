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
        LinkedList<Long> hihatDurations = new LinkedList<Long>();
        LinkedList<Long> snareDurations = new LinkedList<Long>();
        LinkedList<Long> kickDurations = new LinkedList<Long>();
        int maxTapDurationsSize = 2;

        while (!quit) {
            //currentTime = System.currentTimeMillis();
            //if (currentTime > nextBeatMark - 30 &&
                    //currentTime < nextBeatMark + 30) {
                //playDrum(new Snare());
                //Figure out next beat mark
                //if (tapDurations.size() > 0) {
                    //nextBeatMark = currentTime + averageDuration(tapDurations);
                //}
            //}
            triggerEvent = triggerEvents.poll();
            if (triggerEvent != null) {
                // Play the drum as soon as possible. Do processing after,
                // since there will be relatively tons of time.
                Drum drum = triggerEvent.getDrum();
                if (drum != null) {
                    playDrum(drum);
                }
                triggerTime = triggerEvent.getTime();
                // Figure out which drum was hit
                if (drum instanceof HiHat) {
                    long duration = triggerTime - lastHiHatTime;
                    hihatDurations.add(duration);
                    if (hihatDurations.size() > maxTapDurationsSize) {
                        hihatDurations.poll();
                    }
                    lastHiHatTime = triggerTime;
                } else if (drum instanceof Snare) {
                    long duration = triggerTime - lastSnareTime;
                    lastSnareTime = triggerTime;
                } else if (drum instanceof Kick) {
                    lastKickTime = triggerTime;
                } else {
                }
                System.out.println(getBPM(hihatDurations));
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
        openHat = new Drum("Open HiHat", "audio/jazz/hihat - opened 1 - 1.wav");

        frame = new JFrame("EDrumSimulator");
        frame.setSize(new Dimension(380, 240));
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == 81) {
                    quit = true;
                } else if (hihats.contains(keyCode)) {
                    System.out.println("hihat");
                    fireTriggerEvent(new HiHat());
                } else if (snares.contains(keyCode)) {
                    System.out.println("snare");
                    fireTriggerEvent(new Snare());
                } else if (kicks.contains(keyCode)) {
                    System.out.println("kick");
                    fireTriggerEvent(new Kick());
                } else if (openHats.contains(keyCode)) {
                    System.out.println("OpenHat");
                    triggerEvents.add(new TriggerEvent(openHat));
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


    private static void fireTriggerEvent(Drum newDrum) {
        TriggerEvent newEvent = new TriggerEvent(newDrum);
        triggerEvents.add(newEvent);
    }
    
    private static void playDrum(Drum drum) {
        drum.play();
    }

    private static void muteDrum(Drum drum) {
        drum.mute();
    }

}
