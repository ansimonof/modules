package org.myorg.modules.module.core;

import org.json.JSONObject;
import org.myorg.modules.modules.ModuleConfig;

public class CoreConfig extends ModuleConfig {

    public static class Builder extends ModuleConfig.ConfigBuilder<CoreConfig> {

        @Override
        public CoreConfig build(JSONObject jsonObject) {
            return new CoreConfig();
        }
    }
}
