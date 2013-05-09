package pt.ua.pluginslot;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import pt.ua.pluginslot.rim.PluginSlotTask;
import pt.ua.pluginslot.util.Queue;
import pt.ua.rlaas.util.Constants;

/**
 *
 * @author Luís S. Ribeiro
 */
public class ExecutorTask extends Observable implements Runnable, Observer {

    private boolean running = true;
    private boolean idle = false;
    private Queue queue;
    private Thread t;
    private Class cl;
    private ClassLoader loader, systemLoader;
    private Object plugin;
    private File pluginDir;
    private long startTime;
    
    private final Object monitor = new Object();

    public ExecutorTask(File pluginDir) {
        this.pluginDir = pluginDir;
        this.queue = Queue.getInstance();
        systemLoader = ClassLoader.getSystemClassLoader();
    }

    public synchronized void start(String pluginName) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        queue.addObserver(this);
        System.out.println("THREAD STARTED: " + pluginName);

        loader = load();

        cl = loader.loadClass(pluginName);
        t = new Thread(this, "Executor");

        running = true;
        t.start();
    }

    @Override
    public void run() {
        t.setContextClassLoader(loader);

        startTime = System.currentTimeMillis();
        while (running) {
            if (queue.isEmpty()) {
                try {
                    idle = true;
                    // Thread.sleep(3000);
                    synchronized (monitor){
                        monitor.wait();
                    }
                } catch (InterruptedException ex) {
                }
            } else {
                idle = false;
                PluginSlotTask task = queue.remove();//queue.peek();
                System.err.println("Executing task #" + task.getId());
                HashMap<String, Object> context = new HashMap<String, Object>();
                context.put(Constants.SlotPlugin.CLASSLOADER_FIELD, loader);
                context.put(Constants.SlotPlugin.TASKID_FIELD, task.getId());

                try {
                    switch (task.getCommand()) {
                        case 0:
                            runInit(task, context);
                            break;
                        case 1:
                            runProcess(task, context);
                            break;
                        case 2:
                            runDestroy();
                            break;
                        default:
                    }

                    /* PS:
                     * TransformPlugin transform = (TransformPlugin) 
                     * NOT POSSIBLE because different class loaders                     
                     */
                } catch (Exception ex) {
//                    ex.printStackTrace();
                    Logger.getLogger(ExecutorTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.err.println("Completed task #" + task.getId());
            }
        }
    }

    public void stop() {
//        System.err.println("STOP executor");
        this.running = false;
        if (idle) {
            t.interrupt();
        }
    }

    private URLClassLoader load() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        URLClassLoader pluginLoader = null;

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        };

        File[] jars = pluginDir.listFiles(filter);

        int i;
        URL[] urls;
        urls = new URL[jars.length];

        i = 0;
        for (File jar : jars) {
            urls[i] = jar.toURI().toURL();
            i++;
        }


        pluginLoader = new PluginLoader(urls, systemLoader);

        System.out.println("Plugins loaded.");
        return pluginLoader;
    }

    public void runInit(PluginSlotTask task, HashMap<String, Object> context) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method init = cl.getMethod("init", HashMap.class, HashMap.class);

        String path = "WorkDirs" + File.separator + task.getDomain();
        File finalDir = new File(path);
        if (!finalDir.exists()) {
            finalDir.mkdirs();
        }

        System.out.println("INIT");
        plugin = cl.newInstance();

        init.invoke(plugin, task.getSettings(), context);
    }

    private void runProcess(PluginSlotTask task, HashMap<String, Object> context) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method process = cl.getMethod("process", HashMap.class);
        String path = "WorkDirs" + File.separator + task.getDomain();
        File finalDir = new File(path);

        if (!finalDir.exists()) {
            finalDir.mkdirs();
        }

        System.out.println("PROCESS");
        process.invoke(plugin, task.getSettings());

    }

    private void runDestroy() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method destroy = cl.getMethod("destroy");
        System.out.println("DESTROY");
        destroy.invoke(plugin);
        running = false;

        t.setContextClassLoader(systemLoader);
        cl = null;
        loader = null;
        plugin = null;
        System.gc();

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        };
        File[] jars = pluginDir.listFiles(filter);

        for (File jar : jars) {
//            System.out.println("Deleting " + jar + ": " + ((jar.delete() ? "OK" : "FAILED")));
            jar.deleteOnExit();
        }

        System.out.println((System.currentTimeMillis() - startTime) + " ms");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Queue) {
            if (idle) {
                t.interrupt();
            }
        }
    }
}
