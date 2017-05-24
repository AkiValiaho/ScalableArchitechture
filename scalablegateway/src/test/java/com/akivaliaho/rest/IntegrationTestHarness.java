package com.akivaliaho.rest;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by akivv on 6.5.2017.
 */
@Slf4j
public class IntegrationTestHarness {

    private ExecutorService executorService;

    public String addCommand(String s) {
        //TODO Fix command execution order, OR CREATE A DEDICATED configuration holder to decouple everything
        //Basic execution of runnable jar with -jar switch
        return "java -jar " + s;
    }

    public void startCoreServicesAnd(String[] servicesToStart) {
        //TODO
    }


    public void startServices() {
        Runtime runtime = Runtime.getRuntime();
        executorService = Executors.newCachedThreadPool();
        String property = System.getProperty("user.dir");
        File file = new File(property);
        property = file.getParent();
        //Traverse the directory structure to find the runnable jars
        List<String> runnables = traversePath(property);
        log.debug("Number of runnables to start: {}", runnables.size());
        runnables.forEach(s -> log.debug("Starting runnable: {}", s));
        AtomicInteger readyRunnables = new AtomicInteger(0);
        runnables.parallelStream().map(this::addCommand)
                .forEach(cmd -> {
                    try {
                        Process exec = runtime.exec(cmd);
                        executorService.submit(() -> {
                            executorService.submit(() -> {
                                InputStream inputStream = exec.getInputStream();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                                String line;
                                try {
                                    while ((line = bufferedReader.readLine()) != null) {
                                        log.info(line);
                                        if (line.contains("Started") || (line.contains("Apache Camel 2.18.3") && line.contains("started"))) {
                                            readyRunnables.incrementAndGet();
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        while (readyRunnables.get() != runnables.size()) {
            log.debug("Runnables not ready yet, number of runnables ready: {}", readyRunnables.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private List<String> traversePath(String property) {
        File starter = new File(property);
        List<String> jars = new ArrayList<>();
        traversePath(starter, jars);
        return jars;
    }

    private void traversePath(File starter, List<String> jars) {
        if (starter.isDirectory()) {
            File[] files = starter.listFiles();
            Boolean hasADirectory = false;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.getName().contains("jar") && notInFilters(file.getName())) {
                    jars.add(file.getAbsolutePath());
                    //Get rid of all those .-folders in the recursion
                } else if (file.isDirectory() && !file.isHidden()) {
                    traversePath(file, jars);
                }
            }
        }
    }

    private boolean notInFilters(String name) {
        String[] forbiddenNames = {"original", "communication", "surefire", "events", "configurator", "esbrouter", "scalablegateway"};
        for (int i = 0; i < forbiddenNames.length; i++) {
            if (name.contains(forbiddenNames[i])) {
                return false;
            }
        }
        return true;
    }

    public void stopServices() {
        executorService.shutdownNow();
    }
}
