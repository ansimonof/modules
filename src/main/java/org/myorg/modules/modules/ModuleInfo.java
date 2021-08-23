package org.myorg.modules.modules;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.myorg.modules.util.ManifestUtils;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ModuleInfo {

    private String name;
    private Set<String> dependencyUuids;

    public ModuleInfo(String uuid) throws Exception {
        URL manifestUrl = ManifestUtils.getManifest(uuid);
        JSONObject manifest = new JSONObject(new JSONTokener(manifestUrl.openStream()));
        init(manifest);
    }

    public String getName() {
        return name;
    }

    public Set<String> getDependencyUuids() {
        return dependencyUuids;
    }

    private void init(JSONObject manifest) {
        name = manifest.getString("name");
        dependencyUuids = new HashSet<>();

        JSONArray dependencies = manifest.getJSONArray("dependencies");
        for (Object o : dependencies) {
            dependencyUuids.add((String) o);
        }
    }
}
