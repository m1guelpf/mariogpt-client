package cloud.antony.mariogptclient.engine.core;

public class MarioTimer
{
    private long startTimer;
    private long remainingTime;
    
    public MarioTimer(final long remainingTime) {
        this.startTimer = System.currentTimeMillis();
        this.remainingTime = remainingTime;
    }
    
    public long getRemainingTime() {
        return Math.max(0L, this.remainingTime - (System.currentTimeMillis() - this.startTimer));
    }
}
