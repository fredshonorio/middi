package pt.ua.rlaas.tasks;

import pt.ua.pluginslot.PluginSet;
import pt.ua.pluginslot.PluginSlotTask;
import pt.ua.rlaas.PluginManager;
import pt.ua.rlaas.PluginSlotClient;
import pt.ua.rlaas.SettingsHelper;
import pt.ua.rlaas.util.Constants;
import pt.ua.rlaas.util.Util;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class CompareTask extends Task {

    private PluginSlotTask t = new PluginSlotTask();
    private SettingsHelper sh = new SettingsHelper();
    private CompareConfig config;

    public CompareTask(CompareConfig config, String recordSetA, String recordSetB, String domain) {
        super(domain);

        this.config = config;

        sh.put(Constants.CompareTask.PLUGINNAMES_FIELD, Util.concat(";", config.getPlugins()));
        sh.put(Constants.CompareTask.WEIGHTS_FIELD, Util.concat(";", Util.toStringArray(config.getWeights())));
        sh.put(Constants.CompareTask.RECORDSA_FIELD, recordSetA);
        sh.put(Constants.CompareTask.RECORDSB_FIELD, recordSetB);
        sh.put(Constants.CompareTask.THRESHOLDLOW_FIELD, Double.toString(config.getThresholdLow()));
        sh.put(Constants.CompareTask.THRESHOLDHIGH_FIELD, Double.toString(config.getThresholdHigh()));
    }

    @Override
    public PluginSet getPluginSet() {
        PluginManager ph = PluginManager.instance();
        PluginSet ps = super.getPluginSet();

        for (String pluginName : config.getPlugins()) {
            ps.getPlugin().add(ph.getPlugin(pluginName));
        }

        return ps;
    }

    @Override
    public PluginSlotTask getPluginSlotTask(int command) {
        PluginSlotTask t = new PluginSlotTask();
        t.setDomain(domain);
        t.setCommand(command);
        t.setSettings(sh.getSettings());

        return t;
    }

    @Override
    public void send(PluginSlotClient client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
