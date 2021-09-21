package Database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBController {
    Connection conn = null;
    Logger logger;

    public DBController() {
        logger = LoggerFactory.getLogger(DBController.class);
        String url = "jdbc:sqlite:tsBot.db";
        try{
            conn = DriverManager.getConnection(url);
            logger.debug("Connection to sqllite success");
        } catch (SQLException throwables) {
            logger.debug("Connection to sqllite failed");
            logger.debug("Services or functions which depend on a database to be present may not be function.");
            throwables.printStackTrace();
        }
    }


}
