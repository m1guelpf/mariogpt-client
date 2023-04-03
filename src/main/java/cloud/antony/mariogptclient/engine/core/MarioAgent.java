package cloud.antony.mariogptclient.engine.core;

public interface MarioAgent
{
    void initialize(final MarioForwardModel p0, final MarioTimer p1);
    
    boolean[] getActions(final MarioForwardModel p0, final MarioTimer p1);
    
    String getAgentName();
}
