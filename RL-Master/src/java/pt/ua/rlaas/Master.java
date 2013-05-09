/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.rlaas;

import java.util.LinkedList;
import javax.annotation.PostConstruct;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.RecordSet;
import pt.ua.rlaas.data.Schema;
import pt.ua.rlaas.tasks.Task;
import pt.ua.rlaas.tasks.TaskQueue;
import pt.ua.rlaas.tasks.TransformTask;
import pt.ua.rlaas.tasks.UpdatePipeline;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
@WebService(serviceName = "Master")
public class Master {

    static PluginManager m;
    static String idRecA;
    static String idRecB;
    static String domain = "RL";
    static Thread t = null;

    @PostConstruct
    public void init() {
        if (t == null) {
            System.out.println("Initializing plugins...");
            m = PluginManager.instance();
            m.addPlugin(Task.COMMON_NAME, "C:\\Users\\PC\\code\\dissertation\\new\\RL-Common\\dist\\RL-Common.jar");
            m.addPlugin(Task.MASTERPLUGIN_NAME, "C:\\Users\\PC\\code\\dissertation\\new\\RL-MasterPlugins\\dist\\RL-MasterPlugins.jar");
            m.addPlugin(Task.REPOCLIENT_NAME, "C:\\Users\\PC\\code\\dissertation\\new\\RL-RepositoryClient\\dist\\RL-RepositoryClient.jar");
            m.addPlugin(Task.NOTIFICATION_NAME, "C:\\Users\\PC\\code\\dissertation\\new\\RL-MasterClient\\dist\\RL-MasterClient.jar");

            System.out.println("Initializing Task Manager...");
            t = new Thread(new TaskManager());
            t.start();

        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "notifyComplete")
    public String notifyComplete(@WebParam(name = "id") int id) {
        TaskQueue.instance().markComplete(id);
        return "OK";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "test")
    public String test() {


//
//        //
//        RepositoryClient cl = new RepositoryClient();
//
//        Schema schema = new Schema("nome", "bi");
//        //***************LOAD******************
//        RecordSet a = new RecordSet(schema);
//        RecordSet b = new RecordSet(schema);
//
//        LinkedList<Record> lA = new LinkedList<Record>();
//        lA.add(new Record("1", "jose", "123"));
//        lA.add(new Record("1", "marcio", "113"));
//        lA.add(new Record("1", "martim", "112"));
//        lA.add(new Record("1", "zé batatas", "312"));
//
//        LinkedList<Record> lB = new LinkedList<Record>();
//        lB.add(new Record("1", "joze", "123"));
//        lB.add(new Record("1", "marco", "113"));
//        lB.add(new Record("1", "martins", "112"));
//        lB.add(new Record("1", "zé bananas", "312"));
//
//        a.setRecords(lA);
//        b.setRecords(lB);
////
//        idRecA = cl.storeRecordSet(a);
//        idRecB = cl.storeRecordSet(b);
////
////        //***************TRANSFORM******************
//        m.addPlugin("rl.testplugins.ToUppercase", "C:\\Users\\PC\\code\\dissertation\\new\\RL-TestPlugins\\dist\\RL-TestPlugins.jar");
//
//        TransformTask transfA = new TransformTask(idRecA, "trans1", 0, 0, "rl.testplugins.ToUppercase", domain, new String[]{"nome"});
//
//        TransformTask transfB = new TransformTask(idRecB, "trans2", 0, 0, "rl.testplugins.ToUppercase", domain, new String[]{"nome"});
////
//
////        Thread t = new Thread(new TaskManager());
//        t.start();
//
//
//        TaskQueue q = TaskQueue.instance();
////
////        q.add(transfA);
////        q.add(transfB);
//
//        String idTransA = null; //!
//        String idTransB = null; //!
        return "OK";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "uploadPlugin")
    public String uploadPlugin(@WebParam(name = "pluginName") String pluginName, @WebParam(name = "jarContent") byte[] jarContent) {

        PluginManager.instance().addPlugin(pluginName, jarContent);
        return "OK";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "definePipeline")
    public String definePipeline(@WebParam(name = "pipeline") UpdatePipeline pipeline) {
        TaskQueue.instance().add(pipeline);
        return "OK";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateRecords")
    public String updateRecords(@WebParam(name = "recordUpdate") RecordUpdate recordUpdate) {
        TaskQueue.instance().add(recordUpdate);
        return "OK";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "stopPipeline")
    public String stopPipeline(@WebParam(name = "pipelineToStop") String pipelineToStop) {
        TaskQueue.instance().add(pipelineToStop);
        return "OK";
    }
}
