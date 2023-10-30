package me.msicraft.cropcontrol.aCommon.Util;

import me.msicraft.cropcontrol.aCommon.Data.Crop;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CropRegisterUtil {

    private static final Map<Location, Crop> registeredCropMap = new HashMap<>();

    public static boolean isExistCrop(Location location) {
        return registeredCropMap.containsKey(location);
    }

    public static void registerCrop(Location location, Crop crop) {
        registeredCropMap.put(location, crop);
    }

    public static Set<Location> getLocationSet() {
        return registeredCropMap.keySet();
    }

    public static Crop getCrop(Location location) {
        if (registeredCropMap.containsKey(location)) {
            return registeredCropMap.get(location);
        }
        return null;
    }

    public static void removeCrop(Location location) {
        registeredCropMap.remove(location);
    }

}
