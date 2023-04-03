package cloud.antony.mariogptclient.agents.robinBaumgarten;

import cloud.antony.mariogptclient.engine.core.MarioAgent;
import cloud.antony.mariogptclient.engine.core.MarioForwardModel;
import cloud.antony.mariogptclient.engine.core.MarioTimer;
import cloud.antony.mariogptclient.engine.helper.MarioActions;

public class Agent implements MarioAgent
{
    private boolean[] action;
    private AStarTree tree;
    
    @Override
    public void initialize(final MarioForwardModel marioForwardModel, final MarioTimer marioTimer) {
        this.action = new boolean[MarioActions.numberOfActions()];
        this.tree = new AStarTree();
    }
    
    @Override
    public boolean[] getActions(final MarioForwardModel marioForwardModel, final MarioTimer marioTimer) {
        return this.action = this.tree.optimise(marioForwardModel, marioTimer);
    }
    
    @Override
    public String getAgentName() {
        return "RobinBaumgartenAgent";
    }
}
