package com.akivaliaho.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by vagrant on 6/24/17.
 */
public class LoggingTools {

    private final ExecutorService executorService;

    public LoggingTools() {
        this.executorService = Executors.newCachedThreadPool();
    }
    private void startLogging(LoggingTask loggingTask) {
        executorService.submit(loggingTask);
    }

    public void submitLoggingTask(LoggingTask processStartTriggerMonitoring) {
        startLogging(processStartTriggerMonitoring);
    }
}
