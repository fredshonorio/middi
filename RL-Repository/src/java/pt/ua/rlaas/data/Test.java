/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.rlaas.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import pt.ua.rlaas.data.mongodb.MongoPersistence;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Test {

    public static void main(String[] args) {

        MongoPersistence p = MongoPersistence.instance();
////
//        Record ref = new Record(null, "ZÉZINHO", "1234");
////
//        List<Result> res = new ArrayList<Result>();
//        res.add(new Result(ref, new LinkedList<Match>(), null));
//
//
//        Match m1 = new Match(new Record(null, "a", "11"), 0.5);
//        Match m2 = new Match(new Record(null, "b", "111"), 0.7);
////
//
////        res.get(0).getMatches().add(m1);
//        res.get(0).getMatches().add(m2);
//
////        System.out.println(p.getResultsByName("persistence", null));
        System.out.println("FIRST");
        r(p.getResultsByName("result_pipeline", null));

//        p.storeResultsId(res, new Schema("name", "bi"), "persistence");
//
//        System.out.println("SECOND");
//        r(p.getResultsByName("persistence", null));

    }

    public static void r(List<Result> res) {

        for (Result result : res) {
            System.out.println(result.getReferenceRecord());
            for (Match m : result.getMatches()) {
                System.out.println("\t" + m);
            }
        }

    }
}
