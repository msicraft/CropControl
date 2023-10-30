package me.msicraft.cropcontrol.aCommon.Util;

import me.msicraft.cropcontrol.aCommon.Data.Crop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class CropUtil {

    public static boolean isCorrectCrop(Crop crop) {
        Location location = crop.getLocation();
        Block block = location.getBlock();
        return block.getType() == crop.getBlock().getType();
    }

    public static boolean hasNearbyWater(Location location, int radius) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();
        for (int locX = (x - radius); locX < (x + radius); locX++) {
            for (int locY = (y - radius); locY < (y + radius); locY++) {
                for (int locZ = (z - radius); locZ < (z + radius); locZ++) {
                    Location waterLoc = new Location(location.getWorld(), locX, locY, locZ);
                    if (waterLoc.getBlock().getType() == Material.WATER) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
