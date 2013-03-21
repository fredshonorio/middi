package test.persistence;

import pt.ua.rlaas.data.Record;
import pt.ua.rlaas.data.Schema;
import java.util.Random;
import pt.ua.rlaas.util.RecordHelper;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class RandomRecord {

    public final static int SIZE = 15;
    private static Random rnd = new Random();
    private static StringBuffer buffer = new StringBuffer(SIZE);
    private static final int alpha_len = 'z' - 'a';
//    private static final int cap_dist = Math.abs('A' - 'a');

    public static Record getNew(Schema schema) {
        RecordHelper h = new RecordHelper(schema);
        Record r = h.newRecord("id");
        for (int i = 0; i < schema.size(); i++) {
            h.set(r, i, null);
        }
        return r;
    }

    public static String rndString(int size) {
        buffer.delete(0, SIZE);
        for (int i = 0; i < size; i++) {
            buffer.append((char) ('a' + (rnd.nextInt(alpha_len) + 1)));
        }
        return buffer.toString();
    }
}
