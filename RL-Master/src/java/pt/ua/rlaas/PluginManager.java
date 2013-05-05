package pt.ua.rlaas;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import pt.ua.pluginslot.Plugin;
import pt.ua.pluginslot.PluginSet;
import pt.ua.rlaas.tasks.Task;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class PluginManager {

    public static String COMMON_PATH = "C:\\Users\\PC\\code\\dissertation\\new\\RL-Common\\dist\\RL-Common.jar";
    public static String COMMON_NAME = "rl.common";
    public static String MASTERPLUGIN_PATH = "C:\\Users\\PC\\code\\dissertation\\new\\RL-MasterPlugins\\dist\\RL-MasterPlugins.jar";
    public static String MASTERPLUGIN_NAME = "rl.master";
    public static String REPOCLIENT_PATH = "C:\\Users\\PC\\code\\dissertation\\new\\RL-RepositoryClient\\dist\\RL-RepositoryClient.jar";
    public static String REPOCLIENT_NAME = "rl.repoclient";
    public static String NOTIFICATION_NAME = "rl.notification";

    private class PluginInfo {

        public final String id;
        public final String path;

        public PluginInfo(String id, String path) {
            this.id = id;
            this.path = path;
        }
    }
    private static PluginManager instance;
    private static String dir = "plugins";
    private static int lastId = 0;
    private static HashMap<String, PluginInfo> map = new HashMap<String, PluginInfo>();

    private PluginManager() {
    }

    public static PluginManager instance() {
        if (instance == null) {
            instance = new PluginManager();
        }

        return instance;
    }

    public void addPlugin(String name, String path) {
//        assert !map.containsKey(name);

        String id = null;
        if (map.containsKey(name)) {
            id = map.get(name).id;
        } else {
            id = Integer.toString(lastId);
            lastId++;
        }

        map.put(name, new PluginInfo(id, path));
    }

    public void addPlugin(String name, byte[] jarContent) {
        File jar;

//        assert !map.containsKey(name);

        String id = null;
        if (map.containsKey(name)) {
            id = map.get(name).id;
        } else {
            id = Integer.toString(lastId);
            lastId++;
        }
        lastId++;

        String path = pluginPath(id);

        jar = new File(path);

        assert jar.canWrite();
        try {
            write(jarContent, jar);
        } catch (IOException ex) {
        }

        map.put(name, new PluginInfo(id, path));

    }

    public Plugin getPlugin(String name) {
        assert map.containsKey(name);

        String id = map.get(name).id;
        String path = map.get(name).path;

        Plugin p = new Plugin();

        File f = new File(path);
        p.setId(id);
        try {
            p.setValue(read(f));
        } catch (IOException ex) {
            //TODO: asdafasd
        }

        return p;
    }

    private static String pluginPath(String id) {
        return dir + File.separator + id + ".jar";
    }

    private static byte[] read(File file) throws IOException /*, FileTooBigException*/ {
        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }

        return buffer;
    }

    private static void write(byte[] jarContents, File f) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(f));
        bos.write(jarContents);
        bos.flush();
        bos.close();
    }

    public PluginSet getGeneralPluginSet() {
        PluginManager ph = PluginManager.instance();

        PluginSet ps = new PluginSet();
        ps.getPlugin().add(ph.getPlugin(COMMON_NAME));
        ps.getPlugin().add(ph.getPlugin(MASTERPLUGIN_NAME));
        ps.getPlugin().add(ph.getPlugin(REPOCLIENT_NAME));
        ps.getPlugin().add(ph.getPlugin(NOTIFICATION_NAME));

        return ps;
    }
}
