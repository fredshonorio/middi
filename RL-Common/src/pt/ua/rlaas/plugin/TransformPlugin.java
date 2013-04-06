package pt.ua.rlaas.plugin;

import java.util.HashMap;
import java.util.List;
import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.Schema;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public interface TransformPlugin {

    public void init(HashMap<String, String> settings);

    public List<Record> transform(List<Record> records, Schema schema);
}
