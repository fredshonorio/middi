/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import pt.ua.rlaas.plugin.command.Command;

import pt.ua.pluginslot.rim.CacheData;
import pt.ua.pluginslot.rim.PluginSet;
import pt.ua.pluginslot.rim.Plugin;
import pt.ua.pluginslot.rim.PluginSlotTask;
//import pt.ua.pluginslot.rim.QueueTask;
import pt.ua.pluginslot.util.Queue;

/**
 *
 * @author Luís S. Ribeiro
 */
@WebService(serviceName = "PluginSlot")
public class PluginSlot implements IPluginSlot {

    private PluginSlotCore core = PluginSlotCore.instance();
    private boolean isStarted = false;
    

    /**
     * Web service operation
     */
    @WebMethod(operationName = "UploadPluginSet")
    @Override
    public String UploadPluginSet(@WebParam(name = "pluginSet") PluginSet pluginSet) throws FileNotFoundException, IOException {
         try {
            core.savePluginSet(pluginSet);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
        return "OK";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addTask")
    @Override
    public String addTask(@WebParam(name = "task") PluginSlotTask task) {
        if (isValid(task)) {
            Queue.getInstance().addTask(task);
            return "OK";
        } else {
            return "FAIL";
        }
    }

    /**
     * Web service operation
     *
     * @param pluginName Binary name of the class to be loaded
     */
    @WebMethod(operationName = "start")
    @Override
    public String start(@WebParam(name = "pluginName") String pluginName) {
        if (isStarted) {
            return "OK";
        }

        try {
            core.start(pluginName);
            isStarted = true;
//        } catch (ClassNotFoundException ex) {
//            return ex.toString();
//        } catch (MalformedURLException ex) {
//            return ex.toString();
//        } catch (InstantiationException ex) {
//            return ex.toString();
//        } catch (IllegalAccessException ex) {
//            return ex.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
        return "OK";
    }

    /**
     * Web service operation
     *
     * @param data byte array of some file
     */
    @WebMethod(operationName = "push")
    @Override
    public URL push(@WebParam(name = "workDir") String workDirPath, @WebParam(name = "data") CacheData data) {
        //FIXME catches!!!!
        try {
            return core.push(workDirPath, data);
        } catch (IllegalAccessException ex) {
            return null;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (FileAlreadyExistsException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    private boolean isValid(PluginSlotTask task) {
        if (!Command.valid(task.getCommand())) {
            System.err.println("Command NOT VALID " + task.getCommand());
            return false;
        }
        if (task.getDomain() == null || task.getDomain().trim().equals("")) {
            System.err.println("domain NOT VALID");
            return false;
        }
        return true;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "stop")
    @Override
    public String stop() {
        core.stop();
        return "OK";
    }
}
