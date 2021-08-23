package org.myorg.modules.module.core;

import org.myorg.modules.modules.Module;
import org.springframework.stereotype.Component;

@Component
public class CoreModule extends Module {

    private static final String UUID = "org.myorg.module.core";

    private final CoreConfig config;

    public CoreModule() throws Exception {
        super(UUID);

        this.config = new CoreConfig.Builder().build(getJsonConfig());
    }

    @Override
    public String getUuid() {
        return UUID;
    }

    public CoreConfig getConfig() {
        return config;
    }
}
