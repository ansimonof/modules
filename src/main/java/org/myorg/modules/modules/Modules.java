package org.myorg.modules.modules;

import org.myorg.modules.querypool.QueryFuture;
import org.myorg.modules.querypool.QueryPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Modules implements ApplicationListener<ContextRefreshedEvent> {

    private final List<? extends Module> modules;
    private final QueryPool queryPool;

    @Autowired
    public Modules(List<? extends Module> modules,
                   QueryPool queryPool) {
        this.modules = modules;
        this.queryPool = queryPool;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (modules.isEmpty()) {
            throw new RuntimeException("No modules");
        }

        sortModules();

        // Инициализируем все модули
        QueryFuture<Void> initQuery = null;
        for (Module module : modules) {
            if (initQuery == null) {
                initQuery = queryPool.execute(module.init());
            } else {
                initQuery.thenApply(aVoid -> module.init());
            }
        }
        initQuery.join();

        for (Module module : modules) {
            queryPool.execute(module.init());
        }
    }

    public void onDestroy() {
        // Выключаем в обратном порядке
        System.out.println(modules.size());
        QueryFuture<Void> destroyQuery = null;
        for (int i = modules.size() - 1; i >= 0; i--) {
            Module module = modules.get(i);
            if (destroyQuery == null) {
                destroyQuery = queryPool.execute(module.destroy());
            } else {
                destroyQuery.thenApply(aVoid -> module.destroy());
            }
        }

        destroyQuery.join();
    }

    private void sortModules() {
        Map<String, Set<String>> uuidToDependencies = modules.stream()
                .collect(Collectors.toMap(Module::getUuid, v -> v.getModuleInfo().getDependencyUuids()));
        List<String> sortedUuids = new ArrayList<>();
        while(!uuidToDependencies.isEmpty()) {
            String nextUuid = null;
            for (String uuid : uuidToDependencies.keySet()) {
                if (sortedUuids.containsAll(uuidToDependencies.get(uuid))) {
                    nextUuid = uuid;
                    break;
                }
            }

            if (nextUuid == null) {
                throw new RuntimeException("Graph of dependencies is not a tree");
            }
            sortedUuids.add(nextUuid);
            uuidToDependencies.remove(nextUuid);
        }

        modules.sort((x,y) -> {
            int i = sortedUuids.indexOf(x.getUuid());
            int j = sortedUuids.indexOf(y.getUuid());
            return Integer.compare(i, j);
        });
    }
}
