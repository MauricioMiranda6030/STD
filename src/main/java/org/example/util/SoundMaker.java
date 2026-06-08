package org.example.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class SoundMaker {
    public static SoundMaker instance;

    private SoundMaker(){}

    public static SoundMaker getInstance(){
        if(instance == null)
            instance = new SoundMaker();
        return instance;
    }

    public void makeSound(String src){
        File file;
        try{
            file = new File(getClass().getResource(src).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

}
