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
public class ExportStep {
    public String pluginName;
    public HashMap<String, String> settings;

    public ExportStep() {
    }

    public String getPluginName() {
        return pluginName;
    }

    public HashMap<String, String> getSettings() {
        return settings;
    }

}
