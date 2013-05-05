package pt.ua.rlaas.metaplugin;

import java.util.HashMap;
import java.util.List;
import pt.ua.rlaas.Master_Service;
import pt.ua.rlaas.RepositoryClient;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.RecordSet;
import pt.ua.rlaas.data.Schema;
import pt.ua.rlaas.plugin.TaskPlugin;
import pt.ua.rlaas.plugin.TransformPlugin;
import pt.ua.rlaas.util.Constants;
import pt.ua.rlaas.util.RecordHelper;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class TransformMetaPlugin implements TaskPlugin {

    private List<Record> recordsIn, recordsOut;
    private TransformPlugin transform;
    private Schema schema;
    private int taskID;
    private HashMap<String, Object> context;
    private RepositoryClient repo;
    private String recordsOutId;

    @Override
    public void process(HashMap<String, String> settings) {
        recordsOut = transform.transform(recordsIn, schema);
        repo.storeRecords(recordsOut, schema, recordsOutId);
    }

    @Override
    public void init(HashMap<String, String> settings, HashMap<String, Object> context) {
        this.context = context;

        ClassLoader ldr = (ClassLoader) context.get(Constants.SlotPlugin.CLASSLOADER_FIELD);
        repo = (RepositoryClient) Util.getPluginInstance(ldr, Constants.SlotPlugin.REPOSITORYCLIENT_NAME);

        transform = (TransformPlugin) Util.getPluginInstance(ldr, (String) settings.get(Constants.TransformTask.PLUGINNAME_FIELD));
        transform.init(settings);

        taskID = Integer.parseInt(settings.get(Constants.SlotPlugin.TASKID_FIELD));
        String recordSetId = settings.get(Constants.TransformTask.RECORDSIN_FIELD);
        recordsOutId = settings.get(Constants.TransformTask.RECORDSOUT_FIELD);

        for (String k : settings.keySet()) {
            System.out.println(k + ": " + settings.get(k));
        }
        recordsIn = repo.getAllRecords(recordSetId);
        schema = repo.getSchema(recordSetId);
    }

    @Override
    public void destroy() {
        new Master_Service().getMasterPort().notifyComplete(taskID);
//
//        RecordHelper rh = new RecordHelper(schema);
//        for (Record out : recordsOut) {
//            System.out.println(rh.toString(out));
//        }
    }
}
