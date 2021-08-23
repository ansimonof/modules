package org.myorg.modules;

import org.myorg.modules.configuration.database.DbConfig;
import org.myorg.modules.modules.Modules;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class ModulesStarter {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DbConfig.class);
        context.refresh();

        addShutdownHookAndStop(context, () -> {
            Modules modules = context.getBean(Modules.class);
            // Выключаем все модули
            modules.onDestroy();
            return null;
        });
    }

    public static void addShutdownHookAndStop(ApplicationContext context,
                                              Callable<Void> callable) {
        // ShutdownHook
        FutureTask<Void> stopSignal = new FutureTask<>(callable);
        Runtime.getRuntime().addShutdownHook(new Thread(stopSignal, "shutdownHook"));

        try {
            stopSignal.get();
        } catch (Throwable ignore) {}
    }

    public static void addShutdownHook(Runnable runnable) {
        Runtime.getRuntime().addShutdownHook(new Thread(runnable, "shutdownHook"));
    }


}
