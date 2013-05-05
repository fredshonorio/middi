package pt.ua.rlaas.metaplugin;

import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.rlaas.data.Schema;
import pt.ua.rlaas.plugin.ComparePlugin;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Util {

    public static final double EPSILON = 0.0000001;

    public static String[] getStrings(String str) {
        return str.split(";");
    }

    public static boolean nearEquals(double a, double b) {
        return (a - b) < EPSILON;
    }

    public static boolean equals(Schema a, Schema b) {
        return a.getFieldNames().equals(b.getFieldNames());
    }

    public static double[] getDouble(String str) {
        String[] strs = str.split(";");
        double[] d = new double[strs.length];

        int i = 0;
        for (String s : strs) {
            d[i] = Double.parseDouble(s);
            i++;
        }

        return d;
    }

    public static Schema getSchema(String str) {
        return new Schema(getStrings(str));
    }

    public static <T> T[] getPluginInstances(ClassLoader loader, String names, T[] array) {
        int i = 0;
        String[] namesS = getStrings(names);
        int size = namesS.length;

        if (array.length != size) {
            array = (T[]) (new Object[size]);
        }

        for (String name : namesS) {
            System.out.println("loading: " + name);
            array[i] = (T) getPluginInstance(loader, name);
            i++;
        }
        return array;
    }

    public static ComparePlugin[] getPluginInstancesC(ClassLoader loader, String names) {
        int i = 0;
        String[] namesS = getStrings(names);
        int size = namesS.length;

        ComparePlugin[] array = new ComparePlugin[size];

        for (String name : namesS) {
            System.out.println("loading: " + name);
            array[i] = (ComparePlugin) getPluginInstance(loader, name);
            i++;
        }
        return array;
    }

    public static Object getPluginInstance(ClassLoader loader, String name) {
        Object obj = null;
        try {
            Class cl = null;
            cl = loader.loadClass(name);
            obj = cl.newInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }

    public static ComparePlugin[] resizeCmp(ComparePlugin[] cmp, double[] weights) {

        int size = weights.length;
        ComparePlugin[] cmpA = new ComparePlugin[size];

        int c = 0;
        for (int i = 0; i < size; i++) {
            if (Util.nearEquals(weights[i], 0.0d)) {
                cmpA[i] = null;
            } else {
                cmpA[i] = cmp[c];
                c++;
            }
        }

        return cmpA;
    }
}
