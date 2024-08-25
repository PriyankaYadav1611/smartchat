package com.project.SmartChat.enums;

public enum GroupType {
    ONE_TO_ONE("oneToOne"),
    MANY_TO_MANY("manyToMany");

    private final String value;

    GroupType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
    
}


