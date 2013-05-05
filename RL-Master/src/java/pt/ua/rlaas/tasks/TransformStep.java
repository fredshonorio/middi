/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pt.ua.rlaas.tasks;

import java.util.HashMap;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class TransformStep {
    private String pluginName;
    private HashMap<String, String> settings;
    private String[] fields;

    public TransformStep() {
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public HashMap<String, String> getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, String> settings) {
        this.settings = settings;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

}
