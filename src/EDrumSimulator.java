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
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class EDrumSimulator {

    private static JFrame frame;
    private static boolean quit = false;

    private static ArrayList<Integer> hihats = new ArrayList<Integer>(
        Arrays.asList(74, 75) // J, K
    );

    private static ArrayList<Integer> openHats = new ArrayList<Integer>(
        Arrays.asList(76) // L
    );

    private static ArrayList<Integer> snares = new ArrayList<Integer>(
        Arrays.asList(68, 69, 82, 83) // D, E, R, S
    );

    private static ArrayList<Integer> kicks = new ArrayList<Integer>(
        Arrays.asList(70, 73, 85) // F, U, I
    );

    private static Drum hihat, snare, kick, openHat;

    private static LinkedList<Drum> playedDrums = new LinkedList<Drum>();
    private static LinkedList<TriggerEvent> triggerEvents;



    public static void main(String[] args) {
        triggerEvents = new LinkedList<TriggerEvent>();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        System.out.println("Now entering loop");
        TriggerEvent triggerEvent;
        while (!quit) {
            // For some bizzarre reason, some time needs to be taken up
            // in order for drum sounds to be played.
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
            triggerEvent = triggerEvents.poll();
            if (triggerEvent != null) {
                // Do something with the trigger event
                long triggerTime = triggerEvent.getTime();
                Drum drum = triggerEvent.getDrum();
                if (drum != null) {
                    System.out.println("Processing " + drum);
                    playDrum(drum);
                } else {
                    System.out.println("Drum was null");
                }
            }
        }
        System.out.println("Quit Loop");
    }

    private static void createAndShowGUI() {
        hihat = new Drum("HiHat", "audio/jazz/hihat - closed side - 1.wav");
        snare = new Drum("Snare", "audio/jazz/snare - snares on - 1.wav");
        kick = new Drum("Kick", "audio/pearl_master/kick 4 outside - 22in pearl master custom.wav");
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
                    if (triggerEvents.contains(newEvent)) {
                        System.out.println("Added new HiHat trigger event");
                    }
                    //playDrum(hihat);
                    //try {
                        //Thread.sleep(10);
                    //} catch (InterruptedException ex) {
                        //System.out.println(ex);
                    //}
                } else if (snares.contains(keyCode)) {
                    System.out.println("snare");
                    triggerEvents.add(new TriggerEvent(snare));
                    //playDrum(snare);
                    //try {
                        //Thread.sleep(10);
                    //} catch (InterruptedException ex) {
                        //System.out.println(ex);
                    //}
                    muteDrum(kick);
                } else if (kicks.contains(keyCode)) {
                    System.out.println("kick");
                    triggerEvents.add(new TriggerEvent(kick));
                    //playDrum(kick);
                    //try {
                        //Thread.sleep(10);
                    //} catch (InterruptedException ex) {
                        //System.out.println(ex);
                    //}
                    muteDrum(snare);
                } else if (openHats.contains(keyCode)) {
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
