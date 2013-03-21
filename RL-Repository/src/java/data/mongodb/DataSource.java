/**
 * 
 */
package data.mongodb;

import java.util.ArrayList;

import plugin.ImportPlugin;
import plugin.TransformPlugin;

/**
 * @author Frederico Hon�rio <fredericohonorio@ua.pt>
 * 
 */
public class DataSource {

    public final String id;
    public final ImportPlugin importPlugin;
    public final ArrayList<TransformPlugin> transformsImport;

    public DataSource(String id, ImportPlugin importPlugin, ArrayList<TransformPlugin> transformsImport) {
	// TODO allow livefeed
	assert id != null && !id.isEmpty();
	assert importPlugin != null;
	assert transformsImport != null;

	this.id = id;
	this.importPlugin = importPlugin;
	this.transformsImport = transformsImport;
    }

    public void add(DBRecord r) {
//	assert !r.hasSource();
//	r.setSource(id);
    }

}
