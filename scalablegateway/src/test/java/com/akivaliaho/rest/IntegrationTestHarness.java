package com.akivaliaho.rest;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by akivv on 6.5.2017.
 */
@Slf4j
public class IntegrationTestHarness {

    private final RunnableTools runnableTools;
    private ExecutorService executorService;

    public IntegrationTestHarness() {
        this.runnableTools = new RunnableTools();
    }

    public void startCoreServicesAnd(String[] servicesToStart) {
        //TODO
    }


    public void startServices() {
        //TODO Refactor this horror
        Runtime runtime = Runtime.getRuntime();
        executorService = Executors.newCachedThreadPool();
        String property = getRootDir();
        //Traverse the directory structure to find the runnable jars
        List<String> runnables = findRunnables(property);
        log.debug("Number of runnables to start: {}", runnables.size());
        runnables.forEach(s -> log.debug("Starting runnable: {}", s));
        runnableTools.startRunnables(runnables, runtime);

    }

    private String getRootDir() {
        String property = System.getProperty("user.dir");
        File file = new File(property);
        property = file.getParent();
        return property;
    }

    private List<String> findRunnables(String property) {
        File starter = new File(property);
        List<String> jars = new ArrayList<>();
        findRunnables(starter, jars);
        return jars;
    }

    private void findRunnables(File starter, List<String> jars) {
        if (starter.isDirectory()) {
            File[] files = starter.listFiles();
            Boolean hasADirectory = false;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.getName().contains("jar") && notInFilters(file.getName())) {
                    jars.add(file.getAbsolutePath());
                    //Get rid of all those .-folders in the recursion
                } else if (file.isDirectory() && !file.isHidden()) {
                    findRunnables(file, jars);
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
        runnableTools.destroyProcesses();
    }
}
