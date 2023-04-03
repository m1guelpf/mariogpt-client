package cloud.antony.mariogptclient.engine.core;

import cloud.antony.mariogptclient.engine.helper.EventType;

public class MarioEvent
{
    private EventType eventType;
    private int eventParam;
    private float marioX;
    private float marioY;
    private int marioState;
    private int time;
    
    public MarioEvent(final EventType eventType) {
        this.eventType = eventType;
        this.eventParam = 0;
        this.marioX = 0.0f;
        this.marioY = 0.0f;
        this.marioState = 0;
        this.time = 0;
    }
    
    public MarioEvent(final EventType eventType, final int eventParam) {
        this.eventType = eventType;
        this.eventParam = eventParam;
        this.marioX = 0.0f;
        this.marioY = 0.0f;
        this.marioState = 0;
        this.time = 0;
    }
    
    public MarioEvent(final EventType eventType, final float marioX, final float marioY, final int marioState, final int time) {
        this.eventType = eventType;
        this.eventParam = 0;
        this.marioX = marioX;
        this.marioY = marioY;
        this.marioState = marioState;
        this.time = time;
    }
    
    public MarioEvent(final EventType eventType, final int eventParam, final float marioX, final float marioY, final int marioState, final int time) {
        this.eventType = eventType;
        this.eventParam = eventParam;
        this.marioX = marioX;
        this.marioY = marioY;
        this.marioState = marioState;
        this.time = time;
    }
    
    public int getEventType() {
        return this.eventType.getValue();
    }
    
    public int getEventParam() {
        return this.eventParam;
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
    
    public int getTime() {
        return this.time;
    }
    
    @Override
    public boolean equals(final Object o) {
        final MarioEvent marioEvent = (MarioEvent)o;
        return this.eventType == marioEvent.eventType && (this.eventParam == 0 || this.eventParam == marioEvent.eventParam);
    }
}
