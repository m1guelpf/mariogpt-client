package cloud.antony.mariogptclient.agents.robinBaumgarten;

import java.util.ArrayList;

import cloud.antony.mariogptclient.engine.core.MarioForwardModel;
import cloud.antony.mariogptclient.engine.helper.MarioActions;
import cloud.antony.mariogptclient.engine.helper.GameStatus;

public class Helper
{
    public static final int visitedListPenalty = 1500;
    public static final float maxMarioSpeed = 10.909091f;
    
    public static int getMarioDamage(final MarioForwardModel marioForwardModel, final MarioForwardModel marioForwardModel2) {
        int n = 0;
        if (marioForwardModel2.getMarioMode() > marioForwardModel.getMarioMode()) {
            ++n;
        }
        if (marioForwardModel.getGameStatus() == GameStatus.LOSE) {
            if (marioForwardModel.getMarioFloatPos()[1] > marioForwardModel.getLevelFloatDimensions()[1] - 20.0f) {
                n += 5;
            }
            else {
                n += 2;
            }
        }
        return n;
    }
    
    public static String getActionString(final boolean[] array) {
        String str = "";
        if (array[MarioActions.RIGHT.getValue()]) {
            str += "Forward ";
        }
        if (array[MarioActions.LEFT.getValue()]) {
            str += "Backward ";
        }
        if (array[MarioActions.SPEED.getValue()]) {
            str += "Speed ";
        }
        if (array[MarioActions.JUMP.getValue()]) {
            str += "Jump ";
        }
        if (array[MarioActions.DOWN.getValue()]) {
            str += "Duck";
        }
        if (str.length() == 0) {
            str = "[NONE]";
        }
        return str;
    }
    
    public static float[] estimateMaximumForwardMovement(float n, final boolean[] array, final int n2) {
        float n3 = 0.0f;
        final float n4 = array[MarioActions.SPEED.getValue()] ? 1.2f : 0.6f;
        int n5 = 0;
        if (array[MarioActions.LEFT.getValue()]) {
            n5 = -1;
        }
        if (array[MarioActions.RIGHT.getValue()]) {
            n5 = 1;
        }
        for (int i = 0; i < n2; ++i) {
            n += n4 * n5;
            n3 += n;
            n *= 0.89f;
        }
        return new float[] { n3, n };
    }
    
    public static boolean[] createAction(final boolean b, final boolean b2, final boolean b3, final boolean b4, final boolean b5) {
        final boolean[] array = new boolean[5];
        array[MarioActions.DOWN.getValue()] = b3;
        array[MarioActions.JUMP.getValue()] = b4;
        array[MarioActions.LEFT.getValue()] = b;
        array[MarioActions.RIGHT.getValue()] = b2;
        array[MarioActions.SPEED.getValue()] = b5;
        return array;
    }
    
    public static boolean canJumpHigher(final SearchNode searchNode, final boolean b) {
        return (searchNode.parentPos != null && b && canJumpHigher(searchNode.parentPos, false)) || searchNode.sceneSnapshot.mayMarioJump() || searchNode.sceneSnapshot.getMarioCanJumpHigher();
    }
    
    public static ArrayList<boolean[]> createPossibleActions(final SearchNode searchNode) {
        final ArrayList<boolean[]> list = new ArrayList<boolean[]>();
        if (canJumpHigher(searchNode, true)) {
            list.add(createAction(false, false, false, true, false));
        }
        if (canJumpHigher(searchNode, true)) {
            list.add(createAction(false, false, false, true, true));
        }
        list.add(createAction(false, true, false, false, true));
        if (canJumpHigher(searchNode, true)) {
            list.add(createAction(false, true, false, true, true));
        }
        list.add(createAction(false, true, false, false, false));
        if (canJumpHigher(searchNode, true)) {
            list.add(createAction(false, true, false, true, false));
        }
        list.add(createAction(true, false, false, false, false));
        if (canJumpHigher(searchNode, true)) {
            list.add(createAction(true, false, false, true, false));
        }
        list.add(createAction(true, false, false, false, true));
        if (canJumpHigher(searchNode, true)) {
            list.add(createAction(true, false, false, true, true));
        }
        return list;
    }
}
