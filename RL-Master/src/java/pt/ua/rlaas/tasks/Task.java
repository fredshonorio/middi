package pt.ua.rlaas.tasks;

import pt.ua.pluginslot.PluginSet;
import pt.ua.pluginslot.PluginSlotTask;
import pt.ua.rlaas.PluginManager;
import pt.ua.rlaas.PluginSlotClient;
import pt.ua.rlaas.RepositoryClient;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public abstract class Task {

    public static String COMMON_PATH = "C:\\Users\\PC\\code\\dissertation\\new\\RL-Common\\dist\\RL-Common.jar";
    public static String COMMON_NAME = "rl.common";
    public static String MASTERPLUGIN_PATH = "C:\\Users\\PC\\code\\dissertation\\new\\RL-MasterPlugins\\dist\\RL-MasterPlugins.jar";
    public static String MASTERPLUGIN_NAME = "rl.master";
    public static String REPOCLIENT_PATH = "C:\\Users\\PC\\code\\dissertation\\new\\RL-RepositoryClient\\dist\\RL-RepositoryClient.jar";
    public static String REPOCLIENT_NAME = "rl.repoclient";
    public static String NOTIFICATION_NAME = "rl.notification";
    protected String domain;

    public Task(String domain) {
        this.domain = domain;
    }

    public PluginSet getPluginSet() {
        PluginManager ph = PluginManager.instance();

        PluginSet ps = new PluginSet();
        ps.getPlugin().add(ph.getPlugin(COMMON_NAME));
        ps.getPlugin().add(ph.getPlugin(MASTERPLUGIN_NAME));
        ps.getPlugin().add(ph.getPlugin(REPOCLIENT_NAME));

        return ps;
    }

    public abstract PluginSlotTask getPluginSlotTask(int command);

    public abstract void send(PluginSlotClient client);
}
