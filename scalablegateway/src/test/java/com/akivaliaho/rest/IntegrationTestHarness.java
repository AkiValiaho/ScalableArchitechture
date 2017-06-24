package com.akivaliaho.rest;

import com.akivaliaho.tools.RunnableTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Created by akivv on 6.5.2017.
 */
@Slf4j
@Component
public class IntegrationTestHarness extends AbstractTestExecutionListener {

    private RunnableTools runnableTools;
    private ExecutorService executorService;
    private AtomicBoolean servicesStarted;

    public IntegrationTestHarness() {
        this.runnableTools = new RunnableTools();
        this.servicesStarted = new AtomicBoolean(false);
    }

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        stopServices();
        //Close all the services
        super.afterTestClass(testContext);
    }

    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
        startServices();
        super.beforeTestClass(testContext);
    }

    public void startCoreServicesAnd(String[] servicesToStart) {
        servicesStarted.set(true);
        Runtime runtime = Runtime.getRuntime();
        executorService = Executors.newCachedThreadPool();
        String property = getRootDir();
        //Traverse the directory structure to find the runnable jars
        List<String> runnables = findRunnables(property);
        List<String> collect = runnables.stream()
                .filter(runnable -> {
                    for (String s : servicesToStart) {
                        if (runnable.toLowerCase().contains(s.toLowerCase()) || runnable.contains("configuration") || runnable.contains("camel")) {
                            return true;
                        }
                    }
                    return false;
                }).collect(Collectors.toList());
        log.debug("Number of runnables to start: {}", collect.size());
        collect.forEach(s -> log.debug("Starting runnable: {}", s));
        runnableTools.startRunnables(collect, runtime);
    }


    public void startServices() {
        servicesStarted.set(true);
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
        servicesStarted.set(false);
    }

    public void startServicesIfNotOn() {
        if (!servicesStarted.get()) {
            startServices();
        }
    }

    public void startServicesIfNotOn(String[] strings) {
        if (!servicesStarted.get()) {
            startCoreServicesAnd(strings);
        }
    }
}
