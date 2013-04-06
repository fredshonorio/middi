package pt.ua.rlaas.metaplugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import pt.ua.rlaas.RepositoryClient;
import pt.ua.rlaas.data.Record;
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

    @Override
    public void process(HashMap<String, String> settings) {
        recordsOut = transform.transform(recordsIn, schema);
    }

    @Override
    public void init(HashMap<String, String> settings, HashMap<String, Object> context) {
        ClassLoader ldr = (ClassLoader) context.get(Constants.SlotPlugin.CLASSLOADER_FIELD);
        RepositoryClient repo = (RepositoryClient) Util.getPluginInstance(ldr, Constants.SlotPlugin.REPOSITORYCLIENT_NAME);

        transform = (TransformPlugin) Util.getPluginInstance(ldr, (String) settings.get(Constants.TransformTask.PLUGINNAME_FIELD));
        transform.init(settings);

        String recordSetId = settings.get(Constants.TransformTask.RECORDSIN_FIELD);

        System.out.println("HEY");
        
        for (String k : settings.keySet()) {
            System.out.println(k + ": " + settings.get(k));
        }

        recordsIn = repo.getAllRecords(recordSetId);
        schema = repo.getSchema(recordSetId);

        System.out.println(recordsIn.size() + " records in.");
    }

    @Override
    public void destroy() {
        RecordHelper rh = new RecordHelper(schema);
        for (Record out : recordsOut) {
            System.out.println(rh.toString(out));
        }
    }
}
