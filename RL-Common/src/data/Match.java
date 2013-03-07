package data;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Match {

    private Record matchingRecord;
    private double score;

    public Match(Record matchingRecord, double score) {
        this.matchingRecord = matchingRecord;
        this.score = score;
    }

    public Match() {
    }

    public Record getMatchingRecord() {
        return matchingRecord;
    }

    public void setMatchingRecord(Record matchingRecord) {
        this.matchingRecord = matchingRecord;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
