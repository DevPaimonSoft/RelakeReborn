package su.relake.client.api.utils.math.timer;

public class TimerUtil {
    private long prevMS;
    public static long lastMS;
    private long previousTime = -1L;
    public static float timerSpeed = 1.0F;
    private boolean isPaused;
    private long pauseTime;

    public TimerUtil() {
        this.prevMS = 0L;
        this.isPaused = false;
        this.pauseTime = 0L;
    }

    public static boolean hasTimeElapseds(long time) {
        return System.currentTimeMillis() - lastMS >= time;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public boolean check(float milliseconds) {
        if (isPaused) return false;
        return (float)(getCurrentTime() - this.previousTime) >= milliseconds;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (isPaused) return false;
        if (System.currentTimeMillis() - lastMS > time) {
            if (reset) {
                reset();
            }
            return true;
        }
        return false;
    }

    public boolean hasTimeElapsed() {
        return lastMS < System.currentTimeMillis();
    }

    public long getDifference() {
        return getTime() - this.prevMS;
    }

    public void setDifference(long difference) {
        this.prevMS = (getTime() - difference);
    }

    public long getPrevMS() {
        return prevMS;
    }

    public boolean hasReached(double milliseconds) {
        if (isPaused) return false;
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public boolean finished(double milliseconds) {
        if (isPaused) return false;
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public boolean isReached(double milliseconds) {
        if (isPaused) return false;
        return (double)(this.getCurrentMS() - this.lastMS) >= milliseconds;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public boolean delay(float milliSec) {
        if (isPaused) return false;
        return (float)(getTime() - this.prevMS) >= milliSec;
    }

    public void setTime(long time) {
        this.lastMS = time;
    }
    public static void reset1() {
        lastMS = System.currentTimeMillis();
    }
    public long getTime() {
        return this.getCurrentMS() - this.lastMS;
    }
    public static long getTime1() {
        return System.currentTimeMillis() - lastMS;
    }
    public static void setTime1(long time) {
        lastMS = time;
    }
    public void pause() {
        if (!isPaused) {
            isPaused = true;
            pauseTime = getCurrentMS();
        }
    }

    public void resume() {
        if (isPaused) {
            isPaused = false;
            lastMS += (getCurrentMS() - pauseTime);
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public long elapsedTime() {
        return getCurrentMS() - this.lastMS;
    }
}
