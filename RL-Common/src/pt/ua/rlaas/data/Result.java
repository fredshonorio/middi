package pt.ua.rlaas.data;

import java.util.List;

public class Result {

//    public static final String FIELD_REFERENCERECORD = "referenceRecord";
//    public static final String FIELD_MATCHES = "matches";
//    public static final String FIELD_TAXONOMYKEY = "taxonomyKey";
    private Record referenceRecord; // the reference record
    private List<Match> matches; // the matching records
    private String taxonomyKey; // the taxonomy key of the reference

    public Result() {
    }

    public Result(Record referenceRecord, List<Match> matches, String taxonomyKey) {
        this.referenceRecord = referenceRecord;
        this.matches = matches;
        this.taxonomyKey = taxonomyKey;
    }

    public Record getReferenceRecord() {
        return referenceRecord;
    }

    public void setReferenceRecord(Record referenceRecord) {
        this.referenceRecord = referenceRecord;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public String getTaxonomyKey() {
        return taxonomyKey;
    }

    public void setTaxonomyKey(String taxonomyKey) {
        this.taxonomyKey = taxonomyKey;
    }
}
