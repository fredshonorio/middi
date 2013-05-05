package pt.ua.rlaas.tasks;

import pt.ua.rlaas.data.Schema;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class CompareConfig {

    private Schema schema;
    private String[] plugins;
    private double[] weights;
    private double thresholdLow;
    private double thresholdHigh;

    public CompareConfig() {
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public String[] getPlugins() {
        return plugins;
    }

    public void setPlugins(String[] plugins) {
        this.plugins = plugins;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public double getThresholdLow() {
        return thresholdLow;
    }

    public void setThresholdLow(double thresholdLow) {
        this.thresholdLow = thresholdLow;
    }

    public double getThresholdHigh() {
        return thresholdHigh;
    }

    public void setThresholdHigh(double thresholdHigh) {
        this.thresholdHigh = thresholdHigh;
    }

    public CompareConfig(Schema schema, String[] plugins, double[] weights, double thresholdLow,
            double thresholdHigh) {
        super();

        assert schema != null;
        assert plugins != null;
        assert weights != null;
        assert weights.length == plugins.length;
        assert validWeights(weights);
        assert thresholdLow >= 0;
        assert thresholdLow < 1;
        assert thresholdHigh >= 0;
        assert thresholdHigh > thresholdLow;
        assert thresholdHigh < 1;

        this.schema = schema;
        this.plugins = plugins;
        this.weights = weights;
        this.thresholdLow = thresholdLow;
        this.thresholdHigh = thresholdHigh;
    }

    public void reWeight(double[] weights) {
        assert weights != null;
        assert validWeights(weights);

        System.arraycopy(weights, 0, this.weights, 0, this.weights.length);

        assert validWeights(this.weights);
    }

    private static boolean validWeights(double[] weights) {
        assert weights != null;

        double sum = 0;

        for (double w : weights) {
            sum += w;
        }

        return sum - 1f < 0.001f;
    }
}
