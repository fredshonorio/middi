package pt.ua.rlaas.util;

import pt.ua.rlaas.data.Match;
import pt.ua.rlaas.data.Result;
import pt.ua.rlaas.data.Schema;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class ResultHelper {

    public final Schema schema;
    private RecordHelper recordHelper;

    public ResultHelper(Schema schema) {
        this.schema = schema;
        this.recordHelper = new RecordHelper(schema);
    }

    public ResultHelper(RecordHelper recordHelper) {
        this.schema = recordHelper.schema;
        this.recordHelper = recordHelper;
    }

    public CharSequence toString(Result result) {
        StringBuffer buff = new StringBuffer();
        RecordHelper h = recordHelper;

        buff.append('[');
        buff.append("reference: ");
        buff.append(h.toString(result.getReferenceRecord()));
        buff.append(" taxonomy: ");
        buff.append(result.getTaxonomyKey());
        for (Match match : result.getMatches()) {
            buff.append("\n\t match: [ record: ");
            buff.append(h.toString(match.getMatchingRecord()));
            buff.append(" score: ");
            buff.append(match.getScore());
            buff.append(']');
        }
        buff.append('\n');
        buff.append('}');
        buff.append(']');
        return buff;
    }
}
