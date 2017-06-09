package com.akivaliaho;

/**
 * Created by akivv on 7.6.2017.
 */
public enum Addresses {
    CONFIGURATIONSERVICE_DEFAULT("configurationServiceFanout");
    private final String addressString;

    Addresses(String addressString) {
        this.addressString = addressString;
    }

    public String getValue() {
        return addressString;
    }

}
