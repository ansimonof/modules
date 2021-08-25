package org.myorg.modules.util.logback;

import ch.qos.logback.core.PropertyDefinerBase;

public class LogDirPropertyDefiner extends PropertyDefinerBase {

    public static final String LOG_DIR = "logs";

    @Override
    public String getPropertyValue() {
        return LOG_DIR;
    }
}
