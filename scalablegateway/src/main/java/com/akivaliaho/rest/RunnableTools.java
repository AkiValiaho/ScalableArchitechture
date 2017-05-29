package com.akivaliaho.rest;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by akivv on 27.5.2017.
 */
@Slf4j
public class RunnableTools {
    private final ExecutorService executorService;
    private RunnableHolder runnableHolder;

    public RunnableTools() {
        this.runnableHolder = new RunnableHolder();
        this.executorService = Executors.newCachedThreadPool();
    }

    public String addCommand(String s) {
        //Basic execution of runnable jar with -jar switch
        if (s.contains("camel")) {
            return "java -agentlib:jdwp=transport=dt_socket,server=y,address=8000,suspend=n -jar " + s;
        }
        return "java -jar " + s;
    }

    public void startRunnables(List<String> runnables, Runtime runtime) {
        runnables.parallelStream().map(this::addCommand)
                .forEach(cmd -> {
                    try {
                        Process exec = runtime.exec(cmd);
                        runnableHolder.registerStartedProcess(exec);
                        //                       registeredProcesses.add(exec);
                        executorService.submit(() -> {
                            executorService.submit(() -> {
                                loggingTask(exec);
                            });
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        waitForRunnableInitialization(runnables);
    }

    private void loggingTask(Process exec) {
        InputStream inputStream = exec.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            monitorForStartedTrigger(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void monitorForStartedTrigger(BufferedReader bufferedReader) throws IOException {
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            log.info(line);
            if (line.contains("Started") || (line.contains("Apache Camel 2.18.3") && line.contains("started"))) {
                runnableHolder.incrementStartedRunnables();
            }
        }
    }

    private void waitForRunnableInitialization(List<String> runnables) {
        while (runnableHolder.getStartupSituation() != runnables.size()) {
            log.debug("Runnables not ready yet, number of runnables ready: {}", runnableHolder.getStartupSituation());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void destroyProcesses() {
        this.runnableHolder.getRegisteredProcesses()
                .stream()
                .forEach(process -> {
                            process.destroyForcibly();
                            while (process.isAlive()) {

                            }
                        }
                );
    }
}
