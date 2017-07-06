package com.akivaliaho;

import org.junit.Test;

/**
 * Created by akivv on 6.7.2017.
 */
public class DomainEventBuilderStrategyFactoryTest {
    @Test(expected = NullPointerException.class)
    public void resolveBuilderStrategy() throws Exception {
        DomainEventBuilderStrategyFactory domainEventBuilderStrategyFactory = new DomainEventBuilderStrategyFactory();
        domainEventBuilderStrategyFactory.resolveBuilderStrategy(null);
    }

    @Test
    public void resolveBuilderStrategyWorks() throws  Exception {

    }
}