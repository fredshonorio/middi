package pt.ua.rlaas.util;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Constants {

    public static class SlotPlugin {

        public final static String PREFIX = "task.plugin";
        public final static String CLASSLOADER_FIELD = PREFIX + ".classloader";
        public final static String REPOSITORYCLIENT_NAME = "pt.ua.rlaas.RepositoryClient";
        public final static String TASKID_FIELD = PREFIX + ".taskId";
    }

    public static class UpdateTask {

        public final static String PREFIX = "task.plugin.update";
        public final static String METAPLUGIN = "pt.ua.rlaas.metaplugin.UpdateMetaPlugin";
        public final static String SCHEMA = PREFIX + ".schema";
        public final static String RECORDSIN = PREFIX + ".recordSetIn";
        public final static String TRANSFORM_PREFIX = PREFIX + ".transforms";
        public final static String TRANSFORM_COUNT = TRANSFORM_PREFIX + ".count";

        public static String TRANSFORM_PLUGINNAME_FIELD(int transformIndex) {
            return Constants.UpdateTask.TRANSFORM_PREFIX + "." + Integer.toString(transformIndex) + ".pluginName";
        }

        public static String TRANSFORM_FIELDS(int transformIndex) {
            return Constants.UpdateTask.TRANSFORM_PREFIX + "." + Integer.toString(transformIndex) + ".fields";
        }

        public static String TRANSFORM_SETTING_PREFIX(int transformIndex) {
            return Constants.UpdateTask.TRANSFORM_PREFIX + "." + Integer.toString(transformIndex) + ".settings.";
        }
        public final static String COMPARE_PREFIX = PREFIX + ".compare";
        public final static String BLOCK_FIELD = COMPARE_PREFIX + ".block";
        public final static String PLUGINNAMES_FIELD = COMPARE_PREFIX + ".pluginNames";
        public final static String WEIGHTS_FIELD = COMPARE_PREFIX + ".weights";
        public final static String THRESHOLDHIGH_FIELD = COMPARE_PREFIX + ".thresholdHigh";
        public final static String THRESHOLDLOW_FIELD = COMPARE_PREFIX + ".thresholdLow";
        public final static String RESULTNAME = PREFIX + ".resultName";
        public final static String EXPORT_PREFIX = PREFIX + ".export";
        public final static String EXPORT_PLUGINNAME = EXPORT_PREFIX + ".pluginName";
        public final static String EXPORT_SETTING_PREFIX = EXPORT_PREFIX + ".settings.";
        public final static String EXPORTPLUGIN = PREFIX + ".exportPlugin";
    }

    public static class TransformTask {

        public final static String PREFIX = "task.plugin.transform";
        public final static String METAPLUGIN = "pt.ua.rlaas.metaplugin.TransformMetaPlugin";
        public final static String RECORDSIN_FIELD = PREFIX + ".recordSetIn";
        public final static String RECORDSOUT_FIELD = PREFIX + ".recordSetOut";
        public final static String FIRSTRECORD_FIELD = PREFIX + ".firstRecord";
        public final static String LASTRECORD_FIELD = PREFIX + ".lastRecord";
        public final static String PLUGINNAME_FIELD = PREFIX + ".pluginName";
        public final static String FIELDS_FIELD = PREFIX + ".fields";
    }

    public static class CompareTask {

        public final static String PREFIX = "task.plugin.compare";
        public final static String METAPLUGIN = "pt.ua.rlaas.metaplugin.CompareMetaPlugin";
        public final static String RECORDSA_FIELD = PREFIX + ".recordSetA";
        public final static String RECORDSB_FIELD = PREFIX + ".recordSetB";
        public final static String BLOCK_FIELD = PREFIX + ".block";
        public final static String PLUGINNAMES_FIELD = PREFIX + ".pluginNames";
        public final static String WEIGHTS_FIELD = PREFIX + ".weights";
        public final static String THRESHOLDHIGH_FIELD = PREFIX + ".thresholdHigh";
        public final static String THRESHOLDLOW_FIELD = PREFIX + ".thresholdLow";
    }
}
