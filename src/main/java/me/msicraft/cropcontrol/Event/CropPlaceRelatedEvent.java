package me.msicraft.cropcontrol.Event;

import me.msicraft.cropcontrol.CropControl;
import me.msicraft.cropcontrol.aCommon.Data.Crop;
import me.msicraft.cropcontrol.aCommon.Util.CropRegisterUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class CropPlaceRelatedEvent implements Listener {

    private static boolean disabledBoneMeal = false;
    private static boolean onlyBreakOwner = false;

    public static void reloadVariables() {
        disabledBoneMeal = CropControl.getPlugin().getConfig().contains("Setting.Disable-BoneMeal") && CropControl.getPlugin().getConfig().getBoolean("Setting.Disable-BoneMeal");
        onlyBreakOwner = CropControl.getPlugin().getConfig().contains("Setting.OnlyBreakOwner") && CropControl.getPlugin().getConfig().getBoolean("Setting.OnlyBreakOwner");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void disableBoneMeal(PlayerInteractEvent e) {
        if (disabledBoneMeal) {
            Action action = e.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK && e.getItem() != null && e.getItem().getType() == Material.BONE_MEAL) {
                e.setCancelled(true);
            }
        }
    }

    private static final List<Material> availableCropMaterials = Arrays.asList(Material.WHEAT, Material.POTATOES, Material.CARROTS);
    public static List<Material> getAvailableCropMaterialList() {
        return availableCropMaterials;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void placeCrop(BlockPlaceEvent e) {
        Block placeBlock = e.getBlockPlaced();
        if (availableCropMaterials.contains(placeBlock.getType())) {
            if (CropControl.getPlugin().getConfig().getBoolean("Crop." + placeBlock.getType().toString().toUpperCase() + ".Enabled")) {
                Location location = placeBlock.getLocation();
                if (!CropRegisterUtil.isExistCrop(location)) {
                    long time = System.currentTimeMillis();
                    int growthTime = CropControl.getPlugin().getConfig().getInt("Crop." + placeBlock.getType().toString().toUpperCase() + ".GrowthTime");
                    Crop crop = new Crop(location, e.getPlayer().getUniqueId(), time, growthTime);
                    crop.setFullGrowthTime(time + (growthTime * 1000L));
                    crop.setPlaceTime(time);
                    CropRegisterUtil.registerCrop(location, crop);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void breakCrop(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Location location = e.getBlock().getLocation();
        if (onlyBreakOwner) {
            Crop crop = CropRegisterUtil.getCrop(location);
            if (crop != null) {
                if (!crop.getOwnerUUID().equals(player.getUniqueId())) {
                    e.setCancelled(true);
                } else {
                    CropRegisterUtil.removeCrop(location);
                }
            }
        } else {
            if (CropRegisterUtil.isExistCrop(location)) {
                CropRegisterUtil.removeCrop(location);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void disableNaturalGrow(BlockGrowEvent e) {
        Block block = e.getBlock();
        if (availableCropMaterials.contains(block.getType()) && CropRegisterUtil.isExistCrop(block.getLocation())) {
            e.setCancelled(true);
        }
    }

}
