package me.msicraft.cropcontrol.aCommon.Data;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.UUID;

public class Crop {

    private final Block block;
    private final Location location;
    private final UUID ownerUUID;

    private int currentAge;
    private long placeTime;
    private long fullGrowthTime;
    private final int requiredGrowthTime;
    private int leftGrowthTime;
    private double growthRate;

    private boolean isQueue;
    private boolean isCorrect;

    public Crop(Location location, UUID ownerUUID ,long placeTime, int requiredGrowthTime) {
        this.location = location;
        this.block = location.getBlock();
        this.ownerUUID = ownerUUID;
        this.placeTime = placeTime;
        this.requiredGrowthTime = requiredGrowthTime;
        this.leftGrowthTime = requiredGrowthTime;
        this.currentAge = 0;
        this.growthRate = 0;
        this.isQueue = false;
        this.isCorrect = true;
    }

    public Block getBlock() {
        return block;
    }

    public Location getLocation() {
        return location;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public long getPlaceTime() {
        return placeTime;
    }

    public void setPlaceTime(long placeTime) {
        this.placeTime = placeTime;
    }

    public long getFullGrowthTime() {
        return fullGrowthTime;
    }

    public void setFullGrowthTime(long fullGrowthTime) {
        this.fullGrowthTime = fullGrowthTime;
    }

    public int getRequiredGrowthTime() {
        return requiredGrowthTime;
    }

    public int getLeftGrowthTime() {
        return leftGrowthTime;
    }

    public void setLeftGrowthTime(int leftGrowthTime) {
        this.leftGrowthTime = leftGrowthTime;
    }

    public boolean isQueue() {
        return isQueue;
    }

    public void setQueue(boolean queue) {
        isQueue = queue;
    }

    public int getCurrentAge() {
        return currentAge;
    }

    public void setCurrentAge(int currentAge) {
        this.currentAge = currentAge;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }
}
