/**
 * 
 */
package data;

import java.util.ArrayList;

import plugin.ImportPlugin;
import plugin.TransformPlugin;

/**
 * @author Frederico Honório <fredericohonorio@ua.pt>
 * 
 */
public class DataSource {

    public final String id;
    public final ImportPlugin importPlugin;
    public final ArrayList<TransformPlugin> transformsImport;

    public Schema initialSchema, finalSchema;

    public DataSource(String id, ImportPlugin importPlugin, ArrayList<TransformPlugin> transformsImport) {
	// TODO allow livefeed
	assert id != null && !id.isEmpty();
	assert importPlugin != null;
	assert transformsImport != null;

	this.id = id;
	this.importPlugin = importPlugin;
	this.transformsImport = transformsImport;
    }

    public void add(Record r) {
	assert !r.hasSource();
	r.setSource(id);
    }

}
