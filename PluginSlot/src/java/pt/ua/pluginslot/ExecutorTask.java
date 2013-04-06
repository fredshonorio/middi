package pt.ua.pluginslot;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.rlaas.plugin.command.Command;

import pt.ua.pluginslot.rim.QueueTask;
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
    private ClassLoader loader;
    private Object plugin;
    private File pluginDir;

    public ExecutorTask(File pluginDir) {
        this.pluginDir = pluginDir;
        this.queue = Queue.getInstance();
        this.t = new Thread(this, "Executor");
    }

    public synchronized void start(String pluginName) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        queue.addObserver(this);
        System.out.println("THREAD STARTED");

        loader = load();

        cl = loader.loadClass(pluginName);

        t.start();
    }

    @Override
    public void run() {
        System.out.println("HI!123123");
        t.setContextClassLoader(loader);

        while (running) {
            if (queue.isEmpty()) {
                try {
                    idle = true;
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
//                    System.out.println("\thas new tasks to execute");
                }
            } else {

                idle = false;
                QueueTask task = queue.remove();//queue.peek();
                System.out.println("\tExecuting task id = " + task.getId() + "\n\tcommand: " + task.getCommand());

                HashMap<String, Object> context = new HashMap<String, Object>();
                context.put(Constants.SlotPlugin.CLASSLOADER_FIELD, loader);

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
                System.out.println("\tExecuted task id = " + task.getId());
            }
        }
    }

    public void stop() {
        System.err.println("STOP executor");
        this.running = false;
        if (idle) {
            t.interrupt();
        }
    }

    private URLClassLoader load() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
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
            System.out.println(urls[i]);
            i++;
        }

        URLClassLoader urlLoader = new URLClassLoader(urls);


        return urlLoader;
    }

    public void runInit(QueueTask task, HashMap<String, Object> context) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method init = cl.getMethod("init", HashMap.class, HashMap.class);

        String path = "WorkDirs" + File.separator + task.getDomain();
        File finalDir = new File(path);
        if (!finalDir.exists()) {
            finalDir.mkdirs();
        }
        //System.setProperty("user.dir", finalDir.getAbsolutePath());

//                                t.setContextClassLoader(loader);

        System.out.println("\t\tiniting");
        plugin = cl.newInstance();

        init.invoke(plugin, task.getSettings(), context);
        System.out.println("\t\tinited");
    }

    private void runProcess(QueueTask task, HashMap<String, Object> context) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method process = cl.getMethod("process", HashMap.class);
        String path = "WorkDirs" + File.separator + task.getDomain();
        File finalDir = new File(path);

        System.err.println(finalDir.getPath() + " exists? " + finalDir.exists());
        if (!finalDir.exists()) {
            finalDir.mkdirs();
        }
        process.invoke(plugin, task.getSettings());
        System.out.println("\t\tprocessed");
    }

    private void runDestroy() throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Method destroy = cl.getMethod("destroy");
        System.out.println("\t\tdestroying");
        destroy.invoke(plugin);
        System.out.println("\t\tdestroyed");
        running = false;

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Queue) {
            if (idle) {
                System.out.println("Wake up");
                t.interrupt();
            }
        }
    }
}
