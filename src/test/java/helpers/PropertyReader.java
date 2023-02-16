package helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertyReader {
    private final String filePath;

    public PropertyReader() {
        this.filePath = "./src/test/resources/Properties/execution.properties";;
    }

    /**
     * This method is used to get the value of property key from the execution properties file
     * @param key : the key name of the property you want to get its value
     * @return the value of the desired property
     */
    public String getPropertyValue(String key)  {
        Properties prop = new Properties();
        try {
            FileInputStream reader = new FileInputStream(filePath);
            prop.load(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop.getProperty("rootURL");
    }

}
