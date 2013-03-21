package pt.ua.rlaas.data.mongodb;

import pt.ua.rlaas.data.Schema;
import pt.ua.rlaas.plugin.ComparePlugin;

/**
 * Defines a compare configuration.
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class CompareConfiguration {

    public final Schema schema;
    public final ComparePlugin[] plugins;
    public final double[] weights;
    public final double thresholdLow;
    public final double thresholdHigh;

    public CompareConfiguration(Schema schema, ComparePlugin[] plugins, double[] weights, double thresholdLow,
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

    /**
     * Change the weights of the configuration.
     * @param weights the weights.
     */
    public void reWeight(double[] weights) {
	assert weights != null;
	assert validWeights(weights);
        System.arraycopy(weights, 0, this.weights, 0, this.weights.length);

	assert validWeights(this.weights);
    }

    private static boolean validWeights(double[] weights) {
	assert weights != null;

	double sum = 0;

	for (double w : weights)
	    sum += w;

	return sum - 1f < 0.001f;
    }

}
