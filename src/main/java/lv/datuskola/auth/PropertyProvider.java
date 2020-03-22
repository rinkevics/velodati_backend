package lv.datuskola.auth;

import java.util.Properties;

public class PropertyProvider {

    private Properties properties;
    
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public String get(String propertyName) {
        return (String) properties.get(propertyName);
    }
}
