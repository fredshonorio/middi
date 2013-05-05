/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.rlaas.metaplugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import pt.ua.rlaas.RepositoryClient;
import pt.ua.rlaas.data.Match;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.Result;
import pt.ua.rlaas.data.Schema;
import pt.ua.rlaas.plugin.ComparePlugin;
import pt.ua.rlaas.plugin.ExportPlugin;
import pt.ua.rlaas.plugin.TaskPlugin;
import pt.ua.rlaas.plugin.TransformPlugin;
import pt.ua.rlaas.util.Constants;
import pt.ua.rlaas.util.RecordHelper;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class UpdateMetaPlugin implements TaskPlugin {

    private List<TransformPlugin> transforms;
//    private List<HashMap<String, String>> tSettings;
    private List<Record> recordsRef, recordsNew;
    private ExportPlugin export;
    private Schema schema;
    private int taskID;
    private HashMap<String, Object> context;
    private RepositoryClient repo;
    private ComparePlugin comparePlugins[];
    private double[] weights;
//    private RecordSet recordSetA;
//    private RecordSet recordSetB;
    private double thresholdHigh;
    private double thresholdLow;
    private String resultName;

    private List<Record> getReferenceRecords(List<Result> results) {
        List<Record> references = new ArrayList<Record>();

        for (Result r : results) {
            references.add(r.getReferenceRecord());
        }
        return references;
    }

    @Override
    public void process(HashMap<String, String> settings) {
        //LOAD new records
        String recordSetId = settings.get(Constants.UpdateTask.RECORDSIN);

//        System.out.println("in:" + recordSetId);
//        System.out.println("a:" + resultName);

        recordsNew = repo.getDirtyRecords(recordSetId);
//        System.out.println("NEW:" + recordsNew.toString());

        //TRANSFORM 
        List<Record> records = recordsNew;
        for (TransformPlugin transf : transforms) {
            records = transf.transform(records, schema);
        }
        recordsNew = records;

        String taxonomy = null;//recordsNew.get(0).getTaxonomy();

        recordsRef = getReferenceRecords(repo.getResultsByName(resultName, taxonomy)); //TODO: taxonomy
//        System.out.println("OLD:" + recordsRef.toString());

        //MATCH
        LinkedList<Result> results = new LinkedList<Result>();

        Iterator<Record> itRef;
        Iterator<Record> itNew = recordsNew.iterator();

        double score;
        Record recNew;
        Record recOld;
        MultiMap<Record, Match> matches = new MultiMap<Record, Match>();

        int newMatches;

//        long size = (long) recordsRef.size() * (long) recordsNew.size();
//        long n = 0;

        RecordHelper rh = new RecordHelper(schema);

        double fieldScore;
        while (itNew.hasNext()) {
            newMatches = 0;
            recNew = itNew.next();
            itRef = recordsRef.iterator();

            while (itRef.hasNext()) {
                recOld = itRef.next();
                if (!matches.containsKey(recOld)) {
                    matches.put(recOld);
                }

                score = 0; //compute score of fields
                for (int i = 0; i < schema.size(); i++) {
                    if (weights[i] > 0) {
                        fieldScore = comparePlugins[i].compare(rh.get(recOld, i), rh.get(recNew, i));
                        assert fieldScore >= 0.0d & fieldScore <= 1.0d : "score " + fieldScore
                                + " is out of valid range.";
                        score += weights[i] * fieldScore;
                    }
                }

                //evaluate pair similarity
                if (score >= thresholdHigh) { // positive link
                    newMatches++;
                    matches.put(recOld, new Match(recNew, score));
                } else if (score >= thresholdLow) { //probable link
                    // TODO
                } else { //positive non-link
                }

//                assert n < size;
//                n++;
//                Log.progress(n, size);
            }
            if (newMatches == 0) { //integrate non-matched records
                matches.put(recNew);
                recordsRef.add(recNew);
            }
        }

        for (Record ref : matches.keySet()) { //add matches to results
            Result r = new Result(ref, matches.get(ref), ref.getTaxonomy());
            results.add(r);
//            System.out.println("# " + r.toString());
        }

        //STORE RESULTS
        repo.storeResultsId(results, schema, resultName);

        //EXPORT
        export.liveExport(results, repo.getResultsByName(resultName, taxonomy));
    }

    @Override
    public void init(HashMap<String, String> settings, HashMap<String, Object> context) {
        this.context = context;

        //********init********//
        taskID = Integer.parseInt(settings.get(Constants.SlotPlugin.TASKID_FIELD));
        ClassLoader ldr = (ClassLoader) context.get(Constants.SlotPlugin.CLASSLOADER_FIELD);
        repo = (RepositoryClient) Util.getPluginInstance(ldr, Constants.SlotPlugin.REPOSITORYCLIENT_NAME);

        //********transform********//
        int t = Integer.parseInt(settings.get(Constants.UpdateTask.TRANSFORM_COUNT));

        transforms = new ArrayList<TransformPlugin>();

        TransformPlugin transform;
        for (int i = 0; i < t; i++) {
            transform = (TransformPlugin) Util.getPluginInstance(ldr, (String) settings.get(Constants.UpdateTask.TRANSFORM_PLUGINNAME_FIELD(i)));
            //TODO: transform.init(settings);
            transforms.add(transform);
        }

        //********compare********//
        comparePlugins = (ComparePlugin[]) Util.getPluginInstancesC(ldr, settings.get(Constants.UpdateTask.PLUGINNAMES_FIELD));
        //TODO:init
        weights = Util.getDouble(settings.get(Constants.UpdateTask.WEIGHTS_FIELD));
        comparePlugins = Util.resizeCmp(comparePlugins, weights);
        thresholdHigh = Util.getDouble(settings.get(Constants.UpdateTask.THRESHOLDHIGH_FIELD))[0];
        thresholdLow = Util.getDouble(settings.get(Constants.UpdateTask.THRESHOLDLOW_FIELD))[0];
        schema = Util.getSchema(settings.get(Constants.UpdateTask.SCHEMA));
        resultName = settings.get(Constants.UpdateTask.RESULTNAME);

        //********export********//
        export = (ExportPlugin) Util.getPluginInstance(ldr, settings.get(Constants.UpdateTask.EXPORTPLUGIN));
        //TODO:init

    }

    @Override
    public void destroy() {
//        new Master_Service().getMasterPort().notifyComplete(taskID);
    }
}
