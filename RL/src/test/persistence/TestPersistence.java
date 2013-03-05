package test.persistence;

import data.Persistence;
import data.RecordSet;
import data.Record;
import data.Schema;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class TestPersistence {

    public static void main(String[] args) {
        Schema schema = new Schema(new String[]{"name", "bi"});
        Persistence p = Persistence.instance(schema);

        RecordSet rSet = new RecordSet(schema);
        for (int i = 0; i < 2000; i++) {
            rSet.add(RandomRecord.getNew(schema));
        }

        String id = p.storeRecordSet(rSet);

        List<Record> fetched = p.getRecords(id);
        
        Iterator<Record> rIt = rSet.iterator();
        Iterator<Record> fIt = fetched.iterator();
        
        Record tmp;
        while (rIt.hasNext()){
            assert fIt.hasNext();
            tmp = fIt.next();
            assert rIt.next().equals(tmp);
            System.out.println(tmp);
        }
    }
}
