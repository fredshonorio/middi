/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot.rim;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *
 * @author Luís S. Ribeiro
 */
public class PluginSet 
{
    
    List<Plugin> plugins;

    public List<Plugin> getPlugin() {  
        if(plugins == null)
            plugins = new ArrayList<Plugin>();
        return plugins;
    }

    public void setPlugin(List<Plugin> plugin) {
        this.plugins = plugin;
    }
    
        
}
