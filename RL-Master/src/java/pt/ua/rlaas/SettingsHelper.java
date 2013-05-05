package pt.ua.rlaas;

import java.util.HashMap;
import java.util.Map;
import pt.ua.pluginslot.PluginSlotTask;

/**
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class SettingsHelper {

    public HashMap<String, String> map = new HashMap<String, String>();

    public void put(String key, String value) {
        map.put(key, value);
    }

    public void append(HashMap<String, String> appendMap) {
        for (String key : appendMap.keySet()){
            assert !map.containsKey(key);
            map.put(key, appendMap.get(key));
        }
    }

    public PluginSlotTask.Settings getSettings() {
        PluginSlotTask.Settings sett = new PluginSlotTask.Settings();

        for (Map.Entry<String, String> e : map.entrySet()) {
            PluginSlotTask.Settings.Entry entry = new PluginSlotTask.Settings.Entry();
            entry.setKey(e.getKey());
            entry.setValue(e.getValue());
            sett.getEntry().add(entry);
        }

        return sett;
    }
}
