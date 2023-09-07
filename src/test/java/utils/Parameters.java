package utils;

import java.util.Map;

public class Parameters {
    private final Map<Options, Object> properties;
    public Parameters(Map<Options, Object> properties) {
        this.properties = properties;
    }

    public void set(Options key, Object value) {
        this.properties.put(key, value);
    }

    public Boolean isSet(Options key) {
        return properties.containsKey(key);
    }

    public Object get(Options key) {
        return this.properties.getOrDefault(key, null);
    }

    public <T> T get(Options key, Class<T> type) {
        return type.cast(this.get(key));
    }
}
