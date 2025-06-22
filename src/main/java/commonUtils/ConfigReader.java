package commonUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static Properties properties = new Properties();
	  static String projectPath = System.getProperty("user.dir");
      
      static String configPath = projectPath + "/Resources/Config.properties";

    public ConfigReader() {

	        
        try (FileInputStream fis = new FileInputStream(configPath)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties file.");
        }
    }

    public String getBrowser(String browser) {
        return properties.getProperty(browser); 
    }
    public static void setProperty(String key, String value) {
        try (FileInputStream input = new FileInputStream(configPath)) {
            properties.load(input);  
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileOutputStream output = new FileOutputStream(configPath)) {
            properties.setProperty(key, value);
            properties.store(output, null);
            System.out.println("Property '" + key + "' updated with value: " + value);
        } catch (IOException e) {
            e.printStackTrace();
        }
}
    
    public static String readProperty(String property) {
        return properties.getProperty(property);
    }
}