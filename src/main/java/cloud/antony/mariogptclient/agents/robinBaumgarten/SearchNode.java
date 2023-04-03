package cloud.antony.mariogptclient.agents.robinBaumgarten;

import cloud.antony.mariogptclient.engine.core.MarioForwardModel;
import cloud.antony.mariogptclient.engine.helper.GameStatus;
import java.util.Iterator;
import java.util.ArrayList;

public class SearchNode
{
    public int timeElapsed;
    public float remainingTimeEstimated;
    public float remainingTime;
    public SearchNode parentPos;
    public MarioForwardModel sceneSnapshot;
    public int distanceFromOrigin;
    public boolean hasBeenHurt;
    public boolean isInVisitedList;
    boolean[] action;
    int repetitions;
    
    public float calcRemainingTime(final float n, final float n2) {
        return (100000.0f - (this.maxForwardMovement(n2, 1000) + n)) / 10.909091f - 1000.0f;
    }
    
    public float getRemainingTime() {
        if (this.remainingTime > 0.0f) {
            return this.remainingTime;
        }
        return this.remainingTimeEstimated;
    }
    
    public float estimateRemainingTimeChild(final boolean[] array, final int n) {
        final float[] estimateMaximumForwardMovement = Helper.estimateMaximumForwardMovement(this.sceneSnapshot.getMarioFloatVelocity()[0], array, n);
        return this.calcRemainingTime(this.sceneSnapshot.getMarioFloatPos()[0] + estimateMaximumForwardMovement[0], estimateMaximumForwardMovement[1]);
    }
    
    public SearchNode(final boolean[] action, final int repetitions, final SearchNode parentPos) {
        this.timeElapsed = 0;
        this.remainingTimeEstimated = 0.0f;
        this.remainingTime = 0.0f;
        this.parentPos = null;
        this.sceneSnapshot = null;
        this.distanceFromOrigin = 0;
        this.hasBeenHurt = false;
        this.isInVisitedList = false;
        this.repetitions = 1;
        this.parentPos = parentPos;
        if (parentPos != null) {
            this.remainingTimeEstimated = parentPos.estimateRemainingTimeChild(action, repetitions);
            this.distanceFromOrigin = parentPos.distanceFromOrigin + 1;
        }
        this.action = action;
        this.repetitions = repetitions;
        if (parentPos != null) {
            this.timeElapsed = parentPos.timeElapsed + repetitions;
        }
        else {
            this.timeElapsed = 0;
        }
    }
    
    public void initializeRoot(final MarioForwardModel marioForwardModel) {
        if (this.parentPos == null) {
            this.sceneSnapshot = marioForwardModel.clone();
            this.remainingTimeEstimated = this.calcRemainingTime(marioForwardModel.getMarioFloatPos()[0], 0.0f);
        }
    }
    
    public float simulatePos() {
        this.sceneSnapshot = this.parentPos.sceneSnapshot.clone();
        for (int i = 0; i < this.repetitions; ++i) {
            this.sceneSnapshot.advance(this.action);
        }
        final int marioDamage = Helper.getMarioDamage(this.sceneSnapshot, this.parentPos.sceneSnapshot);
        this.remainingTime = this.calcRemainingTime(this.sceneSnapshot.getMarioFloatPos()[0], this.sceneSnapshot.getMarioFloatVelocity()[0]) + marioDamage * (1000000 - 100 * this.distanceFromOrigin);
        if (this.isInVisitedList) {
            this.remainingTime += 1500.0f;
        }
        this.hasBeenHurt = (marioDamage != 0);
        return this.remainingTime;
    }
    
    public ArrayList<SearchNode> generateChildren() {
        final ArrayList<SearchNode> list = new ArrayList<SearchNode>();
        final ArrayList<boolean[]> possibleActions = Helper.createPossibleActions(this);
        if (this.isLeafNode()) {
            possibleActions.clear();
        }
        final Iterator<boolean[]> iterator = possibleActions.iterator();
        while (iterator.hasNext()) {
            list.add(new SearchNode(iterator.next(), this.repetitions, this));
        }
        return list;
    }
    
    public boolean isLeafNode() {
        return this.sceneSnapshot != null && this.sceneSnapshot.getGameStatus() != GameStatus.RUNNING;
    }
    
    private float maxForwardMovement(final float n, final int n2) {
        final float n3 = (float)n2;
        return (float)(99.17355373 * Math.pow(0.89, n3 + 1.0f) - 9.090909091 * n * Math.pow(0.89, n3 + 1.0f) + 10.90909091 * n3 - 88.26446282 + 9.090909091 * n);
    }
}
