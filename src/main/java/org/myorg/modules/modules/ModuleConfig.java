package org.myorg.modules.modules;

import org.json.JSONObject;

public class ModuleConfig {

    public abstract static class ConfigBuilder<T> {

        public abstract T build(JSONObject jsonObject);
    }
}
