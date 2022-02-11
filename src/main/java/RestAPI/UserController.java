package RestAPI;

import UserManagement.User;
import UserManagement.UserManager;
import com.google.gson.JsonArray;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserController {

    static Route getUsers = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            response.type("application/json");
            UserManager userManager = UserManager.getInstance();
            JsonArray userArray = new JsonArray();

            for (User user : userManager.getAllUsers()) {
                userArray.add(user.toJsonObject());
            }

            return userArray.toString();
        }
    };

    static Route getUser = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            response.type("application/json");
            UserManager userManager = UserManager.getInstance();

            User user = userManager.getUser(Integer.parseInt(request.params(":clientId")));

            return user.toJsonObject();
        }
    };
}
