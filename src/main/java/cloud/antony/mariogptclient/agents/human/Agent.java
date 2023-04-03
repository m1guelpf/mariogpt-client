package cloud.antony.mariogptclient.agents.human;

import java.awt.event.KeyEvent;

import cloud.antony.mariogptclient.engine.core.MarioAgent;
import cloud.antony.mariogptclient.engine.core.MarioForwardModel;
import cloud.antony.mariogptclient.engine.core.MarioTimer;
import cloud.antony.mariogptclient.engine.helper.MarioActions;

import java.awt.event.KeyAdapter;

public class Agent extends KeyAdapter implements MarioAgent
{
    private boolean[] actions;
    
    public Agent() {
        this.actions = null;
    }
    
    @Override
    public void initialize(final MarioForwardModel marioForwardModel, final MarioTimer marioTimer) {
        this.actions = new boolean[MarioActions.numberOfActions()];
    }
    
    @Override
    public boolean[] getActions(final MarioForwardModel marioForwardModel, final MarioTimer marioTimer) {
        return this.actions;
    }
    
    @Override
    public String getAgentName() {
        return "HumanAgent";
    }
    
    @Override
    public void keyPressed(final KeyEvent keyEvent) {
        this.toggleKey(keyEvent.getKeyCode(), true);
    }
    
    @Override
    public void keyReleased(final KeyEvent keyEvent) {
        this.toggleKey(keyEvent.getKeyCode(), false);
    }
    
    private void toggleKey(final int n, final boolean b) {
        if (this.actions == null) {
            return;
        }
        switch (n) {
            case 37: {
                this.actions[MarioActions.LEFT.getValue()] = b;
                break;
            }
            case 39: {
                this.actions[MarioActions.RIGHT.getValue()] = b;
                break;
            }
            case 40: {
                this.actions[MarioActions.DOWN.getValue()] = b;
                break;
            }
            case 83: {
                this.actions[MarioActions.JUMP.getValue()] = b;
                break;
            }
            case 65: {
                this.actions[MarioActions.SPEED.getValue()] = b;
                break;
            }
        }
    }
}
