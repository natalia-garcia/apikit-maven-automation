package org.mule.classes;

/**
 * Created by natalia.garcia on 6/3/14.
 */
public class Timeout {

    public static int time;


    private final long duration;
    private final long start;

    public Timeout(long duration)
    {
        this.duration = duration;
        this.start = System.currentTimeMillis();
    }

    public boolean hasTimedOut()
    {
        final long now = System.currentTimeMillis();
        return (now - start) > duration;
    }
}
