package pt.ua.rlaas.metaplugin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import pt.ua.rlaas.RepositoryClient;
import pt.ua.rlaas.data.Match;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.RecordSet;
import pt.ua.rlaas.data.Result;
import pt.ua.rlaas.data.Schema;
import pt.ua.rlaas.plugin.ComparePlugin;
import pt.ua.rlaas.plugin.TaskPlugin;
import pt.ua.rlaas.util.Constants;
import pt.ua.rlaas.util.RecordHelper;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class CompareMetaPlugin implements TaskPlugin {

    private List<Record> recordSetA, recordSetB;
    private ComparePlugin comparePlugins[];
    private double[] weights;
    private Schema schema;
//    private RecordSet recordSetA;
//    private RecordSet recordSetB;
    private double thresholdHigh;
    private double thresholdLow;
    private RepositoryClient repo;
    private String resultSetId;

    @Override
    public void process(HashMap<String, String> settings) {

        //MATCH
        List<Record> matchingA = recordSetA;
        List<Record> matchingB = recordSetB;

        LinkedList<Result> results = new LinkedList<Result>();

        Iterator<Record> itRef;
        Iterator<Record> itOth = matchingB.iterator();

        double score;
        Record recOth;
        Record recRef;
        MultiMap<Record, Match> matches = new MultiMap<Record, Match>();

        int otherMatches;

        long size = (long) matchingA.size() * (long) matchingB.size();
        long n = 0;

        RecordHelper rh = new RecordHelper(schema);

        double fieldScore;
        while (itOth.hasNext()) {
            otherMatches = 0;
            recOth = itOth.next();
//            assert recOth.hasSource();
            itRef = matchingA.iterator();
            while (itRef.hasNext()) {
                recRef = itRef.next();

                if (!matches.containsKey(recRef)) {
                    matches.put(recRef);
                }

                score = 0;
                for (int i = 0; i < schema.size(); i++) {
                    if (weights[i] > 0) {
                        fieldScore = comparePlugins[i].compare(rh.get(recRef, i), rh.get(recOth, i));
                        assert fieldScore >= 0.0d & fieldScore <= 1.0d : "score " + fieldScore
                                + " is out of valid range.";
                        score += weights[i] * fieldScore;
                    }
                }

                if (score >= thresholdHigh) {
                    otherMatches++;
                    matches.put(recRef, new Match(recOth, score));
                } else if (score >= thresholdLow) {
                    // TODO
                } else {
                }

                assert n < size;

                n++;
//                Log.progress(n, size);
            }
//            if (otherMatches == 0 && INTEGRATE) {
//                // result.add();
//            }
        }

        for (Record ref : matches.keySet()) {
//            assert ref.hasTaxonomy();
            results.add(new Result(ref, matches.get(ref), ref.getTaxonomy()));
        }

//        Log.stop("matching " + dataA.id + " with " + dataB.id);

        //STORE RESULTS

        resultSetId = repo.storeResults(results, schema);
    }

    @Override
    public void init(HashMap<String, String> settings, HashMap<String, Object> context) {
        ClassLoader ldr = (ClassLoader) context.get(Constants.SlotPlugin.CLASSLOADER_FIELD);
        repo = (RepositoryClient) Util.getPluginInstance(ldr, Constants.SlotPlugin.REPOSITORYCLIENT_NAME);

        comparePlugins = Util.getPluginInstances(ldr, settings.get(Constants.CompareTask.PLUGINNAMES_FIELD), new ComparePlugin[0]);

        weights = Util.getDouble(settings.get(Constants.CompareTask.WEIGHTS_FIELD));

        comparePlugins = Util.resizeCmp(comparePlugins, weights);

        thresholdHigh = Util.getDouble(settings.get(Constants.CompareTask.THRESHOLDHIGH_FIELD))[0];
        thresholdHigh = Util.getDouble(settings.get(Constants.CompareTask.THRESHOLDLOW_FIELD))[0];

        String recordSetIdA = settings.get(Constants.CompareTask.RECORDSA_FIELD);
        String recordSetIdB = settings.get(Constants.CompareTask.RECORDSB_FIELD);

        if (comparePlugins.length < weights.length) {
            for (String k : settings.keySet()) {
                System.out.println(k + ": " + settings.get(k));
            }
        }

        recordSetA = repo.getAllRecords(recordSetIdA);
        recordSetA = repo.getAllRecords(recordSetIdB);
        schema = repo.getSchema(recordSetIdA);

        assert Util.equals(schema, repo.getSchema(recordSetIdB));
        // validate 
    }

   

    @Override
    public void destroy() {
//        RecordHelper rh = new RecordHelper(schema);
//        for (Record out : recordsOut) {
//            System.out.println(rh.toString(out));
//        }
    }
}
