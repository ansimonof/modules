package org.myorg.modules.access.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.myorg.modules.access.AccessOp;

public class PrivilegeDto {

    @JsonProperty("privilege")
    private String uniqueKey;

    @JsonProperty("ops")
    private AccessOp[] accessOps;

    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public AccessOp[] getAccessOps() {
        return accessOps;
    }

    public void setAccessOps(AccessOp[] accessOps) {
        this.accessOps = accessOps;
    }
}
