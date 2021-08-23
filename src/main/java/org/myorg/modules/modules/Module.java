package org.myorg.modules.modules;

import org.json.JSONObject;
import org.myorg.modules.querypool.Query;
import org.myorg.modules.util.ConfigUtils;


public abstract class Module {

    private final ModuleInfo moduleInfo;

    private final JSONObject jsonConfig;

    public Module(String uuid) throws Exception {
        this.moduleInfo = new ModuleInfo(uuid);
        this.jsonConfig = ConfigUtils.getJsonConfig(uuid);
    }

    public ModuleInfo getModuleInfo() {
        return moduleInfo;
    }

    public JSONObject getJsonConfig() {
        return jsonConfig;
    }

    protected Query<Void> init() {
        return pc -> null;
    }

    protected Query<Void> destroy() {
        return pc -> null;
    }

    public abstract String getUuid();
}
