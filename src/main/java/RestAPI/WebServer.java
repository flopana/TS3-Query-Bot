package RestAPI;

import RestAPI.Controller.ChannelController;
import RestAPI.Controller.UserController;
import RestAPI.Middleware.APIKeyMiddleware;
import com.github.theholywaffle.teamspeak3.TS3Api;

import static spark.Spark.*;

public class WebServer {
    static TS3Api ts3Api;
    static String apiKey;

    public static void startWebserver(TS3Api ts3Api, String apiKey) {
        setTs3Api(ts3Api);
        setApiKey(apiKey);


        get("/", ((request, response) -> {
            return "TS3-Query-Bot API";
        }));

        path("/api", () -> {
            before((request, response) -> response.type("application/json"));
            before("/*" ,APIKeyMiddleware::checkApiKey);
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

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String apiKey) {
        WebServer.apiKey = apiKey;
    }
}
