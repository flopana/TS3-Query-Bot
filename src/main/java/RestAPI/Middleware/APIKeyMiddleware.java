package RestAPI.Middleware;

import RestAPI.WebServer;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;
import spark.Spark;

import static spark.Spark.halt;

public class APIKeyMiddleware {
    public static void checkApiKey(Request request, Response response) {
        String apiKey = request.headers("apiKey");
        JsonObject obj = new JsonObject();

        response.header("Access-Control-Allow-Origin", "*");
        response.header("Access-Control-Allow-Headers", "*");

        if (request.requestMethod().equals("OPTIONS")){
            halt(200, "OK");
        }

        if (apiKey == null){
            obj.addProperty("message","No API-Key provided!");
            halt(400, obj.toString());
        }

        if (!apiKey.equals(WebServer.getApiKey())){
            obj.addProperty("message", "The provided API-Key is wrong!");
            halt(401, obj.toString());
        }

    }
}
