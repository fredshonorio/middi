package core;

import log.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import plugin.ExportPlugin;
import plugin.ImportPlugin;
import plugin.TransformPlugin;
import data.CompareConfiguration;
import data.DataSource;
import data.Match;
import data.Persistence;
import data.Record;
import data.RecordSet;
import data.Result;
import data.Schema;

public class Process {

    private final static boolean INTEGRATE = true;

    private final ArrayList<DataSource> sources;

    public CompareConfiguration compareConfig;

    private final HashMap<String, RecordSet> lastRecordsets;

    private final ExportPlugin exportPlugin;

    public Process(ArrayList<DataSource> sources, CompareConfiguration compareConfig, ExportPlugin exportPlugin) {
	assert sources != null;
	assert compareConfig != null;
	// assert exportPlugin != null;

	this.sources = sources;
	this.compareConfig = compareConfig;
	this.lastRecordsets = new HashMap<String, RecordSet>(sources.size());
	this.exportPlugin = exportPlugin;
    }

    private boolean validIndex(int index) {
	return index >= 0 && index < sources.size();
    }

    private boolean hasSource(String sourceName) {
	return findSource(sourceName) != null;
    }

    private DataSource findSource(String sourceName) {
	for (int i = 0; i < sources.size(); i++) {
	    if (sources.get(i).id.equals(sourceName)) {
		return sources.get(i);
	    }
	}
	return null;
    }

    private int findSourceIdx(String sourceName) {
	assert hasSource(sourceName);

	for (int i = 0; i < sources.size(); i++) {
	    if (sources.get(i).id.equals(sourceName)) {
		return i;
	    }
	}
	return -1;
    }

    public void runImport(int index) {
	assert validIndex(index);

	runImport(sources.get(index));
    }

    public void runImport(String sourceName) {
	assert hasSource(sourceName);

	runImport(findSource(sourceName));
    }

    private void runImport(DataSource dataSource) {
	Log.start("running import for " + dataSource.id);

	ImportPlugin imp = dataSource.importPlugin;

	imp.setup("0");
	imp.start();

	Iterator<Record> it = imp.iterator();

	assert it.hasNext();
	Record first = it.next();

	RecordSet sourceSet = new RecordSet(dataSource.initialSchema);
	dataSource.add(first);
	sourceSet.add(first);

	Record r;
	while (it.hasNext()) {
	    r = it.next();
	    assert r != null;
	    assert r.schema.equals(first.schema);
	    dataSource.add(r);
	    sourceSet.add(r);

	}

	lastRecordsets.put(dataSource.id, sourceSet);

	Log.stop("running import for " + dataSource.id);
    }

    public void runTransforms(int index) {
	assert validIndex(index);

	runTransforms(sources.get(index));
    }

    public void runTransforms(String sourceName) {
	assert hasSource(sourceName);

	runTransforms(findSource(sourceName));
    }

    private void runTransforms(DataSource source) {
	assert source != null;
	assert lastRecordsets.get(source.id) != null;

	lastRecordsets.put(source.id, runTransforms(lastRecordsets.get(source.id), source));
    }

    private RecordSet runTransforms(RecordSet sourceRecords, DataSource source) {
	assert sourceRecords != null;

	LinkedList<RecordSet> tmpRecordSets = new LinkedList<RecordSet>();

	tmpRecordSets.add(sourceRecords);

	for (TransformPlugin transform : source.transformsImport) {
	    System.out.println("running " + transform);
	    tmpRecordSets.add(transform.transform(tmpRecordSets.getLast(), source));
	}

	return tmpRecordSets.getLast();
    }

    public RecordSet getFinalRecordSet(int index) {
	return null;
    }

    public RecordSet getFinalRecordSet(String sourceName) {
	return null;
    }

    @Deprecated
    public List<Result> createResults(int i) {
	// TODO: proper integration with a reference set

	assert validIndex(i);

	RecordSet reference = lastRecordsets.get(sources.get(i).id);

	assert reference != null;

	List<Result> results = new LinkedList<Result>();

	Iterator<Record> itRef = reference.iterator();

	Record recRef;

	while (itRef.hasNext()) {
	    recRef = itRef.next();
	    assert recRef.hasTaxonomy();

	    results.add(new Result(recRef, new LinkedList<Match>(), recRef.getTaxonomy()));
	}
	return results;
    }

