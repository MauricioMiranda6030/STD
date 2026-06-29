package org.example.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundMaker {
    public static SoundMaker instance;

    private SoundMaker(){}

    public static SoundMaker getInstance(){
        if(instance == null)
            instance = new SoundMaker();
        return instance;
    }

    public void makeSound(String src) {
        try {
            InputStream is = getClass().getResourceAsStream(src);

            if (is == null) {
                throw new RuntimeException("No se encontró el recurso: " + src);
            }

            AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(new BufferedInputStream(is));

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
