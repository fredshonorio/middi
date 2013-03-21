/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nejimaster;

import pt.ua.pluginslot.PluginSlot;
import pt.ua.pluginslot.PluginSlotTask;
import pt.ua.pluginslot.PluginSlot_Service;
import pt.ua.rlaas.plugin.command.Command;

/**
 *
 * @author Luís S. Ribeiro
 */
public class NejiMaster {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        PluginSlot_Service stub = new PluginSlot_Service() ;
        PluginSlot api = stub.getPluginSlotPort();
        api.start("pt.ua.tm.neji.cluster.NejiPlugin");
        
        PluginSlotTask init = new PluginSlotTask();
        
        init.setDomain("Neji");
        init.setCommand(Command.INIT);
        api.addTask(init);
        
        PluginSlotTask task = new PluginSlotTask();
        task.setDomain("Neji");
        task.setCommand(Command.PROCESS);
        api.addTask(task);
        
        PluginSlotTask destroy = new PluginSlotTask();
        destroy.setDomain("Neji");
        destroy.setCommand(Command.DESTROY);
        api.addTask(destroy);
    }
}
