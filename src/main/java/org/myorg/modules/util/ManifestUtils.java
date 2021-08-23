package org.myorg.modules.util;

import java.net.URL;

public class ManifestUtils {

    private static final String MANIFEST_DIR = "META-INF/manifest";
    private static final String MANIFEST_EXTENSION = ".manifest.json";

    public static URL getManifest(String subsystemUuid) {
        return ManifestUtils.class.getClassLoader().getResource(MANIFEST_DIR + "/" + subsystemUuid + MANIFEST_EXTENSION);
    }

}