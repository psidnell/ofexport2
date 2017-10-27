package org.psidnell.omnifocus.sqlite;

import org.psidnell.omnifocus.model.NodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.sqlite.SQLiteConfig;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataSource extends AbstractDataSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteDataSource.class);

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private File[] possibleDBLocations;
    private boolean readOnly = true;

    @Override
    public Connection getConnection() throws SQLException {
        final SQLiteConfig config = new SQLiteConfig();
        config.setReadOnly(this.readOnly);
        return DriverManager.getConnection(getDriverURL(), config.toProperties());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    private String getDriverURL() throws SQLException {
        for (final File file : possibleDBLocations) {
            LOGGER.info("Checking database file: {}", file);
            if (file.exists() && file.isFile()) {
                LOGGER.info("Found database file: {}", file);

                return "jdbc:sqlite:" + file.getPath();
            }
        }
        throw new SQLException("Unable to find the OmniFocus SQLite database in any configured locations");
    }


    public void setPossibleDBLocations(final File[] possibleDBLocations) {
        this.possibleDBLocations = possibleDBLocations;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
