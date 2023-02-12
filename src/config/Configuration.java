package src.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

public class Configuration {
    private Properties properties;
    private String path;

    public Configuration() {
        path = "src/config/configuration.txt";
        readFile();
    }

    public void readFile() {
        properties = new Properties();

        try (FileInputStream fis = new FileInputStream(path)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(Keys key) {
        return properties.getProperty(key.getTitle(), "1");
    }

    public void setProperty(Keys key, String value) {
        properties.setProperty(key.getTitle(), value);
    }

    public void save() {
        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String name = field.getName();
                if (name != "path" && name != "properties") {
                    Object value = field.get(this);
                    properties.setProperty(name, value.toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        try (FileOutputStream fos = new FileOutputStream(path)) {
            properties.store(fos, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
