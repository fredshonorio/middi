/**
 * 
 */
package core;

/**
 * @author Frederico Hon�rio <fredericohonorio@ua.pt>
 * 
 */
public interface LogWriter {

    public void write(CharSequence message);

    public void writeReturn(CharSequence message);
}
