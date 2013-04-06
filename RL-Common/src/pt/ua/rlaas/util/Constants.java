package pt.ua.rlaas.util;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Constants {

    public class SlotPlugin {

        public final static String PREFIX = "task.plugin";
        public final static String CLASSLOADER_FIELD = PREFIX + ".classloader";
        public final static String REPOSITORYCLIENT_NAME = "pt.ua.rlaas.RepositoryClient";
    }

    public class TransformTask {

        public final static String PREFIX = "task.plugin.transform";
        public final static String METAPLUGIN = "pt.ua.rlaas.metaplugin.TransformMetaPlugin";
        public final static String RECORDSIN_FIELD = PREFIX + ".recordSetIn";
        public final static String RECORDSOUT_FIELD = PREFIX + ".recordSetOut";
        public final static String FIRSTRECORD_FIELD = PREFIX + ".firstRecord";
        public final static String LASTRECORD_FIELD = PREFIX + ".lastRecord";
        public final static String PLUGINNAME_FIELD = PREFIX + ".pluginName";
        public final static String FIELDS_FIELD = PREFIX + ".fields";
    }
}
