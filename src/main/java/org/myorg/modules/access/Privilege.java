package org.myorg.modules.access;

public class Privilege {

    private int id;
    private String uniqueKey;
    private AccessOpCollection accessOpCollection;

    public Privilege(int id, String uniqueKey, AccessOp... accessOps) {
        this.id = id;
        this.uniqueKey = uniqueKey;
        this.accessOpCollection = new AccessOpCollection(accessOps);
    }

    public int intValue() {
        return id;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public AccessOpCollection getAccessOpCollection() {
        return accessOpCollection;
    }
}
