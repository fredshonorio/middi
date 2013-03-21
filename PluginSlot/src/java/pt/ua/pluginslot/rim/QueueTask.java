/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot.rim;

import java.io.File;
import java.util.HashMap;


/**
 *
 * @author Luís S. Ribeiro
 */
public class QueueTask 
{    
    private int id;
    private String domain;      
    private HashMap<String, String> settings;
    //private String pluginName;    
    private int command;

    public QueueTask(String domain, int command){ //,  String pluginName) {
        this.domain = domain;
        this.command = command;
        
        //this.pluginName = pluginName;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public void setSettings(HashMap<String, String> settings) {
        this.settings = settings;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    

//    public String getPluginName() {
//        return pluginName;
//    }

    public HashMap<String, String> getSettings() {
        return settings;
    }

    public String getDomain() {
        return domain;
    }

    

    
    
    
}
