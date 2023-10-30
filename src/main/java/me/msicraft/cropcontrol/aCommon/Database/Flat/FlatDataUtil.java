package me.msicraft.cropcontrol.aCommon.Database.Flat;

import me.msicraft.cropcontrol.CropControl;
import me.msicraft.cropcontrol.aCommon.Data.Crop;
import me.msicraft.cropcontrol.aCommon.Util.CropRegisterUtil;
import me.msicraft.cropcontrol.aCommon.Util.CropUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;
import java.util.UUID;
import java.util.regex.PatternSyntaxException;

public class FlatDataUtil {

    public static void loadAllData() {
        ConfigurationSection section = CropControl.flatDataFile.getConfig().getConfigurationSection("CropData");
        if (section != null) {
            Set<String> stringSet = section.getKeys(false);
            for (String s : stringSet) {
                Crop crop = loadFileDataToCropData(s);
                if (crop != null) {
                    CropRegisterUtil.registerCrop(crop.getLocation(), crop);
                }
            }
        }
    }

    public static void saveAllData(boolean clearData) {
        if (clearData) {
            CropControl.flatDataFile.getConfig().set("CropData.", null);
            CropControl.flatDataFile.saveConfig();
        }
        Set<Location> locationSet = CropRegisterUtil.getLocationSet();
        for (Location location : locationSet) {
            Crop crop = CropRegisterUtil.getCrop(location);
            if (crop != null) {
                saveCropDataToFileData(crop, false);
            }
        }
        CropControl.flatDataFile.saveConfig();
    }

    public static void saveCropDataToFileData(Crop crop, boolean isAlwaysSave) {
        Location location = crop.getLocation();
        World world = location.getWorld();
        if (world != null) {
            String locationS = world.getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ();
            String path = "CropData." + locationS;
            FileConfiguration config = CropControl.flatDataFile.getConfig();
            config.set(path + ".OwnerUUID", crop.getOwnerUUID().toString());
            config.set(path + ".RequiredGrowthTime", crop.getRequiredGrowthTime());
            config.set(path + ".LeftGrowthTime", crop.getLeftGrowthTime());
            config.set(path + ".CurrentAge", crop.getCurrentAge());
            config.set(path + ".GrowthRate", crop.getGrowthRate());
            if (isAlwaysSave) {
                CropControl.flatDataFile.saveConfig();
            }
        }
    }

    public static Crop loadFileDataToCropData(String locationS) {
        FileConfiguration config = CropControl.flatDataFile.getConfig();
        String path = "CropData." + locationS;
        Location location = stringToLocation(locationS);
        if (config.contains(path) && location != null) {
            String ownerS = config.getString(path + ".OwnerUUID");
            if (ownerS != null) {
                UUID uuid = UUID.fromString(ownerS);
                int requiredGrowthTime = config.getInt(path + ".RequiredGrowthTime");
                int leftGrowthTime = config.getInt(path + ".LeftGrowthTime");
                int currentAge = config.getInt(path + ".CurrentAge");
                double growthRate = config.getDouble(path + ".GrowthRate");
                long time = System.currentTimeMillis();
                Crop crop = new Crop(location, uuid, time, requiredGrowthTime);
                crop.setCurrentAge(currentAge);
                crop.setGrowthRate(growthRate);
                crop.setPlaceTime(time);
                crop.setFullGrowthTime(time + (leftGrowthTime * 1000L));
                return crop;
            }
        }
        return null;
    }

    public static Location stringToLocation(String locationS) {
        try {
            String[] a = locationS.split("_");
            World world = Bukkit.getWorld(a[0]);
            if (world != null) {
                return new Location(world, Double.parseDouble(a[1]), Double.parseDouble(a[2]), Double.parseDouble(a[3]));
            }
        } catch (PatternSyntaxException ignored) {
        }
        return null;
    }

}
