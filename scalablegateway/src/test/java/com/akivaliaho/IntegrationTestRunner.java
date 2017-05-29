package com.akivaliaho;

import com.akivaliaho.rest.IntegrationTestHarness;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by akivv on 28.5.2017.
 */
public class IntegrationTestRunner extends SpringJUnit4ClassRunner {
    private final Class<?> testClass;


    public IntegrationTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        IntegrationTestHarness integrationTestHarness = (IntegrationTestHarness) getTestContextManager().getTestContext().getApplicationContext().getBean("integrationTestHarness");
        integrationTestHarness.startServicesIfNotOn();
        this.testClass = clazz;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            integrationTestHarness.stopServices();
        }));

    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
    }

    @Override
    protected Statement withAfterClasses(Statement statement) {
        return super.withAfterClasses(statement);
    }
}
