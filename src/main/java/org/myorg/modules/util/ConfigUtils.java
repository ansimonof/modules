package org.myorg.modules.util;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigUtils {

    private static final String CONFIG_DIR = "data/config";

    private static final String SUFFIX = ".config.json";

    public static JSONObject getJsonConfig(String uuid) throws IOException {
        Path path = Paths.get(CONFIG_DIR).toAbsolutePath().resolve( uuid + SUFFIX);
        JSONObject config = new JSONObject(new String(Files.readAllBytes(path), StandardCharsets.UTF_8));
        return config;
    }
}
