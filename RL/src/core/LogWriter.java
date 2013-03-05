/**
 * 
 */
package core;

/**
 * @author Frederico Honório <fredericohonorio@ua.pt>
 * 
 */
public interface LogWriter {

    public void write(CharSequence message);

    public void writeReturn(CharSequence message);
}
