package pt.ua.rlaas.plugin.command;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Luís S. Ribeiro
 */
public class Command 
{
    public static int INIT = 0;
    public static int PROCESS = 1;
    public static int DESTROY = 2;
    
    private static ArrayList<Integer> commands = new ArrayList<>(Arrays.asList(new Integer[] {INIT,PROCESS,DESTROY}));
    
    public static boolean valid(int command)
    {
        return commands.contains(command);
    }
}
