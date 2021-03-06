/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.rlaas;

import pt.ua.rlaas.data.Match;
import pt.ua.rlaas.data.PersistenceException_Exception;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.RecordSet;
import pt.ua.rlaas.data.Repository;
import pt.ua.rlaas.data.Repository_Service;
import pt.ua.rlaas.data.Result;
import pt.ua.rlaas.data.Schema;
import java.util.Arrays;
import java.util.List;
import pt.ua.rlaas.util.RecordHelper;
import pt.ua.rlaas.util.ResultHelper;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Test_repo {

    static int tries = 20;
    static int t = 0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Repository r = new Repository_Service().getRepositoryPort();

        while (t < tries) {
            t++;

            System.out.println("######### " + t);
            
            
            System.out.println("new RecordSet");
            RecordSet set = new RecordSet();

            Schema schema = new Schema("nome", "bi");
            System.out.println("new Schema: " + schema);
            set.setSchema(schema);
            RecordHelper helper = new RecordHelper(schema);

            Record r1 = new Record("i1", "zé1", "123");
            System.out.println("add " + helper.toString(r1));
            set.add(r1);

            Record r2 = helper.newRecord("i1", "zé2", "124");
            System.out.println("add " + helper.toString(r2));
            set.add(r2);

            Record r3 = helper.newRecord("i1", "zé3", "125");
            System.out.println("add " + helper.toString(r3));
            set.add(r3);

            Record r4 = helper.newRecord("i1", "zé4", "126");
            System.out.println("add " + helper.toString(r4));
            set.add(r4);

            String id = r.storeRecordSet(set);
            System.out.println("id: " + id);

            System.out.println("get all records");
            List<Record> tmp = r.getAllRecords(id);

            for (Record record : tmp) {
                System.out.println(helper.toString(record));
            }

            System.out.println("get 0-2 records");
            tmp = r.getRecords(id, 0, 2);

            for (Record record : tmp) {
                System.out.println(helper.toString(record));
            }

            System.out.println("get 0-5 records");
            tmp = r.getRecords(id, 0, 5);

            for (Record record : tmp) {
                System.out.println(helper.toString(record));
            }

            System.out.println("get 6-7 records");
            tmp = r.getRecords(id, 6, 1);

            for (Record record : tmp) {
                System.out.println(helper.toString(record));
            }

            String rId = r.storeResults(Arrays.asList(new Result(r1, Arrays.asList(new Match(r2, 0.7), new Match(r3, 0.5)), "zé"), new Result(r2, Arrays.asList(new Match(r3, 0.7)), "1")), schema);
            //

            List<Result> tmpR = r.getAllResults(rId);
            ResultHelper rhelper = new ResultHelper(schema);

            System.out.println("get all results");
            for (Result res : tmpR) {
                System.out.println(rhelper.toString(res));
            }

            System.out.println("get \"1\" results");
            tmpR = r.getTaxonomyResults(rId, "1");
            for (Result res : tmpR) {
                System.out.println(rhelper.toString(res));
            }

            System.out.println("get \"zé\" results");
            tmpR = r.getTaxonomyResults(rId, "zé");
            for (Result res : tmpR) {
                System.out.println(rhelper.toString(res));
            }
//        try {
//            r.operation();
//        } catch (Throwable ex) {
//            System.out.println("#####");
//            Logger.getLogger(Test_repo.class.getName()).log(Level.SEVERE, null, ex);
//        }

        }
    }
}
