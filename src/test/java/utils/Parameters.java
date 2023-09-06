package utils;

import java.util.Map;

public class Parameters {
    private final Map<String, Object> properties;
    public Parameters(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void set(String key, Object value) {
        this.properties.put(key, value);
    }

    public Boolean isSet(String key) {
        return properties.containsKey(key);
    }

    public Object get(String key) {
        return this.properties.getOrDefault(key, null);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(this.get(key));
    }
}
