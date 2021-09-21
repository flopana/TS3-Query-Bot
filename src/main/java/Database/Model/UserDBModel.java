package Database.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDBModel {
    private String tsID;
    private final String name;

    public long getUserOnlineSeconds() {
        return userOnlineSeconds;
    }

    private long userOnlineSeconds;

    private final Connection conn;
    private final Logger logger;

    public UserDBModel(String tsID, String name, Connection conn) {
        this.tsID = tsID;
        this.name = name;
        this.conn = conn;
        this.logger = LoggerFactory.getLogger(UserDBModel.class);
        try {
            CheckIfUserExistsElseCreate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void UpdateTimeOnline(long n) {
        userOnlineSeconds += (n / 1000);
        UpdateUserInDB();
    }

    public void CheckIfUserExistsElseCreate() throws SQLException {
        String sql = "SELECT tsID, userOnlineSeconds from users where tsID = '" + tsID + "';";
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()){
                logger.debug("User " + name + " diddnt exist, creating...");
                CreateUser();
                return;
            }
            logger.debug("User " + name + " exists, updating data.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (resultSet == null) return;

        this.tsID = resultSet.getString("tsID");
        this.userOnlineSeconds = resultSet.getLong("userOnlineSeconds");

    }

    public void UpdateUserInDB() {
        String sql = "UPDATE users SET userOnlineSeconds = ?, name = ? WHERE tsID = ?";
        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setLong(1, userOnlineSeconds);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, tsID);
            preparedStatement.executeUpdate();
            logger.debug("User " + name + " with UUID " + tsID + " was Updated to DB");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void CreateUser() throws SQLException {
        String sql = "Insert into users (tsID, userOnlineSeconds, name) values(?, ?, ?)";

        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            preparedStatement.setString(1, tsID);
            preparedStatement.setLong(2, 0);
            preparedStatement.setString(3, name);
            preparedStatement.executeUpdate();
            logger.debug("User " + name + " with UUID " + tsID + " was added to DB");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public long GetWastedTime(){
        String sql = "SELECT userOnlineSeconds from users where tsID = '" + tsID + "';";

        try {
            PreparedStatement preparedStatement = this.conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            logger.debug("User " + name + " with UUID " + tsID + " time left updated");
            resultSet.next();
            return resultSet.getLong("userOnlineSeconds");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

}
