package cloud.antony.mariogptclient.agents.robinBaumgarten;

import cloud.antony.mariogptclient.engine.core.MarioForwardModel;
import cloud.antony.mariogptclient.engine.core.MarioTimer;
import cloud.antony.mariogptclient.engine.helper.GameStatus;

import java.util.ArrayList;

public class AStarTree
{
    public SearchNode bestPosition;
    public SearchNode furthestPosition;
    float currentSearchStartingMarioXPos;
    ArrayList<SearchNode> posPool;
    ArrayList<int[]> visitedStates;
    private boolean requireReplanning;
    private ArrayList<boolean[]> currentActionPlan;
    int ticksBeforeReplanning;
    
    public AStarTree() {
        this.visitedStates = new ArrayList<int[]>();
        this.requireReplanning = false;
        this.ticksBeforeReplanning = 0;
    }
    
    private MarioForwardModel search(final MarioTimer marioTimer) {
        SearchNode searchNode = this.bestPosition;
        int n = 0;
        final int n2 = 176;
        while (this.posPool.size() != 0 && (this.bestPosition.sceneSnapshot.getMarioFloatPos()[0] - this.currentSearchStartingMarioXPos < n2 || n == 0) && marioTimer.getRemainingTime() > 0L) {
            searchNode = this.pickBestPos(this.posPool);
            if (searchNode == null) {
                return null;
            }
            n = 0;
            final float simulatePos = searchNode.simulatePos();
            if (simulatePos < 0.0f) {
                continue;
            }
            if (!searchNode.isInVisitedList && this.isInVisited((int)searchNode.sceneSnapshot.getMarioFloatPos()[0], (int)searchNode.sceneSnapshot.getMarioFloatPos()[1], searchNode.timeElapsed)) {
                final float n3 = simulatePos + 1500.0f;
                searchNode.isInVisitedList = true;
                searchNode.remainingTime = n3;
                searchNode.remainingTimeEstimated = n3;
                this.posPool.add(searchNode);
            }
            else if (simulatePos - searchNode.remainingTimeEstimated > 0.1) {
                searchNode.remainingTimeEstimated = simulatePos;
                this.posPool.add(searchNode);
            }
            else {
                n = 1;
                this.visited((int)searchNode.sceneSnapshot.getMarioFloatPos()[0], (int)searchNode.sceneSnapshot.getMarioFloatPos()[1], searchNode.timeElapsed);
                this.posPool.addAll(searchNode.generateChildren());
            }
            if (n == 0) {
                continue;
            }
            if (this.bestPosition.getRemainingTime() > searchNode.getRemainingTime()) {
                this.bestPosition = searchNode;
            }
            if (searchNode.sceneSnapshot.getMarioFloatPos()[0] <= this.furthestPosition.sceneSnapshot.getMarioFloatPos()[0]) {
                continue;
            }
            this.furthestPosition = searchNode;
        }
        if (searchNode.sceneSnapshot.getMarioFloatPos()[0] - this.currentSearchStartingMarioXPos < n2 && this.furthestPosition.sceneSnapshot.getMarioFloatPos()[0] > this.bestPosition.sceneSnapshot.getMarioFloatPos()[0] + 20.0f) {
            this.bestPosition = this.furthestPosition;
        }
        return searchNode.sceneSnapshot;
    }
    
    private void startSearch(final MarioForwardModel marioForwardModel, final int n) {
        final SearchNode searchNode = new SearchNode(null, n, null);
        searchNode.initializeRoot(marioForwardModel);
        this.posPool = new ArrayList<SearchNode>();
        this.visitedStates.clear();
        this.posPool.addAll(searchNode.generateChildren());
        this.currentSearchStartingMarioXPos = marioForwardModel.getMarioFloatPos()[0];
        this.bestPosition = searchNode;
        this.furthestPosition = searchNode;
    }
    
    private ArrayList<boolean[]> extractPlan() {
        final ArrayList<boolean[]> list = new ArrayList<boolean[]>();
        if (this.bestPosition == null) {
            for (int i = 0; i < 10; ++i) {
                list.add(Helper.createAction(false, true, false, false, true));
            }
            return list;
        }
        for (SearchNode searchNode = this.bestPosition; searchNode.parentPos != null; searchNode = searchNode.parentPos) {
            for (int j = 0; j < searchNode.repetitions; ++j) {
                list.add(0, searchNode.action);
            }
            if (searchNode.hasBeenHurt) {
                this.requireReplanning = true;
            }
        }
        return list;
    }
    
    private SearchNode pickBestPos(final ArrayList<SearchNode> list) {
        SearchNode o = null;
        float n = 1.0E7f;
        for (final SearchNode searchNode : list) {
            final float n2 = searchNode.getRemainingTime() + searchNode.timeElapsed * 0.9f;
            if (n2 < n) {
                o = searchNode;
                n = n2;
            }
        }
        list.remove(o);
        return o;
    }
    
    public boolean[] optimise(final MarioForwardModel marioForwardModel, final MarioTimer marioTimer) {
        int size = 2;
        final int n = 2;
        final MarioForwardModel clone = marioForwardModel.clone();
        --this.ticksBeforeReplanning;
        this.requireReplanning = false;
        if (this.ticksBeforeReplanning <= 0 || this.currentActionPlan.size() == 0 || this.requireReplanning) {
            this.currentActionPlan = this.extractPlan();
            if (this.currentActionPlan.size() < size) {
                size = this.currentActionPlan.size();
            }
            for (int i = 0; i < size; ++i) {
                marioForwardModel.advance(this.currentActionPlan.get(i));
            }
            this.startSearch(marioForwardModel, n);
            this.ticksBeforeReplanning = size;
        }
        if (marioForwardModel.getGameStatus() == GameStatus.LOSE) {
            this.startSearch(clone, n);
        }
        this.search(marioTimer);
        boolean[] array = new boolean[5];
        if (this.currentActionPlan.size() > 0) {
            array = this.currentActionPlan.remove(0);
        }
        return array;
    }
    
    private void visited(final int n, final int n2, final int n3) {
        this.visitedStates.add(new int[] { n, n2, n3 });
    }
    
    private boolean isInVisited(final int n, final int n2, final int n3) {
        final int n4 = 5;
        final int n5 = 2;
        final int n6 = 2;
        for (final int[] array : this.visitedStates) {
            if (Math.abs(array[0] - n) < n5 && Math.abs(array[1] - n2) < n6 && Math.abs(array[2] - n3) < n4 && n3 >= array[2]) {
                return true;
            }
        }
        return false;
    }
}
