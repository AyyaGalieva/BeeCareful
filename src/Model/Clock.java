package Model;

import java.util.Timer;
import java.util.TimerTask;

public class Clock {
    private Timer timer;
    private long seconds;
    private final ModelObservable observable;

    public Clock(final ModelObservable o) {
        assert o != null;
        observable = o;
    }

    public void start() {
        seconds = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                seconds++;
                observable.notifyUpdateTime(seconds);
            }
        }, 1000, 1000);
    }

    public void stop() {
        if (timer != null)
            timer.cancel();
    }

    public int getTime() {
        return (int) seconds;
    }
}
