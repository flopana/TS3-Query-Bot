package Database;

import Functions.AlgorandAsa;
import UserManagement.User;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DB {
    private static final DB db = new DB();
    private final Logger logger = LoggerFactory.getLogger(DB.class);
    private TS3Api ts3Api;

    private DB() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:tsBot.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                create table if not exists users (
                    dbId integer primary key,
                    uuid text,
                    nickname text,
                    algorandWalletAddr string,
                    timeOnlineSum long,
                    timeOnlineSumWithoutAfk long,
                    alreadyReceivedAsa long
                )
            """);
        } catch (SQLException e) {
            logger.error("Error while creating table", e);
        }
    }

    public static DB getInstance() {
        return db;
    }

    public static void setTs3Api(TS3Api ts3Api) {
        db.ts3Api = ts3Api;
        db.startAfkCheckThread();
    }

    public double getOutstandingASA(User user) {
        double alreadyReceivedAsa = 0;
        long timeOnlineSumWithoutAfk = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:tsBot.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select alreadyReceivedAsa, timeOnlineSumWithoutAfk from users where dbId = " + user.getDbId());
            if (resultSet.next()) {
                alreadyReceivedAsa = resultSet.getDouble("alreadyReceivedAsa");
                timeOnlineSumWithoutAfk = resultSet.getLong("timeOnlineSumWithoutAfk");
            }
        } catch (SQLException e) {
            logger.error("Error while querying for alreadyReceivedAsa, timeOnlineWithoutAfk", e);
        }

        return (timeOnlineSumWithoutAfk/60.0/60.0 * AlgorandAsa.getConfig().getAmountToEarnPerHour()) - alreadyReceivedAsa;
    }

    /**
     * Adds a user to the database if he is not already in there.
     *
     * @param user The user to add.
     */
    public void addUserIfNotExists(User user) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:tsBot.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("""
                        insert or ignore into users (
                            dbId,
                            uuid,
                            nickname,
                            algorandWalletAddr,
                            timeOnlineSum,
                            timeOnlineSumWithoutAfk,
                            alreadyReceivedAsa
                        ) values (
                            %d,
                            '%s',
                            '%s',
                            '%s',
                            %d,
                            %d,
                            %d
                        )
                    """, user.getDbId(), user.getUniqueId(), user.getNickname(), null, 0, 0, 0));
            statement.executeUpdate(String.format("""
                        update users set
                            nickname = '%s'
                        where dbId = %d
                    """, user.getNickname(), user.getDbId()));
        } catch (SQLException e) {
            logger.error("Error while inserting a new user", e);
            logger.error("User: {}", user);
        }
    }

    /**
     * This function updates the total time a user was online in milliseconds
     *
     * @param user The user to update.
     */
    public void updateTimeOnlineSum(User user) {
        long timeOnline = user.getTimeStayed();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:tsBot.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("""
                        update users
                        set
                            timeOnlineSum = timeOnlineSum + %d
                        where dbId = %d
                    """, timeOnline, user.getDbId()));
        } catch (SQLException e) {
            logger.error("Error while updating time online sum", e);
        }
    }

    /**
     * This function starts a daemon thread that updates the time a user was online without being afk in seconds
     */
    private void startAfkCheckThread(){
        Thread thread = new Thread(() -> {
            while (true){
                try {
                    Connection connection = DriverManager.getConnection("jdbc:sqlite:tsBot.db");
                    for(Client client : ts3Api.getClients()) {
                        // If client is afk for less than 30 minutes, don't count it
                        if (client.getIdleTime() < 30 * 60 * 1000) {
                            Statement statement = connection.createStatement();
                            statement.executeUpdate(String.format("""
                                        update users
                                        set
                                            timeOnlineSumWithoutAfk = timeOnlineSumWithoutAfk + %d
                                        where dbId = %d
                                    """, 5, client.getDatabaseId())); // Add 5 seconds to the time online sum because the loop pauses for 5 seconds
                        }
                    }
                } catch (SQLException e) {
                    logger.error("Error while updating time online sum without afk", e);
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        logger.info("Started afk check for Database stats");
    }

    public Long[] getOnlineTimeForUser(User user){
        Long[] times = new Long[2];

        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:tsBot.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format("""
                        select timeOnlineSum, timeOnlineSumWithoutAfk
                        from users
                        where dbId = %d
                    """, user.getDbId()));
            while (resultSet.next()) {
                times[0] = resultSet.getLong("timeOnlineSum");
                times[1] = resultSet.getLong("timeOnlineSumWithoutAfk");
            }
        } catch (SQLException e) {
            logger.error("Error while getting online time for user", e);
        }

        return times;
    }

    public boolean registerAlgorandWallet(User user, String wallet) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:tsBot.db");
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("""
                    update users
                    set
                        algorandWalletAddr = '%s'
                    where dbId = %d
                """, wallet, user.getDbId()));
            return true;
        } catch (SQLException e) {
            logger.error("Error while registering algorand wallet", e);
            return false;
        }
    }
}
