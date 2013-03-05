package test.persistence;

import data.Match;
import data.Persistence;
import data.RecordSet;
import data.Record;
import data.Result;
import data.Schema;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class TestPersistence {

    public static void main(String[] args) {
        Schema schema = new Schema(new String[]{"name", "bi"});
        Persistence p = Persistence.instance(schema);

        p.clear();
        System.out.print("Generating random recordset...");

        RecordSet rSet = new RecordSet(schema);
        for (int i = 0; i < 2000; i++) {
            rSet.add(RandomRecord.getNew(schema).setTaxonomy(i < 1000 ? "asd" : "qwe"));
        }
        System.out.println("done.");


        System.out.print("Storing random recordset...");
        String id = p.storeRecordSet(rSet);
        System.out.println("done.");

        System.out.print("Fetching stored records...");
        List<Record> fetched = p.getRecords(id);
        System.out.println("done.");

        Iterator<Record> rIt = rSet.iterator();
        Iterator<Record> fIt = fetched.iterator();

        System.out.print("Checking if stored records match the records in memory...");
        Record tmp;
        while (rIt.hasNext()) {
            assert fIt.hasNext();
            tmp = fIt.next();
            assert rIt.next().equals(tmp);
        }
        System.out.println("done.");

        System.out.print("Creating results...");

        LinkedList<Result> results = new LinkedList<>();
        for (Record r : rSet) {
            results.add(new Result(r, new LinkedList<Match>(), r.getTaxonomy()));
        }
        System.out.println("done.");

        System.out.print("Storing results...");
        String resultId = p.storeResults(results);
        System.out.println("done.");

        System.out.print("Fetching stored results...");
        List<Result> fetchedResultsASD = p.getResults(resultId, "asd");
        List<Result> fetchedResultsQWE = p.getResults(resultId, "qwe");
        System.out.println("done.");

        System.out.print("Checking if stored results match the results in memory...");
        Iterator<Result> rrIt = results.iterator();
        Iterator<Result> asdIt = fetchedResultsASD.iterator();
        Iterator<Result> qweIt = fetchedResultsQWE.iterator();

        Result tmpref, tmpasd, tmpqwe;

        while (asdIt.hasNext()) {
            assert rrIt.hasNext();
            tmpref = rrIt.next();
            tmpasd = asdIt.next();
            assert tmpasd.equals(tmpref);
        }
        while (qweIt.hasNext()) {
            assert rrIt.hasNext();
            tmpref = rrIt.next();
            tmpqwe = qweIt.next();
            assert tmpqwe.equals(tmpref);
        }
        System.out.println("done.");

    }
}
