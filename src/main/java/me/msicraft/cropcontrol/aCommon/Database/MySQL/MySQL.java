package me.msicraft.cropcontrol.aCommon.Database.MySQL;

import me.msicraft.cropcontrol.aCommon.Database.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL extends Database {

    private Connection connection;
    private final String url;

    public MySQL() {
        this.url = "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDatabaseName() + "?useSSL=false";
        connection = getConnection();
        initializeDatabase();
    }

    @Override
    public Connection getConnection() {
        if (connection != null) {
            return this.connection;
        }
        try {
            this.connection = DriverManager.getConnection(this.url, getUsername(), getPassword());
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
            String sql = "CREATE TABLE IF NOT EXISTS crop_data(LocationS String, OwnerUUID UUID, RequiredGrowthTime int, LeftGrowthTime int, CurrentAge int, GrowthRate double)";
            statement.execute(sql);
            statement.close();
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
