/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test_repo;

import data.Record;
import data.RecordSet;
import data.Repository;
import data.Repository_Service;
import data.Result;
import data.Schema;
import java.util.Arrays;
import java.util.List;
import util.RecordHelper;
import util.ResultHelper;
import util.SchemaHelper;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Test_repo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
//
        Repository r = new Repository_Service().getRepositoryPort();

        Schema schema = SchemaHelper.newSchema("nome", "bi");


//        RecordSet set = r.getRecords("513625c2e433df575eece9ec");
//
//        Schema schema = new Schema();
//        schema.getFieldNames().addAll(Arrays.asList("a", "b"));
//
//        RecordHelper h = new RecordHelper(schema);
//
//        for (Record record : set) {
//            System.out.println(h.toString(record));
//        }

//        System.out.println("\n\n\n");
        List<Result> results = r.getResults("513625c3e433df575eecf1bd", "asd");
//

        System.out.println(results.size());
        ResultHelper rh = new ResultHelper(schema);


        for (Result result : results) {
            System.out.println(rh.toString(result));
        }

//        System.out.println(set);
////        rec.get

        //        sch.


//        r.test2(rec);
    }
}
