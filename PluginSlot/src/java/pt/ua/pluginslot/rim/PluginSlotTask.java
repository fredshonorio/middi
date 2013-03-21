/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot.rim;


/**
 *
 * @author Luís S. Ribeiro
 */
public class PluginSlotTask 
{    
    private String domain;       
    //private String pluginName;
    private int command;

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public PluginSlotTask() {
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
    

//    /**
//     * @return the pluginName
//     */
//    public String getPluginName() {
//        return pluginName;
//    }
//
//    /**
//     * @param pluginName the pluginName to set
//     */
//    public void setPluginName(String pluginName) {
//        this.pluginName = pluginName;
//    }

    
    
    
}
