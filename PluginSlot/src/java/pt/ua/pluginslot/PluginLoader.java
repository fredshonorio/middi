package pt.ua.pluginslot;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.jar.JarFile;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class PluginLoader extends URLClassLoader {

    public PluginLoader(URL[] urls, ClassLoader cl) {
        super(urls, cl);
    }

    public void close() {
        try {
            Class clazz = java.net.URLClassLoader.class;
            Field ucp = clazz.getDeclaredField("ucp");
            ucp.setAccessible(true);
            Object sunMiscURLClassPath = ucp.get(this);
            Field loaders = sunMiscURLClassPath.getClass().getDeclaredField("loaders");
            loaders.setAccessible(true);
            Object collection = loaders.get(sunMiscURLClassPath);
            for (Object sunMiscURLClassPathJarLoader : ((Collection) collection).toArray()) {
                try {
                    Field loader = sunMiscURLClassPathJarLoader.getClass().getDeclaredField("jar");
                    loader.setAccessible(true);
                    Object jarFile = loader.get(sunMiscURLClassPathJarLoader);
                    ((JarFile) jarFile).close();
                    System.out.println("jar:" + ((JarFile) jarFile).toString());
                } catch (Throwable t) {
                    System.out.println("!" + t.getMessage());
                    // if we got this far, this is probably not a JAR loader so skip it
                }
            }
        } catch (Throwable t) {
            System.out.println("!!" + t.getMessage());
            // probably not a SUN VM
        }
    }
}
