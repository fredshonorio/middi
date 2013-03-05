package test.persistence;

import data.Record;
import data.Schema;
import java.util.Random;

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
        Record r = schema.newRecord();
        for (int i = 0; i < schema.length(); i++) {
            r.set(i, rndString(SIZE));
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
