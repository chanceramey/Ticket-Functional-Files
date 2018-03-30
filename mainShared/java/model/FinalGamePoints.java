package model;

/**
 * Created by Chance on 3/30/18.
 */

public class FinalGamePoints implements Comparable {

    public FinalGamePoints(int playerNumber, int routePoints, int finishedDestPoints, int unfinishedDestPoints, int lengthOfLongestPath) {
        this.routePoints = routePoints;
        this.playerNumber = playerNumber;
        this.finishedDestPoints = finishedDestPoints;
        this.unfinishedDestPoints = unfinishedDestPoints;
        this.lengthOfLongestPath = lengthOfLongestPath;
    }

    private int playerNumber = 0;
    private int routePoints = 0;
    private int finishedDestPoints = 0;
    private int unfinishedDestPoints = 0;
    private int lengthOfLongestPath = 0;
    private int longestPathPoints = 0;
    private int totalPoints = 0;

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getFinishedDestPoints() {
        return finishedDestPoints;
    }

    public void setFinishedDestPoints(int finishedDestPoints) {
        this.finishedDestPoints = finishedDestPoints;
    }

    public int getUnfinishedDestPoints() {
        return unfinishedDestPoints;
    }

    public void setUnfinishedDestPoints(int unfinishedDestPoints) {
        this.unfinishedDestPoints = unfinishedDestPoints;
    }

    public int getLengthOfLongestPath() {
        return lengthOfLongestPath;
    }

    public void setLengthOfLongestPath(int lengthOfLongestPath) {
        this.lengthOfLongestPath = lengthOfLongestPath;
    }

    public int getLongestPathPoints() {
        return longestPathPoints;
    }

    public void setLongestPathPoints(int longestPathPoints) {
        this.longestPathPoints = longestPathPoints;
    }

    public void setTotalPoints() {
        totalPoints += routePoints;
        totalPoints += finishedDestPoints;
        totalPoints += unfinishedDestPoints;
        totalPoints += longestPathPoints;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(int routePoints) {
        this.routePoints = routePoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public int compareTo(Object o) {
        if (o.getClass() != FinalGamePoints.class) {
            return 0;
        }
        else {
            FinalGamePoints otherFGPObject = (FinalGamePoints)o;
            if (otherFGPObject.getTotalPoints() > this.getTotalPoints()){
                return  -1;
            } else if (otherFGPObject.getTotalPoints() < this.getTotalPoints()){
                return 1;
            } else {
                return 0;
            }
        }
    }
}
