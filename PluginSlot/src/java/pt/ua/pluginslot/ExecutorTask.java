/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.rlaas.plugin.command.Command;

import pt.ua.pluginslot.rim.QueueTask;
import pt.ua.pluginslot.util.Queue;

/**
 *
 * @author Luís S. Ribeiro
 */
public class ExecutorTask extends Observable implements Runnable, Observer
{

    private boolean running = true;
    private boolean idle = false;
    private Queue queue;
    private Thread t;
    
    private Class cl;
    private ClassLoader loader;
    private Object plugin;
    
    private File pluginDir;
    
    public ExecutorTask(File pluginDir) 
    {
        this.pluginDir = pluginDir;
        this.queue = Queue.getInstance();         
        this.t = new Thread(this, "Executor");        
    }

    public synchronized void start(String pluginName) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {     
        queue.addObserver(this);
        System.out.println("THREAD STARTED");
        
        loader = load();
        cl = loader.loadClass(pluginName);
        t.start();        
    }    
    
    @Override
    public void run() 
    {
        while(running)
        {   
            System.out.println("\tchecking");  
            if(queue.isEmpty())
            {
                try 
                {
                    idle = true;
                    Thread.sleep(30000);
                }
                catch (InterruptedException ex) 
                {
                    System.out.println("\thas new tasks to execute");
                }
            }
            else
            {                
                idle = false;                                              
                QueueTask task = queue.remove();//queue.peek();
                System.out.println("\tExecuting task id = "+task.getId()+"\n\tcommand: "+task.getCommand());                                
                
                try 
                {    
                    
                    switch(task.getCommand())
                    {
                        case 0: 
                            Method init = cl.getMethod("init", File.class);
                            if(init != null)
                            {                 
                                String path = "WorkDirs"+File.separator+task.getDomain();
                                File finalDir = new File(path);
                                if(!finalDir.exists())
                                {
                                    finalDir.mkdirs();
                                }
                                //System.setProperty("user.dir", finalDir.getAbsolutePath());
                                
                                t.setContextClassLoader(loader);
                                
                                System.out.println("\t\tiniting");
                                plugin = cl.newInstance();
                                init.invoke(plugin, finalDir);
                                System.out.println("\t\tinited");
                            }
                            else
                            {
                                //FIXME NOTIFY
                            }
                        break;
                        case 1:
                            System.err.println("ENTRA");
                            Method transform = cl.getMethod("transform", File.class, Map.class);
                            if(transform != null)
                            {
                                System.out.println("\t\tprocessing");
                                String path = "WorkDirs"+File.separator+task.getDomain();
                                File finalDir = new File(path);
                                
                                
                                
                                System.err.println(finalDir.getPath()+" exists? "+finalDir.exists());
                                if(!finalDir.exists())
                                {
                                    finalDir.mkdirs();
                                }
                                transform.invoke(plugin,finalDir, task.getSettings());
                                System.out.println("\t\tprocessed");
                            }
                            else
                            {
                                //FIXME NOTIFY
                            }
                        break;
                        case 2:
                            Method destroy = cl.getMethod("destroy");
                            if(destroy != null)
                            {                        
                                System.out.println("\t\tdestroying");
                                destroy.invoke(plugin);
                                System.out.println("\t\tdestroyed");
                            }
                            else
                            {
                                //FIXME NOTIFY
                            }
                            running = false;
                        break;                                
                        default:
                            
                    }                    
                    
                    /* PS:
                     * TransformPlugin transform = (TransformPlugin) 
                     * NOT POSSIBLE because different class loaders                     
                     */                       
                }           
                catch (UnsupportedOperationException ex)
                {
                    if(task.getCommand() == Command.PROCESS)
                        Logger.getLogger(ExecutorTask.class.getName()).log(Level.SEVERE, null, ex);
                    else
                    {
                        //do nothing 
                    }
                }
                catch (Exception ex)
                {
                    Logger.getLogger(ExecutorTask.class.getName()).log(Level.SEVERE, null, ex);
                }                                
                System.out.println("\tExecuted task id = "+task.getId());
            }
        }
    }

    
    
    public void stop()
    {
        System.err.println("STOP executor");
        this.running = false;
        if(idle)
            t.interrupt();
    }
    
    
    private URLClassLoader load() throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException  
    {
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        };
        File [] jars =  pluginDir.listFiles(filter);           
        
        int i;
        URL[] urls;
        urls = new URL[jars.length];

        i = 0;
        for (File jar : jars) 
        {
            urls[i] = jar.toURI().toURL();  
            System.out.println(urls[i]);
            i++;
        }                
        
        URLClassLoader urlLoader = new URLClassLoader(urls);                                        
            
      
        return urlLoader;
          
           
        
        
    }
    
    
               

    @Override
    public void update(Observable o, Object arg) 
    {
        if(o instanceof Queue)
        {
            if(idle)
            {
                System.out.println("Wake up");
                t.interrupt();
            }
        }
    }
}
