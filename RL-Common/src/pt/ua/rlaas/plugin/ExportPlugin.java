package pt.ua.rlaas.plugin;

import java.util.List;

import pt.ua.rlaas.data.Match;
import pt.ua.rlaas.data.Result;

public interface ExportPlugin extends Plugin {

    public void export(List<Match> matches);

    public void liveExport(List<Result> liveResults, List<Result> completeResults);
}
