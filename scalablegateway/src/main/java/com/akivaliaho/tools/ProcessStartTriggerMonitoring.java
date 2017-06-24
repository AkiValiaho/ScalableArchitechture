package com.akivaliaho.tools;

import com.akivaliaho.rest.RunnableHolder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by vagrant on 6/24/17.
 */
@Slf4j
public class ProcessStartTriggerMonitoring implements LoggingTask {
    private final RunnableHolder runnableHolder;
    private final BufferedReader bufferedReader;

    public ProcessStartTriggerMonitoring(RunnableHolder runnableHolder,BufferedReader bufferedReader) {
        this.runnableHolder = runnableHolder;
        this.bufferedReader = bufferedReader;
    }


    @Override
    public void run() {
        try {
            monitorForStartedTrigger();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void monitorForStartedTrigger() throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            log.info(line);
            if (line.contains("Started") || (line.contains("Apache Camel 2.18.3") && line.contains("started"))) {
                runnableHolder.incrementStartedRunnables();
            }
        }
    }
}
