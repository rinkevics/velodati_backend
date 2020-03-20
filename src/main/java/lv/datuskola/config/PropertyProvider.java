package lv.datuskola.config;

import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class PropertyProvider {

    private final Properties properties;

    public PropertyProvider(Properties prop) {
        this.properties = prop;
    }

    public Properties getProperties() {
        return properties;
    }
}
