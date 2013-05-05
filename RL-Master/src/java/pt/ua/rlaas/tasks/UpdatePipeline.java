package pt.ua.rlaas.tasks;

import java.util.List;
import pt.ua.rlaas.data.Schema;

/**
 *
 * @author Frederico Honório <fredericohonorio@ua.pt>
 */
public class UpdatePipeline {

    private String pipelineName;
    private Schema startingSchema;
    private List<TransformStep> transformSteps;
    private CompareConfig compareConfig;
    private ExportStep exportStep;
    private String domain;

    public UpdatePipeline() {
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }

    public List<TransformStep> getTransformSteps() {
        return transformSteps;
    }

    public void setTransformSteps(List<TransformStep> transformSteps) {
        this.transformSteps = transformSteps;
    }

    public CompareConfig getCompareConfig() {
        return compareConfig;
    }

    public void setCompareConfig(CompareConfig compareConfig) {
        this.compareConfig = compareConfig;
    }

    public ExportStep getExportStep() {
        return exportStep;
    }

    public void setExportStep(ExportStep exportStep) {
        this.exportStep = exportStep;
    }

    public String getDomain() {
        return domain;
    }

    public Schema getStartingSchema() {
        return startingSchema;
    }

    public void setStartingSchema(Schema startingSchema) {
        this.startingSchema = startingSchema;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
