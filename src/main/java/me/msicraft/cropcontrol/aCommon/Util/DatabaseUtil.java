package me.msicraft.cropcontrol.aCommon.Util;

import me.msicraft.cropcontrol.CropControl;
import me.msicraft.cropcontrol.aCommon.Database.Database;
import me.msicraft.cropcontrol.aCommon.Database.Flat.FlatDataUtil;
import me.msicraft.cropcontrol.aCommon.Database.MySQL.MySQL;
import me.msicraft.cropcontrol.aCommon.Database.SQLite.SQLite;
import org.bukkit.Bukkit;

public class DatabaseUtil {

    private static Database database = null;

    private enum DatabaseType {
        FLAT, SQLITE, MYSQL
    }

    public static void loadDatabase() {
        DatabaseType databaseType = DatabaseType.FLAT; //getDatabaseType();
        switch (databaseType) {
            case FLAT:
                FlatDataUtil.loadAllData();
                break;
            case MYSQL:
                database = new MySQL();
                break;
            case SQLITE:
                database = new SQLite();
                break;
        }
        //Bukkit.getConsoleSender().sendMessage(CropControl.getPrefix() + " Connect database: " + databaseType.name().toUpperCase());
    }

    public static DatabaseType getDatabaseType() {
        if (CropControl.getPlugin().getConfig().contains("Database.Type")) {
            String s = CropControl.getPlugin().getConfig().getString("Database.Type");
            if (s != null) {
                try {
                    return DatabaseType.valueOf(s.toUpperCase());
                } catch (IllegalArgumentException ignored) {
                }
            }
        }
        return DatabaseType.FLAT;
    }

    public static String getPrefix() {
        if (CropControl.getPlugin().getConfig().contains("Database.Prefix")) {
            String s = CropControl.getPlugin().getConfig().getString("Database.Prefix");
            if (s != null) {
                return s;
            }
        }
        return "CropControl";
    }

    public static String getHost() {
        if (CropControl.getPlugin().getConfig().contains("Database.Host")) {
            String s = CropControl.getPlugin().getConfig().getString("Database.Host");
            if (s != null) {
                return s;
            }
        }
        return "localhost";
    }

    public static int getPort() {
        if (CropControl.getPlugin().getConfig().contains("Database.Port")) {
            return CropControl.getPlugin().getConfig().getInt("Database.Port");
        }
        return 3306;
    }

    public static String getDatabaseName() {
        if (CropControl.getPlugin().getConfig().contains("Database.Database")) {
            String s = CropControl.getPlugin().getConfig().getString("Database.Database");
            if (s != null) {
                return s;
            }
        }
        return "CropControl";
    }

    public static String getUserName() {
        if (CropControl.getPlugin().getConfig().contains("Database.Username")) {
            String s = CropControl.getPlugin().getConfig().getString("Database.Username");
            if (s != null) {
                return s;
            }
        }
        return "username";
    }

    public static String getPassword() {
        if (CropControl.getPlugin().getConfig().contains("Database.Password")) {
            String s = CropControl.getPlugin().getConfig().getString("Database.Password");
            if (s != null) {
                return s;
            }
        }
        return "password";
    }

}
