package pt.ua.rlaas.plugin;

import java.util.List;

import pt.ua.rlaas.data.Match;

public interface ExportPlugin {

    public void export(List<Match> matches);

}
