/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot.rim;

/**
 *
 * @author Luís S. Ribeiro
 */
public class CacheData 
{
    private byte[] data;
    private String fileName;
    private boolean isZipped;

    public CacheData() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }    
    
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public boolean isIsZipped() {
        return isZipped;
    }

    public void setIsZipped(boolean isZipped) {
        this.isZipped = isZipped;
    }    
}
