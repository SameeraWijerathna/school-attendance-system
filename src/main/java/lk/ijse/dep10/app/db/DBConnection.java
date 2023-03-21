package lk.ijse.dep10.app.db;

import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.AbstractList;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private final Connection connection;

    private DBConnection() {
        File ConfigFile = new File("application.properties");
        Properties configurations = new Properties();
        try {
            FileReader fr = new FileReader(ConfigFile);
            configurations.load(fr);
            fr.close();

            String host = configurations.getProperty("dep10.sas.db.host", "localhost");
            String port = configurations.getProperty("dep10.sas.db.port", "3306");
            String database = configurations.getProperty("dep10.sas.db.database", "dep10_student_attendance");
            String username = configurations.getProperty("dep10.sas.db.username", "root");
            String password = configurations.getProperty("dep10.sas.db.password", "");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?createDatabaseIfNotExist=true&allowMultiQueries=true";

            connection = DriverManager.getConnection(url, username, password);
        } catch (FileNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Configuration file doesn't exists").showAndWait();
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to read configurations").showAndWait();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,
                    "Failed to establish the database connection, try again. If the problem persist please contact the technical team").showAndWait();
            throw new RuntimeException(e);
        }
    }
    public static DBConnection getInstance() {
        return (dbConnection == null)? dbConnection = new DBConnection() : dbConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
