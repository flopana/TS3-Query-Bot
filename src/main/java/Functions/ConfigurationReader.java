package Functions;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class ConfigurationReader {
    public <T> T getConfig(Class<T> classOfT, String path){
        Logger logger = LoggerFactory.getLogger(ConfigurationReader.class);
        logger.info("Reading config for class: " + classOfT.getName() + " and path: " + path);
        Gson gson = new Gson();
        String configJson = "";
        try {
            configJson = Files.readString(Paths.get("configs/"+path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        T toReturn = gson.fromJson(configJson, classOfT);
        return toReturn;
    }
}
