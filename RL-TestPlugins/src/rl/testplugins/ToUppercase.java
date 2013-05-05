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
import pt.ua.rlaas.util.Constants;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class ToUppercase implements TransformPlugin {

    String[] fields;

    @Override
    public void init(HashMap<String, String> settings) {
        String fieldStr = settings.get(Constants.TransformTask.FIELDS_FIELD);
        System.out.println("sett:" + fieldStr);
//        fields = fieldStr.split(";");
//        fields = new String[]{"nome"};
    }

    @Override
    public List<Record> transform(List<Record> records, Schema schema) {
        RecordHelper rh = new RecordHelper(schema);
        fields = new String[]{"name"};
        for (Record r : records) {
            for (String f : fields) {
                rh.set(r, f, rh.get(r, f).toUpperCase());
            }
        }

        return records;
    }
}
