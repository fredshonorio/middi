package pt.ua.rlaas.plugin;

import java.util.HashMap;

public interface TaskPlugin {

    public void process(HashMap<String, String> settings);

    public void init(HashMap<String, String> settings, HashMap<String, Object> context);

    public void destroy();
}
