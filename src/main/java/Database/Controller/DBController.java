package Database.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBController {
    Connection conn = null;
    Logger logger;

    public DBController() {
        logger = LoggerFactory.getLogger(DBController.class);
        String url = "jdbc:sqlite:tsBot.db";
        String initDB = """
                create table if not exists users
                (
                tsID text not null primary key,
                name text not null,
                userOnlineSeconds not null
                );
                """;

        try{
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(initDB);
            logger.debug("Connection and init for sqllite success");
        } catch (SQLException throwables) {
            logger.debug("Connection to sqllite failed");
            logger.debug("Services or functions which depend on a database to be present may not be function.");
            throwables.printStackTrace();
        }

    }

    public Connection getConn() {
        return conn;
    }
}
