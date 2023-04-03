package cloud.antony.mariogptclient.engine.core;

public class MarioAgentEvent
{
    private boolean[] actions;
    private float marioX;
    private float marioY;
    private int marioState;
    private boolean marioOnGround;
    private int time;
    
    public MarioAgentEvent(final boolean[] actions, final float marioX, final float marioY, final int marioState, final boolean marioOnGround, final int time) {
        this.actions = actions;
        this.marioX = marioX;
        this.marioY = marioY;
        this.marioState = marioState;
        this.marioOnGround = marioOnGround;
        this.time = time;
    }
    
    public boolean[] getActions() {
        return this.actions;
    }
    
    public float getMarioX() {
        return this.marioX;
    }
    
    public float getMarioY() {
        return this.marioY;
    }
    
    public int getMarioState() {
        return this.marioState;
    }
    
    public boolean getMarioOnGround() {
        return this.marioOnGround;
    }
    
    public int getTime() {
        return this.time;
    }
}
