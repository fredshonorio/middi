/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.pluginslot.rim.CacheData;
import pt.ua.pluginslot.rim.Plugin;
import pt.ua.pluginslot.rim.PluginSet;
import pt.ua.pluginslot.util.Cache;

/**
 *
 * @author Luís S. Ribeiro
 */
public class PluginSlotCore implements Observer {

    private File pluginDir = null;
    private File mainDir;
    private Cache cache = new Cache();
    private boolean isStarted = false;
    private ExecutorTask executor;
    private static PluginSlotCore instance = null;
    private final static File dir = new File("Plugins");

    public static PluginSlotCore instance() {
        if (instance == null) {
            try {
                instance = new PluginSlotCore(dir);
            } catch (FileNotFoundException ex) {
            }
        }
        return instance;
    }

    public PluginSlotCore(File pluginDir) throws FileNotFoundException {
        this.mainDir = pluginDir;
        if (!mainDir.exists()) {
            mainDir.mkdirs();
        } else { //cleanup previously saved files
            cleanup(mainDir);
            mainDir.mkdirs();
        }
//        setup();
    }
    
    private void cleanup(File dir){
        for (File f : dir.listFiles()){
            if (f.isDirectory()){
                cleanup(f);
            } else if (f.isFile()){
                f.delete();
            }
        }
        
        if (dir.list().length == 0){
            dir.delete();
        }
    }

    public synchronized void start(String pluginName) throws Exception {
        if (!isStarted) {
            executor = new ExecutorTask(pluginDir);
            executor.addObserver(this);
            executor.start(pluginName);
        }
    }

    public synchronized void stop() {
        if (executor != null) {
            executor.stop();
        }
    }

    public void savePluginSet(PluginSet pluginSet) throws IOException {
        pluginDir = new File(mainDir, UUID.randomUUID().toString());
        pluginDir.mkdir();

        System.out.println("Saving plugins on " + pluginDir.getAbsolutePath());

        List<Plugin> plugins = pluginSet.getPlugin();
        for (Plugin plugin : plugins) {
            savePlugin(plugin);
        }
        
        System.out.println("Saving complete.");

    }

    public synchronized void savePlugin(Plugin plugin) throws FileNotFoundException, IOException {
        File jar = new File(pluginDir.getPath() + File.separator + plugin.getId() + ".jar");

        if (jar.exists()) {
            jar.delete();
        }

        System.out.print("\tSaving " + jar.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(jar);
        fos.write(plugin.getValue());
        fos.close();
    }

    public synchronized URL push(String domain, CacheData data) throws FileNotFoundException, FileAlreadyExistsException, IOException, IllegalAccessException {
        File dir = cache.newWorkDir(domain);
        return cache.storeData(dir, data);
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
