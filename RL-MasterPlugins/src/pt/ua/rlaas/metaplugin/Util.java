package pt.ua.rlaas.metaplugin;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Util {

    public static String[] getClassNames(String str) {
        return str.split(";");
    }

    public static Object getPluginInstance(ClassLoader loader, String name) {
        Object obj = null;
        try {
            Class cl = null;
            cl = loader.loadClass(name);
            obj = cl.newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CompareMetaPlugin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CompareMetaPlugin.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CompareMetaPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }
}
