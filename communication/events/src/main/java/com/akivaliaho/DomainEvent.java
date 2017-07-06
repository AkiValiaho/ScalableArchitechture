package com.akivaliaho;

import java.io.Serializable;

/**
 * Created by akivv on 5.7.2017.
 */
public interface DomainEvent extends Serializable{
    String getEventName();

    Object[] getParameters();
}
