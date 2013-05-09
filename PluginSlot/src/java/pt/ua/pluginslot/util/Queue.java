/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot.util;

import java.util.Observable;
import java.util.concurrent.ConcurrentLinkedQueue;
import pt.ua.pluginslot.rim.PluginSlotTask;

/**
 *
 * @author Luís S. Ribeiro
 */
public class Queue extends Observable
{
    private static Queue instance;
    private ConcurrentLinkedQueue<PluginSlotTask> queue = new ConcurrentLinkedQueue<PluginSlotTask>();
    private static int count = 0;
    private Queue()
    {
        super();
    }
    
    public static synchronized Queue getInstance()
    {
        if(instance == null)
            instance = new Queue();
        return instance;
    }
    
    public int addTask(PluginSlotTask task)
    {        
        task.setId(++count);
        queue.add(task);        
        super.setChanged();
        super.notifyObservers();         
        return count;
    }
    
    public PluginSlotTask peek()
    {
        return queue.peek();
    }
    
    public PluginSlotTask remove()
    {
        return queue.remove();
    }
    
    public boolean isEmpty()
    {
        return queue.isEmpty();
    }
}
