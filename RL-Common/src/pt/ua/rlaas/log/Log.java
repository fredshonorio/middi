package pt.ua.rlaas.log;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;

public class Log {

    private static boolean VERBOSE = true;
    private final static HashMap<String, SimpleEntry<Long, Long>> map = new HashMap<String, SimpleEntry<Long, Long>>();
    private final static int PROGRESS_STEP = 10;
    private static long prevP = 0;

    public static LogWriter writer = null;

    public static void start(String name) {
	assert name != null;

	map.put(name, new SimpleEntry<Long, Long>(System.currentTimeMillis(), null));

	if (VERBOSE) {
	    write("\"" + name + "\" started.");
	}
    }

    public static long stop(String name) {
	assert name != null;
	assert map.containsKey(name);
	assert map.get(name).getValue() == null;

	SimpleEntry<Long, Long> e = map.get(name);
	e.setValue(System.currentTimeMillis());

	long runtime = e.getValue() - e.getKey();

	if (VERBOSE) {
	    write("\"" + name + "\" completed in " + runtime + " ms.");
	}

	assert map.get(name).getValue() != null;

	prevP = 0;
	return runtime;
    }

    public static long getRuntime(String name) {
	assert name != null;
	assert map.containsKey(name);
	assert map.get(name).getValue() != null;

	SimpleEntry<Long, Long> e = map.get(name);

	return e.getValue() - e.getKey();
    }

    public static void log(CharSequence message) {
	write(message);
    }

    public static void progress(long current, long max) {
	long p = (current * 100) / max;

	if (p != prevP && p % PROGRESS_STEP == 0) {
	    writeReturn(p + "%");
	}

	prevP = p;

	if (p == 100) {
	    write("");
	}
    }

    private static void writeReturn(CharSequence message) {
	assert message != null;

	if (writer != null)
	    writer.writeReturn(message);
	System.out.print("\r" + message);
    }

    private static void write(CharSequence message) {
	assert message != null;

	if (writer != null)
	    writer.write(message);

	System.out.println(message);
    }
}
