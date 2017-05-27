package com.akivaliaho.rest;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by akivv on 27.5.2017.
 */
public class RunnableHolder {

    @Getter
    private final List<Process> registeredProcesses;
    private final AtomicInteger registeredRunnables;

    public RunnableHolder() {
        this.registeredProcesses = new ArrayList<>();
        this.registeredRunnables = new AtomicInteger(0);
    }

    public void registerStartedProcess(Process exec) {
        registeredProcesses.add(exec);
    }

    public void incrementStartedRunnables() {
        registeredRunnables.incrementAndGet();
    }

    public int getStartupSituation() {
        return registeredRunnables.get();
    }
}
