package com.akivaliaho;

import com.akivaliaho.rest.IntegrationTestHarness;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by akivv on 28.5.2017.
 */
public class IntegrationTestRunner extends SpringJUnit4ClassRunner {
    private final IntegrationTestHarness integrationTestHarness;

    public IntegrationTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        this.integrationTestHarness = new IntegrationTestHarness();
    }

    @Override
    protected Statement withBeforeClasses(Statement statement) {
        //Start the infrastructure
        Statement statement1 = super.withBeforeClasses(statement);
        //TODO Get the testExecutionListeners from the context and add the integration test harness bootup procedure
        try {
            Field testContextManger = statement1.getClass().getDeclaredField("testContextManager");
            testContextManger.setAccessible(true);
            TestContextManager o = (TestContextManager) testContextManger.get(statement1);
            List<TestExecutionListener> testExecutionListeners = o.getTestExecutionListeners();
            testExecutionListeners.add(integrationTestHarness);
            return statement1;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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
