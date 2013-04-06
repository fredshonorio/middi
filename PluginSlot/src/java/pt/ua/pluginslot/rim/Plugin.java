/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.ua.pluginslot.rim;

import java.util.HashMap;

/**
 *
 * @author Luís S. Ribeiro
 */
public class Plugin {

    protected byte[] value;
    protected String id;
    protected HashMap<String, String> settings;

    public String getId() {
        return id;
    }

    public byte[] getValue() {
        return value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }

    public void setSettings(HashMap<String, String> settings) {
        this.settings = settings;
    }

    public HashMap<String, String> getSettings() {
        return settings;
    }
}
