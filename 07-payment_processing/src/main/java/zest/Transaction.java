package zest;

import java.util.ArrayList;

public class Transaction {

    private String id;
    private boolean evaluated;
    private boolean processed;
    private boolean published;
    private ArrayList<AuditService> auditServices = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(boolean evaluated) {
        this.evaluated = evaluated;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public ArrayList<AuditService> getAuditServices() {
        return auditServices;
    }

    public void addAuditService(AuditService auditService){
        this.auditServices.add(auditService);
    }

    public void setAuditServices(ArrayList<AuditService> auditServices) {
        this.auditServices = auditServices;
    }
}
