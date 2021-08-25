package org.myorg.modules.util.logback;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.concurrent.atomic.AtomicLong;

public class LogbackThreadId extends ClassicConverter {

    private static AtomicLong nextId = new AtomicLong(0);
    private static final ThreadLocal<String> threadId = ThreadLocal.withInitial(
            () -> String.format("%08d", nextId.incrementAndGet())
    );

    @Override
    public String convert(ILoggingEvent event) {
        return threadId.get();
    }
}
