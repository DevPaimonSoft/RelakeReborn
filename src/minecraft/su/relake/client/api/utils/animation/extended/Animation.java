package su.relake.client.api.utils.animation.extended;


import su.relake.client.api.utils.math.timer.TimerUtil;

public abstract class Animation {
    private int duration;
    private final double endPoint;
    private Direction direction;

    public Animation(int ms, double endPoint) {
        this(ms, endPoint, Direction.FORWARDS);
    }

    public Animation(int duration, double endPoint, Direction direction) {
        this.duration = duration;
        this.endPoint = endPoint;
        this.direction = direction;
        TimerUtil.reset1();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getEndPoint() {
        return endPoint;
    }

    public Direction getDirection() {
        return direction;
    }

    public Animation setDirection(Direction direction) {
        if (this.direction != direction) {
            this.direction = direction;
            TimerUtil.setTime1(System.currentTimeMillis() - (duration - Math.min(duration, TimerUtil.getTime1())));
        }
        return this;
    }

    public Animation setDirection(boolean forwards) {
        Direction direction = forwards ? Direction.FORWARDS : Direction.BACKWARDS;
        if (this.direction != direction) {
            this.direction = direction;
            TimerUtil.setTime1(System.currentTimeMillis() - (duration - Math.min(duration, TimerUtil.getTime1())));
        }
        return this;
    }

    public Number getOutput() {
        if (direction.forwards()) {
            if (isDone()) {
                return endPoint;
            }
            return getEquation(TimerUtil.getTime1() / (double) duration) * endPoint;
        } else {
            if (isDone()) {
                return 0.0;
            }
            if (correctOutput()) {
                double revTime = Math.min(duration, Math.max(0, duration - TimerUtil.getTime1()));
                return getEquation(revTime / (double) duration) * endPoint;
            }
            return (1 - getEquation(TimerUtil.getTime1() / (double) duration)) * endPoint;
        }
    }

    protected boolean correctOutput() {
        return false;
    }

    public boolean finished(Direction direction) {
        return isDone() && this.direction.equals(direction);
    }

    public void reset() {
        TimerUtil.reset1();
    }

    public boolean isDone() {
        return TimerUtil.hasTimeElapseds(duration);
    }

    protected abstract double getEquation(double x);
}
