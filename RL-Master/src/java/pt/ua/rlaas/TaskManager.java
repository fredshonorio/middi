package pt.ua.rlaas;

import pt.ua.pluginslot.PluginSet;
import pt.ua.pluginslot.PluginSlotTask;
import pt.ua.rlaas.data.RecordSet;
import pt.ua.rlaas.plugin.command.Command;
import pt.ua.rlaas.tasks.TaskQueue;
import pt.ua.rlaas.tasks.TransformTask;
import pt.ua.rlaas.tasks.UpdatePipeline;
import pt.ua.rlaas.util.Constants;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class TaskManager implements Runnable {

    private PluginSlotClient client;
    private SettingsHelper sh;
    private PluginManager pluginManager = PluginManager.instance();
    private TaskQueue queue;
    private RepositoryClient repo;

    @Override
    public void run() {
        queue = TaskQueue.instance();
        TaskQueue.QueueElement e;
        client = new PluginSlotClient("http://localhost:8080/PluginSlot/PluginSlot?wsdl");
        repo = new RepositoryClient();
        TaskDispatcher dispatcher = new TaskDispatcher();
        while (true) {
            e = queue.getAvailableTask();
            switch (e.type) {
                case PIPELINE:
                    dispatcher.sendPipeline(client, e.id, (UpdatePipeline) e.task);
                    queue.markComplete(e.id);
                    break;
                case STOP_PIPELINE:
                    dispatcher.sendStop((String) e.task, client);
                    queue.markComplete(e.id);
                    break;
                case UPDATE:
                    RecordUpdate update = (RecordUpdate) e.task;
                    dispatcher.sendUpdate(repo, update.getPipelineName(), client, update.getRecords());
                    queue.markComplete(e.id);
                    break;


            }
        }
    }

    private void sendFullTransform(TransformTask task, int id) {
        client.start(Constants.TransformTask.METAPLUGIN);

        String outId = "";
        if (!recordSetExists(task.getRecordSetOutName())) {
            task.getRecordSetOutName();
            RecordSet r = new RecordSet();
            r.setName(task.getRecordSetOutName());
            outId = repo.storeRecordSet(r);
        }

        sh = new SettingsHelper();

        sh.put(Constants.TransformTask.RECORDSIN_FIELD, task.getRecordSetInId());
        sh.put(Constants.TransformTask.RECORDSOUT_FIELD, outId);
        sh.put(Constants.TransformTask.FIRSTRECORD_FIELD, Long.toString(task.getFirstRecord()));
        sh.put(Constants.TransformTask.LASTRECORD_FIELD, Long.toString(task.getLastRecord()));
        sh.put(Constants.TransformTask.PLUGINNAME_FIELD, task.getPluginName());

        StringBuilder b = new StringBuilder();
        String[] fields = task.getFields();
        for (int i = 0; i < fields.length; i++) {
            b.append(fields[i]);
            if (i != fields.length - 1) {
                b.append(';');
            }
        }

        sh.put(Constants.TransformTask.FIELDS_FIELD, b.toString());
        sh.put(Constants.SlotPlugin.TASKID_FIELD, Integer.toString(id));

        PluginSlotTask init = new PluginSlotTask();
        init.setDomain(task.getDomain());
        init.setCommand(Command.INIT);
        init.setSettings(sh.getSettings());

        PluginSlotTask process = new PluginSlotTask();
        process.setDomain(task.getDomain());
        process.setCommand(Command.PROCESS);
        process.setSettings(sh.getSettings());

        PluginSlotTask destroy = new PluginSlotTask();
        destroy.setDomain(task.getDomain());
        destroy.setCommand(Command.DESTROY);
        destroy.setSettings(sh.getSettings());

        PluginSet ps = pluginManager.getGeneralPluginSet();
        ps.getPlugin().add(pluginManager.getPlugin(task.getPluginName()));

        try {
            client.uploadPluginSet(ps);
        } catch (Exception ex) {
        }

        client.addTask(init);
        client.addTask(process);
        client.addTask(destroy);
    }

    public boolean recordSetExists(String recordSetName) {
        return false;
    }
}
