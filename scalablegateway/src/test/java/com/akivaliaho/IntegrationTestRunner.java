package com.akivaliaho;

import com.akivaliaho.rest.IntegrationTestHarness;
import com.akivaliaho.rest.ServicesToStart;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by akivv on 28.5.2017.
 */
public class IntegrationTestRunner extends SpringJUnit4ClassRunner {
    private final Class<?> testClass;
    private final IntegrationTestHarness integrationTestHarness;


    public IntegrationTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        integrationTestHarness = (IntegrationTestHarness) getTestContextManager().getTestContext().getApplicationContext().getBean("integrationTestHarness");
        //Check if the actual test class contains an annotation configuring the services to start
        ServicesToStart annotation = clazz.getAnnotation(ServicesToStart.class);
        startServices(annotation);
        this.testClass = clazz;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            integrationTestHarness.stopServices();
        }));

    }

    private void startServices(ServicesToStart annotation) {
        if (annotation != null) {
            //Parse all the service names
            String[] strings = annotation.servicesToStart();
            integrationTestHarness.startServicesIfNotOn(strings);
        } else {
            integrationTestHarness.startServicesIfNotOn();
        }
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
