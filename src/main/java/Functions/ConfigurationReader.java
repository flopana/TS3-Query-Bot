package Functions;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class ConfigurationReader {
    public <T> T getConfig(Class<T> classOfT, String path){
        Gson gson = new Gson();
        String configJson = "";
        try {
            configJson = Files.readString(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        T toReturn = gson.fromJson(configJson, classOfT);
        return toReturn;
    }
}
