package data;

import plugin.ComparePlugin;

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

    public void reWeight(double[] weights) {
	assert weights != null;
	assert validWeights(weights);

	for (int i = 0; i < this.weights.length; i++) {
	    this.weights[i] = weights[i];
	}

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
