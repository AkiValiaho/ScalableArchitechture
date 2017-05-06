package com.akivaliaho.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by akivv on 6.5.2017.
 */
public class IntegrationTestHarness {

    private ExecutorService executorService;

    public void startServices() {
        Runtime runtime = Runtime.getRuntime();
        try {
            executorService = Executors.newCachedThreadPool();
            List<Process> processList = new ArrayList<>();
            Process exec = runtime.exec("java -jar /home/akivv/ScalableArchitecture/calculatorservice/target/calculatorservice-1.0-SNAPSHOT.jar");
            Process exec1 = runtime.exec("java -jar /home/akivv/ScalableArchitecture/camel/target/camel-1.0-SNAPSHOT.jar");
            processList.add(exec);
            processList.add(exec1);
            executorService.submit(() -> {
                processList.parallelStream()
                        .forEach(process -> {
                            try {
                                process.waitFor();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        });
            });
            executorService.submit(() -> {
                InputStreamReader inputStreamReader = new InputStreamReader(exec.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                try {
                    while ((line = bufferedReader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServices() {
        executorService.shutdownNow();
    }
}
