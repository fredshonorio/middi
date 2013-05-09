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
public class ToUppercase implements TransformPlugin {

    @Override
    public void init(HashMap<String, String> settings) {
        System.out.println("ToUppercase init: OK");
    }

    @Override
    public List<Record> transform(List<Record> records, Schema schema, String[] fields) {
        RecordHelper rh = new RecordHelper(schema);
        for (Record r : records) {
            for (String f : fields) {
                rh.set(r, f, rh.get(r, f).toUpperCase());
            }
        }

        return records;
    }

    @Override
    public void destroy() {
    }
}
