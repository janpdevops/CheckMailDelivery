package de.petranek.checkMailDelivery.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

public class PropertyResolver {

    public Properties getProperties() {
        return properties;
    }

    Properties properties ;

    public PropertyResolver (String propertyFileName) throws Exception {

        try {
            File f = new File(propertyFileName);

            FileReader fileReader = new FileReader(f);
            this.properties = new Properties();
            this.properties.load(fileReader);
        } catch (Exception e) {
            throw new Exception("Failed to read property file " + propertyFileName);
        }
    }

    public String resolveProperty(String key) throws Exception {
        if (properties == null ) {
            throw new Exception("No properties");

        }
        String value = (String) this.properties.get(key);
        if (StringUtils.isEmpty(value))  {
            throw new Exception("No value found for " + key);
        }
        return value;
    }


}
