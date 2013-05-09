/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rl.testplugins;

import java.util.HashMap;
import java.util.List;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.Schema;
import pt.ua.rlaas.plugin.TransformPlugin;
import pt.ua.rlaas.util.RecordHelper;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class FirstLetterTaxonomy implements TransformPlugin {

    @Override
    public void init(HashMap<String, String> settings) {
        System.out.println("FirstLetterTaxonomy init: OK");
    }

    @Override
    public List<Record> transform(List<Record> records, Schema schema, String[] fields) {
        RecordHelper rh = new RecordHelper(schema);

        for (Record r : records) {
            String t = rh.get(r, "name").toUpperCase();

            if (t.length() >= 3) {
                t = t.substring(0, 3);
            }

            r.setTaxonomy(t);
        }

        return records;
    }

    @Override
    public void destroy() {
    }
}
