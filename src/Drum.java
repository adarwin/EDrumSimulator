/*
 * Andrew Darwin
 * www.adarwin.com
 * github.com/adarwin
 * SUNY Oswego
 */

package com.adarwin.edrum;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;

public class Drum {

    private String sampleFilepath, name;
    private Clip clip;
    private AudioInputStream audioInputStream;

    public Drum(String name, String sampleFilepath) {
        this.name = name;
        this.sampleFilepath = sampleFilepath;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new File(sampleFilepath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException ex) {
            System.out.println(ex);
        } catch (LineUnavailableException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }


    public String toString() {
        return name;
    }

    public void play()
    {
        clip.setFramePosition(0);
        clip.start();
    }

    public void mute() {
        clip.stop();
    }
}
