package pt.ua.rlaas.tasks;

import pt.ua.pluginslot.Plugin;
import pt.ua.pluginslot.PluginSet;
import pt.ua.pluginslot.PluginSlotTask;
import pt.ua.rlaas.PluginHelper;
import pt.ua.rlaas.SettingsHelper;
import pt.ua.rlaas.util.Constants;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class TransformTask {

//    public final static String PREFIX = "task.plugin.transform";
//    public final static String METAPLUGIN = "pt.ua.rlaas.metaplugin.TransformMetaPlugin";
//    public final static String RECORDSIN_FIELD = PREFIX + ".recordSetIn";
//    public final static String RECORDSOUT_FIELD = PREFIX + ".recordSetOut";
//    public final static String FIRSTRECORD_FIELD = PREFIX + ".firstRecord";
//    public final static String LASTRECORD_FIELD = PREFIX + ".lastRecord";
//    public final static String PLUGINNAME_FIELD = PREFIX + ".pluginName";
    public static String COMMON_PATH = "C:\\Users\\PC\\code\\dissertation\\new\\RL-Common\\dist\\RL-Common.jar";
    public static String COMMON_NAME = "rl.common";
    public static String MASTERPLUGIN_PATH = "C:\\Users\\PC\\code\\dissertation\\new\\RL-MasterPlugins\\dist\\RL-MasterPlugins.jar";
    public static String MASTERPLUGIN_NAME = "rl.master";
    public static String REPOCLIENT_PATH = "C:\\Users\\PC\\code\\dissertation\\new\\RL-RepositoryClient\\dist\\RL-RepositoryClient.jar";
    public static String REPOCLIENT_NAME = "rl.repoclient";
    private String recordSetIn;
    private String recordSetOut;
    private int firstRecord;
    private int lastRecord;
    private String pluginName;
    private Plugin plugin;
    private String domain;
    private String[] fields = null;

    public TransformTask(String recordSetIn, String recordSetOut, int firstRecord, int lastRecord, String pluginName, Plugin plugin, String domain) {
        this.recordSetIn = recordSetIn;
        this.recordSetOut = recordSetOut;
        this.firstRecord = firstRecord;
        this.lastRecord = lastRecord;
        this.pluginName = pluginName;
        this.plugin = plugin;
        this.domain = domain;
    }

    public void setFields(String... fields) {
        this.fields = fields;
    }

    public PluginSet getPluginSet() {
        PluginHelper ph = new PluginHelper();

        PluginSet ps = new PluginSet();
        ps.getPlugin().add(ph.newPlugin(COMMON_PATH, COMMON_NAME));
        ps.getPlugin().add(ph.newPlugin(MASTERPLUGIN_PATH, MASTERPLUGIN_NAME));
        ps.getPlugin().add(ph.newPlugin(REPOCLIENT_PATH, REPOCLIENT_NAME));
        ps.getPlugin().add(plugin);

        return ps;
    }

    public PluginSlotTask getPluginSlotTask(int command) {
        PluginSlotTask t = new PluginSlotTask();

        t.setDomain(domain);
        t.setCommand(command);
        SettingsHelper sh = new SettingsHelper();
        sh.put(Constants.TransformTask.RECORDSIN_FIELD, recordSetIn);
        sh.put(Constants.TransformTask.RECORDSOUT_FIELD, recordSetOut);
        sh.put(Constants.TransformTask.FIRSTRECORD_FIELD, Integer.toString(firstRecord));
        sh.put(Constants.TransformTask.LASTRECORD_FIELD, Integer.toString(lastRecord));
        sh.put(Constants.TransformTask.PLUGINNAME_FIELD, pluginName);
        if (fields != null) {
            StringBuilder b = new StringBuilder();
            for (int i = 0; i < fields.length; i++) {
                b.append(fields[i]);
                if (i != fields.length - 1) {
                    b.append(';');
                }
            }
            sh.put(Constants.TransformTask.FIELDS_FIELD, b.toString());
        }
        t.setSettings(sh.getSettings());

        return t;
    }
}
