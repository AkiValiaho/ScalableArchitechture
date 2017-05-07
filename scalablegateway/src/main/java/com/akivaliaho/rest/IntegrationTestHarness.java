package com.akivaliaho.rest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by akivv on 6.5.2017.
 */
public class IntegrationTestHarness {

    private ExecutorService executorService;

    public String addCommand(String s) {
        //Basic execution of runnable jar with -jar switch
        return "java -jar " + s;
    }

    public void startServices() {
        Runtime runtime = Runtime.getRuntime();
        executorService = Executors.newCachedThreadPool();
        //TODO Implement a generic solution for this
        String property = System.getProperty("user.dir");
        File file = new File(property);
        property = file.getParent();
        //Traverse the directory structure to find the runnable jars
        List<String> runnables = traversePath(property);
        List<Process> processList = runnables.stream()
                .map(this::addCommand)
                .map(cmd -> {
                    try {
                        return runtime.exec(cmd);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());
        executorService.submit(() -> {
            processList.parallelStream()
                    .forEach(process -> {
                        try {
                            executorService.submit(() -> {
                                InputStream inputStream = process.getInputStream();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                                String line;
                                try {
                                    while ((line = bufferedReader.readLine()) != null) {
                                        System.out.println(line);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            process.waitFor();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
        });
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
        String[] forbiddenNames = {"original", "communication", "configurator", "esbrouter", "scalablegateway"};
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