package fr.remiguitreau.aroeven.lucine.api;

import java.util.HashMap;
import java.util.Map;

public class AroevenStay {

    private final Map<String, String> properties = new HashMap<String, String>();

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getProperty(final String code) {
        return properties.get(code);
    }

    public void addProperty(final String code, final String value) {
        if (!properties.containsKey(code)) {
            properties.put(code, value);
        }
    }
}
