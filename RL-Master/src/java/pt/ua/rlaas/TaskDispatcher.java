package pt.ua.rlaas;

import java.util.List;
import pt.ua.pluginslot.PluginSet;
import pt.ua.pluginslot.PluginSlotTask;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.Schema;
import pt.ua.rlaas.plugin.command.Command;
import pt.ua.rlaas.tasks.CompareConfig;
import pt.ua.rlaas.tasks.ExportStep;
import pt.ua.rlaas.tasks.TransformStep;
import pt.ua.rlaas.tasks.UpdatePipeline;
import pt.ua.rlaas.util.Constants;
import pt.ua.rlaas.util.Util;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class TaskDispatcher {

    private SettingsHelper updateSh = new SettingsHelper();
    private String updateDomain;
    private Schema updateSchema;

    public void sendPipeline(PluginSlotClient client, int taskId, UpdatePipeline update) {
        List<TransformStep> transformSteps = update.getTransformSteps();
        CompareConfig compareConfig = update.getCompareConfig();
        ExportStep exportStep = update.getExportStep();
        updateDomain = update.getDomain();
        updateSchema = update.getStartingSchema();

        //PLUGINS go first
        PluginManager pluginManager = PluginManager.instance();

        PluginSet ps = pluginManager.getGeneralPluginSet();

        for (TransformStep st : transformSteps) {
            ps.getPlugin().add(pluginManager.getPlugin(st.getPluginName()));
        }

        for (String pluginName : compareConfig.getPlugins()) {
            ps.getPlugin().add(pluginManager.getPlugin(pluginName));
        }

        ps.getPlugin().add(pluginManager.getPlugin(exportStep.getPluginName()));

        try {
            client.uploadPluginSet(ps);
        } catch (Exception ex) {
            System.err.printf("ERROR UPLOADING");
        }

        System.out.println("START: " + client.start(Constants.UpdateTask.METAPLUGIN));

        updateSh.put(Constants.UpdateTask.COMPARE_SCHEMA, Util.concat(";", updateSchema.getFieldNames().toArray(new String[0])));
        updateSh.put(Constants.UpdateTask.TRANSFORM_COUNT, Integer.toString(transformSteps.size()));

        int i = 0;
        for (TransformStep s : transformSteps) {
            updateSh.put(Constants.UpdateTask.TRANSFORM_PLUGINNAME_FIELD(i), s.getPluginName());
            updateSh.put(Constants.UpdateTask.TRANSFORM_FIELDS(i), Util.concat(";", s.getFields()));

            if (s.getSettings() != null) {
                for (String setting : s.getSettings().keySet()) {
                    updateSh.put(Constants.UpdateTask.TRANSFORM_SETTING_PREFIX(i) + setting, s.getSettings().get(setting));
                }
            }
            i++;
        }

        updateSh.put(Constants.UpdateTask.PLUGINNAMES_FIELD, Util.concat(";", compareConfig.getPlugins()));
        updateSh.put(Constants.UpdateTask.WEIGHTS_FIELD, Util.concat(";", Util.toStringArray(compareConfig.getWeights())));
        updateSh.put(Constants.UpdateTask.THRESHOLDLOW_FIELD, Double.toString(compareConfig.getThresholdLow()));
        updateSh.put(Constants.UpdateTask.THRESHOLDHIGH_FIELD, Double.toString(compareConfig.getThresholdHigh()));

        updateSh.put(Constants.UpdateTask.EXPORT_PLUGINNAME, exportStep.getPluginName());
        if (exportStep.getSettings() != null) {
            for (String setting : exportStep.getSettings().keySet()) {
                updateSh.put(Constants.UpdateTask.EXPORT_SETTING_PREFIX + setting, exportStep.getSettings().get(setting));
            }
        }

        updateSh.put(Constants.SlotPlugin.TASKID_FIELD, Integer.toString(taskId));
        updateSh.put(Constants.UpdateTask.RESULTNAME, "result_" + update.getPipelineName());


        PluginSlotTask init = new PluginSlotTask();
        init.setDomain(updateDomain);
        init.setCommand(Command.INIT);
        init.setSettings(updateSh.getSettings());

        client.addTask(init);
    }

    public void sendUpdate(RepositoryClient repo, String pipelineName, PluginSlotClient client, List<Record> records) {
        repo.storeRecords(records, updateSchema, pipelineName);

        PluginSlotTask process = new PluginSlotTask();
        process.setDomain(updateDomain);
        process.setCommand(Command.PROCESS);
        updateSh.put(Constants.UpdateTask.RECORDSIN, pipelineName);
        process.setSettings(updateSh.getSettings());
        client.addTask(process);
    }

    public void sendStop(String pipelineName, PluginSlotClient client) {

        PluginSlotTask destroy = new PluginSlotTask();
        destroy.setDomain(updateDomain);
        destroy.setCommand(Command.DESTROY);
        client.addTask(destroy);
    }
}
