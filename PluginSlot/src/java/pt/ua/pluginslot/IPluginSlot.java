package pt.ua.pluginslot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import pt.ua.pluginslot.rim.CacheData;
import pt.ua.pluginslot.rim.PluginSet;
import pt.ua.pluginslot.rim.PluginSlotTask;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public interface IPluginSlot {

    String UploadPluginSet(PluginSet pluginSet) throws FileNotFoundException, IOException;

    String addTask(PluginSlotTask task);

    URL push(String workDirPath, CacheData data);

    String start(String pluginName);

    String stop();
}
