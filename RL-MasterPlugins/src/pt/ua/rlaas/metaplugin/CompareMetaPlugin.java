package pt.ua.rlaas.metaplugin;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import pt.ua.rlaas.RepositoryClient;
import pt.ua.rlaas.plugin.ComparePlugin;
import pt.ua.rlaas.plugin.IContext;
import pt.ua.rlaas.plugin.TaskPlugin;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class CompareMetaPlugin implements TaskPlugin {

//    private HashMap<String, Object> context;
    private IContext context;
    private ComparePlugin[] plugins;

    @Override
    public void process(HashMap<String, String> settings) {
        //compare records
//                System.out.println(cmp.compare("asf", "asdf2"));
    }

    @Override
    public void init(HashMap<String, String> settings, HashMap<String, Object> context) {
        //load records
        //load plugins

        ClassLoader ldr = (ClassLoader) context.get("ClassLoader");
        String[] classNames = Util.getClassNames(settings.get("task.pluginNames"));
        RepositoryClient repo = new RepositoryClient((String) context.get("RepoInstanceAddr"));


        int i = 0;
        plugins = new ComparePlugin[classNames.length];
        for (String className : classNames) {
            plugins[i] = (ComparePlugin) Util.getPluginInstance(ldr, className);
            i++;
        }
    }

    @Override
    public void destroy() {
    }
}
