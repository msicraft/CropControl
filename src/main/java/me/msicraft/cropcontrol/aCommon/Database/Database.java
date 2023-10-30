package me.msicraft.cropcontrol.aCommon.Database;

import me.msicraft.cropcontrol.aCommon.Util.DatabaseUtil;

import java.sql.Connection;

public abstract class Database {

    private final String host = DatabaseUtil.getHost();
    private final int port = DatabaseUtil.getPort();
    private final String databaseName = DatabaseUtil.getDatabaseName();
    private final String username = DatabaseUtil.getUserName();
    private final String password = DatabaseUtil.getPassword();
    private final String prefix = DatabaseUtil.getPrefix();

    public abstract Connection getConnection();

    public abstract void initializeDatabase();

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getPrefix() {
        return prefix;
    }
}
