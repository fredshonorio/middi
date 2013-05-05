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

import java.util.Observable;
import java.util.Observer;
import pt.ua.pluginslot.rim.CacheData;
import pt.ua.pluginslot.rim.Plugin;
import pt.ua.pluginslot.util.Cache;

/**
 *
 * @author Luís S. Ribeiro
 */
public class PluginSlotCore implements Observer
{    
    private File pluginDir;
    
    private Cache cache = new Cache();
    
    private boolean isStarted = false;
    
    private ExecutorTask executor;    

    public PluginSlotCore(File pulginDir) throws FileNotFoundException 
    {                       
        this.pluginDir = pulginDir;
        if(!pluginDir.exists())
            pluginDir.mkdirs();
        setup();
    }
        
    
    
    private void setup() throws FileNotFoundException 
    {
        if(!pluginDir.exists())
        {
            pluginDir.mkdirs();
        }          
    }
       
    public synchronized void start(String pluginName) throws Exception
    {    
        if(!isStarted)
        {
            executor = new ExecutorTask(pluginDir);
            executor.addObserver(this);
            System.out.println("STARTING EXECUTOR");
            executor.start(pluginName);                   
        }                  
    }
    
    public synchronized void stop()
    {        
        if(executor != null) 
            executor.stop();
    }
    
    public synchronized void savePlugin(Plugin plugin) throws FileNotFoundException, IOException
    {
        
        File jar = new File(pluginDir.getPath()+File.separator+plugin.getId()+".jar");

        if (jar.exists()){
            jar.delete();
        }
        
        System.out.println("saving " + jar.getAbsolutePath());
        FileOutputStream fos = new FileOutputStream(jar) ;
        fos.write(plugin.getValue());
        fos.close();
    }

    public synchronized URL push(String domain, CacheData data) throws FileNotFoundException, FileAlreadyExistsException, IOException, IllegalAccessException
    {        
        File dir = cache.newWorkDir(domain); 
        return cache.storeData(dir,data);
    }
    
    
    @Override
    public void update(Observable o, Object arg) 
    {                
        
        
    }

    
    
}
