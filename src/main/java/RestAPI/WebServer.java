package RestAPI;

import RestAPI.Controller.ChannelController;
import RestAPI.Controller.UserController;
import com.github.theholywaffle.teamspeak3.TS3Api;

import static spark.Spark.*;

public class WebServer {
    static TS3Api ts3Api;

    public static void startWebserver(TS3Api ts3Api) {
        setTs3Api(ts3Api);

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
        get("/", ((request, response) -> {
            return "TS3-Query-Bot API";
        }));

        path("/api", () -> {
            get("/users", UserController.getUsers);
            get("/user/:clientId", UserController.getUser);

            get("/channels", ChannelController.getChannels);
        });

    }

    public static TS3Api getTs3Api() {
        return ts3Api;
    }

    public static void setTs3Api(TS3Api ts3Api) {
        WebServer.ts3Api = ts3Api;
    }
}
