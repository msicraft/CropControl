package me.msicraft.cropcontrol.aCommon.Database.SQLite;

import me.msicraft.cropcontrol.CropControl;
import me.msicraft.cropcontrol.aCommon.Database.Database;
import me.msicraft.cropcontrol.aCommon.Util.DatabaseUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite extends Database {

    private Connection connection = null;

    private final String databaseName;

    public SQLite() {
        this.databaseName = DatabaseUtil.getDatabaseName();
        this.connection = getConnection();
        initializeDatabase();
    }

    @Override
    public Connection getConnection() {
        File dbFile = new File(CropControl.getPlugin().getDataFolder(), this.databaseName + ".db");
        if (!dbFile.exists()) {
            try {
                dbFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        try {
            if(this.connection != null && !this.connection.isClosed()) {
                return this.connection;
            }
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            return this.connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initializeDatabase() {
        if (this.connection == null) {
            return;
        }
        try {
            Statement statement = this.connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS crop_data (" +
                    "LocationS TEXT NOT NULL, " +
                    "OwnerUUID TEXT NOT NULL, " +
                    "RequiredGrowthTime INTEGER NOT NULL DEFAULT 300, " +
                    "LeftGrowthTime INTEGER NOT NULL DEFAULT 300, " +
                    "CurrentAge INTEGER NOT NULL DEFAULT 1, " +
                    "GrowthRate REAL NOT NULL DEFAULT 0.0)");
            statement.close();
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
