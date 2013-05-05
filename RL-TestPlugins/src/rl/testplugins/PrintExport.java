/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rl.testplugins;

import java.util.List;
import pt.ua.rlaas.data.Match;
import pt.ua.rlaas.data.Result;
import pt.ua.rlaas.plugin.ExportPlugin;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class PrintExport implements ExportPlugin {

    @Override
    public void export(List<Match> matches) {
    }

    @Override
    public void liveExport(List<Result> liveResults, List<Result> completeResults) {
        System.out.println("LIVE RESULTS");
        for (Result r : liveResults) {
            System.out.println("\n" + r.getReferenceRecord());
            for (Match m : r.getMatches()) {
                System.out.println("\t" + m);
            }

        }
    }
}
