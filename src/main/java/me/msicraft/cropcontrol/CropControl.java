package me.msicraft.cropcontrol;

import me.msicraft.cropcontrol.Command.MainCommand;
import me.msicraft.cropcontrol.Command.MainTabComplete;
import me.msicraft.cropcontrol.Event.CropPlaceRelatedEvent;
import me.msicraft.cropcontrol.aCommon.Database.Flat.FlatDataFile;
import me.msicraft.cropcontrol.aCommon.Database.Flat.FlatDataUtil;
import me.msicraft.cropcontrol.aCommon.Task.CropTaskUtil;
import me.msicraft.cropcontrol.aCommon.Util.DatabaseUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class CropControl extends JavaPlugin {

    private static CropControl plugin;
    public static CropControl getPlugin() {
        return plugin;
    }
    public static String getPrefix() { return ChatColor.GREEN + "[CropControl]"; }

    public static FlatDataFile flatDataFile;

    @Override
    public void onEnable() {
        plugin = this;
        flatDataFile = new FlatDataFile(this);
        createConfigFile();
        final int configVersion = plugin.getConfig().contains("config-version", true) ? plugin.getConfig().getInt("config-version") : -1;
        if (configVersion != 1) {
            getServer().getConsoleSender().sendMessage(getPrefix() + ChatColor.RED + " You are using the old config");
            getServer().getConsoleSender().sendMessage(getPrefix() + ChatColor.RED + " Created the latest config.yml after replacing the old config.yml with config_old.yml");
            replaceConfig();
            createConfigFile();
        } else {
            getServer().getConsoleSender().sendMessage(getPrefix() + " You are using the latest version of config.yml");
        }
        configFilesReload();
        eventsRegister();
        commandsRegister();
        DatabaseUtil.loadDatabase();
        CropTaskUtil.startCropAsyncTask(this);
        getServer().getConsoleSender().sendMessage(getPrefix() + " Plugin Enabled");
    }

    @Override
    public void onDisable() {
        FlatDataUtil.saveAllData(true);
        getServer().getConsoleSender().sendMessage(getPrefix() + ChatColor.RED + " Plugin Disabled");
    }

    private void eventsRegister() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new CropPlaceRelatedEvent(), this);
    }

    private void commandsRegister() {
        PluginCommand pluginCommand = getServer().getPluginCommand("cropcontrol");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(new MainCommand());
            pluginCommand.setTabCompleter(new MainTabComplete());
        }
    }

    public void configFilesReload() {
        reloadConfig();
        CropPlaceRelatedEvent.reloadVariables();
        CropTaskUtil.reloadVariables();
    }

    protected FileConfiguration config;

    private void createConfigFile() {
        File configf = new File(getDataFolder(), "config.yml");
        if (!configf.exists()) {
            configf.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }
        FileConfiguration config = new YamlConfiguration();
        try {
            config.load(configf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void replaceConfig() {
        File file = new File(getDataFolder(), "config.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        File config_old = new File(getDataFolder(),"config_old-" + dateFormat.format(date) + ".yml");
        file.renameTo(config_old);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + getPrefix() + " Plugin replaced the old config.yml with config_old.yml and created a new config.yml");
    }

}
