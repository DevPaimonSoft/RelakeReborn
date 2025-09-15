package su.relake.client.api.utils.client;



import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundPlayer {
    private Clip clip;
    private String currentTrackPath;
    private boolean isPlaying = false;
    private boolean isPause = false;
    private float volume = 1.0f;
    private float balance = 0.0f;
    private long clipPosition = 0;

    public SoundPlayer() {
    }

    public void playWithVolume(String sound, float volume) {
        try {
//            stop();

            String path = "assets/" + "relake" + "/sounds/" + sound;
            URL resourceUrl = getClass().getClassLoader().getResource(path);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Sound file not found: " + path);
            }

            if (!sound.toLowerCase().endsWith(".mp3") && !sound.toLowerCase().endsWith(".wav")) {
                throw new IllegalArgumentException("Only MP3 and WAV files are supported: " + sound);
            }

            currentTrackPath = sound;

            try (AudioInputStream ais = AudioSystem.getAudioInputStream(resourceUrl)) {
                AudioFormat format = ais.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(ais);
                updateVolumeAndBalance(volume);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP && !clip.isRunning()) {
                        synchronized (this) {
                            isPlaying = false;
                            clipPosition = 0;
                        }
                    }
                });
                clip.setMicrosecondPosition(clipPosition);
                clip.start();
                isPlaying = true;
            }
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file: " + sound + ". Ensure mp3spi, tritonus-share, and jlayer are included for MP3 support.");
            e.printStackTrace();
            isPlaying = false;
        } catch (LineUnavailableException | IOException e) {
            System.err.println("Error playing sound: " + sound + ", " + e.getMessage());
            e.printStackTrace();
            isPlaying = false;
        }
    }

    public void play(String sound) {
        try {
//            stop();

            String path = "assets/" + "relake" + "/sounds/" + sound;
            URL resourceUrl = getClass().getClassLoader().getResource(path);
            if (resourceUrl == null) {
                throw new IllegalArgumentException("Sound file not found: " + path);
            }

            if (!sound.toLowerCase().endsWith(".mp3") && !sound.toLowerCase().endsWith(".wav")) {
                throw new IllegalArgumentException("Only MP3 and WAV files are supported: " + sound);
            }

            currentTrackPath = sound;

            try (AudioInputStream ais = AudioSystem.getAudioInputStream(resourceUrl)) {
                AudioFormat format = ais.getFormat();
                DataLine.Info info = new DataLine.Info(Clip.class, format);
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(ais);
                updateVolumeAndBalance(volume);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP && !clip.isRunning()) {
                        synchronized (this) {
                            isPlaying = false;
                            clipPosition = 0;
                        }
                    }
                });
                clip.setMicrosecondPosition(clipPosition);
                clip.start();
                isPlaying = true;
            }
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio file: " + sound + ". Ensure mp3spi, tritonus-share, and jlayer are included for MP3 support.");
            e.printStackTrace();
            isPlaying = false;
        } catch (LineUnavailableException | IOException e) {
            System.err.println("Error playing sound: " + sound + ", " + e.getMessage());
            e.printStackTrace();
            isPlaying = false;
        }
    }

    public void stop() {
        try {
            synchronized (this) {
                if (clip != null) {
                    clipPosition = 0;
                    clip.stop();
                    clip.close();
                    clip = null;
                }
                isPlaying = false;
            }
        } catch (Exception e) {
            System.err.println("Error stopping sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            synchronized (this) {
                if (!isPlaying || clip == null) {
                    return;
                }
                clipPosition = clip.getMicrosecondPosition();
                clip.stop();
                isPlaying = false;
                isPause = true;
            }
        } catch (Exception e) {
            System.err.println("Error pausing sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void resume() {
        try {
            synchronized (this) {
                if (isPlaying || clip == null) {
                    return;
                }
                updateVolumeAndBalance(volume);
                clip.setMicrosecondPosition(clipPosition);
                clip.start();
                isPause = false;
                isPlaying = true;
            }
        } catch (Exception e) {
            System.err.println("Error resuming sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setVolume(double volume) {
        try {
            if (volume < 0.0 || volume > 1.0) {
                throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
            }
            synchronized (this) {
                this.volume = (float) volume;
                if (clip != null) {
                    updateVolumeAndBalance(volume);
                }
            }
        } catch (Exception e) {
            System.err.println("Error setting volume: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public double getVolume() {
        return volume;
    }

    public void setBalance(double balance) {
        try {
            if (balance < -1.0 || balance > 1.0) {
                throw new IllegalArgumentException("Balance must be between -1.0 and 1.0");
            }
            synchronized (this) {
                this.balance = (float) balance;
                if (clip != null) {
                    updateVolumeAndBalance(volume);
                }
            }
        } catch (Exception e) {
            System.err.println("Error setting balance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPause() {
        return isPause;
    }

    private void updateVolumeAndBalance(double volume) {
        if (clip == null) {
            return;
        }
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log10(Math.max(volume, 0.0001)) * 20); // Avoid log(0)
            gainControl.setValue(Math.max(gainControl.getMinimum(), Math.min(dB, gainControl.getMaximum())));

            if (clip.isControlSupported(FloatControl.Type.BALANCE)) {
                FloatControl balanceControl = (FloatControl) clip.getControl(FloatControl.Type.BALANCE);
                balanceControl.setValue(balance);
            }
        } catch (Exception e) {
            System.err.println("Error updating volume/balance: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void playSound(String s, float v) {
        setVolume(v);
        play(s);
    }
}
