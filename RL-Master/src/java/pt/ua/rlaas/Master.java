package pt.ua.rlaas;

import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.pluginslot.Plugin;
import pt.ua.pluginslot.PluginSlotTask;
import pt.ua.rlaas.plugin.command.Command;
import pt.ua.rlaas.tasks.TransformTask;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Master {

    public static void main(String args[]) {
        PluginSlotClient cl = new PluginSlotClient("http://localhost:8080/PluginSlot/PluginSlot?wsdl");

        cl.start("pt.ua.rlaas.metaplugin.TransformMetaPlugin");
        PluginHelper ph = new PluginHelper();
        Plugin p = ph.newPlugin("C:\\Users\\PC\\code\\dissertation\\new\\RL-TestPlugins\\dist\\RL-TestPlugins.jar", "rl.testplugins.ToLowercase");

        TransformTask t = new TransformTask("514b39686671cbbdd2a3dd77", "asdasd", 0, 0, "rl.testplugins.ToLowercase", p, "work123123");
        
        t.setFields("nome");
        PluginSlotTask init = t.getPluginSlotTask(Command.INIT);
        PluginSlotTask process = t.getPluginSlotTask(Command.PROCESS);
        PluginSlotTask destroy = t.getPluginSlotTask(Command.DESTROY);
        
//        process.getSettings()
        try {
            cl.uploadPluginSet(t.getPluginSet());
        } catch (Exception ex) {
            Logger.getLogger(Master.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(cl.addTask(init));
        System.out.println(cl.addTask(process));
        System.out.println(cl.addTask(destroy));

    }

//    public static CacheData getFileAsCacheData(File file) {
//        CacheData c = new CacheData();
//        try {
//            c.setData(read(file));
//        } catch (IOException ex) {
//        }
//        
//        c.setFileName(file.getName());
//        return c;
//    }
}
