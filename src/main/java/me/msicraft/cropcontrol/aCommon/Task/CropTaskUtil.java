package me.msicraft.cropcontrol.aCommon.Task;

import me.msicraft.cropcontrol.CropControl;
import me.msicraft.cropcontrol.aCommon.Data.Crop;
import me.msicraft.cropcontrol.aCommon.Database.Flat.FlatDataUtil;
import me.msicraft.cropcontrol.aCommon.Util.CropRegisterUtil;
import me.msicraft.cropcontrol.aCommon.Util.CropUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class CropTaskUtil {

    private static int autoSaveTicks = 6000;
    private static int tickCounter = 0;

    private static boolean checkWater = false;
    private static int waterRadius = 0;

    private static int limitPerAttempt = 500;

    private static final Queue<Crop> growthCheckQueue = new LinkedList<>();
    private static final Queue<Crop> replaceQueue = new LinkedList<>();

    public static void reloadVariables() {
        checkWater = CropControl.getPlugin().getConfig().contains("Setting.CheckWater.Enabled") && CropControl.getPlugin().getConfig().getBoolean("Setting.CheckWater.Enabled");
        waterRadius = CropControl.getPlugin().getConfig().contains("Setting.CheckWater.Radius") ? CropControl.getPlugin().getConfig().getInt("Setting.CheckWater.Radius") : 0;
        limitPerAttempt = CropControl.getPlugin().getConfig().contains("Setting.LimitPerAttempt") ? CropControl.getPlugin().getConfig().getInt("Setting.LimitPerAttempt") : 500;
        if (limitPerAttempt < 1) {
            limitPerAttempt = 1;
        }
        autoSaveTicks = CropControl.getPlugin().getConfig().contains("Database.AutoSave") ? CropControl.getPlugin().getConfig().getInt("Database.AutoSave") : 6000;
    }

    public static void startCropAsyncTask(CropControl cropControl) {
        BukkitTask autoSaveTask = new BukkitRunnable() {
            @Override
            public void run() {
                tickCounter = tickCounter + 20;
                if (tickCounter >= autoSaveTicks) {
                    tickCounter = 0;
                    FlatDataUtil.saveAllData(true);
                }
            }
        }.runTaskTimerAsynchronously(cropControl, 0L, 20L);
        BukkitTask mainTask = new BukkitRunnable() {
            @Override
            public void run() {
                Set<Location> locationSet = CropRegisterUtil.getLocationSet();
                for (Location location : locationSet) {
                    Crop crop = CropRegisterUtil.getCrop(location);
                    if (crop != null) {
                        if (!crop.isQueue()) {
                            long leftTime = (crop.getFullGrowthTime() - System.currentTimeMillis()) / 1000;
                            if (leftTime < 0) {
                                leftTime = 0;
                            }
                            crop.setLeftGrowthTime((int) leftTime);
                            double requiredTime = crop.getRequiredGrowthTime();
                            double percent = 1 - (leftTime / requiredTime);
                            crop.setGrowthRate(percent);
                            crop.setQueue(true);
                            growthCheckQueue.offer(crop);
                        }
                    }
                }
                int limitCount = 0;
                while (!growthCheckQueue.isEmpty()) {
                    if (limitCount > limitPerAttempt) {
                        break;
                    }
                    Crop growthCrop = growthCheckQueue.poll();
                    if (growthCrop != null) {
                        if (!growthCrop.isCorrect()) {
                            CropRegisterUtil.removeCrop(growthCrop.getLocation());
                            continue;
                        }
                        Block block = growthCrop.getBlock();
                        BlockData blockData = block.getBlockData();
                        if (blockData instanceof Ageable) {
                            limitCount++;
                            Ageable ageable = (Ageable) blockData;
                            int currentAge = growthCrop.getCurrentAge();
                            int maxAge = ageable.getMaximumAge();
                            int calAge = (int) Math.round(maxAge * growthCrop.getGrowthRate());
                            int age = Math.max(calAge, currentAge);
                            if (growthCrop.getLeftGrowthTime() == 0) {
                                age = maxAge;
                                CropRegisterUtil.removeCrop(growthCrop.getLocation());
                            } else {
                                if (age == maxAge) {
                                    age = maxAge - 1;
                                }
                            }
                            growthCrop.setQueue(false);
                            if (currentAge == age) {
                                continue;
                            }
                            growthCrop.setCurrentAge(age);
                            replaceQueue.offer(growthCrop);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(cropControl, 0L, 5L);
        BukkitTask growthTask = new BukkitRunnable() {
            @Override
            public void run() {
                while (!replaceQueue.isEmpty()) {
                    Crop crop = replaceQueue.poll();
                    if (crop != null) {
                        Bukkit.getScheduler().runTask(cropControl, ()-> {
                            Block block = crop.getBlock();
                            if (crop.isCorrect() && CropUtil.isCorrectCrop(crop)) {
                                boolean success = true;
                                if (checkWater) {
                                    if (!CropUtil.hasNearbyWater(block.getLocation(), waterRadius)) {
                                        success = false;
                                    }
                                }
                                if (success) {
                                    BlockData blockData = block.getBlockData();
                                    if (blockData instanceof Ageable) {
                                        Ageable ageable = (Ageable) blockData;
                                        ageable.setAge(crop.getCurrentAge());
                                        block.setBlockData(blockData);
                                    }
                                }
                            } else {
                                crop.setCorrect(false);
                            }
                        });
                    }
                }
            }
        }.runTaskTimerAsynchronously(cropControl, 0L, 5L);
    }

}
