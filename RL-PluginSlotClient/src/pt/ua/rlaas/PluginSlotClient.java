package pt.ua.rlaas;

import javax.xml.ws.BindingProvider;
import pt.ua.pluginslot.*;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class PluginSlotClient {

    private PluginSlot_Service svc;
    private PluginSlot pluginSlot;

    public PluginSlotClient(String location) {

        svc = new PluginSlot_Service();
        pluginSlot = svc.getPluginSlotPort();

        ((BindingProvider) pluginSlot).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                location);
    }

    public String push(String workDir, CacheData data) {
        return pluginSlot.push(workDir, data);
    }

    public String start(String pluginName) {
        return pluginSlot.start(pluginName);
    }

    public String addTask(PluginSlotTask task) {
        return pluginSlot.addTask(task);
    }

    public String uploadPluginSet(PluginSet pluginSet) throws FileNotFoundException_Exception, IOException_Exception {
        return pluginSlot.uploadPluginSet(pluginSet);
    }
}
