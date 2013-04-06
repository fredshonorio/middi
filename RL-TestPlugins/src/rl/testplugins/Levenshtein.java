package rl.testplugins;

import java.util.HashMap;
import pt.ua.rlaas.plugin.ComparePlugin;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class Levenshtein implements ComparePlugin {

    @Override
    public double compare(String valueA, String valueB) {
        return 0.5656;
    }

}