    public List<Result> match(int indexA, int indexB) {
	assert validIndex(indexA);
	assert validIndex(indexB);

	return match(sources.get(indexA), sources.get(indexB));
    }

    public List<Result> match(String sourceAName, String sourceBName) {
	assert hasSource(sourceAName);
	assert hasSource(sourceBName);

	return match(findSource(sourceAName), findSource(sourceBName));
    }

    private List<Result> match(DataSource dataA, DataSource dataB) {
	assert dataA != null;
	assert dataB != null;

	RecordSet matchingA = lastRecordsets.get(findSourceIdx(dataA.id));
	RecordSet matchingB = lastRecordsets.get(findSourceIdx(dataB.id));

	return match(dataA, dataB, matchingA, matchingB);
    }

    private List<Result> match(DataSource dataA, DataSource dataB, RecordSet matchingA, RecordSet matchingB) {
	assert dataA != null;
	assert dataB != null;

	assert compareConfig.schema.equals(matchingA.schema);
	assert compareConfig.schema.equals(matchingB.schema);

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

	Log.start("matching " + dataA.id + " with " + dataB.id);

	double fieldScore;
	while (itOth.hasNext()) {
	    otherMatches = 0;
	    recOth = itOth.next();
	    assert recOth.hasSource();
	    itRef = matchingA.iterator();
	    while (itRef.hasNext()) {
		recRef = itRef.next();

		if (!matches.containsKey(recRef))
		    matches.put(recRef);

		score = 0;
		assert recRef.hasSource();
		for (int i = 0; i < compareConfig.schema.length(); i++) {
		    if (compareConfig.weights[i] > 0) {

			fieldScore = compareConfig.plugins[i].compare(recRef.get(i), recOth.get(i));
			assert fieldScore >= 0.0d & fieldScore <= 1.0d;
			score += compareConfig.weights[i] * fieldScore;
		    }
		}

		if (score >= compareConfig.thresholdHigh) {
		    otherMatches++;
		    matches.put(recRef, new Match(recRef, recOth, score));
		} else if (score >= compareConfig.thresholdLow) {
		    // TODO
		} else {
		}

		assert n < size;

		n++;
		Log.progress(n, size);
	    }
	    if (otherMatches == 0 && INTEGRATE) {
		// result.add();
	    }
	}

	for (Record ref : matches.keySet()) {
	    assert ref.hasTaxonomy();
	    results.add(new Result(ref, matches.get(ref), ref.getTaxonomy()));
	}

	Log.stop("matching " + dataA.id + " with " + dataB.id);

	return results;
    }

    public List<Match> matchSingle(Record record, DataSource source) {
	assert record != null;
	assert source != null;
	assert hasSource(source.id);

	Schema schema = source.initialSchema;

	Persistence db = Persistence.instance(schema);

	RecordSet inputSet = new RecordSet(schema).add(record);
	inputSet = runTransforms(inputSet, source);

	assert inputSet.size() == 1;
	record = inputSet.iterator().next();

	assert record.hasTaxonomy();
	List<Record> taxMatches = null; //db.fetchResult(record.getTaxonomy());

	RecordSet taxonomySet = new RecordSet(schema).add(taxMatches);
	RecordSet single = new RecordSet(schema).add(record);

	// WAT
	List<Result> matchResults = match(source, new DataSource("reference", new ImportPlugin() {

	    @Override
	    public void start() {
	    }

	    @Override
	    public void setup(String opt) {
	    }

	    @Override
	    public Iterator<Record> iterator() {
		// TODO Auto-generated method stub
		return null;
	    }
	}, new ArrayList<TransformPlugin>()), single, taxonomySet);

	assert matchResults.size() == 1 || matchResults.size() == 0;

	List<Match> matches;

	if (matchResults.size() == 0)
	    matches = new LinkedList<Match>();
	else
	    matches = matchResults.get(0).matches;

	return matches;
    }
}
